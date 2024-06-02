package md.ceiti.frontend.layout;

import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.theme.lumo.LumoUtility;
import md.ceiti.frontend.constant.I18n;
import md.ceiti.frontend.service.ProfileService;
import md.ceiti.frontend.view.cms.CmsInstitutionsView;
import md.ceiti.frontend.view.cms.CmsUsersView;

public class CmsLayout extends BasicLayout {

    public CmsLayout(ProfileService profileService) {
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
                new SideNavItem(I18n.Layout.INSTITUTIONS, CmsInstitutionsView.class,
                        VaadinIcon.INSTITUTION.create()),
                new SideNavItem(I18n.Layout.USERS, CmsUsersView.class,
                        VaadinIcon.GROUP.create())
        );
        return nav;
    }
}
