package md.ceiti.frontend.dto.cms;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import md.ceiti.frontend.constant.ConstraintViolationMessage;
import md.ceiti.frontend.constant.fields.CmsInstitutionDtoFields;

@Getter
@Setter
public class CmsInstitutionDto {

    private Long id;

    @NotNull(message = CmsInstitutionDtoFields.MASTER_LABEL + ConstraintViolationMessage.REQUIRED)
    private SimpleAccountDto master;

    @NotBlank(message = CmsInstitutionDtoFields.NAME_LABEL + ConstraintViolationMessage.REQUIRED)
    private String name;

    private boolean active;
    private boolean enabled;
}
