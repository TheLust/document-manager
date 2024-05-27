package md.ceiti.frontend.component;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasValueAndElement;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.data.binder.Binder;
import md.ceiti.frontend.exception.CrudException;
import md.ceiti.frontend.exception.FrontendException;
import md.ceiti.frontend.util.ErrorHandler;
import org.vaadin.crudui.form.CrudFormFactory;

import java.util.List;

public class DMFormFactory {
    public static <T> GenericCrudFormFactory<T> getDefaultFormFactory(Class<T> domainType) {
        GenericCrudFormFactory<T> formFactory = new GenericCrudFormFactory<>(domainType) {
            @Override
            protected void configureForm(FormLayout formLayout, List<HasValueAndElement> fields) {
                Component nameField = (Component) fields.get(0);
                formLayout.setColspan(nameField, 1);
            }
        };

        formFactory.setShowNotifications(true);
        formFactory.setErrorListener(e -> {
            if (e instanceof CrudException) {
                Notification.show("Please fix the errors and try again");
            } else {
                ErrorHandler.handle(e);
            }
        });
        formFactory.setUseBeanValidation(true);


        return formFactory;
    }

    public static <T> Binder<T> getBinder(CrudFormFactory<T> crudFormFactory) {
        if (crudFormFactory instanceof GenericCrudFormFactory<T> genericCrudFormFactory) {
            return genericCrudFormFactory.getFormBinder();
        }

        throw new FrontendException("crudFormFactory is not generic/custom to extract the binder");
    }
}
