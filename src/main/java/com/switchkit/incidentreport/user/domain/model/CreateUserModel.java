package com.switchkit.incidentreport.user.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUserModel {
    @NotBlank(message = "Name is required")
    private  String name;
    @NotBlank(message = "Username is required")
    private  String username;
    @NotBlank(message = "Password is required")
    private  String password;
    private Set<String> roles = new HashSet<>();
}
