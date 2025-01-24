package com.example.securityOnePosts.Services;

import com.example.securityOnePosts.Entities.User;
import com.example.securityOnePosts.Repositories.UserRepository;
import com.example.securityOnePosts.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username).orElseThrow(()-> new ResourceNotFoundException("user with email:"+username+"not found!"));
    }

    public User getUserDetails(Long id)
    {
        return userRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("User with id:"+id+" not found"));
    }
}
