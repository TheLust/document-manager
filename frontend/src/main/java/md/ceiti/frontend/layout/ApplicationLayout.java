package md.ceiti.frontend.layout;

import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.theme.lumo.LumoUtility;
import md.ceiti.frontend.service.ImageService;
import md.ceiti.frontend.service.ProfileService;

public class ApplicationLayout extends BasicLayout {

    public ApplicationLayout(ProfileService profileService) {
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
                new SideNavItem("Institutions", "application/institutions",
                        VaadinIcon.DASHBOARD.create()),
                new SideNavItem("Users", "application/users",
                        VaadinIcon.CART.create())
        );
        return nav;
    }
}
