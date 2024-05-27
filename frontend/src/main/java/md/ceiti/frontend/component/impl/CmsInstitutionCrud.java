package md.ceiti.frontend.component.impl;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.TextRenderer;
import lombok.Getter;
import lombok.Setter;
import md.ceiti.frontend.component.CrudComponent;
import md.ceiti.frontend.dto.CmsAccountDto;
import md.ceiti.frontend.dto.CmsInstitutionDto;
import md.ceiti.frontend.mapper.GenericMapper;
import md.ceiti.frontend.service.impl.CmsAccountService;
import md.ceiti.frontend.service.impl.CmsInstitutionService;
import md.ceiti.frontend.service.impl.GenericCrudService;
import md.ceiti.frontend.util.DMFormFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.vaadin.crudui.crud.impl.GridCrud;
import org.vaadin.crudui.form.impl.field.provider.ComboBoxProvider;
import org.vaadin.crudui.form.impl.form.factory.DefaultCrudFormFactory;

import java.util.List;

@Component
@Getter
@Setter
public class CmsInstitutionCrud extends VerticalLayout implements CrudComponent<CmsInstitutionDto> {

    private final GenericCrudService<CmsInstitutionService, CmsInstitutionDto> cmsInstitutionService;
    private final GenericCrudService<CmsAccountService, CmsAccountDto> cmsAccountService;
    private final GenericMapper genericMapper;
    private List<CmsInstitutionDto> elements;

    @Autowired
    public CmsInstitutionCrud(CmsInstitutionService cmsInstitutionService,
                              CmsAccountService cmsAccountService,
                              GenericMapper genericMapper) {
        this.cmsInstitutionService = new GenericCrudService<>(cmsInstitutionService);
        this.cmsAccountService = new GenericCrudService<>(cmsAccountService);
        this.genericMapper = genericMapper;

        buildComponent();
    }

    @Override
    public void buildComponent() {
        add(getGridCrud());
    }

    @Override
    public GridCrud<CmsInstitutionDto> getGridCrud() {
        GridCrud<CmsInstitutionDto> crud = getGridCrudWithFactory();

        crud.getGrid().setColumns("id", "name");
        crud.getGrid()
                .addColumn(response -> response.getMaster().getUsername()).setHeader("Master")
                .setSortable(true);
        crud.getGrid().getColumnByKey("id").setVisible(false);

        crud.setFindAllOperation(cmsInstitutionService::findAll);
        crud.setAddOperation(cmsInstitutionService::insert);
        crud.setUpdateOperation(cmsInstitutionService::update);
        crud.setDeleteOperation(cmsInstitutionService::delete);

        return crud;
    }

    @Override
    public GridCrud<CmsInstitutionDto> getGridCrudWithFactory() {
        DefaultCrudFormFactory<CmsInstitutionDto> formFactory = DMFormFactory.getDefaultFormFactory(
                CmsInstitutionDto.class
        );

        List<CmsAccountDto> accounts = cmsAccountService.findAll();
        formFactory.setVisibleProperties("name", "master");
        formFactory.setFieldProvider("master",
                new ComboBoxProvider<>(accounts));
        formFactory.setFieldProvider("master",
                new ComboBoxProvider<>("Master",
                        accounts,
                        new TextRenderer<>(CmsAccountDto::getUsername),
                        CmsAccountDto::getUsername)
        );

        return new GridCrud<>(CmsInstitutionDto.class, formFactory);
    }
}
