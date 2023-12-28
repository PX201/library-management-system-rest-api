package com.lahmamsi.librarymanagementsystem.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.lahmamsi.librarymanagementsystem.user.LibrarianRepository;

@Configuration
public class ApplicationConfig {
	
	@Autowired
	private LibrarianRepository librarianRepository;
	
	/**
     * Configures a UserDetailsService bean responsible for loading user-specific data.
     * It fetches a librarian by their email using the LibrarianRepository and throws
     * a UsernameNotFoundException if not found.
     */
	@Bean
	public UserDetailsService userDetailsService() {
		return username -> librarianRepository.findByEmail(username)
				.orElseThrow(() -> new UsernameNotFoundException("can't find user wirh email = "+ username));
	}
	
	/**
     * Configures an AuthenticationProvider bean using DaoAuthenticationProvider.
     * It specifies the user details service and password encoder.
     */
	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}
	
	/**
     * Creates an AuthenticationManager bean by retrieving it from the AuthenticationConfiguration.
     * This method is used for authentication purposes within the security configuration.
     * @param config AuthenticationConfiguration object used to retrieve the AuthenticationManager
     */
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager(); 
	}

	/**
     * Defines a PasswordEncoder bean using BCryptPasswordEncoder for encoding passwords.
     * This method configures password encoding for secure storage in the application.
     */
	@Bean
	public PasswordEncoder passwordEncoder() {
		
		return new BCryptPasswordEncoder();
	}
	
	 /* Commented-out method - Example of CORS Configuration */
//	@Bean
//	public CorsFilter corsFilter() {
//	    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//	    CorsConfiguration config = new CorsConfiguration();
//	    config.addAllowedOrigin("http://localhost:3000"); // Adjust this to match your React app's origin.
//	    config.addAllowedMethod("*");
//	    config.addAllowedHeader("*");
//	    source.registerCorsConfiguration("/**", config);
//	    return new CorsFilter(source);
//	}

}
