package md.ceiti.backend.validator;

import lombok.RequiredArgsConstructor;
import md.ceiti.backend.model.Account;
import md.ceiti.backend.model.Institution;
import md.ceiti.backend.service.impl.InstitutionService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class InstitutionValidator implements Validator, UpdateValidator {

    private final InstitutionService institutionService;

    @Override
    public boolean supports(Class<?> clazz) {
        return Institution.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        validate(target, errors, false);
    }

    @Override
    public void validate(Object target, Errors errors, boolean isUpdate) {
        Institution institution = (Institution) target;
        GenericValidator.validate(target, errors);

        if (!isUpdate) {
            GenericValidator.unique(errors, "master", institutionService.findByMaster(institution.getMaster()));
        } else {
            GenericValidator.unique(errors,
                    "master",
                    "id",
                    Long.class,
                    institution,
                    institutionService.findByMaster(institution.getMaster()));
        }
        GenericValidator.throwValidationException(errors);
    }
}
