package md.ceiti.frontend.dto.institution;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import md.ceiti.frontend.constant.ConstraintViolationMessage;
import md.ceiti.frontend.constant.fields.DocumentDtoFields;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class DocumentDto {

    private UUID id;

    @NotNull(message = DocumentDtoFields.CATEGORY_LABEL + ConstraintViolationMessage.REQUIRED)
    private CategoryDto category;
    private AccountDto createdBy;

    @NotBlank(message = DocumentDtoFields.NAME_LABEL + ConstraintViolationMessage.REQUIRED)
    private String name;
    private String extension;
    private LocalDate createdAt;
}
