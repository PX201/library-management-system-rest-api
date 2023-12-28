package com.lahmamsi.librarymanagementsystem.book;

import static com.lahmamsi.librarymanagementsystem.utilities.Utils.page;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
@Disabled
@ExtendWith(MockitoExtension.class)
class BookServiceTest {
	
	private static final boolean IS_ALL_SIGNED_OUT = false;
	private static final int SIGNED_OUT_COPIES = 2;
	private static final int COPIES_NUMBER = 5;
	private static final String SHELF_ADRESS = "A1";
	private static final String ISBN = "978-0-13-235088-4";
	private static final int PUBLICATION_YEAR = 1999;
	private static final String DESCRIPTION = "Description123456";
	private static final String AUTHOR = "Author123";
	private static final String TITLE = "Title123";
	private static final String VALID_FIELD_NAME = "title";
	private static final int VALID_PAGE_NUMBER = 0;
	private static final int PAGE_SIZE = 10;
	
	
	@Mock BookService underTest;
	@Mock GenreRepository genreRepoMock;
	@Autowired
	BookRepository bookRepoMock;
	
	private BookRequestDTO bookRequest;
	private ArrayList books;
	private Book book1;
	private ArrayList genres;

	@BeforeEach
	void setUp() throws Exception {
		//Initializing Objects
		var genre1 = new Genre("action");
		var genre2 = new Genre("Adventure");
		var genre3 = new Genre("Romance");
		genre1.setGenreId(101);
		genre1.setGenreId(102);
		genre1.setGenreId(103);
		
		genres = new ArrayList<Genre>();
		genres.addAll(List.of(genre1, genre2, genre3));
		
		book1 = new Book(TITLE, AUTHOR , DESCRIPTION, PUBLICATION_YEAR , ISBN, SHELF_ADRESS, genres, COPIES_NUMBER, SIGNED_OUT_COPIES, IS_ALL_SIGNED_OUT);
		
		bookRequest = new BookRequestDTO();
		// Set Dummy properties for object validation
		bookRequest.setTitle(TITLE);
		bookRequest.setAuthor(AUTHOR);
		bookRequest.setDescription(DESCRIPTION);
		bookRequest.setPublicationYear(PUBLICATION_YEAR);
		bookRequest.setIsbn(ISBN);
		bookRequest.setShelfAddress(SHELF_ADRESS);
		bookRequest.setCopiesNumber(5);
		bookRequest.setSignedOutCopies(2);
		bookRequest.setAllSignedOut(false);
		
		
		var book2 = new Book( "Togachi new & " , "onepiece", "anime book",2003 ,ISBN,"Shelf DS077",genres, 5, 2, false);
		var book3 = new Book("book ay", "Ay TogachiIIIIIII" , "chi",2023 , "9780132350884","Shelf DS0",genres, 2, 2, true);
		var book4 = new Book("ayman book", "ayman & " , "new book @",2023 ,ISBN,"Shelf DS0",genres , 5, 2, false);
		book2.setBookId(1002);
		book3.setBookId(1003);
		book4.setBookId(1004);
		
		books = new ArrayList<>();
		books.addAll(List.of( book2,  book3, book4));
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	
	@Disabled
	@Test
	void testRetrieveBooksBySorting() {
		//Given 
		Page<Book> bookPage = new PageImpl<>(books);
		// need to mock the static method later
		Pageable pageable = page(PAGE_SIZE, VALID_PAGE_NUMBER, VALID_FIELD_NAME);

		//When 
		when(bookRepoMock.findAll(pageable)).thenReturn(bookPage);
		var resut = underTest.retrieveBooksBySorting(VALID_FIELD_NAME, VALID_PAGE_NUMBER);
		//Then
		assertNotNull(resut);
		assertFalse(resut.isEmpty());
		assertEquals(bookPage, resut);
		//Verify
		verify(bookRepoMock).findAll(pageable);
	}

	@Test
	void testSearchBook() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	void testGetBookById() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	void testAddBook() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	void testDeleteBook() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	void testUpdateBook() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	void testGetByIsbn() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	void testBorrowBook() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	void testReturnBook() {
		fail("Not yet implemented"); // TODO
	}

}
