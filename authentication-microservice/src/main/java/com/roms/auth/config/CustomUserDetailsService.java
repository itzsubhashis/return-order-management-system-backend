package com.roms.auth.config;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Optional;

import com.roms.auth.entity.User;
import com.roms.auth.model.UserDetail;
import com.roms.auth.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	UserRepository userRepository;
	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> users = userRepository.findByUsername(username);
		if(users.isEmpty())
		{
			throw new UsernameNotFoundException("User not found with the name "+ username);
		}
		else
		{
			return new UserDetail(users.get());
		}
	}
}
