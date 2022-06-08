package com.switchkit.incidentreport.user.domain.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleModel {

    @NotBlank(message = "RoleId is required")
    private Long roleId;
    @NotBlank(message = "RoleName is required")
    private String roleName;
}
