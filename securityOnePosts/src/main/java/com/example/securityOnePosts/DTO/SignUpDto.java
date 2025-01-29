package com.example.securityOnePosts.DTO;

import com.example.securityOnePosts.Entities.enums.Role;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Data
@Getter
@Setter
public class SignUpDto {

    private String email;
    private String username;
    private String password;
    private Set<Role> roles;
}
