package md.ceiti.backend.dto.response;

import lombok.Getter;
import lombok.Setter;
import md.ceiti.backend.dto.cms.SimpleInstitutionDto;
import md.ceiti.backend.model.Image;
import md.ceiti.backend.model.Role;

import java.time.LocalDate;

@Getter
@Setter
public class Profile {

    private Long id;
    private String username;
    private SimpleInstitutionDto institution;
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
