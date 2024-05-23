package md.ceiti.frontend.util;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextFieldBase;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import md.ceiti.frontend.constant.I18n;
import md.ceiti.frontend.dto.Image;
import md.ceiti.frontend.dto.response.Profile;
import md.ceiti.frontend.exception.FrontendException;
import org.apache.commons.lang3.tuple.Pair;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

public class ComponentUtils {

    public static Avatar getAvatar(Profile profile) {
        return getAvatar(profile, false);
    }

    public static Avatar getAvatar(Profile profile, boolean editable) {
        Avatar avatar = new Avatar();
        avatar.setImage(getProfileImageEndpoint(profile.getImage()));
        if (editable) {
            avatar.setClassName("profile-avatar");
            avatar.getElement().addEventListener("mouseover", event -> avatar.setImage("https://cdn-icons-png.flaticon.com/512/6065/6065488.png"));
            avatar.getElement().addEventListener("mouseout", event -> avatar.setImage(getProfileImageEndpoint(profile.getImage())));
        }
        return avatar;
    }

    public static ConfirmDialog getGenericConfirmDialog(String header, String text) {
        ConfirmDialog confirmDialog = new ConfirmDialog();
        confirmDialog.setHeader(header);
        confirmDialog.setText(text);
        confirmDialog.setCancelable(true);
        confirmDialog.setCancelText(I18n.CANCEL);
        confirmDialog.setRejectable(false);

        return confirmDialog;
    }

    public static Pair<HorizontalLayout, Pair<Button, Button>> getGenericDialogButtonLayout(Dialog dialog) {
        Button cancel = new Button(I18n.CANCEL);
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        cancel.addClickListener(event -> {
            dialog.close();
        });

        Button change = new Button(I18n.CHANGE);
        change.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        HorizontalLayout container = new HorizontalLayout();
        container.setWidthFull();
        container.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        container.add(cancel, change);

        return Pair.of(container, Pair.of(cancel, change));
    }

    public static <T> Pair<HorizontalLayout, Pair<Button, Button>> getGenericDialogButtonLayout(Dialog dialog, BeanValidationBinder<T> binder) {
        Pair<HorizontalLayout, Pair<Button, Button>> buttonLayout = getGenericDialogButtonLayout(dialog);
        buttonLayout.getRight().getLeft().addClickListener(event -> {
            binder.setBean(ti stgetInstanceOfT());
        });

        return buttonLayout;
    }

    private static <T> T getInstanceOfT(Class<T> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new FrontendException("Something happened...");
        }
    }

    private static String getProfileImageEndpoint(Image image) {
        if (image == null) {
            return null;
        }

        return ApiUtils.IMAGES_ENDPOINT + "?uuid=" + image.getId();
    }

}
