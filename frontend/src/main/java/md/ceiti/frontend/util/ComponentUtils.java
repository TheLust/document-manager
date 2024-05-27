package md.ceiti.frontend.util;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import md.ceiti.frontend.constant.I18n;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.BeanUtils;

import java.util.function.Supplier;

public class ComponentUtils {

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
        container.getRight().getLeft().addClickListener(event -> dialog.close());

        return container;
    }

    public static <T> Pair<HorizontalLayout, Pair<Button, Button>> getGenericDialogButtonLayout(Dialog dialog,
                                                                                                T bean,
                                                                                                Supplier<T> supplier) {
        Pair<HorizontalLayout, Pair<Button, Button>> buttonLayout = getGenericDialogButtonLayout(dialog);
        buttonLayout.getRight().getLeft().addClickListener(event -> BeanUtils.copyProperties(supplier.get(), bean));

        return buttonLayout;
    }
}
