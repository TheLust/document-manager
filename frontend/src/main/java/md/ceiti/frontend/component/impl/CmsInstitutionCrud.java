package md.ceiti.frontend.component.impl;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.TextRenderer;
import lombok.Getter;
import lombok.Setter;
import md.ceiti.frontend.component.CrudComponent;
import md.ceiti.frontend.component.CustomCrudListener;
import md.ceiti.frontend.component.DMFormFactory;
import md.ceiti.frontend.component.GenericCrudFormFactory;
import md.ceiti.frontend.dto.CmsAccountDto;
import md.ceiti.frontend.dto.CmsInstitutionDto;
import md.ceiti.frontend.exception.BadRequestException;
import md.ceiti.frontend.mapper.GenericMapper;
import md.ceiti.frontend.service.impl.CmsAccountService;
import md.ceiti.frontend.service.impl.CmsInstitutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.vaadin.crudui.crud.impl.GridCrud;
import org.vaadin.crudui.form.impl.field.provider.ComboBoxProvider;

import java.util.ArrayList;
import java.util.List;

@Component
@Getter
@Setter
public class CmsInstitutionCrud extends VerticalLayout implements CrudComponent<CmsInstitutionDto> {

    private final CmsInstitutionService cmsInstitutionService;
    private final CmsAccountService cmsAccountService;
    private final GenericMapper genericMapper;
    private List<CmsInstitutionDto> elements;

    @Autowired
    public CmsInstitutionCrud(CmsInstitutionService cmsInstitutionService,
                              CmsAccountService cmsAccountService,
                              GenericMapper genericMapper) {
        this.cmsInstitutionService = cmsInstitutionService;
        this.cmsAccountService = cmsAccountService;
        this.genericMapper = genericMapper;

        buildComponent();
    }

    @Override
    public void buildComponent() {
        add(getGridCrud());
    }

    @Override
    public GridCrud<CmsInstitutionDto> getGridCrud() {
        GridCrud<CmsInstitutionDto> crud = DMFormFactory.getDefaultCrud(CmsInstitutionDto.class, cmsInstitutionService);
        DMFormFactory.setFieldProvider(crud, cmsAccountService, "master", "username");

        return crud;
    }
}
