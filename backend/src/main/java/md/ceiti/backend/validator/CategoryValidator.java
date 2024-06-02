package md.ceiti.backend.validator;

import lombok.RequiredArgsConstructor;
import md.ceiti.backend.model.Category;
import md.ceiti.backend.service.impl.CategoryService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class CategoryValidator implements Validator, UpdateValidator {

    private final CategoryService categoryService;

    @Override
    public boolean supports(Class<?> clazz) {
        return Category.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        validate(target, errors, false);
    }

    @Override
    public void validate(Object target, Errors errors, boolean isUpdate) {
        Category category = (Category) target;
        GenericValidator.validate(target, errors);

        if (!isUpdate) {
            GenericValidator.unique(
                    errors,
                    "name",
                    categoryService.findByInstitutionAndName(category.getInstitution(), category.getName()));
        } else {
            GenericValidator.unique(errors,
                    "name",
                    "id",
                    Long.class,
                    category,
                    categoryService.findByInstitutionAndName(category.getInstitution(), category.getName()));
        }
        GenericValidator.throwValidationException(errors);
    }
}
