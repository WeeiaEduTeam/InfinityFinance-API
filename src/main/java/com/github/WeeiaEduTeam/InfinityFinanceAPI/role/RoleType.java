package com.github.WeeiaEduTeam.InfinityFinanceAPI.role;

public enum RoleType {
    ROLE_USER("ROLE_USER"),
    ROLE_ADMIN("ROLE_ADMIN");

    private final String name;
    RoleType(String role_user) {
        this.name = role_user;
    }

    public String getName() {
        return name;
    }
}
