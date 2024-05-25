package md.ceiti.frontend.view;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import md.ceiti.frontend.constant.I18n;
import md.ceiti.frontend.constant.Routes;
import md.ceiti.frontend.layout.CmsLayout;

@Route(value = Routes.CMS_INSTITUTIONS, layout = CmsLayout.class)
@PageTitle(value = I18n.CMS_INSTITUTIONS)
public class CmsInstitutionsView extends VerticalLayout {

}
