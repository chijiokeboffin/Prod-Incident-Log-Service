package com.switchkit.incidentreport.user.controller;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.switchkit.incidentreport.common.ApiResponse;
import com.switchkit.incidentreport.exception.AuthenticationFailedException;
import com.switchkit.incidentreport.user.domain.entities.AppUser;
import com.switchkit.incidentreport.user.domain.entities.Role;
import com.switchkit.incidentreport.user.domain.model.CreateUserModel;
import com.switchkit.incidentreport.user.domain.model.EditUserModel;
import com.switchkit.incidentreport.user.domain.model.RoleToUserRequest;
import com.switchkit.incidentreport.user.service.UserService;
import com.switchkit.incidentreport.user.utilities.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/users")
public class UserController {


    private final UserService userService;

    @GetMapping()
    public ResponseEntity<ApiResponse> getUsers(@RequestParam("pageNo") int pageNo
            ,@RequestParam("pageSize") int pageSize){
        ApiResponse response = ApiResponse
                .builder()
                .data(userService.getUsers(pageNo, pageSize))
                .message("success")
                .code(HttpStatus.OK.value()).build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/create-user")
    public ResponseEntity<ApiResponse> createUser(@Valid @RequestBody CreateUserModel model){
        URI uri = URI.create(ServletUriComponentsBuilder
                .fromCurrentContextPath().path("api/users/create-user").toUriString());
        return userService.createUser(model);
    }

    @PostMapping("/edit-user/{userId}")
    public ResponseEntity<ApiResponse> createRole(@PathVariable("userId") long userId, EditUserModel model){
        return userService.editUser(userId, model);
    }


    @PostMapping("/role-to-user")
    public ResponseEntity<ApiResponse> addRoleToUser(@Valid @RequestBody RoleToUserRequest roleToUserRequest){
        return userService.addRoleToUser(roleToUserRequest.getUsername(), roleToUserRequest.getRoleName());
    }

    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response){
        userService.refreshToken(request, response);
    }

}
