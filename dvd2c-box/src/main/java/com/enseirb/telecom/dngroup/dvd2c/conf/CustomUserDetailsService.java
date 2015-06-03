package com.enseirb.telecom.dngroup.dvd2c.conf;

import java.util.Arrays;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.enseirb.telecom.dngroup.dvd2c.exception.NoSuchUserException;
import com.enseirb.telecom.dngroup.dvd2c.modeldb.Role;
import com.enseirb.telecom.dngroup.dvd2c.modeldb.User;
import com.enseirb.telecom.dngroup.dvd2c.repository.RoleRepository;
import com.enseirb.telecom.dngroup.dvd2c.service.AccountService;

@Component
public class CustomUserDetailsService implements UserDetailsService {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(CustomUserDetailsService.class);
	@Autowired
	private AccountService accountService;
	@Inject
	private RoleRepository roleRepository;

	@Override
	public UserDetails loadUserByUsername(String userName)
			throws UsernameNotFoundException {
		User user;
		try {
			user = accountService.findUserByEmail(userName);
		} catch (NoSuchUserException e) {
			throw new UsernameNotFoundException("UserName " + userName
					+ " not found");
		}
		SecurityUser securityUser = new SecurityUser(user);
		securityUser
				.setUserRoles(roleRepository.findByType(user.getId(), "Box"));
		if (securityUser.getUserRoles().isEmpty()){
			Role role = new Role();
			role.setName("User");
			LOGGER.warn("Role User add");
			securityUser.setUserRoles(Arrays.asList(role));
		}
		return securityUser;

	}
}