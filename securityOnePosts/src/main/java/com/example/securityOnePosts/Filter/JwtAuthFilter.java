package com.example.securityOnePosts.Filter;

import com.example.securityOnePosts.Entities.User;
import com.example.securityOnePosts.Services.JwtService;
import com.example.securityOnePosts.Services.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserService userService;

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver handlerExceptionResolver;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            final String requestToken = request.getHeader("Authorization");
            if (requestToken == null || !requestToken.startsWith("Bearer")) {
                filterChain.doFilter(request, response);
                return;
            }
            //  String token=requestToken.split("Bearer")[1];
            // Extract and trim the token
            String token = requestToken.substring(7).trim(); // Remove "Bearer " and trim any whitespace
            Long userId = jwtService.getUserIdFromToken(token);
            //SecurityContextHolder.getContext().getAuthentication()==null we are adding this because, once we set the
            // context still there are chances that we come back and hit this, so no need to check again
            if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                User user = userService.getUserDetails(userId);
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                //adding few more details to authentication token, not necessary
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
            //passing the request to the next filter
            filterChain.doFilter(request, response);
        }
        catch (Exception ex)
        {
            handlerExceptionResolver.resolveException(request,response,null,ex);
        }
    }

}
