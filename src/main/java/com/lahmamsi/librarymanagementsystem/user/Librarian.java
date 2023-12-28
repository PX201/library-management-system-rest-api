package com.lahmamsi.librarymanagementsystem.user;

/**
 * @author Aiman
 */
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "librarian")

public class Librarian implements UserDetails {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Id
	private long id;
	@NotBlank
	private String firstName;
	@NotBlank
	private String lastName;
	@Email
	@Column(unique = true)
	private String email;
	@Pattern(regexp = "^[0-9]{10}$")
	private String PhoneNumber;

	private String password;

	@Enumerated(EnumType.STRING)
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "librarian_roles", joinColumns = @JoinColumn(name = "librarian_id"))
	@Column(name = "role")
	private Set<Role> roles = new HashSet<>();

	private short pin;

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
		return PhoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		PhoneNumber = phoneNumber;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public short getPin() {
		return pin;
	}

	public void setPin(short pin) {
		this.pin = pin;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Set<GrantedAuthority> authorities = new HashSet<>();
		for (var role : this.roles)
			authorities.add(new SimpleGrantedAuthority("Role_" + role.name()));
		return authorities;
	}

	@Override
	public String getUsername() {
		return this.email;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
	
    // PrePersist method to assign default roles if not provided
	@PrePersist
	public void doFirst() {
		var addedRoles = new HashSet<Role>();
		addedRoles.add(Role.LIBRARIAN);
		addedRoles.addAll(getRoles());
		this.setRoles(addedRoles);
	}

	@Override
	public String toString() {
		return "Librarian [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email
				+ ", PhoneNumber=" + PhoneNumber + ", password=" + password + ", roles=" + roles + ", pin=" + pin + "]";
	}

}
