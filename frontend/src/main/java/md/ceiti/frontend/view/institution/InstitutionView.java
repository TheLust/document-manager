package md.ceiti.frontend.view.institution;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import md.ceiti.frontend.constant.I18n;
import md.ceiti.frontend.constant.Routes;
import md.ceiti.frontend.dto.institution.InstitutionDto;
import md.ceiti.frontend.exception.FrontendException;
import md.ceiti.frontend.layout.InstitutionLayout;
import md.ceiti.frontend.service.impl.InstitutionService;

import java.util.Optional;

@Route(value = Routes.INSTITUTION, layout = InstitutionLayout.class)
@PageTitle(value = I18n.Page.INSTITUTION)
public class InstitutionView extends VerticalLayout implements BeforeEnterObserver {

    private final InstitutionService institutionService;

    private InstitutionDto institutionDto;

    public InstitutionView(InstitutionService institutionService) {
        this.institutionService = institutionService;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        Optional<String> institutionId = beforeEnterEvent.getRouteParameters().get("institutionId");
        if (institutionId.isEmpty()) {
            throw new FrontendException("Cannot get institutionId");
        }

        this.institutionDto = institutionService.get(institutionId.get());
        buildPage();
    }

    public void buildPage() {
        add(new H2("institution = " + institutionDto.getName()));
    }
}
