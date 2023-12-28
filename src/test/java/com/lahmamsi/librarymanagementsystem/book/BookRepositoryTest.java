/**
 * 
 */
package com.lahmamsi.librarymanagementsystem.book;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

/**
 * @author Aiman
 *
 */
//@SpringBootTest
@DataJpaTest
//@ExtendWith(MockitoExtension.class)
class BookRepositoryTest {
	private static List<Genre> genresList;
	private static List<Book> booksList;

	@Autowired
	 BookRepository underTest;
	
	@Autowired GenreRepository genreRepository;
	
	@BeforeEach
	void setUp() {
		List<Genre> genres = new ArrayList<Genre>();
		genres.add(new Genre("action"));
		genres.add(new Genre("act"));
		genres.add(new Genre("act"));
		genresList = genreRepository.saveAll(genres);
		
		List<Book> books = new ArrayList<>();
		
		books.add(new Book("Title123", "Author123" , "Description123456",1999 ,    "9780306406157","Shelf DS077",genresList, 5, 2, false));
		books.add(new Book( "Togachi new & " , "onepiece", "anime book",2003 ,"9780131495050","Shelf DS077",genresList, 5, 2, false));
		books.add(new Book("book ay", "Ay TogachiIIIIIII" , "chi",2023 , "9783161484100","Shelf DS0",genresList, 2, 2, true));
//		books.add(new Book("ayman book", "ayman & " , "new book @",2023 ,"9780672336209","Shelf DS0",genresList, 5, 2, false));

		booksList = underTest.saveAll(books);
	}
	
	@Test
	void itShouldCheckCountByTitleAuthorOrDescriptionMethod() {
		//given
		var existing_Keyword = "To";
		var expectedResult = 2;
		
		//when
		var result =  underTest.countByAuthorContainingIgnoreCaseOrTitleContainingIgnoreCase( existing_Keyword, existing_Keyword);

		//then
		assertEquals(expectedResult, result);
		
	}
	@Test
	void itShouldCheckCountByTitleAuthorOrDescriptionMethod_ReturnZero() {
		
		//given
		var Rndom_Non_Existing_Keyword = "XXXXX";
		var expectedResult = 0;
		
		//when
		var result =  underTest.countByAuthorContainingIgnoreCaseOrTitleContainingIgnoreCase( Rndom_Non_Existing_Keyword, Rndom_Non_Existing_Keyword);

		//then
		assertEquals(expectedResult, result);
		
	}
	
	@Test
	void itShouldSearchForBooksByAuthorOrTitleOrDescription_ReturnTwoBooks() {
		//given
		Pageable first_PAGE_CONTAIN_LESS_THAN_TENN_ENTITIES = PageRequest.of(0,10);
		String keyword = "OnePiece";
		
		var expectedResult = booksList.stream().filter((book)-> book.getAuthor().toLowerCase().contains(keyword.toLowerCase()) || book.getTitle().toLowerCase().contains(keyword.toLowerCase())).toList();
		
		//when
		Page<Book> books  = underTest.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(keyword , keyword, first_PAGE_CONTAIN_LESS_THAN_TENN_ENTITIES);
		
		//then
		assertNotNull(books);
		assertTrue(books.hasContent());
		assertEquals(expectedResult, books.toList());
		
		
	}
	
	@Test
	void itShouldSearchForBookBtISBN_successfully() {
		//Given
		String oneOfExistingIsbn = booksList.get(0).getIsbn();
		//When
		var book = underTest.findByIsbn(oneOfExistingIsbn);
		//Then
		assertTrue(book.isPresent());
		assertEquals(booksList.get(0), book.get());
	}
	
	@Test
	void itShouldSearchForBookByIsbn_returnEmptyOptional() {
		//Given
		String nonExistingIsbn = "0132350882";
		//When
		var book = underTest.findByIsbn(nonExistingIsbn);
		//Then
		assertTrue(book.isEmpty());
	}
	
	@Test
	void testing() {
		var genre = new Genre("action");
		var savedGenre = genreRepository.save(genre);
		var book  = new Book("Title123", "Author123" , "Description123456",1999 ,    "9780132350884","Shelf DS077",List.of(savedGenre), 5, 2, false);
		var savedBook = underTest.save(book);
	}
	
}
