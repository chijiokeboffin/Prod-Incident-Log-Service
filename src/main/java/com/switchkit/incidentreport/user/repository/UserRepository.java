package com.switchkit.incidentreport.user.repository;

import com.switchkit.incidentreport.user.domain.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser>  findByUsername(String username);
    Boolean existsByUsername(String username);
}
