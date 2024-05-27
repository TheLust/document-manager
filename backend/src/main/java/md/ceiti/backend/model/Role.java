package md.ceiti.backend.model;

import lombok.Getter;

@Getter
public enum Role {

    MASTER,
    INSTITUTION_MASTER,
    INSTITUTION_USER,
    GHOST;
}
