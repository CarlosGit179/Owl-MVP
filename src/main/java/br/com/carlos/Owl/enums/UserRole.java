package br.com.carlos.Owl.enums;

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
