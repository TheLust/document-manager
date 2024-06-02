package md.ceiti.backend.dto.cms;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CmsInstitutionDto {

    private Long id;
    private SimpleAccountDto master;
    private String name;
    private boolean active;
    private boolean enabled;
}
