package md.ceiti.frontend.exception;

import lombok.Getter;

import java.util.Map;
import java.util.TreeMap;

@Getter
public class ValidationException extends RuntimeException {

    private final Map<String, String> errors = new TreeMap<>();

    public ValidationException(Map<String, String> errors, String errorCode) {
        super(errorCode);
        this.errors.putAll(errors);
    }
}
