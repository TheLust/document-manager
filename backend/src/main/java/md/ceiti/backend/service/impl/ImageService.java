package md.ceiti.backend.service.impl;

import lombok.RequiredArgsConstructor;
import md.ceiti.backend.constant.ErrorCodes;
import md.ceiti.backend.exception.ApplicationException;
import md.ceiti.backend.exception.NotFoundException;
import md.ceiti.backend.model.Image;
import md.ceiti.backend.repository.ImageRepository;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;

    public Image getById(UUID id) {
        return imageRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Image", "id", id));
    }

    public byte[] getImage(Image image) {
        if (image == null) {
            throw new ApplicationException(
                    "Cannot get the image because the profile does not have one. Check frontend",
                    ErrorCodes.INTERNAL_ERROR);
        }

        try {
            return Files.readAllBytes(getFile(image).toPath());
        } catch (IOException e) {
            throw new ApplicationException(
                    String.format("Could not read image file, exception=[%s]", e.getMessage()),
                    ErrorCodes.INTERNAL_ERROR);
        }
    }

    @Transactional
    public Image insert(MultipartFile imageFile) {
        Image image = new Image();
        image.setExtension(FilenameUtils.getExtension(imageFile.getOriginalFilename()));
        image = imageRepository.save(image);

        File file = getFile(image, false);
        try (OutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(imageFile.getBytes());
        } catch (IOException e) {
            throw new ApplicationException(
                    String.format("Could not save file server local storage, exception=[%s]", e.getMessage()),
                    ErrorCodes.INTERNAL_ERROR);
        }

        return image;
    }

    @Transactional
    public void delete(Image entity) {
        deleteFile(entity);
        imageRepository.delete(entity);
    }

    @Transactional
    public void deleteById(UUID id) {
        Image image = getById(id);
        delete(image);
    }

    private File getFile(Image image) {
        return getFile(image, true);
    }

    private File getFile(Image image, boolean useCheck) {
        String FILE_PATH = "files/%s.%s";
        File file = new File(String.format(FILE_PATH, image.getId(), image.getExtension()));

        if (!file.exists() && useCheck) {
            throw new ApplicationException(
                    "Could not read file from server local storage",
                    ErrorCodes.INTERNAL_ERROR);
        }

        return file;
    }

    private void deleteFile(Image image) {
        File file = getFile(image);
        if (!file.delete()) {
            throw new ApplicationException(
                    "Could not delete file from server local storage",
                    ErrorCodes.INTERNAL_ERROR);
        }
    }
}
