package md.ceiti.frontend.constant.fields;

import md.ceiti.frontend.model.Field;

public interface DocumentDtoFields {

    String NAME_LABEL = "Name";
    String CATEGORY_LABEL = "Category";

    Field NAME = new Field("name", NAME_LABEL, "AwesomeDocument");
    Field CATEGORY = new Field("category", CATEGORY_LABEL, "nustiu");
}
