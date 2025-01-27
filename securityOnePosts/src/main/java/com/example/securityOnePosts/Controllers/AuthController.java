package com.example.securityOnePosts.Controllers;

import com.example.securityOnePosts.DTO.LoginDto;
import com.example.securityOnePosts.DTO.LoginResponseDto;
import com.example.securityOnePosts.DTO.SignUpDto;
import com.example.securityOnePosts.DTO.UserDto;
import com.example.securityOnePosts.Services.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jdk.jfr.Frequency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Value("${deploy.env")
    private String deployEnv;
    @Autowired
    private AuthService authService;
    @PostMapping("/signup")
    public ResponseEntity<UserDto> singupuser(@RequestBody SignUpDto signUpDto){
        UserDto userDto=authService.signup(signUpDto);
        return ResponseEntity.ok(userDto);
    }
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> loginuser(@RequestBody LoginDto loginDto, HttpServletResponse httpServletResponse)
    {
            LoginResponseDto loginResponseDto =authService.login(loginDto);
        Cookie cookie=new Cookie("refreshToken",loginResponseDto.getRefreshToken());
        cookie.setHttpOnly(true);
        cookie.setSecure("production".equals(deployEnv));
        httpServletResponse.addCookie(cookie);
        return ResponseEntity.ok(loginResponseDto);
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDto> refresh(HttpServletRequest request){
        String refreshToken=Arrays.stream(request.getCookies())
                .filter(x-> "refreshToken".equals(x.getName()))
                .findFirst()
                .map(x->x.getValue())
                .orElseThrow(()->new AuthenticationServiceException("Refresh token not foudn inside the cookies"));
        LoginResponseDto loginResponseDto=authService.refreshToken(refreshToken);
       return ResponseEntity.ok(loginResponseDto);
    }
}
