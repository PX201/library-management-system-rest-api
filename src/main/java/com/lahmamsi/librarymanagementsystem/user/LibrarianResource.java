package com.lahmamsi.librarymanagementsystem.user;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

/**
 * @author Aiman
 */
@RestController
@RequestMapping("/api/v1/librarians")
public class LibrarianResource {
	@Autowired
	private LibrarianService service;

	@PutMapping("/Admin/{id}/Role")
	public ResponseEntity<LibrarianResponseDto> updateUserInfo(@PathVariable long id, @RequestBody Set<Role> roles) {
		return ResponseEntity.ok(service.updateRole(id, roles));
	}

	@PostMapping("/Admin")
	public ResponseEntity<Void> registerLibrarian(@Valid @RequestBody AuthRequest request) {
		try {
			service.add(request);
			return ResponseEntity.status(HttpStatus.CREATED).build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
		}
	}

	@GetMapping("/Admin")
	public ResponseEntity<List<LibrarianResponseDto>> getLibrarians(@RequestParam(defaultValue = "") String keyword) {
		if (!keyword.isBlank() || !keyword.isEmpty()) {
			var librarian = service.findByEmail(keyword);
			return 
				ResponseEntity
					.ok(List.of(new LibrarianResponseDto(librarian.getId(), librarian.getFirstName(), librarian.getLastName(), librarian.getEmail(), librarian.getPhoneNumber(), librarian.getRoles())));
		}
		return ResponseEntity.ok(service.retrieveAllLibrarians());
	}

	@GetMapping("/librarian")
	public ResponseEntity<LibrarianResponseDto> getLibrarian() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String librarianEmail = authentication.getName();
		var librarian = service.findByEmail(librarianEmail);
		if (librarian != null) {
			var libraianResponse = new LibrarianResponseDto(librarian.getId(), librarian.getFirstName(), librarian.getLastName(), librarian.getEmail(), librarian.getPhoneNumber(), librarian.getRoles());
			return new ResponseEntity<>(libraianResponse, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Void> changePassword(@PathVariable long id, @RequestParam String currentPassword,
			@RequestParam String newPassword) throws UserPrincipalNotFoundException {
		System.out.println("changing password");
		if (service.updatePassword(id, currentPassword, newPassword))
			return ResponseEntity.status(HttpStatus.ACCEPTED).build();
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}

	@PutMapping("")
	public ResponseEntity<LibrarianResponseDto> updateUserInfo(@Valid @RequestBody LibrarianDTO librarianDto) {
		System.out.println("inside endPoint");
		return ResponseEntity.ok(service.updateInfo(librarianDto));
	}

}
