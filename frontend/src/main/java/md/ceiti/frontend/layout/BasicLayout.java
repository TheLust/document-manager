package md.ceiti.frontend.layout;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import md.ceiti.frontend.constant.I18n;
import md.ceiti.frontend.dto.response.Profile;
import md.ceiti.frontend.exception.BadRequestException;
import md.ceiti.frontend.service.ImageService;
import md.ceiti.frontend.service.ProfileService;
import md.ceiti.frontend.util.ComponentUtils;
import md.ceiti.frontend.util.ErrorHandler;
import md.ceiti.frontend.util.NavigationUtils;
import md.ceiti.frontend.view.LoginView;
import md.ceiti.frontend.view.ProfileView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BasicLayout extends AppLayout {

    private final ProfileService profileService;

    @Autowired
    public BasicLayout(ProfileService profileService) {
        this.profileService = profileService;

        Profile profile;
        try {
            profile = profileService.getProfile();
        } catch (BadRequestException e) {
            ErrorHandler.handle(e);
            return;
        }

        buildLayout(profile);
    }

    private void buildLayout(Profile profile) {
        DrawerToggle toggle = new DrawerToggle();

        H1 title = new H1("Application");
        title.getStyle()
                .set("font-size", "var(--lumo-font-size-l)")
                .set("margin", "0");

        addToNavbar(toggle, title, getProfileLayout(profile));
    }

    private HorizontalLayout getProfileLayout(Profile profile) {
        Avatar avatar = ComponentUtils.getAvatar(profileService.getImage());
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

        MenuItem profileMenuItem = contextMenu.addItem(I18n.PROFILE);
        profileMenuItem.addClickListener(menuItemClickEvent -> NavigationUtils.navigateTo(ProfileView.class));

        contextMenu.addItem(I18n.FAQ);
        contextMenu.add(new Hr());

        ConfirmDialog signOutConfirmDialog = ComponentUtils.getGenericConfirmDialog(
                I18n.SIGN_OUT_HEADER,
                I18n.SIGN_OUT_TEXT
        );
        signOutConfirmDialog.setConfirmButton(I18n.SIGN_OUT, confirmEvent -> NavigationUtils.navigateTo(LoginView.class));
        MenuItem signOutMenuItem = contextMenu.addItem(I18n.SIGN_OUT);
        signOutMenuItem.addClickListener(menuItemClickEvent -> signOutConfirmDialog.open());
        signOutMenuItem.getStyle().set("color", "red");

        return container;
    }
}
