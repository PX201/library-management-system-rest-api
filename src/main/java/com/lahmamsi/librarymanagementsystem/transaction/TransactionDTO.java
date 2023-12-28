package com.lahmamsi.librarymanagementsystem.transaction;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

/**
 * @author Aiman
 * Data Transfer Object (DTO) representing transaction-related information.
 * Contains details necessary for creating or updating a transaction.
 * 
 */
public class TransactionDTO {

	private long bookId;

	private long borrowerId;

	@NotNull
	@DateTimeFormat(pattern = "MM-dd-yyyy")
	private LocalDate borrowDate;

	@NotNull
	@Future
	@DateTimeFormat(pattern = "MM-dd-yyyy")
	private LocalDate returnDate;

	public long getBookId() {
		return bookId;
	}

	public TransactionDTO() {

	}

	public void setBookId(long bookId) {
		this.bookId = bookId;
	}

	public long getBorrowerId() {
		return borrowerId;
	}

	public void setBorrowerId(long borrowerId) {
		this.borrowerId = borrowerId;
	}

	public LocalDate getBorrowDate() {
		return borrowDate;
	}

	public void setBorrowDate(LocalDate borrowDate) {
		this.borrowDate = borrowDate;
	}

	public LocalDate getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(LocalDate returnDate) {
		this.returnDate = returnDate;
	}

}
