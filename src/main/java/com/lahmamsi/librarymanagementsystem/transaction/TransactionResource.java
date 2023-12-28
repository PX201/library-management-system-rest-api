package com.lahmamsi.librarymanagementsystem.transaction;

import static com.lahmamsi.librarymanagementsystem.utilities.URIUtils.buildResourceUri;

import java.util.ArrayList;
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

import com.lahmamsi.librarymanagementsystem.utilities.URIUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

/**
 * 
 * @author Aiman
 *
 */
@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionResource {

	@Autowired
	private TransactionService service;

	private final static String DEFAULT_STRING = "DUMMY";

	// Endpoint to create a new transaction
	@PostMapping("")
	public ResponseEntity<Transaction> createTransaction(@Valid @RequestBody TransactionDTO transactionDTO,
			HttpServletRequest request) {
		// Validate the DTO and create a transaction
		Transaction createdTransaction = service.createTransaction(transactionDTO);
		var uri = URIUtils.buildResourceUri(request, createdTransaction.getTransactionId());
		return ResponseEntity.created(uri).body(createdTransaction);
	}

	// Old approach to create a new transaction
	@PostMapping("/old")
	public ResponseEntity<Void> newTransaction(@RequestBody Transaction transaction, HttpServletRequest request) {
		try {
			var addedTransaction = service.addTransaction(transaction);
			var uri = buildResourceUri(request, addedTransaction.getTransactionId());

			return ResponseEntity.created(uri).build();
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	//
	@PutMapping("/{transactionId}")
	public ResponseEntity<Void> updateTransaction(@PathVariable long transactionId,
			@RequestBody Transaction transaction) {
		service.updateTransaction(transaction, transactionId);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/{transactionId}")
	public ResponseEntity<Void> removeTransaction(@PathVariable long transactionId) {
		service.deleteTransaction(transactionId);
		return ResponseEntity.noContent().build();
	}

	// Get transactions based on transaction number or retrieve all transactions
	@GetMapping("")
	public ResponseEntity<List<Transaction>> getTransactions(
			@RequestParam(defaultValue = DEFAULT_STRING) String transactionNumber) {

		// Check if the input is a valid transaction number format
		if (transactionNumber.matches("^([0-9]{13})?$")) {
			List<Transaction> transactions = new ArrayList<>();
			try {
				var transaction = service.getByTransactionNumber(transactionNumber);
				transactions.add(transaction);

				return ResponseEntity.accepted().body(transactions);
			} catch (Exception e) {
				return ResponseEntity.internalServerError().build();
			}
		}
		// If not a valid transaction number format, retrieve all transactions
		return ResponseEntity.accepted().body(service.retrieveAllTransactions());
	}

	@GetMapping("/{transactionId}")
	public Transaction getTransaction(@PathVariable long transactionId) {
		return service.getTransactionById(transactionId);

	}

	@GetMapping("/transaction/{transactionNumber}")
	public Transaction getTransaction(@PathVariable String transactionNumber) {
		return service.getByTransactionNumber(transactionNumber);
	}

	@PutMapping("/checkin")
	public ResponseEntity<Void> checkIn(@RequestBody Transaction transaction) {
		var isChecked = service.checkIn(transaction);
		if (isChecked)
			return ResponseEntity.accepted().build();
		return ResponseEntity.internalServerError().build();
	}

	// Get all transactions for a specific borrower by borrower ID
	@GetMapping("/borrowers/{borrowerId}")
	public List<Transaction> getAllBorrowerTransactions(@PathVariable long borrowerId) {
		return service.retrieveAllTransactionsByBorrowerId(borrowerId);
	}

	@GetMapping("/books/{bookId}")
	public List<Transaction> getAllCurrentBookBorrowingTransactions(@PathVariable long borrowerId) {
		return service.retrieveAllTransactionsByBookIdWHereBookNotReturnedYet(borrowerId);

	}

	// Get transaction history for a specific book by book ID
	@GetMapping("/transactionHistory/{bookId}")
	public ResponseEntity<List<Transaction>> bookBorrowingHistory(@PathVariable long bookId) {
		return ResponseEntity.ok(service.bookTransactionHistory(bookId));
	}

	// Get a transaction by borrower number and ISBN
	@GetMapping("/transactionHistory/{BorrowerNumber}/{isbn}")
	public ResponseEntity<Transaction> retrievTransactionByBorrowerAndBook(@PathVariable String BorrowerNumber,
			@PathVariable String isbn) {
		return ResponseEntity.ok(service.getTransactionByBorrowerNumberAndBookIsbn(BorrowerNumber, isbn));
	}
}
