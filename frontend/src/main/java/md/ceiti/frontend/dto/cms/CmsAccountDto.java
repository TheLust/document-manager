package md.ceiti.frontend.dto.cms;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import md.ceiti.frontend.constant.ConstraintViolationMessage;
import md.ceiti.frontend.constant.Constraints;
import md.ceiti.frontend.constant.fields.CmsAccountDtoFields;
import md.ceiti.frontend.dto.Image;
import md.ceiti.frontend.dto.Role;

import java.time.LocalDate;

@Getter
@Setter
public class CmsAccountDto {

    private Long id;

    @NotBlank(message = CmsAccountDtoFields.USERNAME_LABEL + ConstraintViolationMessage.REQUIRED)
    @Size(
            min = Constraints.USERNAME_MIN,
            max = Constraints.USERNAME_MAX,
            message = CmsAccountDtoFields.USERNAME_LABEL + ConstraintViolationMessage.LENGTH
    )
    private String username;

    @Size(
            min = Constraints.PASSWORD_MIN,
            max = Constraints.PASSWORD_MAX,
            message = CmsAccountDtoFields.PASSWORD_LABEL + ConstraintViolationMessage.LENGTH
    )
    private String password;

    private SimpleInstitutionDto institution;
    private Image image;

    @NotNull(message = CmsAccountDtoFields.ROLE_LABEL + ConstraintViolationMessage.REQUIRED)
    private Role role;

    @Email(message = CmsAccountDtoFields.EMAIL_LABEL + ConstraintViolationMessage.EMAIL)
    @NotBlank(message = CmsAccountDtoFields.EMAIL_LABEL + ConstraintViolationMessage.REQUIRED)
    private String email;

    @NotBlank(message = CmsAccountDtoFields.PHONE_NUMBER_LABEL + ConstraintViolationMessage.REQUIRED)
    private String phoneNumber;

    @NotBlank(message = CmsAccountDtoFields.FIRST_NAME_LABEL + ConstraintViolationMessage.REQUIRED)
    private String firstName;

    @NotBlank(message = CmsAccountDtoFields.LAST_NAME_LABEL + ConstraintViolationMessage.REQUIRED)
    private String lastName;

    @Past(message = CmsAccountDtoFields.BIRTH_DATE_LABEL + ConstraintViolationMessage.PAST)
    @NotNull(message = CmsAccountDtoFields.BIRTH_DATE_LABEL + ConstraintViolationMessage.REQUIRED)
    private LocalDate birthDate;

    private boolean active;
    private boolean enabled;
}
