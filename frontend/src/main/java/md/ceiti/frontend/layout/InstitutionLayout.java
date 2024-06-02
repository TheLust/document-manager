package md.ceiti.frontend.layout;

import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.RouteParam;
import com.vaadin.flow.router.RouteParameters;
import com.vaadin.flow.theme.lumo.LumoUtility;
import md.ceiti.frontend.constant.I18n;
import md.ceiti.frontend.constant.Routes;
import md.ceiti.frontend.dto.Role;
import md.ceiti.frontend.service.ProfileService;
import md.ceiti.frontend.view.institution.InstitutionCategoriesView;
import md.ceiti.frontend.view.institution.InstitutionDocumentsView;
import md.ceiti.frontend.view.institution.InstitutionUsersView;
import md.ceiti.frontend.view.institution.InstitutionView;

public class InstitutionLayout extends BasicLayout {

    public InstitutionLayout(ProfileService profileService) {
        super(profileService);
        buildLayout();
    }

    private void buildLayout() {
        SideNav nav = getSideNav();
        Scroller scroller = new Scroller(nav);
        scroller.setClassName(LumoUtility.Padding.SMALL);
        addToDrawer(scroller);

        setPrimarySection(Section.DRAWER);
    }

    private SideNav getSideNav() {
        SideNav nav = new SideNav();
        nav.addItem(
                new SideNavItem(I18n.Layout.INSTITUTION,
                        InstitutionView.class,
                        new RouteParameters(
                                new RouteParam(Routes.Params.INSTITUTION_ID, profile.getInstitution().getId())),
                        VaadinIcon.INSTITUTION.create()));
        if (Role.INSTITUTION_MASTER.equals(profile.getRole())) {
            nav.addItem(new SideNavItem(I18n.Layout.USERS,
                    InstitutionUsersView.class,
                    new RouteParameters(
                            new RouteParam(Routes.Params.INSTITUTION_ID, profile.getInstitution().getId())),
                    VaadinIcon.GROUP.create()));
            nav.addItem(new SideNavItem(I18n.Layout.CATEGORIES,
                    InstitutionCategoriesView.class,
                    new RouteParameters(
                            new RouteParam(Routes.Params.INSTITUTION_ID, profile.getInstitution().getId())),
                    VaadinIcon.FOLDER.create()));
        }
        nav.addItem(new SideNavItem(I18n.Layout.DOCUMENTS,
                InstitutionDocumentsView.class,
                new RouteParameters(
                        new RouteParam(Routes.Params.INSTITUTION_ID, profile.getInstitution().getId())),
                VaadinIcon.LINES.create()));

        return nav;
    }
}
