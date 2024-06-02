package md.ceiti.frontend.constant;

public interface Routes {

    interface Params {
        String INSTITUTION_ID = "institutionId";
    }

    String CMS = "cms";
    String CMS_INSTITUTIONS = "cms/institutions";
    String CMS_USERS = "cms/users";

    String INSTITUTION = "institutions/:institutionId";
    String INSTITUTION_USERS = "institutions/:" + Params.INSTITUTION_ID + "/users";
    String INSTITUTION_CATEGORIES = "institutions/:" + Params.INSTITUTION_ID + "/categories";
    String INSTITUTION_DOCUMENTS = "institutions/:" + Params.INSTITUTION_ID + "/documents";
}
