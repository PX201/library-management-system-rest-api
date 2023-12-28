package com.lahmamsi.librarymanagementsystem.transaction;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import com.lahmamsi.librarymanagementsystem.book.Book;
import com.lahmamsi.librarymanagementsystem.borrowers.Borrower;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

/**
 * 
 * @author Aiman
 *
 */
@Component
@Entity
@Table(name = "`transaction_record`")
public class Transaction {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long transactionId;

	@Column(unique = true)
	private String transactionNumber;

	@ManyToOne
	@JoinColumn(name = "borrowerId")
	private Borrower borrower;

	@ManyToOne
	@JoinColumn(name = "bookId")
	private Book book;
	@NotNull
	// @DateTimeFormat(pattern = "MM-dd-yyyy")
	private LocalDate borrowDate;

	@NotNull
	// @DateTimeFormat(pattern = "MM-dd-yyyy")
	private LocalDate returnDate;

	@Column(columnDefinition = "BIT DEFAULT 0")
	private boolean isReturned;

	@Column(nullable = true)
	private LocalDate actualReturnDate;

	@Column(columnDefinition = "DOUBLE DEFAULT 0.00 CHECK (late_fee >= 0)")
	private double lateFee;

	@Column(columnDefinition = "DOUBLE DEFAULT 0.00 CHECK (damage_fine >= 0)")
	private double damageFine;

	@Column(columnDefinition = "BIT DEFAULT 0")
	private boolean isPaid;

	public Long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
	}

	public String getTransactionNumber() {
		return transactionNumber;
	}

	public void setTransactionNumber(String transactionNumber) {
		this.transactionNumber = transactionNumber;
	}

	public Borrower getBorrower() {
		return borrower;
	}

	public void setBorrower(Borrower borrower) {
		this.borrower = borrower;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
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

	public boolean isReturned() {
		return isReturned;
	}

	public void setReturned(boolean isReturned) {
		this.isReturned = isReturned;
	}

	public LocalDate getActualReturnDate() {
		return actualReturnDate;
	}

	public void setActualReturnDate(LocalDate actualReturnDate) {
		this.actualReturnDate = actualReturnDate;
	}

	public double getLateFee() {
		return lateFee;
	}

	public void setLateFee(double lateFee) {
		this.lateFee = lateFee;
	}

	public double getDamageFine() {
		return damageFine;
	}

	public void setDamageFine(double damageFine) {
		this.damageFine = damageFine;
	}

	public boolean isPaid() {
		return isPaid;
	}

	public void setPaid(boolean isPaid) {
		this.isPaid = isPaid;
	}

	// Method executed before persisting the transaction to generate a unique
	// transaction number
	@PrePersist
	public void doBefore() {
		this.setTransactionNumber(generateTransactionNumber());
	}

	// Method to generate a unique transaction number
	private String generateTransactionNumber() {
		Long time = System.currentTimeMillis();
		return time + "";
	}

	@Override
	public String toString() {
		return "Transaction [transactionId=" + transactionId + ", borrower=" + borrower + ", book=" + book
				+ ", borrowDate=" + borrowDate + ", returnDate=" + returnDate + ", isReturned=" + isReturned
				+ ", actualReturnDate=" + actualReturnDate + ", lateFee=" + lateFee + ", damageFine=" + damageFine
				+ ", isPaid=" + isPaid + "]";
	}

}
