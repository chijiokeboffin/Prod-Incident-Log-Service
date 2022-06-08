package com.switchkit.incidentreport.user.service;

import com.switchkit.incidentreport.common.ApiResponse;
import com.switchkit.incidentreport.user.domain.entities.AppUser;
import com.switchkit.incidentreport.user.domain.entities.Role;
import com.switchkit.incidentreport.user.domain.model.CreateUserModel;
import com.switchkit.incidentreport.user.domain.model.EditUserModel;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface UserService {
    ResponseEntity<ApiResponse> createUser(CreateUserModel model);
    ResponseEntity<ApiResponse> editUser(long userId,EditUserModel model);
    ResponseEntity<ApiResponse> deleteUser(Long userId);
    Role createRole(Role role);
    ResponseEntity<ApiResponse> addRoleToUser(String username, String roleName);
     AppUser getUser(String username);
    ResponseEntity<ApiResponse> getUsers(int pageNo, int pageSize);
    void refreshToken(HttpServletRequest request, HttpServletResponse response);
}
