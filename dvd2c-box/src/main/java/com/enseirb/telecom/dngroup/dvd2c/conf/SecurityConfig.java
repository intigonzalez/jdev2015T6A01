package com.enseirb.telecom.dngroup.dvd2c.conf;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.RememberMeServices;
import com.enseirb.telecom.dngroup.dvd2c.security.AjaxAuthenticationFailureHandler;
import com.enseirb.telecom.dngroup.dvd2c.security.AjaxAuthenticationSuccessHandler;
import com.enseirb.telecom.dngroup.dvd2c.security.AjaxLogoutSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private CustomUserDetailsService customUserDetailsService;
	
    @Inject
    private AjaxAuthenticationSuccessHandler ajaxAuthenticationSuccessHandler;

    @Inject
    private AjaxAuthenticationFailureHandler ajaxAuthenticationFailureHandler;
    
    @Inject
    private AjaxLogoutSuccessHandler ajaxLogoutSuccessHandler;
    

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
			.antMatchers("/api/application.wadl","/api/app/register/").permitAll()
			.antMatchers("/api/box/**").permitAll()
			.antMatchers("/api/app/**").hasIpAddress("127.0.0.1")
			.anyRequest().authenticated()
		.and()
			.formLogin()
			.loginPage("/index.html")
			.loginProcessingUrl("/api/authentication")
			.successHandler(ajaxAuthenticationSuccessHandler)
			.failureHandler(ajaxAuthenticationFailureHandler)
			.usernameParameter("j_username")
			.passwordParameter("j_password")
			.permitAll()
//		.and()
//		    .rememberMe()
//		    .rememberMeServices(rememberMeServices)
//		    .key("5c37379956bd1242f5636c8cb322c2966ad81277")
         .and()
		     .logout()
		     .logoutUrl("/api/logout")
		     .logoutSuccessHandler(ajaxLogoutSuccessHandler)
		     .deleteCookies("JSESSIONID")
		     .permitAll()
	     .and()
	     	.csrf()
	     	.disable();
//	     .and()
//		    .addFilterAfter(new CsrfHeaderFilter(), CsrfFilter.class);
//			.requireCsrfProtectionMatcher()
//			.csrfTokenRepository(csrfTokenRepository());
	}


	protected void configure(AuthenticationManagerBuilder registry)
			throws Exception { 
		registry.userDetailsService(customUserDetailsService);
	}
}
