package com.lahmamsi.librarymanagementsystem.borrowers;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
/**
 * 
 * @author Aiman
 *
 */
public interface BorrowerRepository extends JpaRepository<Borrower, Long>{
	public Optional<Borrower> findByBorrowerId(long borrowerId);
	
	public Optional<Borrower> findByEmail(String emial);
	
	//@Query()
	public Optional<Borrower> findByPhone(String phone);
	
	public List<Borrower> findByPhoneEndingWith(String lastFourDigits);
	
	public Optional<Borrower> findByBorrowerNumber(String borrowerNumber);

}
