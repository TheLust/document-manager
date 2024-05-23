package md.ceiti.frontend.util;

import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.server.AbstractStreamResource;
import com.vaadin.flow.server.StreamResource;
import md.ceiti.frontend.constant.I18n;
import md.ceiti.frontend.dto.Image;
import md.ceiti.frontend.dto.response.Profile;
import md.ceiti.frontend.exception.BadRequestException;
import md.ceiti.frontend.service.ImageService;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.BeanUtils;

import java.io.ByteArrayInputStream;
import java.util.UUID;
import java.util.function.Supplier;

public class ComponentUtils {

    public static Avatar getAvatar(Profile profile, ImageService imageService) {
        return getAvatar(profile, imageService, false);
    }

    public static Avatar getAvatar(Profile profile, ImageService imageService, boolean editable) {
        Avatar avatar = new Avatar();
        AbstractStreamResource image = getProfileImageStreamResource(imageService, profile.getImage());
        avatar.setImageResource(image);

        if (editable) {
            avatar.setClassName("profile-avatar");
            avatar.getElement().addEventListener("mouseover",
                    event -> avatar.setImage("https://cdn-icons-png.flaticon.com/512/6065/6065488.png"));
            avatar.getElement().addEventListener("mouseout",
                    event -> avatar.setImageResource(image));
            avatar.getElement().addEventListener("click",
                    event -> avatar.setImageResource(image));
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

    public static Pair<HorizontalLayout, Pair<Button, Button>> getGenericDialogButtonLayout() {
        Button cancel = new Button(I18n.CANCEL);
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        Button change = new Button(I18n.CHANGE);
        change.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        HorizontalLayout container = new HorizontalLayout();
        container.setWidthFull();
        container.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        container.add(cancel, change);

        return Pair.of(container, Pair.of(cancel, change));
    }

    public static Pair<HorizontalLayout, Pair<Button, Button>> getGenericDialogButtonLayout(Dialog dialog) {
        Pair<HorizontalLayout, Pair<Button, Button>> container = getGenericDialogButtonLayout();
        container.getRight().getLeft().addClickListener(event -> {
            dialog.close();
        });

        return container;
    }

    public static <T> Pair<HorizontalLayout, Pair<Button, Button>> getGenericDialogButtonLayout(Dialog dialog,
                                                                                                T bean,
                                                                                                Supplier<T> supplier) {
        Pair<HorizontalLayout, Pair<Button, Button>> buttonLayout = getGenericDialogButtonLayout(dialog);
        buttonLayout.getRight().getLeft().addClickListener(event -> {
            BeanUtils.copyProperties(supplier.get(), bean);
        });

        return buttonLayout;
    }

    private static AbstractStreamResource getProfileImageStreamResource(ImageService imageService, Image image) {
        UUID uuid = image != null ? image.getId() : null;
        if (uuid != null) {
            try {
                return new StreamResource(uuid.toString(), () -> new ByteArrayInputStream(imageService.getImage(uuid)));
            } catch (BadRequestException ignored) {
                return null;
            }
        }

        return null;
    }

    private static String getProfileImageEndpoint(Image image) {
        if (image == null) {
            return null;
        }

        return ApiUtils.IMAGES_ENDPOINT + "?uuid=" + image.getId();
    }

}
