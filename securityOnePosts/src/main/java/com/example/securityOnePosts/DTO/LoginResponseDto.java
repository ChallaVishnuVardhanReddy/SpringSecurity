package com.example.securityOnePosts.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDto {
  private Long id;
  private String accessToken;
  private String refreshToken;
}
