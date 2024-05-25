package md.ceiti.frontend.view;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import md.ceiti.frontend.layout.CmsLayout;

@Route(value = "institution/{institution_id}", layout = CmsLayout.class)
@PageTitle(value = "DM | Institution")
public class InstitutionView extends VerticalLayout {

}
