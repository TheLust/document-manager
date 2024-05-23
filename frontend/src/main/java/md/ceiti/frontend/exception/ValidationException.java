package md.ceiti.frontend.exception;

import lombok.Getter;
import org.springframework.validation.Errors;

import java.util.Map;
import java.util.TreeMap;

@Getter
public class ValidationException extends RuntimeException {

    private final Map<String, String> errors = new TreeMap<>();

    public ValidationException(Map<String, String> errors) {
        this.errors.putAll(errors);
    }
}
