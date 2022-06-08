package com.switchkit.incidentreport.user.domain.model;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class RoleToUserRequest {
    @NotNull(message = "Username is required")
    private String username;
    @NotNull(message = "Rolename is required")
    private String roleName;
}
