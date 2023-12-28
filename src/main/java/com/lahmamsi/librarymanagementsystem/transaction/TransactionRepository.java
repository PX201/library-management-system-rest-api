package com.lahmamsi.librarymanagementsystem.transaction;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

	List<Transaction> findByBorrowerBorrowerId(long borrowerId);
	
	List<Transaction> findByBookBookIdAndIsReturned(long bookId, boolean returned);
	
	List<Transaction> findByBorrowerBorrowerIdAndIsReturned(long borrowerId, boolean isReturned);
	
	List<Transaction> findByBorrowerBorrowerIdAndIsPaid(long borrowerId, boolean isPaid);
	
	Optional<Transaction> findByTransactionNumber(String transactionNumber);
	
	List<Transaction> findByBookBookId(long bookId);
	
	Optional<Transaction> findByBookIsbnAndBorrowerBorrowerNumber(String borrowerNumber, String bookIsbn);

}
