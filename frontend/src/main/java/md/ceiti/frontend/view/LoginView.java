package md.ceiti.frontend.view;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import md.ceiti.frontend.dto.request.LoginRequest;
import md.ceiti.frontend.dto.response.Profile;
import md.ceiti.frontend.exception.BadRequestException;
import md.ceiti.frontend.exception.FrontendException;
import md.ceiti.frontend.service.AuthService;
import md.ceiti.frontend.service.ProfileService;
import md.ceiti.frontend.util.CookiesManager;
import md.ceiti.frontend.util.ErrorHandler;
import md.ceiti.frontend.util.JwtUtils;
import md.ceiti.frontend.util.NavigationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.ResourceAccessException;

@Route(value = "login")
@PageTitle(value = "Login")
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

    private final AuthService authService;
    private final ProfileService profileService;

    private final LoginForm loginForm = new LoginForm();

    @Autowired
    public LoginView(AuthService authService, ProfileService profileService) {
        this.authService = authService;
        this.profileService = profileService;
        buildView();
    }

    private void buildView() {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.AROUND);

        loginForm.addLoginListener(event -> {
            if (!event.getUsername().isBlank() && !event.getPassword().isBlank()) {
                try {
                    LoginRequest loginRequest = new LoginRequest();
                    loginRequest.setUsername(event.getUsername());
                    loginRequest.setPassword(event.getPassword());
                    String token = authService.login(loginRequest);

                    if (token != null) {
                        JwtUtils.setHttpOnlyJwtCookie(token);

                        Profile profile = profileService.getProfile(token);
                        NavigationUtils.navigateToHomePage(profile);
                    }

                } catch (BadRequestException | ResourceAccessException e) {
                    event.getSource().setError(true);

                    if (e instanceof ResourceAccessException) {
                        UI.getCurrent().navigate(ErrorView.class);
                    }
                }
            }

            event.getSource().onEnabledStateChanged(true);
        });

        add(loginForm);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        JwtUtils.deleteJwtCookie();
        CookiesManager.delete("homePage");
    }
}
