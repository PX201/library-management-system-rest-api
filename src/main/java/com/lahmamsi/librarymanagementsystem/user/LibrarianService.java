package com.lahmamsi.librarymanagementsystem.user;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.lahmamsi.librarymanagementsystem.exception.LibraryManagmentException;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

/**
 * @author Aiman
 */
@Service
public class LibrarianService {
	@Value("${library.admin.email}")
	private String defaultAdminEmail;

	@Value("${library.admin.password}")
	private String defaultAdminPassword;

	@Value("${library.admin.firstname}")
	private String defaultAdminFirstName;

	@Value("${library.admin.lastname}")
	private String defaultAdminLastName;

	@Value("${library.admin.phonenumber}")
	private String defaultAdminPhoneNumber;

	private LibrarianRepository librarianRepository;
	private PasswordEncoder encoder;
	private AuthenticationManager authenticationManager;

	public LibrarianService(LibrarianRepository librarianRepository, PasswordEncoder encoder,
			AuthenticationManager authenticationManager) {
		this.librarianRepository = librarianRepository;
		this.encoder = encoder;
		this.authenticationManager = authenticationManager;

	}

    // Method to add a new librarian based on incoming request
	public Librarian add(AuthRequest request) {

		var librarian = new Librarian();
		librarian.setEmail(request.getEmail());
		librarian.setFirstName(request.getFirstName());
		librarian.setLastName(request.getLastName());
		librarian.setPhoneNumber(request.getPhoneNumber());
		librarian.setPassword(encoder.encode(request.getPassword()));

		return librarianRepository.save(librarian);
	}

	public LibrarianResponseDto findByEmail(String librarianEmail) {
		 // Finding librarian by email and throwing an exception if not found
		Librarian librarian =  librarianRepository.findByEmail(librarianEmail)
													.orElseThrow(() -> new LibraryManagmentException("Can't find librarian with email " + librarianEmail));
        // Returning a response DTO containing librarian details
		return new LibrarianResponseDto(librarian.getId(), librarian.getFirstName(), librarian.getLastName(), librarian.getEmail(), librarian.getPhoneNumber(), librarian.getRoles());
	}

	public LibrarianResponseDto updateInfo(@Valid LibrarianDTO librarianDTO) {
		// Fetch the Librarian entity by ID
		Librarian existingLibrarian = librarianRepository.findById(librarianDTO.getId())
				.orElseThrow(() -> new EntityNotFoundException("Librarian not found"));

		// Update the fields from the DTO
		existingLibrarian.setFirstName(librarianDTO.getFirstName());
		existingLibrarian.setLastName(librarianDTO.getLastName());
		existingLibrarian.setEmail(librarianDTO.getEmail());
		existingLibrarian.setPhoneNumber(librarianDTO.getPhoneNumber());
		// Update other fields accordingly

		// Save the updated Librarian entity
		var librarian = librarianRepository.save(existingLibrarian);
		
		return new LibrarianResponseDto(librarian.getId(), librarian.getFirstName(), librarian.getLastName(), librarian.getEmail(), librarian.getPhoneNumber(), librarian.getRoles());
	}

	public LibrarianResponseDto updateRole(long id, Set<Role> newRoles) {
		// Fetch the Librarian entity by ID
		Librarian librarian = librarianRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Librarian not found"));

		if (!newRoles.isEmpty()) {
			librarian.setRoles(newRoles);
			var updatedLibrarian =  librarianRepository.save(librarian);
			var librarianResponse =  new LibrarianResponseDto();
			librarianResponse.setFirstName(updatedLibrarian.getFirstName());
			librarianResponse.setLastName(updatedLibrarian.getLastName());
			librarianResponse.setEmail(updatedLibrarian.getEmail());
			librarianResponse.setPhoneNumber(updatedLibrarian.getPhoneNumber());
			librarianResponse.setRoles(updatedLibrarian.getRoles());
			
			return librarianResponse;
		}
		return null;
	}

	public boolean updatePassword(Long id, String currentPassword, String newPassword)
			throws UserPrincipalNotFoundException {
		// Fetch the Librarian entity by ID
		Librarian librarian = librarianRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Librarian not found"));
		try {
			authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(librarian.getEmail(), currentPassword));
			librarian.setPassword(encoder.encode(newPassword));
			librarianRepository.save(librarian);
			return true;
		} catch (Exception e) {
			return false;
		}

	}

	public List<LibrarianResponseDto> retrieveAllLibrarians() {
		// TODO Auto-generated method stub
		var librarians =  librarianRepository.findAll();
		return librarians.stream().map(librarian -> new LibrarianResponseDto(librarian.getId(), librarian.getFirstName(), librarian.getLastName(), librarian.getEmail(), librarian.getPhoneNumber(), librarian.getRoles())).toList();
	}
	
    // Method to create a default admin user if not present in the repository
	public void createDefaultAdminUser() {
	    Librarian admin = librarianRepository.findByEmail(defaultAdminEmail).orElse(new Librarian());

	    // Check if the email is null or blank
	    if (admin.getEmail() == null || admin.getEmail().isBlank()) {
	        admin.setFirstName(defaultAdminFirstName);
	        admin.setLastName(defaultAdminLastName);
	        admin.setEmail(defaultAdminEmail);
	        admin.setPhoneNumber(defaultAdminPhoneNumber);
	    } else {
	        // If not null or blank, ensure it matches the default email
	        if (!admin.getEmail().equals(defaultAdminEmail)) {
	            admin.setEmail(defaultAdminEmail);
	        }
	    }

	    admin.setPassword(encoder.encode(defaultAdminPassword)); // Encode the password
	    admin.setRoles(Collections.singleton(Role.ADMIN)); // Set the admin role

	    librarianRepository.save(admin);
	}


	
}
