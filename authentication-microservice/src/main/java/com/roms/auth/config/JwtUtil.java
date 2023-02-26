package com.roms.auth.config;

import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.roms.auth.model.UserDetail;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Service
public class JwtUtil {

	Logger logger = LoggerFactory.getLogger(JwtUtil.class);

	@PersistenceContext
	private EntityManager entityManager;

	private String secret;
	private int jwtExpirationInMs;

	@Value("${jwt.secret}")
	public void setSecret(String secret) {
		this.secret = secret;
	}

	@Value("${jwt.expirationDateInMs}")
	public void setJwtExpirationInMs(int jwtExpirationInMs) {
		this.jwtExpirationInMs = jwtExpirationInMs;
	}

	public String generateToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();

		Collection<? extends GrantedAuthority> roles = userDetails.getAuthorities();

		if (roles.contains(new SimpleGrantedAuthority("ROLE_USER"))) {
			claims.put("isUser", true);
		}

		return doGenerateToken(claims, userDetails.getUsername());
	}

	private String doGenerateToken(Map<String, Object> claims, String subject) {
		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + jwtExpirationInMs))
				.signWith(SignatureAlgorithm.HS512, secret).compact();

	}

	public String validateToken(String authToken) {
		String validity = "Invalid";
		logger.info("Starting token validation");
		try {
			Jwts.parser().setSigningKey(secret).parseClaimsJws(authToken.trim());
			logger.info("Valid token");
			validity = "Valid";
		} catch (SignatureException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException ex) {
			validity = "Credentials are invalid";
		} catch (ExpiredJwtException ex) {
			validity = "Token has expired";
		}
		return validity;
	}


	public UserDetail decodeToken(String token) {
		Base64.Decoder decoder = Base64.getUrlDecoder();
		String[] chunks = (token.contains("Bearer") ? token.substring(6, token.length()) : token).split("\\.");
		String payload = new String(decoder.decode(chunks[1]));
		logger.info(payload);
		JSONObject json = new JSONObject(payload);
		logger.info("Payload received");
		UserDetail ud = new UserDetail(json);
		ud.setId(getUserId(ud));
		return ud;

	}

	public String getUserId(UserDetail user) {
		Query q = entityManager.createNativeQuery("select l.id from login l where l.username=?");
		q.setParameter(1, user.getUsername());
		return q.getSingleResult().toString();
	}

}
