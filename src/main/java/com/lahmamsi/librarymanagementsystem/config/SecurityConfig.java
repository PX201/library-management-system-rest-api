package com.lahmamsi.librarymanagementsystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.lahmamsi.librarymanagementsystem.user.Role;

@EnableWebSecurity
@Configuration
public class SecurityConfig {
	private final JwtAuthenticationFIlter jwtAuthenticationFIlter;
	private final AuthenticationProvider authenticationProvider;

	//Injecting necessary component for configuring security
	public SecurityConfig(JwtAuthenticationFIlter jwtAuthenticationFIlter,
			AuthenticationProvider authenticationProvider) {
		this.jwtAuthenticationFIlter = jwtAuthenticationFIlter;
		this.authenticationProvider = authenticationProvider;
	}

	// Configuring the filter chain for handling HTTP security
	@Bean
	public SecurityFilterChain fiterChain(HttpSecurity http) throws Exception {
		return http.csrf(csrf -> csrf.disable())
				.formLogin(form -> form.disable())
				.authorizeHttpRequests(auth -> 
						auth.requestMatchers("/api/v1/auth/login").permitAll()
								.requestMatchers("/api/v1/auth/resetpass/**").permitAll()
								.requestMatchers("/v3/api-docs/**", "/swagger-ui/**").permitAll()
								.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
								.requestMatchers("/api/v1/librarians/Admin/**").hasAnyAuthority("Role_" + Role.ADMIN.name())
								.anyRequest().hasAnyAuthority("Role_" + Role.LIBRARIAN.name(), "Role_" + Role.ADMIN.name()))
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authenticationProvider(authenticationProvider)
				.addFilterBefore(jwtAuthenticationFIlter, UsernamePasswordAuthenticationFilter.class).build();
	}
}

// 				.anyRequest().authenticated()
//				.oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))