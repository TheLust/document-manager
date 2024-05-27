package md.ceiti.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import md.ceiti.backend.constant.ConstraintViolationCodes;
import md.ceiti.backend.constant.Constraints;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Institution institution;

    @OneToOne(mappedBy = "master")
    private Institution masterOf;

    @ManyToOne
    private Image image;

    @NotNull(message = ConstraintViolationCodes.REQUIRED)
    @Enumerated(EnumType.STRING)
    private Role role;

    @NotBlank(message = ConstraintViolationCodes.REQUIRED)
    @Size(
            min = Constraints.USERNAME_MIN,
            max = Constraints.USERNAME_MAX,
            message = ConstraintViolationCodes.LENGTH
    )
    private String username;

    @NotBlank(message = ConstraintViolationCodes.REQUIRED)
    @Size(
            min = Constraints.PASSWORD_MIN,
            max = Constraints.PASSWORD_MAX,
            message = ConstraintViolationCodes.LENGTH
    )
    private String password;

    @Email(message = ConstraintViolationCodes.EMAIL)
    @NotBlank(message = ConstraintViolationCodes.REQUIRED)
    private String email;

    @NotBlank(message = ConstraintViolationCodes.REQUIRED)
    private String phoneNumber;

    @NotBlank(message = ConstraintViolationCodes.REQUIRED)
    private String firstName;

    @NotBlank(message = ConstraintViolationCodes.REQUIRED)
    private String lastName;

    @Past(message = ConstraintViolationCodes.PAST)
    @NotNull(message = ConstraintViolationCodes.REQUIRED)
    private LocalDate birthDate;

    private boolean enabled;
}
