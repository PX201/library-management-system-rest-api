package com.lahmamsi.librarymanagementsystem.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.lahmamsi.librarymanagementsystem.borrowers.Borrower;
import com.lahmamsi.librarymanagementsystem.borrowers.BorrowerRepository;
import com.lahmamsi.librarymanagementsystem.borrowers.BorrowerService;
import com.lahmamsi.librarymanagementsystem.exception.BorrowerNotFoundException;

@ExtendWith(MockitoExtension.class)
class BorrowerServiceTest {
	private static final String ADDRESS = "560 perry St Auburn, AL";
	private static final String PHONE = "3345591653";
	private static final String LAST_NAME = "Lahmamsi";
	private static final String FIRST_NAME = "Aiman";
	private static final String EMAIL = "aymanhm09@gmail.com";
	private static final LocalDate DATE = LocalDate.of(1999, 10, 27);
	
	private static final String NON_EXIST_PHONE = "3345591659";
	private static final String LST_FOUR_DIGITS_OF_PHONE = "1653";
	private static final String LST_FOUR_DIGITS_OF_NON_EXIST_PHONE = "1656";
	
	@Mock BorrowerRepository borrowerRepoMock;
	@InjectMocks
	BorrowerService underTest;
	private Borrower borrower;

	@BeforeEach
	void setUp(){

		borrower = new Borrower(FIRST_NAME, LAST_NAME, EMAIL, PHONE, ADDRESS, DATE);
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Disabled
	@Test
	void testGetAllBorrowers() {
		 // TODO
	}
	@Test
	void testGetByLastFourDigits_ItShouldReturnListOfBorrower() {
		
		// Given
		List<Borrower> borrowers = List.of(borrower) ;
		//When
		when(borrowerRepoMock.findByPhoneEndingWith(LST_FOUR_DIGITS_OF_PHONE)).thenReturn( borrowers);
		var result = underTest.getByLastFourDigits(LST_FOUR_DIGITS_OF_PHONE);
		//Then
		assertFalse(result.isEmpty());
		assertEquals(borrowers.get(0), result.get(0));
		
		
	}
	@Test
	void testGetBorrowerByID_ItShouldReturnBorrower() {
		//Given
		long id = 1001L;
		borrower.setBorrowerId(id);
		
		//When
		when(borrowerRepoMock.findByBorrowerId(id)).thenReturn(Optional.of(borrower));
		//Old >> getBorrowerByID(id)
		var result = underTest.getBorrowerByBorrowerId(id);
		//Then
		assertNotNull(result);
		assertEquals(borrower, result);
		
	}
	@Test
	void testGetBorrowerByID_ItShouldReturnExceptionNotFound() {
		//Given
		long id = 1001L;
		String expectedErrorMSG = "Can't find Borrower with id: "+id;
		//When
		doThrow(new BorrowerNotFoundException(expectedErrorMSG)).when(borrowerRepoMock).findByBorrowerId(id);
		
		
		//Then
		BorrowerNotFoundException result = assertThrows(BorrowerNotFoundException.class, () ->{
													underTest.getBorrowerByBorrowerId(id); //Old >> getBorrowerByID(id)
												});
		
		assertEquals(expectedErrorMSG, result.getMessage());
		
	}
	
	@Test
	void testGetBorrowerByEmail_itSHouldReturnBorrower() {
		//When
		when(borrowerRepoMock.findByEmail(EMAIL)).thenReturn(Optional.of(borrower));
		
		var result = underTest.getBorrowerByEmail(EMAIL);
		//Then
		assertNotNull(result);
		assertEquals(borrower, result);
	}
	@Test
	void testGetBorrowerByEmail_itSHouldReturnNotFoundException() {
		String expectedErrorMSG = "Can't find Borrower with email: "+EMAIL;
		//When
		doThrow(new BorrowerNotFoundException(expectedErrorMSG)).when(borrowerRepoMock).findByEmail(EMAIL);
		
		
		//Then
		BorrowerNotFoundException result = assertThrows(BorrowerNotFoundException.class, () ->{
													underTest.getBorrowerByEmail(EMAIL);
												});
		
		assertEquals(expectedErrorMSG, result.getMessage());
		
	}
	@Test
	void testGetBorrowerByPhone_itShouldReturnBorrower() {
		//When
		when(borrowerRepoMock.findByPhone(PHONE)).thenReturn(Optional.of(borrower));
		
		var result = underTest.getBorrowerByPhone(PHONE);
		//Then
		assertNotNull(result);
		assertEquals(borrower, result);
	}
	@Test
	void testGetBorrowerByPhone_ItShouldReturnNotFoundException() {
		String expectedErrorMSG = "Can't find Borrower with phone: "+PHONE;
		//When
		doThrow(new BorrowerNotFoundException(expectedErrorMSG)).when(borrowerRepoMock).findByPhone(PHONE);
		
		
		//Then
		BorrowerNotFoundException result = assertThrows(BorrowerNotFoundException.class, () ->{
													underTest.getBorrowerByPhone(PHONE);
												});
		
		assertEquals(expectedErrorMSG, result.getMessage());
	}
	@Disabled
	
	@Test
	void testAddBorrower() {
		//WHen
		when(borrowerRepoMock.save(borrower)).thenReturn(borrower);
		
		var result = underTest.addBorrower(borrower);
		//Then
		assertNotNull(result);
		assertEquals(borrower, result);
		
	}
	
	@Test
	void testAddBorrower_FaildToSave() {
		//WHen
		doThrow(new RuntimeException()).when(borrowerRepoMock).save(borrower);
		
		RuntimeException result = assertThrows(RuntimeException.class, () -> {
			borrowerRepoMock.save(borrower);
		});
		//Then
		assertEquals(RuntimeException.class, result.getClass());
		
	}
	
	@Test
    void testUpdateBorrower_() {
		//Given
        long id = 1001L;
        
        Borrower updatedBorrower = new Borrower("Updated", "Name", "updated@example.com", "9876543210", "New Address", LocalDate.now());
        //When
        when(borrowerRepoMock.existsById(id)).thenReturn(true);
        underTest.updateBorrower(updatedBorrower, id);
        
        //Then
        assertEquals(id, updatedBorrower.getBorrowerId());
        verify(borrowerRepoMock).save(updatedBorrower);
    }
	
	@Test
	void testUpdateBorrower_ItSouldThrowBorrowerNotFound() {
		 long id = 1002L;
		 
		when(borrowerRepoMock.existsById(id)).thenReturn(false);


       BorrowerNotFoundException result = assertThrows(BorrowerNotFoundException.class, () ->{
    	   										underTest.updateBorrower(borrower, id);
											        });
       
       verify(borrowerRepoMock, never()).save(any());
	}

    @Test
    void testDeleteBorrower_Success() {
        long id = 1002L;
        when(borrowerRepoMock.existsById(id)).thenReturn(true);
        underTest.deleteBorrower(id);

        verify(borrowerRepoMock).deleteById(id);
    }
	
	@Test
	void testDeleteBorrower_ItShouldReturnNotFoundException() {
		 long id = 1002L;
		 
		when(borrowerRepoMock.existsById(id)).thenReturn(false);


        var result = assertThrows(BorrowerNotFoundException.class, () ->{
											            underTest.deleteBorrower(id);
											        });
        
        verify(borrowerRepoMock, never()).deleteById(id);
	}

}
