package md.ceiti.frontend.view;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import md.ceiti.frontend.layout.ApplicationLayout;

@Route(value = "application", layout = ApplicationLayout.class)
@PageTitle(value = "DM | Application")
public class ApplicationView extends VerticalLayout {

}
