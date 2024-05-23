package md.ceiti.frontend.constant;

public interface ConstraintViolationMessage {

    // GENERIC

    String REQUIRED = " is required";
    String LENGTH = " must have {min}-{max} characters";
    String EMAIL = " must be valid";
    String PAST = " must be in the past";

    // PASSWORD

    String PASSWORDS_NOT_MATCH = "Passwords do not match";
}
