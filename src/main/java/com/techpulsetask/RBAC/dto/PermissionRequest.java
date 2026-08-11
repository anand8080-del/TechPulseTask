package com.techpulsetask.RBAC.dto;


import jakarta.validation.constraints.NotBlank;

public class PermissionRequest {

    @NotBlank(message = "name is required")
    private String name;

    public PermissionRequest() {
    }

    public PermissionRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

