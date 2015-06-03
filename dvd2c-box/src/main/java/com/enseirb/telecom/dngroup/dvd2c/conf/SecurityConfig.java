package com.enseirb.telecom.dngroup.dvd2c.conf;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

import com.enseirb.telecom.dngroup.dvd2c.filter.CsrfHeaderFilter;
import com.enseirb.telecom.dngroup.dvd2c.security.AjaxAuthenticationFailureHandler;
import com.enseirb.telecom.dngroup.dvd2c.security.AjaxAuthenticationSuccessHandler;
import com.enseirb.telecom.dngroup.dvd2c.security.AjaxLogoutSuccessHandler;
import com.enseirb.telecom.dngroup.dvd2c.security.Http401UnauthorizedEntryPoint;

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

	// @Override
	// protected void configure(HttpSecurity http) throws Exception {
	//
	// http
	// .authorizeRequests()
	// .anyRequest().hasRole("USER")
	// .and()
	// .httpBasic();
	// }
	// @Bean
	// public RememberMeServices rememberMeServices() {
	// // Key must be equal to rememberMe().key()
	// TokenBasedRememberMeServices rememberMeServices = new
	// TokenBasedRememberMeServices("your_key", null);
	// rememberMeServices.setCookieName("remember_me_cookie");
	// rememberMeServices.setParameter("remember_me_checkbox");
	// rememberMeServices.setTokenValiditySeconds(2678400); // 1month
	// return rememberMeServices;
	// }
	// http
	//
	// .authorizeRequests()
	// .anyRequest().authenticated()
	// .and()
	// .formLogin()
	// .loginPage("/index.html").permitAll()
	// .and()
	// .formLogin()
	// .defaultSuccessUrl("/main.html")
	// .and().formLogin().failureUrl("/error.html").permitAll();
	// }

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
//		 http
////         .csrf()
////         .ignoringAntMatchers("/websocket/**")
////     .and()
//         .addFilterAfter(new CsrfHeaderFilter(), CsrfFilter.class)
//         .exceptionHandling()
//         .authenticationEntryPoint(authenticationEntryPoint)
//     .and()
////         .rememberMe()
////         .rememberMeServices(rememberMeServices)
//////         .rememberMeParameter("remember-me")
////         .key("5c37379956bd1242f5636c8cb322c2966ad81277")
////     .and()
//         .formLogin()
//         .loginProcessingUrl("/index.html")
//         .successHandler(ajaxAuthenticationSuccessHandler)
//         .failureHandler(ajaxAuthenticationFailureHandler)
//         .usernameParameter("j_username")
//         .passwordParameter("j_password")
//         .permitAll()
//     .and()
//         .logout()
//         .logoutUrl("/api/logout")
//         .logoutSuccessHandler(ajaxLogoutSuccessHandler)
//         .deleteCookies("JSESSIONID")
//         .permitAll()
//     .and()
//         .headers()
//         .frameOptions()
//         .disable();

		http
		.csrf().disable()
//		.addFilterAfter(new CsrfHeaderFilter(), CsrfFilter.class).csrf()
//			.requireCsrfProtectionMatcher(csrfRequestMatcher)
//			.csrfTokenRepository(csrfTokenRepository())
//		.and()
			.authorizeRequests()
			.antMatchers("/api/application.wadl","/api/app/register/").permitAll()
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
         .and()
		     .logout()
		     .logoutUrl("/api/logout")
		     .logoutSuccessHandler(ajaxLogoutSuccessHandler)
		     .deleteCookies("JSESSIONID")
		     .permitAll();
	}

	private CsrfTokenRepository csrfTokenRepository() {
		HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
		repository.setHeaderName("X-XSRF-TOKEN");
		return repository;
	}

	protected void configure(AuthenticationManagerBuilder registry)
			throws Exception { 
		registry.userDetailsService(customUserDetailsService);
	}
}
