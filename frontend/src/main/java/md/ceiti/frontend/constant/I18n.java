package md.ceiti.frontend.constant;

public interface I18n {
    interface Functional {
        String DATE_FORMAT = "dd/MM/yyyy";
        String DEFAULT_EDIT_IMAGE = "https://cdn-icons-png.flaticon.com/512/6065/6065488.png";
    }

    interface Role {

        String MASTER = "CMS Master";
        String INSTITUTION_MASTER = "Master";
        String INSTITUTION_USER = "User";
        String GHOST = "None";
    }

    interface Page {

        String CMS = "CMS";
        String CMS_INSTITUTIONS = "CMS | Institutions";
        String CMS_USERS = "CMS | Users";
        String INSTITUTION = "DM | Institution";
        String INSTITUTION_USERS = "DM | Users";
        String INSTITUTION_CATEGORIES = "DM | Categories";
        String INSTITUTION_DOCUMENTS = "DM | Documents";
    }

    interface Layout {
        String INSTITUTIONS = "Institutions";
        String INSTITUTION = "Institution";
        String USERS = "Users";
        String CATEGORIES = "Categories";
        String DOCUMENTS = "Documents";
    }

    interface Component {

        String BACK = "Back";
        String CANCEL = "Cancel";
        String SAVE = "Save";
        String EDIT = "Edit";
        String CHANGE = "Change";
        String CHANGE_PASSWORD = "Change Password";
        String PROFILE = "Profile";
        String SIGN_OUT = "Sign out";
        String FAQ = "FAQ";
        String NONE = "None";
        String SIGN_OUT_HEADER = "Exit";
        String SIGN_OUT_TEXT = "Are you sure you want to sign out?";
        String IMAGE_READ_ERROR = "Cannot read the image, please try another one";
        String DOWNLOAD = "Download";
        String NAVIGATE = "Navigate";
    }

}
