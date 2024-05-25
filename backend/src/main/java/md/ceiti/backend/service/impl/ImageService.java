package md.ceiti.backend.service.impl;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.ExifIFD0Directory;
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

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Iterator;
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
            outputStream.write(resizeImage(imageFile, 500));
        } catch (IOException | ImageProcessingException | MetadataException e) {
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

    public byte[] resizeImage(MultipartFile file, int targetHeight) throws IOException, ImageProcessingException, MetadataException {
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        if (extension == null || extension.isBlank()) {
            throw new ApplicationException(
                    "Could not extract exception form file=" + file.getOriginalFilename(),
                    ErrorCodes.BAD_REQUEST
            );
        }

        // Convert MultipartFile to BufferedImage
        BufferedImage originalImage = convertMultipartFileToBufferedImage(file);

        // Rotate the image if needed based on EXIF metadata
        originalImage = correctImageOrientation(originalImage, file.getBytes());

        // Calculate the new width based on the aspect ratio
        int originalWidth = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();
        int newWidth = (int) ((double) originalWidth * ((double) targetHeight / (double) originalHeight));

        // Create a new resized image
        BufferedImage resizedImage = new BufferedImage(newWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, newWidth, targetHeight, null);
        g.dispose();

        // Convert BufferedImage to byte array
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(resizedImage, extension, outputStream);

        return outputStream.toByteArray();
    }

    private BufferedImage convertMultipartFileToBufferedImage(MultipartFile file) throws IOException {
        try (ImageInputStream iis = ImageIO.createImageInputStream(file.getInputStream())) {
            Iterator<ImageReader> imageReaders = ImageIO.getImageReaders(iis);
            if (imageReaders.hasNext()) {
                ImageReader reader = imageReaders.next();
                reader.setInput(iis, true);
                return reader.read(0);
            } else {
                throw new IllegalArgumentException("Unsupported image file format");
            }
        }
    }

    private BufferedImage correctImageOrientation(BufferedImage image, byte[] imageData) throws IOException, MetadataException, ImageProcessingException {
        Metadata metadata = ImageMetadataReader.readMetadata(new ByteArrayInputStream(imageData));
        ExifIFD0Directory exifIFD0Directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
        if (exifIFD0Directory != null && exifIFD0Directory.containsTag(ExifIFD0Directory.TAG_ORIENTATION)) {
            int orientation = exifIFD0Directory.getInt(ExifIFD0Directory.TAG_ORIENTATION);
            switch (orientation) {
                // Normal - no rotation needed
                case 2 -> image = flipHorizontal(image);
                case 3 -> image = rotate180(image);
                case 4 -> image = flipVertical(image);
                case 5 -> image = flipVertical(rotate90(image));
                case 6 -> image = rotate90(image);
                case 7 -> image = flipVertical(rotate270(image));
                case 8 -> image = rotate270(image);
                default -> {
                }
                // Invalid orientation value
            }
        }
        return image;
    }

    private BufferedImage rotate90(BufferedImage image) {
        AffineTransform transform = new AffineTransform();
        transform.rotate(Math.PI / 2, (double) image.getHeight() / 2, (double) image.getHeight() / 2);
        BufferedImageOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
        BufferedImage rotatedImage = new BufferedImage(image.getHeight(), image.getWidth(), image.getType());
        return op.filter(image, rotatedImage);
    }

    private BufferedImage rotate180(BufferedImage image) {
        AffineTransform transform = new AffineTransform();
        transform.rotate(Math.PI, (double) image.getWidth() / 2, (double) image.getHeight() / 2);
        BufferedImageOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
        BufferedImage rotatedImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        return op.filter(image, rotatedImage);
    }

    private BufferedImage rotate270(BufferedImage image) {
        AffineTransform transform = new AffineTransform();
        transform.rotate(3 * Math.PI / 2, (double) image.getWidth() / 2, (double) image.getWidth() / 2);
        BufferedImageOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
        BufferedImage rotatedImage = new BufferedImage(image.getHeight(), image.getWidth(), image.getType());
        return op.filter(image, rotatedImage);
    }

    private BufferedImage flipHorizontal(BufferedImage image) {
        AffineTransform transform = AffineTransform.getScaleInstance(-1, 1);
        transform.translate(-image.getWidth(), 0);
        return applyTransform(image, transform);
    }

    private BufferedImage flipVertical(BufferedImage image) {
        AffineTransform transform = AffineTransform.getScaleInstance(1, -1);
        transform.translate(0, -image.getHeight());
        return applyTransform(image, transform);
    }

    private BufferedImage applyTransform(BufferedImage image, AffineTransform transform) {
        BufferedImageOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
        BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        return op.filter(image, result);
    }
}
