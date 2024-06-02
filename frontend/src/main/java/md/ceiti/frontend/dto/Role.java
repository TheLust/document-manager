package md.ceiti.frontend.dto;

import md.ceiti.frontend.constant.I18n;

public enum Role {

    MASTER(I18n.Role.MASTER),
    INSTITUTION_MASTER(I18n.Role.INSTITUTION_MASTER),
    INSTITUTION_USER(I18n.Role.INSTITUTION_USER),
    GHOST(I18n.Role.GHOST);

    final String label;

    Role(String label) {
        this.label = label;
    }
}
