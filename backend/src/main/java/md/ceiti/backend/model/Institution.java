package md.ceiti.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import md.ceiti.backend.constant.ConstraintViolationCodes;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Institution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "institution")
    private List<Account> accounts = new ArrayList<>();

    @NotBlank(message = ConstraintViolationCodes.REQUIRED)
    private String name;

}
