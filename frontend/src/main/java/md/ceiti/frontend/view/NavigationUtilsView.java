package md.ceiti.frontend.view;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import md.ceiti.frontend.util.NavigationUtils;

public class NavigationUtilsView extends VerticalLayout implements BeforeEnterObserver {

    protected boolean isFirstPageLoad;

    public void navigateToPreviousPage() {
        if (isFirstPageLoad) {
            NavigationUtils.navigateToHomePage();
        } else {
            UI.getCurrent().getPage().getHistory().back();
        }
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        UI ui = beforeEnterEvent.getUI();
        isFirstPageLoad = ui.getInternals()
                .getActiveViewLocation()
                .getFirstSegment()
                .isEmpty();
    }
}
