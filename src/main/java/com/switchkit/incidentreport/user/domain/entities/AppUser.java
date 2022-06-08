package com.switchkit.incidentreport.user.domain.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

import static javax.persistence.FetchType.EAGER;

@Entity
@Data
@Table(	name = "app_user",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username")
        })
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppUser {

    @Id
    @SequenceGenerator(
            name = "appuser_id_sequence"
            ,sequenceName ="appuser_id_sequence"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "appuser_id_sequence"
    )
    private Long id;
    @Column(nullable = false)
    @Size(max = 50)
    private  String name;
    @Column(nullable = false, unique = true)
    private  String username;
    @JsonIgnore()
    @Column(nullable = false)
    private  String password;

    private  Boolean isActive;
    private LocalDateTime createAt;

    @ManyToMany(fetch = EAGER)
    private Collection<Role> roles = new ArrayList<>();

}
