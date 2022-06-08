package com.switchkit.incidentreport.user.security;

import com.switchkit.incidentreport.user.domain.entities.AppUser;
import com.switchkit.incidentreport.user.filter.CustomAuthenticationFilter;
import com.switchkit.incidentreport.user.filter.CustomAuthorizationFilter;
import com.switchkit.incidentreport.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.*;


import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig  {

    private final  AuthenticationConfiguration authenticationConfiguration;
    private  final UserRepository userRepository;



    @Bean
    public UserDetailsService userDetailsService() {
        return new ReportUserDetailsService();
    }

    private class ReportUserDetailsService implements UserDetailsService {
        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            AppUser user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
            if(Objects.isNull(user)){
                throw new UsernameNotFoundException("User [%s] not found".formatted(username));
            }

            Collection<SimpleGrantedAuthority> authourities = new ArrayList<>();
            user.getRoles().forEach((role)-> {
                authourities.add(new SimpleGrantedAuthority(role.getName()));
            });
            return new User(user.getUsername(), user.getPassword(), authourities);
        }
    }


    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(8);
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        CustomAuthenticationFilter customAuthenticationFilter =
                new CustomAuthenticationFilter(authenticationManager(authenticationConfiguration));
        customAuthenticationFilter.setFilterProcessesUrl("/api/login");

                 http.cors().and().csrf().disable();
                 http.sessionManagement().sessionCreationPolicy(STATELESS);
                 http.authorizeRequests().antMatchers("/api/login/**", "/api/users/token/refresh/**").permitAll();
                 http.authorizeRequests().antMatchers(GET, "/api/users/**").hasAnyRole("ADMIN","SUPER_ADMIN");
                 http.authorizeRequests().antMatchers(POST, "/api/users/**").hasAnyRole("ADMIN","SUPER_ADMIN");
                 http.authorizeRequests().antMatchers(GET, "/api/v1/report/incident-reports")
                         .hasAnyRole("USER", "ASSIGNEE", "CREATOR", "ADMIN", "SUPER_ADMIN");
                 http.authorizeRequests().antMatchers(POST, "/api/v1/report/update-report-detail/**")
                .hasAnyRole( "ASSIGNEE", "CREATOR");
                 http.authorizeRequests().antMatchers(POST, "/api/v1/report/update-report-status/**")
                .hasRole( "ASSIGNEE");
                 http.authorizeRequests().anyRequest().authenticated();
                http.addFilter(customAuthenticationFilter);
                http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);



        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers("/images/**", "/js/**", "/webjars/**");
    }



}

