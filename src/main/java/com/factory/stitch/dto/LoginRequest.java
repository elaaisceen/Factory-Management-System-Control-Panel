package com.factory.stitch.dto;


/**
 * Bu sinif Fabrika ERP backend modulu icin dokumante edilmis Java bileşenidir.
 */
public class LoginRequest {
    private String username;
    private String role;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}

