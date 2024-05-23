package md.ceiti.frontend.dto.response;

import lombok.Getter;
import lombok.Setter;
import md.ceiti.frontend.dto.Image;
import md.ceiti.frontend.dto.Role;

import java.time.LocalDate;

@Getter
@Setter
public class Profile {

    private Long id;
    private String username;
    private Image image;
    private Role role;
    private String email;
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private boolean enabled;
}
