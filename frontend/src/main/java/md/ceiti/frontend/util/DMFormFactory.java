package md.ceiti.frontend.util;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasValueAndElement;
import com.vaadin.flow.component.formlayout.FormLayout;
import org.vaadin.crudui.form.impl.form.factory.DefaultCrudFormFactory;

import java.util.List;

public class DMFormFactory {
    public static <T> DefaultCrudFormFactory<T> getDefaultFormFactory(Class<T> domainType) {
        DefaultCrudFormFactory<T> formFactory = new DefaultCrudFormFactory<>(domainType) {
            @Override
            protected void configureForm(FormLayout formLayout, List<HasValueAndElement> fields) {
                Component nameField = (Component) fields.get(0);
                formLayout.setColspan(nameField, 1);
            }
        };

        formFactory.setShowNotifications(true);
        formFactory.setErrorListener(ErrorHandler::handle);
        formFactory.setUseBeanValidation(true);

        return formFactory;
    }
}
