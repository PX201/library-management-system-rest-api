package com.lahmamsi.librarymanagementsystem.user;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.lahmamsi.librarymanagementsystem.borrowers.Borrower;
import com.lahmamsi.librarymanagementsystem.borrowers.BorrowerRepository;

@DataJpaTest
class BorrowerRepositoryTest {
	private static final String ADDRESS = "560 perry St Auburn, AL";
	private static final String PHONE = "3345591653";
	private static final String LAST_NAME = "Lahmamsi";
	private static final String FIRST_NAME = "Aiman";
	private static final String EMAIL = "aymanhm09@gmail.com";
	private static final LocalDate DATE = LocalDate.of(1999, 10, 27);
	
	private static final String NON_EXIST_PHONE = "3345591659";
	
	Borrower borrower;
	Borrower returnedBorrower;

	@Autowired
	BorrowerRepository underTesr;
	
	
	
	@BeforeEach
	void setUp(){
		borrower = new Borrower(FIRST_NAME, LAST_NAME, EMAIL, PHONE, ADDRESS, DATE);
		returnedBorrower = underTesr.save(borrower);
	}

	@Test
	void itShouldFindByBorrowerEmail() {
				
		// When
		var foundedBorrower = underTesr.findByEmail(EMAIL);
		
		//then
		assertFalse(foundedBorrower.isEmpty());
		assertNotNull(foundedBorrower.get());
		assertEquals(returnedBorrower, foundedBorrower.get());
	}
	
	@Test
	void itShouldFindBorrowerById() {
		
		// When
		var foundedBorrower = underTesr.findById(returnedBorrower.getBorrowerId());
		
		//Then
		assertFalse(foundedBorrower.isEmpty());
		assertNotNull(foundedBorrower.get());
		assertEquals(returnedBorrower, foundedBorrower.get());
	}
	
	@Test 
	void testFindByPhone_ReturnBorrower() {
		var result = underTesr.findByPhone(PHONE);
		
		//Then
		assertFalse(result.isEmpty());
		assertNotNull(result.get());
		assertEquals(returnedBorrower, result.get());
	}
	
	@Test 
	void testFindByPhone_ReturnEmptyOptional() {
		//when
		var result = underTesr.findByPhone(NON_EXIST_PHONE);
		
		//Then
		assertFalse(result.isPresent(), "Expected no Borrower to found");
	}
	
	@Test
	void testFindByPhoneEndingWith_ReturnList() {
		var result = underTesr.findByPhoneEndingWith("1653");
		//Then
		assertFalse(result.isEmpty());
		assertEquals(returnedBorrower, result.get(0));
		
	}
	
	@Test
	void testFindByPhoneEndingWith_ReturnEmptyList() {
		//
		var result = underTesr.findByPhoneEndingWith("1653");
		//Then
		assertFalse(result.isEmpty());
		assertEquals(returnedBorrower, result.get(0));
		
	}
	
	

}
