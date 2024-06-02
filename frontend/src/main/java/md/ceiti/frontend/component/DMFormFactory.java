package md.ceiti.frontend.component;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.HasValueAndElement;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.TextRenderer;
import md.ceiti.frontend.constant.I18n;
import md.ceiti.frontend.exception.BadRequestException;
import md.ceiti.frontend.exception.CrudException;
import md.ceiti.frontend.exception.FrontendException;
import md.ceiti.frontend.service.CrudService;
import md.ceiti.frontend.util.ErrorHandler;
import org.apache.commons.lang3.ArrayUtils;
import org.modelmapper.ModelMapper;
import org.vaadin.crudui.crud.impl.GridCrud;
import org.vaadin.crudui.form.CrudFormFactory;
import org.vaadin.crudui.form.impl.field.provider.ComboBoxProvider;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class DMFormFactory {

    public static <T, G extends CrudService<T>> GridCrud<T> getDefaultCrud(Class<T> domainType,
                                                                           G service,
                                                                           List<String> hiddenColumns,
                                                                           List<String> excludedFields) {
        Field[] fields = domainType.getDeclaredFields();
        String[] formFields = ArrayUtils.remove(getSortedFieldNames(fields, excludedFields), 0);
        String[] gridColumns = Arrays.stream(fields)
                .filter(field -> (
                        String.class.equals(field.getType())
                        || Integer.class.equals(field.getType())
                        || UUID.class.equals(field.getType())
                        || Double.class.equals(field.getType())
                        || Float.class.equals(field.getType())
                        || Long.class.equals(field.getType())
                        || LocalDate.class.equals(field.getType())
                ))
                .map(Field::getName)
                .toArray(String[]::new);
        String[] booleanGridColumns = Arrays.stream(fields)
                .filter(field -> boolean.class.equals(field.getType()))
                .map(Field::getName)
                .toArray(String[]::new);

        for (String field : excludedFields) {
            gridColumns = ArrayUtils.removeAllOccurrences(gridColumns, field);
            booleanGridColumns = ArrayUtils.removeAllOccurrences(booleanGridColumns, field);
        }

        GridCrud<T> crud = new GridCrud<>(domainType, getDefaultFormFactory(domainType, formFields));

        crud.getGrid().removeAllColumns();
        crud.getGrid().setColumns(gridColumns);
        hiddenColumns.forEach(field ->
                crud.getGrid().getColumnByKey(field).setVisible(false)
        );

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
                    .setSortable(true)
                    .setKey(booleanColumn);
        }

        crud.setCrudListener(new GenericCrudListener<>(service, crud));

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
            if (e instanceof CrudException crudException) {
                if (crudException.getMessage() != null && !crudException.getMessage().isBlank()) {
                    Notification.show(crudException.getMessage());
                } else {
                    Notification.show("Please fix the errors and try again");
                }
            } else {
                ErrorHandler.handle(e);
            }
        });
        formFactory.setUseBeanValidation(true);
        formFactory.setVisibleProperties(visibleFields);

        return formFactory;
    }

    public static <T extends CrudService<G>, G, H> void setFieldProvider(GridCrud<?> crud,
                                                                         T service,
                                                                         String field,
                                                                         String propertyName,
                                                                         Class<H> type) {
        ModelMapper mapper = new ModelMapper();
        List<H> data = new ArrayList<>();
        try {
            data = service.findAll()
                    .stream()
                    .map(entity -> mapper.map(entity, type))
                    .toList();
        } catch (BadRequestException ignored) {}

        setFieldProvider(crud, data, field, propertyName);
    }

    public static <T> void setFieldProvider(GridCrud<?> crud,
                                            List<T> data,
                                            String field,
                                            String propertyName) {
        crud.getGrid()
                .addColumn(object -> getStringValue(getValue(object, field), propertyName))
                .setHeader(convertCamelCaseToTitle(field))
                .setSortable(true)
                .setKey(field);
        crud.getCrudFormFactory()
                .setFieldProvider(field,
                        new ComboBoxProvider<>(data));
        crud.getCrudFormFactory()
                .setFieldProvider(
                        field,
                        new ComboBoxProvider<>(convertCamelCaseToTitle(field),
                                data,
                                new TextRenderer<>(object -> getStringValue(object, propertyName)),
                                object -> getStringValue(object, propertyName))
                );
    }

    public static <T> void orderColumns(GridCrud<T> crud,
                                        Class<T> domainType,
                                        List<String> excludedFields) {
        Field[] fields = domainType.getDeclaredFields();
        String[] columnNames = getSortedFieldNames(fields, excludedFields);

        List<Grid.Column<T>> columns = crud.getGrid().getColumns();
        List<Grid.Column<T>> sortedColumns = new ArrayList<>();

        if (columns.size() != Arrays.stream(columnNames).toList().size()) {
            throw new FrontendException("Column number does not match check configuration");
        }

        for (String columnName : columnNames) {
            Grid.Column<T> column = columns.stream()
                    .filter(tColumn -> columnName.equals(tColumn.getKey()))
                    .findFirst()
                    .orElseThrow(() -> new FrontendException("Could not find column with column name"));
            sortedColumns.add(column);
        }

        crud.getGrid().setColumnOrder(sortedColumns);
    }

    public static <T> FormLayout getForm(CrudFormFactory<T> crudFormFactory) {
        if (crudFormFactory instanceof GenericCrudFormFactory<T> genericCrudFormFactory) {
            return genericCrudFormFactory.getFormLayout();
        }

        throw new FrontendException("crudFormFactory is not generic/custom to extract the binder");
    }

    public static <T> Binder<T> getBinder(CrudFormFactory<T> crudFormFactory) {
        if (crudFormFactory instanceof GenericCrudFormFactory<T> genericCrudFormFactory) {
            return genericCrudFormFactory.getFormBinder();
        }

        throw new FrontendException("crudFormFactory is not generic/custom to extract the binder");
    }


    @SuppressWarnings("unchecked")
    public static <T, G> void addConditionalField(GridCrud<T> crud,
                                                  String parentName,
                                                  String childName,
                                                  List<G> expectedValues,
                                                  Class<G> expectedType) {
        Runnable runnable = () -> {
            Binder<T> binder = getBinder(crud.getCrudFormFactory());
            Optional<Binder.Binding<T, ?>> parentBinding = binder.getBinding(parentName);
            if (parentBinding.isEmpty()) {
                throw new FrontendException("Cannot find the binding for property with name = " + parentName);
            }

            AbstractField<?, ?> parentField = getAbstractFieldFromBinder(parentBinding.get());

            Optional<Binder.Binding<T, ?>> childBinding = binder.getBinding(childName);
            if (childBinding.isEmpty()) {
                throw new FrontendException("Cannot find the binding for property with name = " + childName);
            }

            AbstractField<?, ?> childField = getAbstractFieldFromBinder(childBinding.get());
            if (parentField.getValue() != null) {
                if (parentField.getValue().getClass().equals(expectedType)) {
                    childField.setVisible(expectedValues.contains((G) parentField.getValue()));
                }
            } else {
                childField.setVisible(false);
            }

            parentField.addValueChangeListener(gComponentValueChangeEvent -> {
                if (gComponentValueChangeEvent.getValue().getClass().equals(expectedType)) {
                    G value = (G) gComponentValueChangeEvent.getValue();
                    childField.setVisible(expectedValues.contains(value));
                }
            });
        };

        crud.getAddButton().addClickListener(event -> runnable.run());
        crud.getUpdateButton().addClickListener(event -> runnable.run());
        crud.getDeleteButton().addClickListener(event -> runnable.run());
    }

    public static AbstractField<?, ?> getAbstractFieldFromBinder(Binder.Binding<?, ?> binding) {
        HasValue<?, ?> hasValue = binding.getField();
        return (AbstractField<?, ?>) hasValue;
    }

    private static String[] getSortedFieldNames(Field[] fields, List<String> excludedFields) {
        return Arrays.stream(fields)
                .filter(field -> !excludedFields.contains(field.getName()))
                .sorted((o1, o2) -> {
                    int priority1 = getTypePriority(o1.getType());
                    int priority2 = getTypePriority(o2.getType());

                    if (o1.getName().equals("id")) {
                        priority1 = 0;
                    }
                    if (o2.getName().equals("id")) {
                        priority2 = 0;
                    }

                    return Integer.compare(priority1, priority2);
                })
                .map(Field::getName)
                .toArray(String[]::new);
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
            if (object == null) {
                return I18n.Component.NONE;
            }

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
        String result = camelCaseString.replaceAll("([a-z])([A-Z])", "$1 $2");
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

    private static int getTypePriority(Class<?> type) {
        if (type.equals(String.class)) {
            return 1;
        } else if (type.equals(Long.class)
                || type.equals(Integer.class)
                || type.equals(Double.class)
                || type.equals(Float.class)
                || type.equals(LocalDate.class)) {
            return 2;
        } else if (type.equals(Boolean.class)
                || type.equals(boolean.class)) {
            return 4;
        } else {
            // Default priority for other types
            return 3;
        }
    }
}
