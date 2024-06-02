package md.ceiti.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import md.ceiti.backend.constant.ConstraintViolationCodes;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull(message = ConstraintViolationCodes.REQUIRED)
    @ManyToOne
    private Category category;

    @NotNull(message = ConstraintViolationCodes.REQUIRED)
    @ManyToOne
    private Account createdBy;

    @NotBlank(message = ConstraintViolationCodes.REQUIRED)
    private String name;

    @NotBlank(message = ConstraintViolationCodes.REQUIRED)
    private String extension;

    @NotNull(message = ConstraintViolationCodes.REQUIRED)
    private LocalDate createdAt;
}
