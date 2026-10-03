package br.com.carlos.Owl.enums;

/** Roles assigned to accounts and used to authorize protected endpoints. */
public enum UserRole {

    ADMIN("admin"), STUDENT("student");

    private String role;

    UserRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
