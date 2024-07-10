package com.lahmamsi.librarymanagementsystem.config;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.HashSet;
import java.util.Properties;

import com.lahmamsi.librarymanagementsystem.user.AuthRequest;
import com.lahmamsi.librarymanagementsystem.user.Librarian;
import com.lahmamsi.librarymanagementsystem.user.LibrarianRepository;
import com.lahmamsi.librarymanagementsystem.user.Role;
import jakarta.mail.Authenticator;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMessage.RecipientType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
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

	public void sendPasswordResetLink(String email, String baseUrl) throws UserPrincipalNotFoundException {
		Librarian librarian = repository.findByEmail(email).orElseThrow(() -> new UserPrincipalNotFoundException("can't find user With Email =" + email));
		String jwtToken = jwtService.generateToken(librarian);
		
		String resetLink = createResetLink(baseUrl, jwtToken, librarian.getId());
		sendResetLink(resetLink, email);
	}
	
	private void sendResetLink(String resetLink, String email) {
		Properties prop = new Properties();
		prop.put("mail.smtp.host", "smtp.gmail.com");
		prop.put("mail.smtp.port", "587");
		prop.put("mail.smtp.auth", "true");
		prop.put("mail.smtp.starttls.enable", "true");
		prop.put("mail.smtp.ssl.trust", "*");
		
		String username = "lahmamsiaiman99@gmail.com";
		String password = "xeubprxkimtvyljr";
		
		Session session = Session.getDefaultInstance(prop, new Authenticator() {
			 protected jakarta.mail.PasswordAuthentication getPasswordAuthentication() {
	                return new jakarta.mail.PasswordAuthentication(username, password);
	            }
		});
		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(username));
			message.addRecipients(RecipientType.TO ,email);
			message.setSubject("Reset Your Password..");
			message.setText("Your reset link is " + resetLink);
			
			Transport.send(message);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private String createResetLink(String baseURL, String token, long id) {
		return baseURL + "/resetpassword/" +  token + "/" + id; 
		
	}


}
