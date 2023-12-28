package com.lahmamsi.librarymanagementsystem;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lahmamsi.librarymanagementsystem.book.Book;
import com.lahmamsi.librarymanagementsystem.book.BookRepository;
import com.lahmamsi.librarymanagementsystem.book.Genre;
import com.lahmamsi.librarymanagementsystem.book.GenreRepository;
import com.lahmamsi.librarymanagementsystem.borrowers.Borrower;
import com.lahmamsi.librarymanagementsystem.borrowers.BorrowerRepository;
import com.lahmamsi.librarymanagementsystem.transaction.Transaction;
import com.lahmamsi.librarymanagementsystem.transaction.TransactionRepository;

import jakarta.annotation.PostConstruct;

//@Component
public class TestDataSeeder {
	private static final String ADDRESS = "560 perry St Auburn, AL";
	private static final String PHONE = "3345591653";
	private static final String LAST_NAME = "Lahmamsi";
	private static final String FIRST_NAME = "Aiman";
	private static final String EMAIL = "aymanhm09@gmail.com";
	private static final LocalDate DATE = LocalDate.of(1999, 10, 27);
	
	private static final String NON_EXIST_PHONE = "3345591659";

	@Autowired
	BookRepository bookRepository;
	@Autowired
	GenreRepository genreRepository;
	@Autowired
	BorrowerRepository borrowerRepository;
	@Autowired
	TransactionRepository transactionRepository;
	
//	@Autowired
//	public TestDataSeeder(BookRepository bookRepository, GenreRepository genreRepository, BorrowerRepository borrowerRepository, TransactionRepository transactionRepository) {
//		this.bookRepository = bookRepository;
//		this.genreRepository = genreRepository;
//		this.bookRepository = bookRepository;
//		this.transactionRepository = transactionRepository;
//	}
	
	@PostConstruct
	public void seedData() {
		Genre genre1 =  new Genre("Fantasy");
		Genre genre2 =  new Genre("Action");
		Genre genre3 =  new Genre("Drama");
		
		genre1 =genreRepository.save(genre1);
		genre2 =genreRepository.save(genre2);
		genre3 =genreRepository.save(genre3);
		

		var book1 = bookRepository.save(new Book("Book A", "Author 1", "Description 1", 2022, "0-13-235088-2", "Shelf A", List.of(genre1), 5, 2, false));
		var book2 = bookRepository.save(new Book("Book B", "Author 2", "Description 2", 2021, "978-0-13-235088-4", "Shelf B", List.of(genre2), 5, 2, false));
        bookRepository.save(new Book("Book C", "Author 3", "Description 3", 1999, "9780132350884", "Shelf C", List.of(genre3), 5, 2, false));
        
        
		var borrower = borrowerRepository.save( new Borrower(FIRST_NAME, LAST_NAME, EMAIL, PHONE, ADDRESS, DATE));

		var transaction1 = new Transaction();
		transaction1.setReturned(false);
		transaction1.setReturnDate(LocalDate.now().plusWeeks(2));
		transaction1.setLateFee(123.44);
		transaction1.setBorrower(borrower);
		transaction1.setBook(book1);
		transaction1.setBorrowDate(LocalDate.now());
		
		var transaction2 = new Transaction();
		transaction2.setReturned(false);
		transaction2.setReturnDate(LocalDate.now().plusWeeks(1));
		transaction2.setLateFee(15.44);
		transaction2.setBorrower(borrower);
		transaction2.setBook(book2);
		transaction2.setBorrowDate(LocalDate.now());

		
		transactionRepository.save(transaction1);
//		transactionRepository.save(transaction2);
	}
	
	public void cleanUpData() {
		bookRepository.deleteAll();
		genreRepository.deleteAll();
		borrowerRepository.deleteAll();
		transactionRepository.deleteAll();
	}
   
	
}
