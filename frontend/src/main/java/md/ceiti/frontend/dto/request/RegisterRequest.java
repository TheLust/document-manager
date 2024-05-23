package md.ceiti.frontend.dto.request;

import lombok.Getter;
import lombok.Setter;
import md.ceiti.frontend.dto.Role;

import java.time.LocalDate;

@Getter
@Setter
public class RegisterRequest {

    private String username;
    private String password;
    private Role role;
    private String email;
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
}
