package md.ceiti.backend.dto.institution;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InstitutionDto {

    private Long id;
    private String name;
    private boolean active;
}
