package md.ceiti.frontend.dto;

import md.ceiti.frontend.constant.I18n;

public enum Role {

    MASTER(I18n.MASTER),
    INSTITUTION_MASTER(I18n.INSTITUTION_MASTER),
    INSTITUTION_USER(I18n.INSTITUTION_USER),
    GHOST(I18n.GHOST);

    final String label;

    Role(String label) {
        this.label = label;
    }
}
