package md.ceiti.frontend.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CmsAccountDto {

    private Long id;
    private String username;
    private String password;
    private CmsInstitutionDto institution;
    private Image image;
    private Role role;
    private String email;
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private boolean enabled;
}
