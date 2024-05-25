package md.ceiti.frontend.component;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.StreamResource;

@Tag("profile-avatar")
public class ProfileAvatar extends Div {

    protected Avatar avatar = new Avatar();
    private StreamResource streamResource;
    private StreamResource hoverStreamResource;
    private String hoverImageUrl;

    public ProfileAvatar(StreamResource streamResource) {
        super();
        this.add(avatar);
        this.streamResource = streamResource;
        avatar.setImageResource(this.streamResource);
    }

    public ProfileAvatar(StreamResource streamResource, StreamResource hoverStreamResource) {
        this(streamResource);
        this.hoverStreamResource = hoverStreamResource;
        getElement().addEventListener("mouseover",
                event -> avatar.setImageResource(this.hoverStreamResource));
        getElement().addEventListener("mouseout",
                event -> avatar.setImageResource(this.streamResource));
        getElement().addEventListener("click",
                event -> avatar.setImageResource(this.streamResource));
    }

    public ProfileAvatar(StreamResource streamResource, String hoverImageUrl) {
        this(streamResource);
        this.hoverImageUrl = hoverImageUrl;
        getElement().addEventListener("mouseover",
                event -> avatar.setImage(this.hoverImageUrl));
        getElement().addEventListener("mouseout",
                event -> avatar.setImageResource(this.streamResource));
        getElement().addEventListener("click",
                event -> avatar.setImageResource(this.streamResource));
    }

    public ProfileAvatar(StreamResource streamResource, StreamResource hoverStreamResource, Runnable runnable) {
        this(streamResource, hoverStreamResource);
        getElement().addEventListener("click",
                event -> runnable.run());
    }

    public ProfileAvatar(StreamResource streamResource, String hoverImageUrl, Runnable runnable) {
        this(streamResource, hoverImageUrl);
        getElement().addEventListener("click",
                event -> runnable.run());
    }

    public void setStreamResource(StreamResource streamResource) {
        this.streamResource = streamResource;
        this.avatar.setImageResource(this.streamResource);
    }

    public void setActionOnClick(Runnable runnable) {
        getElement().addEventListener("click",
                event -> runnable.run());
    }
}
