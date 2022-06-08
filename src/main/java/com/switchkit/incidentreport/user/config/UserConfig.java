package com.switchkit.incidentreport.user.config;

import com.switchkit.incidentreport.user.domain.entities.AppUser;
import com.switchkit.incidentreport.user.domain.entities.Role;
import com.switchkit.incidentreport.user.domain.model.CreateUserModel;
import com.switchkit.incidentreport.user.repository.UserRepository;
import com.switchkit.incidentreport.user.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Configuration
public class UserConfig {




    @Bean
    CommandLineRunner commandLineRunner(UserService userService){
        return args -> {
            userService.createRole(new Role(null, "USER"));
            userService.createRole(new Role(null, "ASSIGNEE"));
            userService.createRole(new Role(null, "CREATOR"));
            userService.createRole(new Role(null, "ADMIN"));
            userService.createRole(new Role(null, "SUPER_ADMIN"));


            Set<String> roles1 = new HashSet<>();
            roles1.add("ADMIN");
            roles1.add("SUPER_ADMIN");

            Set<String> roles2 = new HashSet<>();
            roles1.add("ADMIN");
            roles1.add("CREATOR");

            userService.createUser(new CreateUserModel("Super Admin", "admin", "password123",roles1));
            userService.createUser(new CreateUserModel("John Doe", "johndoe", "password123", roles2));


        };
    }
}
