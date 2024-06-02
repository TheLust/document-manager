package md.ceiti.frontend.component;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.HasValueAndElement;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.data.binder.Binder;
import lombok.Getter;
import md.ceiti.frontend.exception.FrontendException;
import org.vaadin.crudui.crud.CrudOperation;
import org.vaadin.crudui.form.CrudFormConfiguration;
import org.vaadin.crudui.form.impl.form.factory.DefaultCrudFormFactory;

@Getter
public class GenericCrudFormFactory<T> extends DefaultCrudFormFactory<T> {

    private FormLayout formLayout;
    private Binder<T> formBinder;

    public GenericCrudFormFactory(Class<T> domainType) {
        super(domainType);
    }

    @Override
    public Component buildNewForm(CrudOperation operation, T domainObject, boolean readOnly, ComponentEventListener<ClickEvent<Button>> cancelButtonClickListener, ComponentEventListener<ClickEvent<Button>> operationButtonClickListener) {
        Component component = super.buildNewForm(operation, domainObject, readOnly, cancelButtonClickListener, operationButtonClickListener);

        formLayout = (FormLayout) component.getChildren()
                .filter(comp -> comp instanceof FormLayout)
                .findFirst()
                .orElseThrow(() -> new FrontendException("Cannot find formLayout in genericCrudFormFactory"));

        formBinder = binder;



        return component;
    }

    @Override
    protected HasValueAndElement buildField(CrudFormConfiguration configuration,
                                            String property,
                                            Class<?> propertyType,
                                            T domainObject) throws InstantiationException, IllegalAccessException {
        HasValueAndElement field = super.buildField(configuration, property, propertyType, domainObject);
        field.getElement().setAttribute("id", property);

        return field;
    }

}
