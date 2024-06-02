package md.ceiti.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = ConstraintViolationCodes.REQUIRED)
    @ManyToOne
    private Institution institution;

    @OneToMany(mappedBy = "category")
    private List<Document> documents = new ArrayList<>();

    @NotBlank(message = ConstraintViolationCodes.REQUIRED)
    private String name;

    private boolean enabled;
}
