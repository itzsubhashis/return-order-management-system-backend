package com.roms;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.roms.auth.config.CustomUserDetailsService;
import com.roms.auth.controller.AuthenticationController;
import com.roms.auth.model.AuthenticationRequest;

@SpringBootTest
class AuthenticationMicroserviceApplicationTests {

	@Autowired
	CustomUserDetailsService customUserDetailsService;

	@Autowired
	AuthenticationController authenticationController;

	@Test
	void whenUsernameIsPresent() {
		assertNotNull(customUserDetailsService.loadUserByUsername("abc"));
		assertNotNull(customUserDetailsService.loadUserByUsername("cde"));
		assertNotNull(authenticationController.createAuthenticationToken(new AuthenticationRequest("abc", "admin")));
		String abc = authenticationController.createAuthenticationToken(new AuthenticationRequest("abc", "admin")).getBody().getToken();
		assertEquals("1",authenticationController.decodeToken(abc).getBody());
		assertEquals("Valid",authenticationController.validate(abc));
		String invalidToken="eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhYmMiLCJpc1VzZXIiOnRydWUsImV4cCI6MTY1MjcxOTUxNCwiaWF0IjoxNjUyNzAxNTE0fQ.wqmX9yj6q2tbnJB67burCWAYu67RUN_XXqNE5ER6r_RSDZRo3kyHgW-iSTlLib2fqefCzXlReESDD6HQLVRj";
		assertEquals("Credentials are invalid",authenticationController.validate(invalidToken));
		String expiredToken="eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhYmMiLCJpc1VzZXIiOnRydWUsImV4cCI6MTY1MjcxOTUxNCwiaWF0IjoxNjUyNzAxNTE0fQ.wqmX9yj6q2tbnJB67burCWAYu67RUN_XXqNE5ER6r_RSDZRo3kyHgW-iSTlLib2fqefCzXlReESDD6HQLVRjqQ";
		assertEquals("Token has expired",authenticationController.validate(expiredToken));
	}

	@Test
	void whenUsernameIsNotPresent() {
			assertThatExceptionOfType(UsernameNotFoundException.class)
	           .isThrownBy(() -> { customUserDetailsService.loadUserByUsername("def"); })
	           .withMessage("User not found with the name def"); 
	}

}
