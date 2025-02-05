package com.example.securityOnePosts.Configs;

import com.example.securityOnePosts.Entities.enums.Permission;
import com.example.securityOnePosts.Filter.JwtAuthFilter;
import com.example.securityOnePosts.handlers.OAuth2Successhandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.example.securityOnePosts.Entities.enums.Role.ADMIN;
import static com.example.securityOnePosts.Entities.enums.Role.CREATOR;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final OAuth2Successhandler oAuth2SuccessHandler;
    private static final String[] publicRoutes={
           "/error","/auth/**","home.html"
    };
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception
    {
        httpSecurity
                .authorizeHttpRequests(auth-> auth
                        .requestMatchers(publicRoutes).permitAll()
                        .requestMatchers( HttpMethod.GET,"/posts/**").permitAll()
                        .requestMatchers(HttpMethod.POST,"/posts/**")
                               .hasAnyRole(ADMIN.name(),CREATOR.name())
                        .requestMatchers(HttpMethod.POST,"/posts/**")
                               .hasAnyAuthority(Permission.POST_CREATE.name())
                        .requestMatchers(HttpMethod.GET,"/posts/**")
                               .hasAnyAuthority(Permission.POST_VIEW.name())
                        .requestMatchers(HttpMethod.PUT,"/posts/**")
                               .hasAnyAuthority(Permission.POST_UPDATE.name())
                        .requestMatchers(HttpMethod.DELETE,"/posts/**")
                                .hasAnyAuthority(Permission.POST_DELETE.name())
                        .anyRequest().authenticated())
                .csrf(csrfConfig->csrfConfig.disable())
                .sessionManagement(sessionConfig->sessionConfig
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .oauth2Login(oauth2Config->oauth2Config
                    .failureUrl("/login?error=true")
                .successHandler(oAuth2SuccessHandler)
                );
        //.formLogin((Customizer.withDefaults()));
        return httpSecurity.build();
    }
/*
    @Bean
    UserDetailsService myInMemoryUserDetailsService(){
        UserDetails userDetails= User
                .withUsername("vishnu")
                .password(passwordEncoder().encode("challa"))
                .roles("USER")
                .build();
        UserDetails adminUser= User
                .withUsername("admin")
                .password(passwordEncoder().encode("admin"))
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(userDetails,adminUser);

    }
*/

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    AuthenticationManager authenticaionManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
