package md.ceiti.frontend.constant.fields;

import md.ceiti.frontend.model.Field;

public interface ProfileChangePasswordRequestFields {

    String CURRENT_PASSWORD_LABEL = "Current password";
    String NEW_PASSWORD_LABEL = "New password";
    String CONFIRM_PASSWORD_LABEL = "Confirm password";

    Field CURRENT_PASSWORD = new Field("currentPassword", CURRENT_PASSWORD_LABEL, null);
    Field NEW_PASSWORD = new Field("newPassword",NEW_PASSWORD_LABEL, null);
    Field CONFIRM_PASSWORD = new Field("confirmPassword", CONFIRM_PASSWORD_LABEL, null);
}
