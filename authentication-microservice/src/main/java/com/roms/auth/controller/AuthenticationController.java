package com.roms.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.roms.auth.config.CustomUserDetailsService;
import com.roms.auth.config.JwtUtil;
import com.roms.auth.model.AuthenticationRequest;
import com.roms.auth.model.AuthenticationResponse;

@RestController
public class AuthenticationController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private CustomUserDetailsService userDetailsService;

	@Autowired
	private JwtUtil jwtUtil;
	
	@PostMapping("/auth/authenticate")
	public ResponseEntity<AuthenticationResponse> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					authenticationRequest.getUsername(), authenticationRequest.getPassword()));
		
		UserDetails userdetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
		String token = jwtUtil.generateToken(userdetails);
		return ResponseEntity.ok(new AuthenticationResponse(token));
	}
	
	@PostMapping("/auth/validate")
    public String validate(@RequestHeader("Authorization") String token){
		return jwtUtil.validateToken(token);
    }
	@PostMapping("/auth/decode")
    public ResponseEntity<String> decodeToken(@RequestHeader("Authorization") String token) {
		return ResponseEntity.ok(jwtUtil.decodeToken(token).getId());
    }
}
