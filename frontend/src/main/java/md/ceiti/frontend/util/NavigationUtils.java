package md.ceiti.frontend.util;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.RouteConfiguration;
import md.ceiti.frontend.dto.response.Profile;
import md.ceiti.frontend.view.CmsView;
import md.ceiti.frontend.view.InstitutionView;

public class NavigationUtils {
    /**
     * Should be used only for after login navigation cuz it forces the reload of the page
     * */
    public static void navigateToHomePage(Profile profile) {
        switch (profile.getRole()) {
            case MASTER ->  {
                CookiesManager.set("homePage", RouteConfiguration.forSessionScope().getUrl(CmsView.class));
                setLocation(CmsView.class);
            }
            case INSTITUTION_MASTER, INSTITUTION_USER -> {
                CookiesManager.set("homePage", RouteConfiguration.forSessionScope().getUrl(InstitutionView.class));
                setLocation(InstitutionView.class);
            }
        }
    }

    public static void setLocation(Class<? extends Component> clazz) {
        String uri = RouteConfiguration.forSessionScope().getUrl(clazz);
        setLocation(uri);
    }

    public static void setLocation(String uri) {
        UI.getCurrent().getPage().setLocation(uri);
    }

    /**
     * Should be used by default for navigating through pages
     * */
    public static void navigateToHomePage(boolean force) {
        String homePage = CookiesManager.get("homePage").orElse("login");
        if (force) {
            setLocation(homePage);
        } else {
            navigateTo(homePage);
        }
    }

    public static void navigateToHomePage() {
        navigateToHomePage(false);
    }

    public static void navigateTo(Class<? extends Component> clazz) {
        UI.getCurrent().navigate(clazz);
    }

    public static void navigateTo(String uri) {
        UI.getCurrent().navigate(uri);
    }
}
