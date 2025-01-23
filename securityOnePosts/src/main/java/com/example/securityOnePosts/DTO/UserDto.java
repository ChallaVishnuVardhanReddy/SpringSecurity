package com.example.securityOnePosts.DTO;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class UserDto {

    private Long id;
    private String username;
    private String email;

}
