package md.ceiti.frontend.util;

import com.vaadin.flow.server.VaadinService;
import jakarta.servlet.http.Cookie;

public class JwtUtils {

    public static void setHttpOnlyJwtCookie(String token) {
        Cookie jwtCookie = new Cookie("token", token);
        jwtCookie.setPath("/");
        jwtCookie.setHttpOnly(true);
        jwtCookie.setMaxAge(24 * 60 * 60);
        VaadinService.getCurrentResponse().addCookie(jwtCookie);
    }

    public static void setHttpOnlyJwtCookie(String token, Runnable callback) {
        setHttpOnlyJwtCookie(token);
        callback.run();
    }

    public static String getJwtTokenFromCookie() {
        return CookiesManager.get("token")
                .orElse(null);
    }

    public static void deleteJwtCookie() {
        CookiesManager.delete("token");
    }
}
