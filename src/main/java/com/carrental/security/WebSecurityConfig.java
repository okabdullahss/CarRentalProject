package com.carrental.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.carrental.security.jwt.AuthTokenFilter;

import lombok.AllArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@AllArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{

	private UserDetailsService userDetailsService;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		 
		http.csrf().disable().
		sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().
		
		authorizeRequests().antMatchers(HttpMethod.OPTIONS,"/**").permitAll().and(). //CORS-PREFLIGHT (PUT/DELETE/PATCH METHODS) ALLOW
		
		authorizeRequests().antMatchers("/login","/register","/files/download/**","/files/display/**",
				"/contactmessage/visitors","/car/visitors/**").permitAll().
		anyRequest().authenticated();
		
		http.addFilterBefore(authJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
		//this filter will be executed before requests
		
	}
	
	//------------------------------SWAGGER DOCUMENTATION NECESSARY CONFIGS------------------------------------------
		public static final String[] AUTH_WHITE_LIST= {
				"/v3/api-docs/**",
				"swagger-ui.html",
				"/swagger-ui/**",
				"/",
				"index.html",
				"/images/**"
				
		//SWAGGER LOCAL SITE  = http://localhost:8080/swagger-ui/index.html" 
		};
		
		@Override //DO NOT FILTER THE SITE WITHIN THE WHITE LIST
		public void configure(WebSecurity web) throws Exception {
			 web.ignoring().antMatchers(AUTH_WHITE_LIST);
		}
	//-------------------------------------------------------------------------------------------------------------
	@Bean
	public AuthTokenFilter authJwtTokenFilter() {
		return new AuthTokenFilter();
	}
	
	@Bean
	@Override
	protected AuthenticationManager authenticationManager() throws Exception {		 
		return super.authenticationManager();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		 auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
}
