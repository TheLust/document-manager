package md.ceiti.backend.controller;

import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import md.ceiti.backend.facade.ImageFacade;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping(value = "${api.url.base}/images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageFacade imageFacade;

    @GetMapping("/{uuid}")
    public ResponseEntity<byte[]> getImage(@PathVariable("uuid") UUID uuid) {
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(imageFacade.findImageById(uuid));
    }
}
