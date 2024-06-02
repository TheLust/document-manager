package md.ceiti.frontend.view.cms;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import md.ceiti.frontend.constant.I18n;
import md.ceiti.frontend.constant.Routes;
import md.ceiti.frontend.layout.CmsLayout;

@Route(value = Routes.CMS, layout = CmsLayout.class)
@PageTitle(value = I18n.Page.CMS)
public class CmsView extends VerticalLayout {

}
