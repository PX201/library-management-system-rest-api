package com.lahmamsi.librarymanagementsystem.borrowers;

import java.util.List;

import org.springframework.stereotype.Service;

import com.lahmamsi.librarymanagementsystem.exception.BorrowerNotFoundException;;

/**
 * 
 * @author Aiman
 *
 */
@Service
public class BorrowerService {

	BorrowerRepository borrowerRepo;
	

	public BorrowerService(BorrowerRepository borrowerRepo) {
		this.borrowerRepo = borrowerRepo;
	}

	public List<Borrower> getAllBorrowers(){
		return borrowerRepo.findAll();
	}
	
	public List<Borrower> getByLastFourDigits(String  lastFOurDigits){
		return borrowerRepo.findByPhoneEndingWith(lastFOurDigits);
	}

	public Borrower getBorrowerByBorrowerId(long borrowerId) {
		return borrowerRepo.findByBorrowerId(borrowerId)
				.orElseThrow(() -> new BorrowerNotFoundException("Can't find Borrower with id: " + borrowerId));

	}
	public Borrower getBorrowerByEmail(String email) {
		return borrowerRepo.findByEmail(email)
				.orElseThrow(() -> new BorrowerNotFoundException("Can't find Borrower with email: " + email));

	}
	public Borrower getBorrowerByPhone(String phone) {
		return borrowerRepo.findByPhone(phone)
				.orElseThrow(() -> new BorrowerNotFoundException("Can't find Borrower with phone: " + phone));

	}
	public Borrower getBorrowerByBorrowerNumber(String borrowerNumber) {
		return borrowerRepo.findByBorrowerNumber(borrowerNumber)
				.orElseThrow(() -> new BorrowerNotFoundException("Can't find Borrower with Borrower Number: " + borrowerNumber));

	}
	

	public Borrower addBorrower(Borrower borrower) {
		borrower.setMembershipStatus(MembershipStatus.ACTIVE);
		return borrowerRepo.save(borrower);
	}

	

	public void updateBorrower(Borrower borrower, long id) {
		if(!borrowerRepo.existsById(id))
			throw new BorrowerNotFoundException("Can't find Borrower with id: " + id);

		borrower.setBorrowerId(id);
		borrowerRepo.save(borrower);
	}

	public void deleteBorrower(long id) {
		if(borrowerRepo.existsById(id))
			borrowerRepo.deleteById(id);
		else
			throw new BorrowerNotFoundException("Can't find Borrower with id: " + id);
	}


}
