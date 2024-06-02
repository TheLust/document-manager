package md.ceiti.frontend.component.impl;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import md.ceiti.frontend.component.CrudComponent;
import md.ceiti.frontend.component.DMFormFactory;
import md.ceiti.frontend.dto.institution.AccountDto;
import md.ceiti.frontend.dto.institution.CategoryDto;
import md.ceiti.frontend.service.impl.InstitutionAccountService;
import md.ceiti.frontend.service.impl.InstitutionCategoryService;
import org.vaadin.crudui.crud.impl.GridCrud;

import java.util.Collections;
import java.util.List;

public class InstitutionCategoriesCrud extends VerticalLayout implements CrudComponent<CategoryDto> {

    private final InstitutionCategoryService institutionCategoryService;
    private final boolean hasWriteAccess;

    public InstitutionCategoriesCrud(InstitutionCategoryService institutionCategoryService, boolean hasWriteAccess) {
        this.institutionCategoryService = institutionCategoryService;
        this.hasWriteAccess = hasWriteAccess;
        buildComponent();
    }

    @Override
    public void buildComponent() {
        add(getGridCrud());
    }

    @Override
    public GridCrud<CategoryDto> getGridCrud() {
        GridCrud<CategoryDto> crud = DMFormFactory.getDefaultCrud(
                CategoryDto.class,
                institutionCategoryService,
                List.of("id"),
                Collections.emptyList()
        );
        DMFormFactory.orderColumns(crud, CategoryDto.class, Collections.emptyList());
        crud.getAddButton().setVisible(hasWriteAccess);
        crud.getUpdateButton().setVisible(hasWriteAccess);
        crud.getDeleteButton().setVisible(hasWriteAccess);

        return crud;
    }
}
