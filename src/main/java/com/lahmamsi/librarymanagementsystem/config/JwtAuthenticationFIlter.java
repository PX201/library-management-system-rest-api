package com.lahmamsi.librarymanagementsystem.config;

import java.io.IOException;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.lahmamsi.librarymanagementsystem.user.Librarian;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
/**
 * 
 * @author Aiman
 *
 */
@Component
public class JwtAuthenticationFIlter extends OncePerRequestFilter{
	
	private JwtService jwtService;
	private UserDetailsService userDetailsService;

	 /**
     * Constructs the JwtAuthenticationFilter with JwtService and UserDetailsService dependencies.
     * @param jwtService        JwtService for JWT-related operations
     * @param userDetailsService UserDetailsService for managing user details
     */
	public JwtAuthenticationFIlter(JwtService jwtService, UserDetailsService userDetailsService) {
		this.jwtService = jwtService;
		this.userDetailsService = userDetailsService;
	}


	 /**
     * Intercepts the incoming HTTP requests to check for JWT authentication.
     * Verifies the JWT in the Authorization header and sets the authenticated user in the SecurityContext.
     * @param request     HttpServletRequest representing the incoming request
     * @param response    HttpServletResponse representing the outgoing response
     * @param filterChain FilterChain for handling the filter chain
     * @throws ServletException if an issue occurs during filtering
     * @throws IOException      if an I/O exception occurs
     */
	@Override
	protected void doFilterInternal(
			@NonNull HttpServletRequest request, 
			@NonNull HttpServletResponse response, 
			@NonNull FilterChain filterChain
			)throws ServletException, IOException {
		String authHeader = request.getHeader("Authorization");
		String jwt;
		String username;
		
		if(authHeader == null || !authHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}
		jwt = authHeader.substring(7);
		username = jwtService.extractUsername(jwt);
		if( username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			Librarian librarian = (Librarian) this.userDetailsService.loadUserByUsername(username);
			if(jwtService.isTokenValid(jwt, librarian)) {
				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(librarian, null, librarian.getAuthorities());
				authToken.setDetails( new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authToken);
			}
		}
		filterChain.doFilter(request, response);
	}

}

