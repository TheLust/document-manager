package md.ceiti.backend.repository;

import md.ceiti.backend.model.Category;
import md.ceiti.backend.model.Institution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByInstitutionAndName(Institution institution,
                                                String name);
}
