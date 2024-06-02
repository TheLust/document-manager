package md.ceiti.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @OneToOne
    @NotNull(message = ConstraintViolationCodes.REQUIRED)
    private Account master;

    @OneToMany(mappedBy = "institution")
    private List<Account> accounts = new ArrayList<>();

    @OneToMany(mappedBy = "institution")
    private List<Category> categories = new ArrayList<>();

    @NotBlank(message = ConstraintViolationCodes.REQUIRED)
    private String name;

    private boolean active;
    private boolean enabled;
}
