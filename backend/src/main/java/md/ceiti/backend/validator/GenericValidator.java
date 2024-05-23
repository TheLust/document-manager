package md.ceiti.backend.validator;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import md.ceiti.backend.constant.ConstraintViolationCodes;
import md.ceiti.backend.exception.ValidationException;
import org.springframework.validation.Errors;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.Optional;

public class GenericValidator {

    public static <T> void validate(T object, Errors errors) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        for (ConstraintViolation<T> violation : validator.validate(object)) {
            errors.rejectValue(violation.getPropertyPath().toString(), "", violation.getMessage());
        }
    }

    public static <T> void unique(Errors errors, String field, Optional<T> found) {
        if (found.isPresent()) {
            errors.rejectValue(field, "", ConstraintViolationCodes.UNIQUE);
        }
    }

    public static <T, G> void unique(Errors errors,
                                     String field,
                                     Class<G> fieldType,
                                     T entity,
                                     Optional<T> found) {
        try {
            if (found.isPresent()) {
                G presentField = getFieldValue(entity, field, fieldType);
                G foundField = getFieldValue(found.get(), field, fieldType);
                if (!presentField.equals(foundField)) {
                    errors.rejectValue(field, "", ConstraintViolationCodes.UNIQUE);
                }
            }
        } catch (Exception e) {
            errors.rejectValue(field, "", ConstraintViolationCodes.UNIQUE);
        }
    }

    public static void notNull(Errors errors, String field, Object value) {
        if (Objects.isNull(value)) {
            errors.rejectValue(field, "", ConstraintViolationCodes.REQUIRED);
        }
    }

    public static void throwValidationException(Errors errors) {
        if (errors.hasErrors()) {
            throw new ValidationException(errors);
        }
    }

    public static <T, G> G getFieldValue(T entity, String fieldName, Class<G> fieldType)
            throws NoSuchFieldException, IllegalAccessException {
        Class<?> clazz = entity.getClass();
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        Object value = field.get(entity);
        return fieldType.cast(value);
    }
}
