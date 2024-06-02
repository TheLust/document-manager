package md.ceiti.backend.dto.institution;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class DocumentDto {

    private UUID id;
    private CategoryDto category;
    private AccountDto createdBy;
    private String name;
    private String extension;
    private LocalDate createdAt;
}
