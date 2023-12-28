package com.lahmamsi.librarymanagementsystem.user;

import java.util.Set;

/**
 * @author Aiman
 */
public class LibrarianResponseDto {
	private long id;

	private String firstName;

	private String lastName;

	private String email;

	private String phoneNumber;

	private Set<Role> roles;

	LibrarianResponseDto(){	
	}
	
	public LibrarianResponseDto(long id, String firstName, String lastName, String email, String phoneNumber,
			Set<Role> roles) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.roles = roles;
	}



	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	@Override
	public String toString() {
		return "LibrarianResponseDto [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", email="
				+ email + ", phoneNumber=" + phoneNumber + ", roles=" + roles + "]";
	}

}
