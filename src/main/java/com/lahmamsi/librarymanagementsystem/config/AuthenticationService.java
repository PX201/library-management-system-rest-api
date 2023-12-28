package com.lahmamsi.librarymanagementsystem.config;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.HashSet;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.lahmamsi.librarymanagementsystem.user.AuthRequest;
import com.lahmamsi.librarymanagementsystem.user.Librarian;
import com.lahmamsi.librarymanagementsystem.user.LibrarianRepository;
import com.lahmamsi.librarymanagementsystem.user.Role;
/**
 * 
 * @author Aiman
 *
 */
@Service
public class AuthenticationService {

	private LibrarianRepository repository;
	private PasswordEncoder passwordEncoder;
	private JwtService jwtService;
	private AuthenticationManager authenticationManager;

	/**
	 * Constructs and inject the AuthenticationService with required dependencies
	 * @param repository LibrarianRepository for database interaction 
	 * @param passwordEncoder PasswordRncoder for hashing the password
	 * @param jwtService JwtService for generating JWT Token
	 * @param authenticationManager AuthenticationManager for user authentication
	 */
	public AuthenticationService(LibrarianRepository repository, PasswordEncoder passwordEncoder, JwtService jwtService,
			AuthenticationManager authenticationManager) {
		this.repository = repository;
		this.passwordEncoder = passwordEncoder;
		this.jwtService = jwtService;
		this.authenticationManager = authenticationManager;
	}

	/**
	 * Register a new Librarian based on the AuthRequest details.
     * Encodes the password, assigns roles, and generates a JWT token for the registered librarian.
	 * @param request AuthRequest containing Librarian registration details
	 * @return new AuthenticationRespose that contain the JWT token for future requests 
	 */
	public AuthenticationResponse register(AuthRequest request) {
		var roles = new HashSet<Role>();
		roles.add(Role.LIBRARIAN);
		
		var librarian = new Librarian();
		librarian.setEmail(request.getEmail());
		librarian.setFirstName(request.getFirstName());
		librarian.setLastName(request.getLastName());
		librarian.setPhoneNumber(request.getPhoneNumber());
		librarian.setPassword(passwordEncoder.encode(request.getPassword()));
		librarian.setRoles(roles);
		
		repository.save(librarian);
		
		String generatedToken = jwtService.generateToken(librarian);
		return new AuthenticationResponse(generatedToken);
	}
/**
 * Authenticate the Librarian based on the AuthRequest details provided (email and password)
 * Uses the AuthenticationManager to authenticate the librarian and generate a JWT Token
 * @param request AuthRequest contains the librarian credentials for authentication
 * @return AuthResponse containing JWT token
 * @throws UserPrincipalNotFoundException when the user principal is not found
 */
	public AuthenticationResponse authenticate(AuthRequest request) throws UserPrincipalNotFoundException {
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
		var librarian = repository.findByEmail(request.getEmail()).orElseThrow(() -> new UserPrincipalNotFoundException("can't find user With Email =" + request.getEmail()));
		String jwtToken = jwtService.generateToken(librarian);
		return new AuthenticationResponse(jwtToken);
	}

}
