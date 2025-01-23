package com.example.securityOnePosts.DTO;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class SignUpDto {

    private String email;
    private String username;
    private String password;
}
