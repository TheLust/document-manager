package md.ceiti.backend.exception;

import lombok.Getter;
import org.springframework.validation.Errors;

import java.util.Map;
import java.util.TreeMap;

@Getter
public class ValidationException extends RuntimeException {

    private final Map<String, String> errors = new TreeMap<>();

    public ValidationException(Errors errors) {
        this.errors.putAll(toMap(errors));
    }

    public ValidationException(Map<String, String> errors) {
        this.errors.putAll(errors);
    }

    private Map<String, String> toMap(Errors errors) {
        if (errors.hasErrors()) {
            Map<String, String> errorsMap = new TreeMap<>();
            errors.getFieldErrors()
                    .forEach(field -> {
                        errorsMap.put(field.getField(), field.getDefaultMessage());
                    });
            return errorsMap;
        }

        return new TreeMap<>();
    }
}
