package com.techment.OtrsSystem.domain;

public enum Roles {

    Admin("admin"), Customer_Service_Representative("csr"), User("user");
    private String label;

    Roles(String label) {
        this.label = label;
    }

    public static Roles findByLabel(String byLabel) {
        for(Roles r:Roles.values()) {
            if (r.label.equalsIgnoreCase(byLabel))
                return r;
        }
        return null;
    }

    public String getLabel() {
        return label;
    }
}
