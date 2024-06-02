package md.ceiti.frontend.component.impl;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import lombok.Getter;
import lombok.Setter;
import md.ceiti.frontend.component.CrudComponent;
import md.ceiti.frontend.component.DMFormFactory;
import md.ceiti.frontend.dto.cms.CmsAccountDto;
import md.ceiti.frontend.dto.Role;
import md.ceiti.frontend.dto.cms.SimpleInstitutionDto;
import md.ceiti.frontend.mapper.GenericMapper;
import md.ceiti.frontend.service.impl.CmsAccountService;
import md.ceiti.frontend.service.impl.CmsInstitutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.vaadin.crudui.crud.impl.GridCrud;

import java.util.Arrays;
import java.util.List;

@Component
@Getter
@Setter
public class CmsUsersCrud extends VerticalLayout implements CrudComponent<CmsAccountDto> {

    private final CmsAccountService cmsAccountService;
    private final CmsInstitutionService cmsInstitutionService;
    private final GenericMapper genericMapper;

    @Autowired
    public CmsUsersCrud(CmsAccountService cmsAccountService,
                        CmsInstitutionService cmsInstitutionService,
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
    public GridCrud<CmsAccountDto> getGridCrud() {
        GridCrud<CmsAccountDto> crud = DMFormFactory.getDefaultCrud(
                CmsAccountDto.class,
                cmsAccountService,
                List.of("id", "password"),
                List.of("image")
        );

        DMFormFactory.setFieldProvider(crud,
                cmsInstitutionService,
                "institution",
                "name",
                SimpleInstitutionDto.class);
        DMFormFactory.setFieldProvider(crud, Arrays.stream(Role.values()).toList(), "role", "label");
        DMFormFactory.orderColumns(crud, CmsAccountDto.class, List.of("image"));
        DMFormFactory.addConditionalField(
                crud,
                "role",
                "institution",
                List.of(Role.INSTITUTION_MASTER, Role.INSTITUTION_USER),
                Role.class);
        return crud;
    }
}
