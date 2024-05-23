package md.ceiti.backend.validator;

import org.springframework.validation.Errors;

public interface UpdateValidator {
    void validate(Object target, Errors errors, boolean isUpdate);
}
