package md.ceiti.backend.model;

import lombok.Getter;

@Getter
public enum Role {

    MASTER("ROLE_MASTER"),
    INSTITUTION_MASTER("ROLE_INSTITUTION_MASTER"),
    INSTITUTION_USER("ROLE_INSTITUTION_USER"),
    GHOST("ROLE_GHOST");

    final String label;
    Role(String label) {
        this.label = label;
    }
}
