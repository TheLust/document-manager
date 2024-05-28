package md.ceiti.frontend.component;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasValueAndElement;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.TextRenderer;
import md.ceiti.frontend.exception.BadRequestException;
import md.ceiti.frontend.exception.CrudException;
import md.ceiti.frontend.exception.FrontendException;
import md.ceiti.frontend.service.CrudService;
import md.ceiti.frontend.util.ErrorHandler;
import org.vaadin.crudui.crud.impl.GridCrud;
import org.vaadin.crudui.form.CrudFormFactory;
import org.vaadin.crudui.form.impl.field.provider.ComboBoxProvider;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DMFormFactory {

    public static <T, G extends CrudService<T>> GridCrud<T> getDefaultCrud(Class<T> domainType, G service) {
        Field[] fields = domainType.getDeclaredFields();
        String[] formFields = Arrays.stream(fields)
                .map(Field::getName)
                .filter(name -> !"id".equals(name))
                .toArray(String[]::new);
        String[] gridColumns = Arrays.stream(fields)
                .filter(field -> (
                        String.class.equals(field.getType())
                        || Integer.class.equals(field.getType())
                        || Double.class.equals(field.getType())
                        || Float.class.equals(field.getType())
                        || Long.class.equals(field.getType())
                ))
                .map(Field::getName)
                .toArray(String[]::new);
        String[] booleanGridColumns = Arrays.stream(fields)
                .filter(field -> boolean.class.equals(field.getType()))
                .map(Field::getName)
                .toArray(String[]::new);

        GridCrud<T> crud = new GridCrud<>(domainType, getDefaultFormFactory(domainType, formFields));

        crud.getGrid().removeAllColumns();
        crud.getGrid().setColumns(gridColumns);
        crud.getGrid().getColumnByKey("id").setVisible(false);

        for (String booleanColumn: booleanGridColumns) {
            crud.getGrid().addColumn(new ComponentRenderer<>(item -> {
                Icon icon;
                if (getBooleanValue(item, booleanColumn)) {
                    icon = VaadinIcon.CHECK.create();
                    icon.setColor("green");
                } else {
                    icon = VaadinIcon.CLOSE.create();
                    icon.setColor("red");
                }
                return icon;
            })).setHeader(convertCamelCaseToTitle(booleanColumn))
                    .setSortable(true);
        }

        crud.setCrudListener(new CustomCrudListener<>(service, crud));

        return crud;
    }

    private static <T> GenericCrudFormFactory<T> getDefaultFormFactory(Class<T> domainType, String[] visibleFields) {
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
        formFactory.setVisibleProperties(visibleFields);

        return formFactory;
    }

    public static <T extends CrudService<G>, G> void setFieldProvider(GridCrud<?> crud,
                                                                      T service,
                                                                      String field,
                                                                      String propertyName) {
        List<G> data = new ArrayList<>();
        try {
            data = service.findAll();
        } catch (BadRequestException ignored) {}

        crud.getGrid()
                .addColumn(object -> getStringValue(getValue(object, field), propertyName))
                .setHeader(convertCamelCaseToTitle(field));
        crud.getCrudFormFactory()
                .setFieldProvider(field, new ComboBoxProvider<>(data));
        crud.getCrudFormFactory()
                .setFieldProvider(field, new ComboBoxProvider<>(convertCamelCaseToTitle(field),
                        data,
                        new TextRenderer<>(object -> getStringValue(object, propertyName)),
                        object -> getStringValue(object, propertyName))
                );
    }

    public static <T> Binder<T> getBinder(CrudFormFactory<T> crudFormFactory) {
        if (crudFormFactory instanceof GenericCrudFormFactory<T> genericCrudFormFactory) {
            return genericCrudFormFactory.getFormBinder();
        }

        throw new FrontendException("crudFormFactory is not generic/custom to extract the binder");
    }

    private static <T> boolean getBooleanValue(T object, String propertyName) {
        try {
            Field field = object.getClass().getDeclaredField(propertyName);
            field.setAccessible(true);

            if (!field.getType().equals(boolean.class)) {
                throw new IllegalArgumentException("Field '" + propertyName + "' is not of type boolean");
            }

            return field.getBoolean(object);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new FrontendException("Can't get generic boolean value");
        }
    }

    private static <T> String getStringValue(T object, String propertyName) {
        try {
            Field field = object.getClass().getDeclaredField(propertyName);
            field.setAccessible(true);

            if (!String.class.equals(field.getType())) {
                throw new IllegalArgumentException("Field '" + propertyName + "' is not of type boolean");
            }

            return (String) field.get(object);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new FrontendException("Can't get generic string value");
        }
    }

    private static <T> Object getValue(T object, String propertyName) {
        try {
            Field field = object.getClass().getDeclaredField(propertyName);
            field.setAccessible(true);
            return field.get(object);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new FrontendException("Can't get generic string value");
        }
    }

    private static String convertCamelCaseToTitle(String camelCaseString) {
        if (camelCaseString == null || camelCaseString.isEmpty()) {
            return camelCaseString;
        }

        // Add a space before each uppercase letter
        String result = camelCaseString.replaceAll("([a-z])([A-Z])", "$1 $2");

        // Capitalize the first letter of each word
        result = capitalizeFully(result);

        return result;
    }

    private static String capitalizeFully(String str) {
        String[] words = str.split("\\s");
        StringBuilder capitalizedString = new StringBuilder();

        for (String word : words) {
            if (!word.isEmpty()) {
                capitalizedString.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1).toLowerCase())
                        .append(" ");
            }
        }

        return capitalizedString.toString().trim();
    }
}
