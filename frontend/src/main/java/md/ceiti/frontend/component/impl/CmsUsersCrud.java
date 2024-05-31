package md.ceiti.frontend.component.impl;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.TextRenderer;
import lombok.Getter;
import lombok.Setter;
import md.ceiti.frontend.component.CrudComponent;
import md.ceiti.frontend.component.DMFormFactory;
import md.ceiti.frontend.constant.fields.CmsAccountDtoFields;
import md.ceiti.frontend.dto.CmsAccountDto;
import md.ceiti.frontend.dto.CmsInstitutionDto;
import md.ceiti.frontend.dto.Role;
import md.ceiti.frontend.mapper.GenericMapper;
import md.ceiti.frontend.service.RoleService;
import md.ceiti.frontend.service.impl.CmsAccountService;
import md.ceiti.frontend.service.impl.CmsInstitutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.vaadin.crudui.crud.impl.GridCrud;
import org.vaadin.crudui.form.impl.field.provider.ComboBoxProvider;

import java.util.Arrays;
import java.util.List;

@Component
@Getter
@Setter
public class CmsUsersCrud extends VerticalLayout implements CrudComponent<CmsAccountDto> {

    private final CmsAccountService cmsAccountService;
    private final CmsInstitutionService cmsInstitutionService;
    private final RoleService roleService;
    private final GenericMapper genericMapper;

    @Autowired
    public CmsUsersCrud(CmsAccountService cmsAccountService,
                        CmsInstitutionService cmsInstitutionService,
                        RoleService roleService,
                        GenericMapper genericMapper) {
        this.cmsInstitutionService = cmsInstitutionService;
        this.cmsAccountService = cmsAccountService;
        this.roleService = roleService;
        this.genericMapper = genericMapper;

        buildComponent();
    }

    @Override
    public void buildComponent() {
        add(getGridCrud());
    }

    @Override
    public GridCrud<CmsAccountDto> getGridCrud() {
        GridCrud<CmsAccountDto> crud = DMFormFactory.getDefaultCrud(
                CmsAccountDto.class,
                cmsAccountService,
                List.of("id", "password"),
                List.of("image")
        );

        DMFormFactory.setFieldProvider(crud, roleService, "role", "label");
        DMFormFactory.setFieldProvider(crud, cmsInstitutionService, "institution", "name");
        DMFormFactory.orderColumns(crud, CmsAccountDto.class, List.of("image"));
//        FormLayout form = DMFormFactory.getFormLayout(crud.getCrudFormFactory());

        return crud;
    }
}
