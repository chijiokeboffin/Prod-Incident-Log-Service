package com.switchkit.incidentreport.user.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.switchkit.incidentreport.common.ApiResponse;
import com.switchkit.incidentreport.exception.AuthenticationFailedException;
import com.switchkit.incidentreport.exception.RecordNotFoundException;
import com.switchkit.incidentreport.exception.UsernameNameTakenException;
import com.switchkit.incidentreport.user.domain.entities.AppUser;
import com.switchkit.incidentreport.user.domain.entities.Role;
import com.switchkit.incidentreport.user.domain.model.CreateUserModel;
import com.switchkit.incidentreport.user.domain.model.EditUserModel;
import com.switchkit.incidentreport.user.repository.RoleRepository;
import com.switchkit.incidentreport.user.repository.UserRepository;
import com.switchkit.incidentreport.user.utilities.UserUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {

    private final long EXPIRATION_TIME = 10 * 60 * 1000;
    private final  String BEARER = "Bearer ";
    private final UserRepository userRepository;
    private  final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ResponseEntity<ApiResponse> createUser(CreateUserModel model) {
        boolean usernameExist = userRepository.existsByUsername(model.getUsername());
        if(usernameExist){
            throw new UsernameNameTakenException("Username %s is already taken".formatted(model.getUsername()));
        }
        AppUser user = new AppUser();
        user.setName(model.getName());
        user.setUsername(model.getUsername());
        user.setIsActive(true);
        user.setPassword(passwordEncoder.encode(model.getPassword()));
        if(model.getRoles() != null && model.getRoles().size() > 0){
            model.getRoles().forEach((roleName)->{
                Role role = roleRepository.findByName(roleName);
                if(Objects.isNull(role)){
                    throw new IllegalArgumentException("Role must be USER or ASSIGNEE or CREATOR or ADMIN");
                }
                user.getRoles().add(role);
            });
        }

        user.setCreateAt(LocalDateTime.now());

        userRepository.save(user);
        ApiResponse response = ApiResponse
                .builder()
                .code(HttpStatus.CREATED.value())
                .message("created successfully")
                .build();
        return  ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    public ResponseEntity<ApiResponse> editUser(long userId,  EditUserModel model) {
      AppUser user =  userRepository.findById(userId).orElseThrow(
                ()-> new IllegalArgumentException("No user found with the Id %s".formatted(userId))
        );
        if(!user.getUsername().equalsIgnoreCase(model.getUsername())){
            throw new IllegalArgumentException("Username cannot be edited");
        }

        if(Objects.nonNull(model.getName())){
            user.setName(model.getName());
        }

        if(Objects.nonNull(model.getPassword())){
            user.setName(passwordEncoder.encode(model.getPassword()));
        }

        if(model.getRoles().size() > 0){
            model.getRoles().forEach((roleName)->{
                Role role = roleRepository.findByName(roleName);
                if(Objects.isNull(role)){
                    throw new IllegalArgumentException("Role must be USER or ASSIGNEE or CREATOR or ADMIN");
                }
                if(!user.getRoles().contains(role)){
                    user.getRoles().add(role);
                }

            });
        }

        userRepository.save(user);

        ApiResponse response = ApiResponse
                .builder()
                .code(HttpStatus.CREATED.value())
                .message("edited successfully")
                .build();
        return  ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    public ResponseEntity<ApiResponse> deleteUser(Long userId) {
       var user = userRepository.findById(userId).orElseThrow(
               ()-> new IllegalArgumentException("Invalid userid")
       );
       //The user record should not be removed from database
        //Primary delete
      user.setIsActive(false);
        ApiResponse response = ApiResponse
                .builder()
                .code(HttpStatus.CREATED.value())
                .message("deleted successfully")
                .build();
        return  ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    public Role createRole(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public ResponseEntity<ApiResponse> addRoleToUser(String username, String roleName) {
        Optional<AppUser> optional  = userRepository.findByUsername(username);
        if(optional.isEmpty()){
            throw new IllegalArgumentException("Invalid username");
        }
        AppUser user = optional.get();
        Role role = roleRepository.findByName(roleName);
        user.getRoles().add(role);
        ApiResponse response = ApiResponse
                .builder()
                .code(HttpStatus.OK.value())
                .message("success")
                .build();
        return  ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    public AppUser getUser(String username) {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
        return  user;
    }

    @Override
    public ResponseEntity<ApiResponse> getUsers(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);

        var page = userRepository.findAll(pageable);
        var users = page.get().collect(Collectors.toList());

        if(users == null){
            throw new RecordNotFoundException("No record found in the database");
        }

        ApiResponse response = ApiResponse
                .builder()
                .data(users)
                .code(HttpStatus.OK.value())
                .message("success")
                .build();
        return  ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @Override
    public void refreshToken(HttpServletRequest request, HttpServletResponse response){
        String authorizationHeader = request.getHeader(AUTHORIZATION);

        if(authorizationHeader !=null && authorizationHeader.startsWith(BEARER)){
            try{
                String refresh_token = authorizationHeader.substring(BEARER.length());
                Algorithm algorithm = UserUtil.getAlgorithm();
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refresh_token);
                String username = decodedJWT.getSubject();
                AppUser user = this.getUser(username);

                List<String> roles = user.getRoles()
                        .stream().map(Role::getName).collect(Collectors.toList());

                String access_token = JWT.create()
                        .withSubject(user.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                        .withIssuer(request.getRequestURI())
                        .withClaim("roles", roles)
                        .sign(algorithm);


                Map<String, String> tokens = new HashMap<>();
                tokens.put("access_token", access_token);
                tokens.put("refresh_token", refresh_token);
                tokens.put("expire_at", String.valueOf(EXPIRATION_TIME));

                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);

            }catch (Exception ex){
                throw new AuthenticationFailedException("Oops something went wrong");
            }
        }else {
            throw new RuntimeException("Refresh Token is Missing");
        }
    }


}
