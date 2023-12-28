package com.lahmamsi.librarymanagementsystem.config;

/**
 * Used To return the Authentication Response Token
 * @author Aiman
 *
 */
public class AuthenticationResponse {

	private String Token;

	public AuthenticationResponse(String token) {
		Token = token;
	}

	public String getToken() {
		return Token;
	}

	public void setToken(String token) {
		Token = token;
	}

}
