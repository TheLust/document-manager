package md.ceiti.frontend.util;

import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.notification.Notification;
import md.ceiti.frontend.dto.Image;
import md.ceiti.frontend.dto.response.Profile;

import java.util.Optional;
import java.util.UUID;

public class ComponentUtils {

    public static Avatar getAvatar(Profile profile) {
        Avatar avatar = new Avatar();
        avatar.setImage(getProfileImageEndpoint(profile.getImage()));
        avatar.setClassName("profile-avatar");
        avatar.getElement().addEventListener("mouseover", event -> avatar.setImage("https://cdn-icons-png.flaticon.com/512/6065/6065488.png"));
        avatar.getElement().addEventListener("mouseout", event -> avatar.setImage(getProfileImageEndpoint(profile.getImage())));

        return avatar;
    }

    public static String getProfileImageEndpoint(Image image) {
        if (image == null) {
            return null;
        }

        return ApiUtils.IMAGES_ENDPOINT + "?uuid=" + image.getId();
    }

}
