package md.ceiti.backend.dto.request;

import lombok.Getter;
import lombok.Setter;
import md.ceiti.backend.model.Role;

import java.time.LocalDate;

@Getter
@Setter
public class RegisterRequest {

    private String username;
    private String password;
    private String email;
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
}
