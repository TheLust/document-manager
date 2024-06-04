package md.ceiti.frontend.layout;

import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.RouteParam;
import com.vaadin.flow.router.RouteParameters;
import com.vaadin.flow.theme.lumo.LumoUtility;
import md.ceiti.frontend.constant.I18n;
import md.ceiti.frontend.constant.Routes;
import md.ceiti.frontend.dto.Role;
import md.ceiti.frontend.exception.FrontendException;
import md.ceiti.frontend.service.ProfileService;
import md.ceiti.frontend.view.institution.InstitutionCategoriesView;
import md.ceiti.frontend.view.institution.InstitutionDocumentsView;
import md.ceiti.frontend.view.institution.InstitutionUsersView;
import md.ceiti.frontend.view.institution.InstitutionView;

import java.util.Optional;

public class InstitutionLayout extends BasicLayout implements BeforeEnterObserver {

    public InstitutionLayout(ProfileService profileService) {
        super(profileService);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        Optional<String> institutionId = beforeEnterEvent.getRouteParameters().get(Routes.Params.INSTITUTION_ID);
        buildLayout(
                institutionId.orElseThrow(() -> new FrontendException("Can't get id param"))
        );
    }

    private void buildLayout(String institutionId) {
        boolean alreadyAdded = getChildren().anyMatch(component -> component.getId().isPresent()
                && component.getId().get().equals("my-scroller"));
        if (alreadyAdded) {
            return;
        }

        SideNav nav = getSideNav(institutionId);
        Scroller scroller = new Scroller(nav);
        scroller.setClassName(LumoUtility.Padding.SMALL);
        scroller.setId("my-scroller");

        addToDrawer(scroller);

        setPrimarySection(Section.DRAWER);
    }

    private SideNav getSideNav(String institutionId) {
        SideNav nav = new SideNav();
        nav.addItem(
                new SideNavItem(I18n.Layout.INSTITUTION,
                        InstitutionView.class,
                        new RouteParameters(
                                new RouteParam(Routes.Params.INSTITUTION_ID, institutionId)),
                        VaadinIcon.INSTITUTION.create()));
        if (Role.INSTITUTION_MASTER.equals(profile.getRole()) || Role.MASTER.equals(profile.getRole())) {
            nav.addItem(new SideNavItem(I18n.Layout.USERS,
                    InstitutionUsersView.class,
                    new RouteParameters(
                            new RouteParam(Routes.Params.INSTITUTION_ID, institutionId)),
                    VaadinIcon.GROUP.create()));
            nav.addItem(new SideNavItem(I18n.Layout.CATEGORIES,
                    InstitutionCategoriesView.class,
                    new RouteParameters(
                            new RouteParam(Routes.Params.INSTITUTION_ID, institutionId)),
                    VaadinIcon.FOLDER.create()));
        }
        nav.addItem(new SideNavItem(I18n.Layout.DOCUMENTS,
                InstitutionDocumentsView.class,
                new RouteParameters(
                        new RouteParam(Routes.Params.INSTITUTION_ID, institutionId)),
                VaadinIcon.LINES.create()));

        return nav;
    }
}
