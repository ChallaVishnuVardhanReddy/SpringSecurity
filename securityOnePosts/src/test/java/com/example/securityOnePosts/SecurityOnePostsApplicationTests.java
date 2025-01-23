package com.example.securityOnePosts;

import com.example.securityOnePosts.DTO.LoginDto;
import com.example.securityOnePosts.Entities.User;
import com.example.securityOnePosts.Services.AuthService;
import com.example.securityOnePosts.Services.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.util.AssertionErrors.assertNotNull;

@SpringBootTest
class SecurityOnePostsApplicationTests {

	@Autowired
	private AuthService authService;

	@Test
	void testLogin() {
		// Create and set up the DTO manually
		LoginDto loginDto = new LoginDto();
		loginDto.setEmail("cjr@gmail.com");
		loginDto.setPassword("cjr");

		// Call the login method
		String token = authService.login(loginDto);

		System.out.println(token);
	}
}
