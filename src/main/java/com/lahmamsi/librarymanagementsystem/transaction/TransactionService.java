package com.lahmamsi.librarymanagementsystem.transaction;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lahmamsi.librarymanagementsystem.book.BookService;
import com.lahmamsi.librarymanagementsystem.borrowers.BorrowerService;
import com.lahmamsi.librarymanagementsystem.exception.TransactionNotFoundException;

/**
 * 
 * @author Aiman
 *
 */
@Service
public class TransactionService {

	private TransactionRepository transactionRepo;
	private BookService bookService;
	private BorrowerService borrowerService;

	public TransactionService(TransactionRepository transactionRepo, BookService bookService,
			BorrowerService borrowerService) {
		this.transactionRepo = transactionRepo;
		this.bookService = bookService;
		this.borrowerService = borrowerService;
	}

	public List<Transaction> retrieveAllTransactions() {
		return transactionRepo.findAll();
	}

	public List<Transaction> retrieveAllTransactionsByBorrowerId(long borrowerid) {
		return transactionRepo.findByBorrowerBorrowerId(borrowerid);
	}

	public List<Transaction> retrieveAllTransactionsByBookIdWHereBookNotReturnedYet(long bookId) {
		return transactionRepo.findByBorrowerBorrowerIdAndIsReturned(bookId, false);
	}

	public List<Transaction> retrieveAllTransactionsByBookId(long bookId, boolean isReturned) {
		return transactionRepo.findByBookBookIdAndIsReturned(bookId, isReturned);
	}

	public Transaction getTransactionById(long transactionId) {
		return transactionRepo.findById(transactionId).orElseThrow(
				() -> new TransactionNotFoundException("Can't find any transaction with the id=" + transactionId));
	}

	@Transactional
	public Transaction addTransaction(Transaction transaction) {
		bookService.borrowBook(transaction.getBook().getBookId());
		return transactionRepo.save(transaction);
	}

	@Transactional
	public List<Transaction> addAllTransactions(List<Transaction> transactions) {
		transactions.stream().forEach(transaction -> bookService.borrowBook(transaction.getBook().getBookId()));
		return transactionRepo.saveAll(transactions);
	}

	@Transactional
	public void updateTransaction(Transaction transaction, long transactionId) {
		transaction.setTransactionId(transactionId);
		if (!transactionRepo.existsById(transactionId))
			throw new TransactionNotFoundException("Can't find any transaction with the id=" + transactionId);
		transactionRepo.save(transaction);

	}

	@Transactional
	public void deleteTransaction(long transactionId) {
		if (!transactionRepo.existsById(transactionId))
			throw new TransactionNotFoundException("Can't find any transaction with the id=" + transactionId);
		transactionRepo.deleteById(transactionId);
	}

	public void deleteAll(List<Long> transactionIds) {
		var existedIds = transactionIds.stream().filter(id -> transactionRepo.existsById(id)).toList();
		transactionRepo.deleteAllById(existedIds);
	}

    // Creates a new transaction using a DTO and updates book borrowing count
	public Transaction createTransaction(TransactionDTO transactionDTO) {
		var borrower = borrowerService.getBorrowerByBorrowerId(transactionDTO.getBorrowerId());
		var book = bookService.getBookById(transactionDTO.getBookId());

		bookService.borrowBook(book.getBookId()); // so will increase the borrowing copies by one

		var transaction = new Transaction();
		transaction.setBook(book);
		transaction.setBorrower(borrower);
		transaction.setBorrowDate(transactionDTO.getBorrowDate());
		transaction.setReturnDate(transactionDTO.getReturnDate());

		return transactionRepo.save(transaction);
	}

	// Checks in a transaction and returns the associated book
	public boolean checkIn(Transaction transaction) {
		try {
			transactionRepo.save(transaction);
			bookService.returnBook(transaction.getBook().getBookId());
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public Transaction getByTransactionNumber(String transactionNumber) {
		return transactionRepo.findByTransactionNumber(transactionNumber).orElseThrow(
				() -> new TransactionNotFoundException("can't find transaction with number " + transactionNumber));
	}

	public Transaction getTransactionByBorrowerNumberAndBookIsbn(String borrowerNumber, String bookIsbn) {
		return transactionRepo.findByBookIsbnAndBorrowerBorrowerNumber(bookIsbn, borrowerNumber).orElseThrow(
				() -> new TransactionNotFoundException("can't find transaction with book isbn  " + bookIsbn));
	}

	public List<Transaction> bookTransactionHistory(long bookId) {
		return transactionRepo.findByBookBookId(bookId);
	}

}
