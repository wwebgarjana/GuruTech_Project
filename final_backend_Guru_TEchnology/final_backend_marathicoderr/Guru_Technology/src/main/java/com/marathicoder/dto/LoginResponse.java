


package com.marathicoder.dto;

public class LoginResponse {
    private String token;
    private String role;
    private Boolean firstLogin;

    public LoginResponse(String token, String role, Boolean firstLogin) {
        this.token = token;
        this.role = role;
        this.firstLogin = firstLogin;
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public Boolean getFirstLogin() { return firstLogin; }
    public void setFirstLogin(Boolean firstLogin) { this.firstLogin = firstLogin; }
}