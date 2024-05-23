package md.ceiti.backend.exception;

import lombok.Getter;

@Getter
public class ApplicationException extends RuntimeException {

    String errorCode;

    public ApplicationException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
