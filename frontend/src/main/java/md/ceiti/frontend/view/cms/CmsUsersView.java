package md.ceiti.frontend.view.cms;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import md.ceiti.frontend.component.impl.CmsUsersCrud;
import md.ceiti.frontend.constant.I18n;
import md.ceiti.frontend.constant.Routes;
import md.ceiti.frontend.layout.CmsLayout;
import md.ceiti.frontend.mapper.GenericMapper;
import md.ceiti.frontend.service.impl.CmsAccountService;
import md.ceiti.frontend.service.impl.CmsInstitutionService;

@Route(value = Routes.CMS_USERS, layout = CmsLayout.class)
@PageTitle(value = I18n.Page.CMS_USERS)
public class CmsUsersView extends VerticalLayout {

    public CmsUsersView(CmsAccountService cmsAccountService,
                        CmsInstitutionService cmsInstitutionService,
                        GenericMapper genericMapper) {
        CmsUsersCrud crud = new CmsUsersCrud(
                cmsAccountService,
                cmsInstitutionService,
                genericMapper);
        add(crud);
    }
}
