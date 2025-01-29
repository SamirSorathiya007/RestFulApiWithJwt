package com.samirproject.securingrestapi.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.samirproject.securingrestapi.service.CustomeUserDetailService;
import com.samirproject.securingrestapi.util.JwtUtil;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private CustomeUserDetailService customeUserDetailService;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		System.out.println("Inside password encoder bean");
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		System.out.println("Inside Authentication manager bean");
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		System.out.println("Inside the security filter chain bean");
		httpSecurity.csrf(csrf -> csrf.disable())
					.authorizeHttpRequests(auth -> auth.requestMatchers("/api/register", "/api/login").permitAll().anyRequest().authenticated())
					.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		
		httpSecurity.addFilterBefore(jwtRequestFilter(), UsernamePasswordAuthenticationFilter.class);
		return httpSecurity.build();
	}
	
	@Bean
	public JwtRequestFilter jwtRequestFilter() {
		System.out.println("Inside the jwt request filetr bean");
		return new JwtRequestFilter(jwtUtil, customeUserDetailService);
	}
}
