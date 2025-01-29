package com.example.securityOnePosts.Entities;

import com.example.securityOnePosts.Entities.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User implements UserDetails {


@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;
private String email;
private String password;
private String username;

@ElementCollection(fetch=FetchType.EAGER)
@Enumerated(EnumType.STRING)
private Set<Role> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return roles.stream()
                .map(role->new SimpleGrantedAuthority("ROLE_"+role.name()))
                .collect(Collectors.toSet());
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }
}
