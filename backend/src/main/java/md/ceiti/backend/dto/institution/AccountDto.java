package md.ceiti.backend.dto.institution;

import lombok.Getter;
import lombok.Setter;
import md.ceiti.backend.model.Image;

import java.time.LocalDate;

@Getter
@Setter
public class AccountDto {

    private Long id;
    private String username;
    private String password;
    private Image image;
    private String email;
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private boolean active;
    private boolean enabled;
}
