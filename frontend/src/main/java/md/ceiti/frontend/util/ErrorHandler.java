package md.ceiti.frontend.util;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.TextFieldBase;
import md.ceiti.frontend.constant.ErrorCodes;
import md.ceiti.frontend.exception.BadRequestException;
import md.ceiti.frontend.exception.ExceptionResponse;
import md.ceiti.frontend.exception.FrontendException;
import md.ceiti.frontend.exception.ValidationException;
import md.ceiti.frontend.view.ErrorView;
import md.ceiti.frontend.view.ForbiddenView;
import md.ceiti.frontend.view.LoginView;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Optional;

public class ErrorHandler {

    public static void handle(BadRequestException e) {
        String errorCode = e.getMessage();

        if (errorCode != null && !errorCode.isEmpty()) {
            UI current = UI.getCurrent();
            if (current == null) {
                return;
            }

            if (errorCode.equals(ErrorCodes.UNAUTHORIZED)) {
                NavigationUtils.setLocation(LoginView.class);
            }

            if (errorCode.equals(ErrorCodes.ACCESS_DENIED)) {
                NavigationUtils.setLocation(ForbiddenView.class);
            }
        }
    }

    public static void handle(FrontendException e) {
        UI current = UI.getCurrent();
        if (current == null) {
            return;
        }

        NavigationUtils.setLocation(ErrorView.class);
    }

    public static void handle(HttpClientErrorException e) throws BadRequestException, ValidationException {
        ExceptionResponse exceptionResponse = e.getResponseBodyAs(ExceptionResponse.class);

        if (exceptionResponse != null && ErrorCodes.VALIDATION_ERROR.equals(exceptionResponse.getErrorCode())) {
            throw new ValidationException(exceptionResponse.getValidationErrors());
        }

        throw new BadRequestException(
                exceptionResponse != null && exceptionResponse.getErrorCode() != null
                        ? exceptionResponse.getErrorCode()
                        : ErrorCodes.TIMEOUT
        );
    }

    public static void handle(Exception ignored) {
        NavigationUtils.setLocation(ErrorView.class);
    }

    public static void setErrors(FormLayout formLayout, ValidationException e) {
        final String exceptionMessage = "All fields from form should have id as the field from from the dto!";

        formLayout.getChildren().forEach(component -> {
            if (component instanceof TextFieldBase<?, ?> textField) {
                if (textField.getId().isEmpty()) {
                    throw new FrontendException(exceptionMessage);
                }

                Optional<String> errorCode = getErrorCodeForField(textField.getId().get(), e);
                if (errorCode.isPresent()) {
                    textField.setInvalid(true);
                    textField.setErrorMessage(errorCode.get());
                }
            } else if (component instanceof DatePicker datePicker) {
                if (datePicker.getId().isEmpty()) {
                    throw new FrontendException(exceptionMessage);
                }

                Optional<String> errorCode = getErrorCodeForField(datePicker.getId().get(), e);
                if (errorCode.isPresent()) {
                    datePicker.setInvalid(true);
                    datePicker.setErrorMessage(errorCode.get());
                }
            }
        });
    }

    public static Optional<String> getErrorCodeForField(String fieldId, ValidationException e) {
        String errorCode = e.getErrors().get(fieldId);
        if (errorCode != null) {
            return Optional.of(errorCode);
        }

        return Optional.empty();
    }
}
