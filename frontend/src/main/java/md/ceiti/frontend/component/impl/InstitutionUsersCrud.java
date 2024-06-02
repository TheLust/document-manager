package md.ceiti.frontend.component.impl;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import md.ceiti.frontend.component.CrudComponent;
import md.ceiti.frontend.component.DMFormFactory;
import md.ceiti.frontend.dto.institution.AccountDto;
import md.ceiti.frontend.service.impl.InstitutionAccountService;
import org.vaadin.crudui.crud.impl.GridCrud;

import java.util.List;

public class InstitutionUsersCrud extends VerticalLayout implements CrudComponent<AccountDto> {

    private final InstitutionAccountService institutionAccountService;
    private final boolean hasWriteAccess;

    public InstitutionUsersCrud(InstitutionAccountService institutionAccountService, boolean hasWriteAccess) {
        this.institutionAccountService = institutionAccountService;
        this.hasWriteAccess = hasWriteAccess;
        buildComponent();
    }

    @Override
    public void buildComponent() {
        add(getGridCrud());
    }

    @Override
    public GridCrud<AccountDto> getGridCrud() {
        GridCrud<AccountDto> crud = DMFormFactory.getDefaultCrud(
                AccountDto.class,
                institutionAccountService,
                List.of("id", "password"),
                List.of("image", "enabled")
        );
        DMFormFactory.orderColumns(crud, AccountDto.class, List.of("image", "enabled"));
        crud.getAddButton().setVisible(hasWriteAccess);
        crud.getUpdateButton().setVisible(hasWriteAccess);
        crud.getDeleteButton().setVisible(hasWriteAccess);

        return crud;
    }
}
