package md.ceiti.frontend.component.impl;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import lombok.Getter;
import lombok.Setter;
import md.ceiti.frontend.component.CrudComponent;
import md.ceiti.frontend.component.DMFormFactory;
import md.ceiti.frontend.dto.cms.CmsInstitutionDto;
import md.ceiti.frontend.dto.cms.SimpleAccountDto;
import md.ceiti.frontend.mapper.GenericMapper;
import md.ceiti.frontend.service.impl.CmsAccountService;
import md.ceiti.frontend.service.impl.CmsInstitutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.vaadin.crudui.crud.impl.GridCrud;

import java.util.Collections;
import java.util.List;

@Component
@Getter
@Setter
public class CmsInstitutionCrud extends VerticalLayout implements CrudComponent<CmsInstitutionDto> {

    private final CmsInstitutionService cmsInstitutionService;
    private final CmsAccountService cmsAccountService;
    private final GenericMapper genericMapper;

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
        GridCrud<CmsInstitutionDto> crud = DMFormFactory.getDefaultCrud(
                CmsInstitutionDto.class,
                cmsInstitutionService,
                List.of("id"),
                Collections.emptyList()
        );
        DMFormFactory.setFieldProvider(crud,
                cmsAccountService,
                "master",
                "username",
                SimpleAccountDto.class);
        DMFormFactory.orderColumns(crud, CmsInstitutionDto.class, Collections.emptyList());

        return crud;
    }
}
