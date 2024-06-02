package md.ceiti.frontend.view.institution;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import md.ceiti.frontend.component.impl.InstitutionUsersCrud;
import md.ceiti.frontend.constant.I18n;
import md.ceiti.frontend.constant.Routes;
import md.ceiti.frontend.exception.BadRequestException;
import md.ceiti.frontend.exception.FrontendException;
import md.ceiti.frontend.layout.InstitutionLayout;
import md.ceiti.frontend.service.ProfileService;
import md.ceiti.frontend.service.impl.InstitutionAccountService;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Route(value = Routes.INSTITUTION_USERS, layout = InstitutionLayout.class)
@PageTitle(value = I18n.Page.INSTITUTION_USERS)
public class InstitutionUsersView extends VerticalLayout implements BeforeEnterObserver {

    private InstitutionAccountService institutionAccountService;
    private boolean hasWriteAccess = true;

    public InstitutionUsersView(ProfileService profileService) {
        try {
            hasWriteAccess = profileService.getProfile().isActive();
        } catch (BadRequestException ignored) {}
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        Optional<String> institutionId = beforeEnterEvent.getRouteParameters().get("institutionId");
        if (institutionId.isEmpty()) {
            throw new FrontendException("Cannot get institutionId");
        }

        this.institutionAccountService = new InstitutionAccountService(new RestTemplate(), institutionId.get());
        buildPage();
    }

    private void buildPage() {
        InstitutionUsersCrud crud = new InstitutionUsersCrud(institutionAccountService, hasWriteAccess);
        crud.setId("institution-users-crud");
        boolean crudAlreadyAdded = getChildren().anyMatch(component -> {
            if (component.getId().isEmpty()) {
                return false;
            }

            return component.getId().get().equals("institution-users-crud");
        });

        if (!crudAlreadyAdded) {
            add(crud);
        }
    }
}
