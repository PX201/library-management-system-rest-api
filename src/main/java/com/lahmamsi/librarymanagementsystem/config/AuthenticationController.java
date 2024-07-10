package com.lahmamsi.librarymanagementsystem.config;

import java.nio.file.attribute.UserPrincipalNotFoundException;

import com.lahmamsi.librarymanagementsystem.user.AuthRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 
 * @author Aiman
 *
 */
@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

	@Autowired
	private AuthenticationService authService;
	
	
//	@PostMapping("/register")
//	public ResponseEntity<AuthenticationResponce> register(@RequestBody AuthRequest request){
//		
//		return ResponseEntity.ok(authService.register(request));
//	}
	
	/**
     * Handles POST requests to '/api/v1/auth/login' for user authentication.
     * Accepts an AuthRequest object containing user credentials.
     * Invokes the AuthenticationService to authenticate the user and returns
     * an AuthenticationResponse ResponseEntity with the authentication result.
     * @param request AuthRequest object containing user credentials
     * @return ResponseEntity containing the AuthenticationResponse based on the authentication result
     * @throws UserPrincipalNotFoundException when the user principal is not found
     */
	@PostMapping("/login")
	public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthRequest request) throws UserPrincipalNotFoundException{
		
		return ResponseEntity.ok(authService.authenticate(request));
	}
	
	@PostMapping("/resetpass/{email}")
	public ResponseEntity<Void> passwordResetReq(@PathVariable String email, @RequestHeader("X-Base-URL") String baseURL){		
		try {
			authService.sendPasswordResetLink(email, baseURL);
			
		} catch (UserPrincipalNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}

		return ResponseEntity.status(HttpStatus.OK).build();
	}
}
