package de.lenneflow.lenneflowclient.enums;



public enum Role {

    ROLE_USER("ROLE_USER"),
    ROLE_ADMIN("ROLE_ADMIN");

    private String value;

    Role(String value) {
        this.value = value;
    }
    public String getValue() {
        return this.value;
    }

    public String getAuthority() {
        return value;
    }
}