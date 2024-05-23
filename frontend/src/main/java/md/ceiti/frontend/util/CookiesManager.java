package md.ceiti.frontend.util;

import com.vaadin.flow.server.VaadinRequest;
import com.vaadin.flow.server.VaadinService;
import jakarta.servlet.http.Cookie;

import java.util.Optional;

public class CookiesManager {

    public static void set(String name, String value) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setMaxAge(24 * 60 * 60);
        VaadinService.getCurrentResponse().addCookie(cookie);
    }

    public static Optional<String> get(String name) {
        VaadinRequest vaadinRequest = VaadinService.getCurrentRequest();
        if (vaadinRequest == null) {
            return Optional.empty();
        }

        Cookie[] cookies = vaadinRequest.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return Optional.of(cookie.getValue());
                }
            }
        }
        return Optional.empty();
    }

    public static void delete(String name) {
        Cookie[] cookies = VaadinService.getCurrentRequest().getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    cookie.setMaxAge(0);
                    cookie.setPath("/");
                    VaadinService.getCurrentResponse().addCookie(cookie);
                    return;
                }
            }
        }
    }
}
