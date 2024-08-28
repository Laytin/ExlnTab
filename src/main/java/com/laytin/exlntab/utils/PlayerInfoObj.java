package com.laytin.exlntab.utils;

public class PlayerInfoObj {
    private String role;
    private String roleDisplayName;
    private String username;
    private int weight;

    public PlayerInfoObj() {
    }

    public PlayerInfoObj(String role, String roleDisplayName, String username, int weight) {
        this.role = role;
        this.roleDisplayName = roleDisplayName;
        this.username = username;
        this.weight = weight;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getRoleDisplayName() {
        return roleDisplayName;
    }

    public void setRoleDisplayName(String roleDisplayName) {
        this.roleDisplayName = roleDisplayName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "PlayerInfoObj{" +
                "role='" + role + '\'' +
                ", roleDisplayName='" + roleDisplayName + '\'' +
                ", username='" + username + '\'' +
                ", weight=" + weight +
                '}';
    }
}
