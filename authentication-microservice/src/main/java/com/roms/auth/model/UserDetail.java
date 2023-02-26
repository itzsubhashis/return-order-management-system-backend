package com.roms.auth.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.roms.auth.entity.User;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
@Data
@Getter
@Setter
public class UserDetail implements UserDetails {

	private static final long serialVersionUID = 7081925439332506668L;
		private String id;
		private String username;
		private String password;
		private List<GrantedAuthority> roles;

		public UserDetail(User user) {
			this.setId(user.getId()+"");
			this.username = user.getUsername();
			this.password = user.getPassword();
			String[] allRoles=user.getRoles().split(",");
			List<GrantedAuthority> roleList=new ArrayList<>();
			for(String r:allRoles) {
				roleList.add(new SimpleGrantedAuthority(r));
			}
			this.roles=roleList;
		}
		
		public UserDetail(JSONObject json) {
			this.username=json.getString("sub");
			this.roles=new ArrayList<>();
			if(json.toString().contains("isUser")&&json.getBoolean("isUser"))
				this.roles.add(new SimpleGrantedAuthority("ROLE_USER"));
		}

		@Override
		public List<GrantedAuthority> getAuthorities() {
			return roles;
		}


		@Override
		public boolean isAccountNonExpired() {
			return true;
		}

		@Override
		public boolean isAccountNonLocked() {
			return true;
		}

		@Override
		public boolean isCredentialsNonExpired() {
			return true;
		}

		@Override
		public boolean isEnabled() {
			return true;
		}

	}

