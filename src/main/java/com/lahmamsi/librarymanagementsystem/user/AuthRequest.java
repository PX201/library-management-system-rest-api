package com.lahmamsi.librarymanagementsystem.user;

/**
 * @author Aiman
 */
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
public class AuthRequest {

	private String firstName;
	
	private String lastName;

	@Email
	private String email;

	@Pattern(regexp = "^[0-9]{10}$")
	private String PhoneNumber;

	private String password;

	public AuthRequest() {
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhoneNumber() {
		return PhoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		PhoneNumber = phoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
