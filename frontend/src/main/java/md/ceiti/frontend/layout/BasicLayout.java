package md.ceiti.frontend.layout;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import md.ceiti.frontend.dto.response.Profile;
import md.ceiti.frontend.exception.BadRequestException;
import md.ceiti.frontend.service.ProfileService;
import md.ceiti.frontend.util.ComponentUtils;
import md.ceiti.frontend.util.ErrorHandler;
import md.ceiti.frontend.util.NavigationUtils;
import md.ceiti.frontend.view.ApplicationView;
import md.ceiti.frontend.view.LoginView;
import md.ceiti.frontend.view.ProfileView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BasicLayout extends AppLayout {

    private Profile profile;

    @Autowired
    public BasicLayout(ProfileService profileService) {
        try {
            profile = profileService.getProfile();
        } catch (BadRequestException e) {
            ErrorHandler.handle(e);
            return;
        }

        buildLayout();
    }

    private void buildLayout() {
        DrawerToggle toggle = new DrawerToggle();

        H1 title = new H1("Application");
        title.getStyle()
                .set("font-size", "var(--lumo-font-size-l)")
                .set("margin", "0");

        addToNavbar(toggle, title, getProfileLayout());
    }

    private HorizontalLayout getProfileLayout() {
        Avatar avatar = ComponentUtils.getAvatar(profile);
        H1 username = new H1(profile.getUsername());
        username.getStyle()
                .set("font-size", "var(--lumo-font-size-l)")
                .set("margin", "0");

        HorizontalLayout container = new HorizontalLayout();
        container.setClassName("profile-layout");
        container.setAlignItems(FlexComponent.Alignment.CENTER);
        container.add(username, avatar);

        ContextMenu contextMenu = new ContextMenu();
        contextMenu.setTarget(container);
        contextMenu.setOpenOnClick(true);

        MenuItem profileMenuItem = contextMenu.addItem("Profile");
        profileMenuItem.addClickListener(menuItemClickEvent -> NavigationUtils.navigateTo(ProfileView.class));

        contextMenu.addItem("Info");
        contextMenu.add(new Hr());

        MenuItem signOutMenuItem = contextMenu.addItem("Sign out");
        signOutMenuItem.addClickListener(menuItemClickEvent -> NavigationUtils.navigateTo(LoginView.class));
        signOutMenuItem.getStyle().set("color", "red");

        return container;
    }
}
