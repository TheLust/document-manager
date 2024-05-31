package md.ceiti.frontend.constant.fields;

import md.ceiti.frontend.model.Field;

public interface CmsAccountDtoFields {

    String ROLE_LABEL = "Role";
    String USERNAME_LABEL = "Username";
    String EMAIL_LABEL = "Email";
    String PHONE_NUMBER_LABEL = "Phone number";
    String FIRST_NAME_LABEL = "First name";
    String LAST_NAME_LABEL = "Last name";
    String BIRTH_DATE_LABEL = "Birth date";

    Field ROLE = new Field("role", ROLE_LABEL, "MASTER");
    Field USERNAME = new Field("username", USERNAME_LABEL, "gigel69");
    Field EMAIL = new Field("email", EMAIL_LABEL, "gigel.marcel@gmail.com");
    Field PHONE_NUMBER = new Field("phoneNumber", PHONE_NUMBER_LABEL, "37369696969");
    Field FIRST_NAME = new Field("firstName", FIRST_NAME_LABEL, "Gigel");
    Field LAST_NAME = new Field("lastName", LAST_NAME_LABEL, "Marcel");
    Field BIRTH_DATE = new Field("birthDate", BIRTH_DATE_LABEL, "07/03/2004");
}
