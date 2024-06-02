package md.ceiti.backend.dto.institution;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryDto {

    private Long id;
    private String name;
    private boolean enabled;
}
