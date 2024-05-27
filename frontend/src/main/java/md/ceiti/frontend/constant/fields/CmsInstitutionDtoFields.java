package md.ceiti.frontend.constant.fields;

import md.ceiti.frontend.model.Field;

public interface CmsInstitutionDtoFields {

    String MASTER_LABEL = "Master";
    String NAME_LABEL = "Name";

    Field MASTER = new Field("master", MASTER_LABEL, null);
    Field NAME = new Field("name", NAME_LABEL, null);
}
