package com.lahmamsi.librarymanagementsystem.borrowers;

import static com.lahmamsi.librarymanagementsystem.utilities.URIUtils.buildResourceUri;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/borrowers")
public class BorrowerResource {

	@Autowired
	BorrowerService service;
	
	@GetMapping("")
	public ResponseEntity<List<Borrower>>  retrieveAll() {
		return ResponseEntity.ok(service.getAllBorrowers());
	}

	@GetMapping("/search")
	public ResponseEntity<List<Borrower>> searchByKeyword( @RequestParam(defaultValue = "") String keyword) {
		
		if(keyword.matches("^[0-9]{4}$"))
			return ResponseEntity.ok(service.getByLastFourDigits(keyword));
		if(keyword.matches("^[0-9]{10}$"))
			return ResponseEntity.ok(List.of(service.getBorrowerByPhone(keyword)));
		if(keyword.matches("^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$"))
			return ResponseEntity.ok(List.of(service.getBorrowerByEmail(keyword)));
		return ResponseEntity.ok(service.getAllBorrowers());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Borrower> getBorrower(@PathVariable long id) {
		return ResponseEntity.ok(service.getBorrowerByBorrowerId(id));
	}
	
	@GetMapping("/borrower/{borrowerNumber}")
	public ResponseEntity<Borrower> getBorrower(@PathVariable String borrowerNumber) {
		return ResponseEntity.ok(service.getBorrowerByBorrowerNumber(borrowerNumber));
	}

	@PostMapping("")
	public ResponseEntity<Borrower> addBorrower(@Valid @RequestBody Borrower borrower, HttpServletRequest request) {
		try {
			var addedBorrower = service.addBorrower(borrower);
			URI uri = buildResourceUri(request, addedBorrower.getBorrowerId());

			return ResponseEntity.created(uri).build();
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<Void> updateBorrower(@PathVariable long id, @RequestBody Borrower borrower) {
		service.updateBorrower(borrower, id);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteBorrower(@PathVariable long id) {
		service.deleteBorrower(id);
		return ResponseEntity.noContent().build();
	}

}
