package md.ceiti.frontend.exception;

public class CrudException extends RuntimeException {

    public CrudException() {
    }

    public CrudException(String message) {
        super(message);
    }
}
