package com.example.securityOnePosts.Controllers;

import com.example.securityOnePosts.DTO.LoginDto;
import com.example.securityOnePosts.DTO.SignUpDto;
import com.example.securityOnePosts.DTO.UserDto;
import com.example.securityOnePosts.Services.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;
    @PostMapping("/signup")
    public ResponseEntity<UserDto> singupuser(@RequestBody SignUpDto signUpDto){
        UserDto userDto=authService.signup(signUpDto);
        return ResponseEntity.ok(userDto);
    }
    @PostMapping("/login")
    public ResponseEntity<String> loginuser(@RequestBody LoginDto loginDto, HttpServletResponse httpServletResponse)
    {
            String token=authService.login(loginDto);
        Cookie cookie=new Cookie("Token",token);
        cookie.setHttpOnly(true);
        httpServletResponse.addCookie(cookie);
        return ResponseEntity.ok(token);
    }
}
