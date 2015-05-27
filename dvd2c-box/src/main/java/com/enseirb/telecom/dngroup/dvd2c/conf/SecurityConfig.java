package com.enseirb.telecom.dngroup.dvd2c.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
//	 @Override
//	    protected void configure(HttpSecurity http) throws Exception {
//
//	       http
//	       .authorizeRequests()
//			.anyRequest().hasRole("USER")
//			.and()
//        .httpBasic();
//	    }
//	 @Bean
//	    public RememberMeServices rememberMeServices() {
//	        // Key must be equal to rememberMe().key() 
//	        TokenBasedRememberMeServices rememberMeServices = new TokenBasedRememberMeServices("your_key", null);
//	        rememberMeServices.setCookieName("remember_me_cookie");
//	        rememberMeServices.setParameter("remember_me_checkbox");
//	        rememberMeServices.setTokenValiditySeconds(2678400); // 1month
//	        return rememberMeServices;
//	    }
//	        http
//	        
//	            .authorizeRequests()
//	                .anyRequest().authenticated()
//	                .and()
//	            .formLogin()
//	                .loginPage("/index.html").permitAll()
//	                .and()
//	            .formLogin()
//	            	.defaultSuccessUrl("/main.html")
//	            	.and().formLogin().failureUrl("/error.html").permitAll();
//	    }

	    @Override
	    protected void configure(HttpSecurity http) throws Exception {
	        http
	            .authorizeRequests()
//	                .antMatchers("/**").permitAll() 
	                .anyRequest().hasAnyRole("USER")
	                .and()
	            .formLogin()
	                .loginPage("/index.html")
	                .loginProcessingUrl("/j_spring_security_check")
	                .defaultSuccessUrl("/main.html")
	                .failureUrl("/")
	                .permitAll()
	                .and()
	                
	            .logout()                                    
	                .permitAll();
	    }


	// @formatter:off
	@Autowired
	public void configureGlobal(
			AuthenticationManagerBuilder auth) throws Exception {
		auth
			.inMemoryAuthentication()
				.withUser("user").password("password").roles("USER");
	}
	// @formatter:on
}
