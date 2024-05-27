package md.ceiti.backend.validator;

import lombok.RequiredArgsConstructor;
import md.ceiti.backend.model.Institution;
import md.ceiti.backend.service.impl.InstitutionService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class InstitutionValidator implements Validator {

    private final InstitutionService institutionService;

    @Override
    public boolean supports(Class<?> clazz) {
        return Institution.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        GenericValidator.validate(target, errors);
        GenericValidator.throwValidationException(errors);
    }
}
