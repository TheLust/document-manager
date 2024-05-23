package md.ceiti.backend.facade;

import lombok.RequiredArgsConstructor;
import md.ceiti.backend.model.Image;
import md.ceiti.backend.service.impl.ImageService;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ImageFacade {

    private final ImageService imageService;

    public byte[] findImageById(UUID uuid) {
        Image image = imageService.getById(uuid);
        return imageService.getImage(image);
    }
}
