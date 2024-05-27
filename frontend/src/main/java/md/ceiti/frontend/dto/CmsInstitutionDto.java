package md.ceiti.frontend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CmsInstitutionDto {

    private Long id;
    private CmsAccountDto master;
    private String name;
}
