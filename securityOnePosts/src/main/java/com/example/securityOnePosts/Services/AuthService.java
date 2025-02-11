package com.example.securityOnePosts.Services;

import com.example.securityOnePosts.DTO.LoginDto;
import com.example.securityOnePosts.DTO.LoginResponseDto;
import com.example.securityOnePosts.DTO.SignUpDto;
import com.example.securityOnePosts.DTO.UserDto;
import com.example.securityOnePosts.Entities.User;
import com.example.securityOnePosts.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

     private final UserRepository userRepository;
     private final UserService userService;
     private final PasswordEncoder passwordEncoder;
     private final ModelMapper modelMapper;
     private final AuthenticationManager authenticationManager;
     private final JwtService jwtSerice;
     private final SessionService sessionService;
    public UserDto signup(SignUpDto signUpDto) {
        Optional<User> user = userRepository.findByEmail(signUpDto.getEmail());
        if(user.isPresent())
        {
            throw new BadCredentialsException("the user with email:"+signUpDto.getEmail()+" already exists!");
        }
        User savingUser= modelMapper.map(signUpDto,User.class);
        savingUser.setPassword(passwordEncoder.encode(savingUser.getPassword()));
        User returnUser=userRepository.save(savingUser);
        return modelMapper.map(returnUser,UserDto.class);
    }

    public LoginResponseDto login(LoginDto loginDto) {

        Authentication authentication=authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(),loginDto.getPassword())
        );
        User user=(User) authentication.getPrincipal();
        String accessToken= jwtSerice.generateToken(user);
        String refreshToken= jwtSerice.generateRefreshToken(user);
        sessionService.generateNewSession(user,refreshToken);
        return new LoginResponseDto(user.getId(),accessToken,refreshToken);
    }

    public LoginResponseDto refreshToken(String refreshToken) {
        Long userId=jwtSerice.getUserIdFromToken(refreshToken);
        sessionService.validateSession(refreshToken);
        User user=userService.getUserDetails(userId);
        String accessToken= jwtSerice.generateToken(user);
        return new LoginResponseDto(user.getId(),accessToken,refreshToken);
    }
}
