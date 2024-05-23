package md.ceiti.backend.constant;

public interface ConstraintViolationCodes {

    String REQUIRED = "required";
    String LENGTH = "length(min: {min}, max: {max})";
    String UNIQUE = "unique";
    String EMAIL = "email";
    String PAST = "past";
    String MATCH = "match";
    String INCORRECT_PASSWORD = "incorrect_password";
    String SAME_PASSWORD = "same_password";
}
