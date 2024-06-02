package md.ceiti.backend.dto.cms;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SimpleInstitutionDto {

    private Long id;
    private String name;
    private boolean active;
    private boolean enabled;
}
