package md.ceiti.frontend.component;

import org.vaadin.crudui.crud.impl.GridCrud;

public interface CrudComponent<T> {

    void buildComponent();
    GridCrud<T> getGridCrud();
}
