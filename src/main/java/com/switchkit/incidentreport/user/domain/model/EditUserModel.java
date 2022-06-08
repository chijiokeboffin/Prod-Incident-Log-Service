package com.switchkit.incidentreport.user.domain.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditUserModel {

    private  String name;
    @NotBlank(message = "Username is required")
    private  String username;
    private  String password;
    private Set<String> roles = new HashSet<>();
}
