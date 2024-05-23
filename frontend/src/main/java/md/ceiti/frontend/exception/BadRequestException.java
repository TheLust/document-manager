package md.ceiti.frontend.exception;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String errorCode) {
        super(errorCode);
    }
}
