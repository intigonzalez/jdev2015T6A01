package com.enseirb.telecom.dngroup.dvd2c.conf;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.enseirb.telecom.dngroup.dvd2c.modeldb.Role;
import com.enseirb.telecom.dngroup.dvd2c.modeldb.User;




public class SecurityUser extends User implements UserDetails {
	private static final long serialVersionUID = 1L;
	

	

	List<Role> userRoles;
	
	public SecurityUser(User user) {
		if (user != null) {
			this.setId(user.getId());
			this.setSurname(user.getSurname());
			this.setFirstname(user.getFirstname());
			this.setEmail(user.getEmail());
			this.setEncryptedPassword(user.getEncryptedPassword());
			
		}
	}

	public List<Role> getUserRoles() {
		return userRoles;
	}

	public void setUserRoles(List<Role> userRoles) {
		this.userRoles = userRoles;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> authorities = new ArrayList<>();
		
		if (userRoles != null) {
			for (Role role : userRoles) {
				SimpleGrantedAuthority authority = new SimpleGrantedAuthority(
						role.getName());
				authorities.add(authority);
			}
		}
		return authorities;
	}

	@Override
	public String getPassword() {
		return super.getEncryptedPassword();
	}

	@Override
	public String getUsername() {
		return super.getId().toString();
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