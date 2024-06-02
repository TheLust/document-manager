package md.ceiti.frontend.dto.institution;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import md.ceiti.frontend.constant.ConstraintViolationMessage;
import md.ceiti.frontend.constant.Constraints;
import md.ceiti.frontend.constant.fields.AccountDtoFields;
import md.ceiti.frontend.dto.Image;

import java.time.LocalDate;

@Getter
@Setter
public class AccountDto {

    private Long id;

    @NotBlank(message = AccountDtoFields.USERNAME_LABEL + ConstraintViolationMessage.REQUIRED)
    @Size(
            min = Constraints.USERNAME_MIN,
            max = Constraints.USERNAME_MAX,
            message = AccountDtoFields.USERNAME_LABEL + ConstraintViolationMessage.LENGTH
    )
    private String username;

    @Size(
            min = Constraints.PASSWORD_MIN,
            max = Constraints.PASSWORD_MAX,
            message = AccountDtoFields.PASSWORD_LABEL + ConstraintViolationMessage.LENGTH
    )
    private String password;
    private Image image;

    @Email(message = AccountDtoFields.EMAIL_LABEL + ConstraintViolationMessage.EMAIL)
    @NotBlank(message = AccountDtoFields.EMAIL_LABEL + ConstraintViolationMessage.REQUIRED)
    private String email;

    @NotBlank(message = AccountDtoFields.PHONE_NUMBER_LABEL + ConstraintViolationMessage.REQUIRED)
    private String phoneNumber;

    @NotBlank(message = AccountDtoFields.FIRST_NAME_LABEL + ConstraintViolationMessage.REQUIRED)
    private String firstName;

    @NotBlank(message = AccountDtoFields.LAST_NAME_LABEL + ConstraintViolationMessage.REQUIRED)
    private String lastName;

    @Past(message = AccountDtoFields.BIRTH_DATE_LABEL + ConstraintViolationMessage.PAST)
    @NotNull(message = AccountDtoFields.BIRTH_DATE_LABEL + ConstraintViolationMessage.REQUIRED)
    private LocalDate birthDate;

    private boolean active;
    private boolean enabled;
}
