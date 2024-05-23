package md.ceiti.frontend.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class Image {

    private UUID id;
    private String name;
    private String extension;
}
