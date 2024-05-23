package md.ceiti.frontend.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class ExceptionResponse {

    private String message;
    private String errorCode;
    private Map<String, String> validationErrors;
    private long timestamp;
}
