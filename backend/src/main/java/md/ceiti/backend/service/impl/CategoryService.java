package md.ceiti.backend.service.impl;

import lombok.RequiredArgsConstructor;
import md.ceiti.backend.exception.NotFoundException;
import md.ceiti.backend.model.Category;
import md.ceiti.backend.model.Institution;
import md.ceiti.backend.repository.CategoryRepository;
import md.ceiti.backend.service.GenericService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService implements GenericService<Category, Long> {

    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Category getById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category", "id", id));
    }

    @Override
    public Category insert(Category entity) {
        return categoryRepository.save(entity);
    }

    @Override
    public Category update(Category presentEntity, Category updatedEntity) {
        BeanUtils.copyProperties(updatedEntity, presentEntity,
                "id");
        return categoryRepository.save(presentEntity);
    }

    @Override
    public void delete(Category entity) {
        categoryRepository.delete(entity);
    }

    @Override
    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }

    public Optional<Category> findByInstitutionAndName(Institution institution, String name) {
        return categoryRepository.findByInstitutionAndName(institution, name);
    }
}
