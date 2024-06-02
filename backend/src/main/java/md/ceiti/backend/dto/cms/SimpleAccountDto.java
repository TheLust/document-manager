package md.ceiti.backend.dto.cms;

import lombok.Getter;
import lombok.Setter;
import md.ceiti.backend.model.Image;
import md.ceiti.backend.model.Role;

import java.time.LocalDate;

@Getter
@Setter
public class SimpleAccountDto {

    private Long id;
    private String username;
    private String password;
    private Image image;
    private Role role;
    private String email;
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private boolean active;
    private boolean enabled;
}
