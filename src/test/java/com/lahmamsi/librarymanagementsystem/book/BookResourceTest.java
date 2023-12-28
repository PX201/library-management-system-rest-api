package com.lahmamsi.librarymanagementsystem.book;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lahmamsi.librarymanagementsystem.config.JwtService;
import com.lahmamsi.librarymanagementsystem.exception.BookNotFoundException;
import com.lahmamsi.librarymanagementsystem.exception.InvalidPageNumberException;
import com.lahmamsi.librarymanagementsystem.exception.NotAFieldException;

@WebMvcTest(BookResource.class)
@WithMockUser(username = "Aiman", roles = {"LIBRARIAN", "USER" })
class BookResourceTest {
	private static final boolean IS_ALL_SIGNED_OUT = false;
	private static final int SIGNED_OUT_COPIES = 2;
	private static final int COPIES_NUMBER = 5;
	private static final String SHELF_ADRESS = "A1";
	private static final String ISBN = "978-0-13-235088-4";
	private static final int PUBLICATION_YEAR = 1999;
	private static final String DESCRIPTION = "Description123456";
	private static final String AUTHOR = "Author123";
	private static final String TITLE = "Title123";
	private final static int FIRST_PAGE = 0;
	private final static int NON_EXISTING_PAGENUMBER = 999;
	private final static int PAGE_SIZE = 10;
	private final static String NON_EXISTING_FIELD_NAME = "Non";
	private final static String EXISTING_FIELD_NAME = "title";
	private final static Pageable VALID_PAGE = PageRequest.of(FIRST_PAGE, PAGE_SIZE);
	private final static Pageable PAGE_WIth_NON_EXISTING_PAGENUMBER = PageRequest.of(NON_EXISTING_PAGENUMBER, PAGE_SIZE);
	private final static long VALID_BOOK_ID = 1L;
	private final static long INVALID_BOOK_ID = 9999L;
	private static final String DUMMY_ERROR_MSG = "Dummy Error Message ....";

	
	private static List<Book> books;
	private  static List<Genre> genres;
	private Book book1;
	private BookRequestDTO bookRequest;
	
	private static final String URL = "/api/v1/books";
	
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean 
	private BookService bookServiceMock;
	
	@MockBean JwtService jwtService;

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


//	@Test
	void testGetAllBooksWithSorting_Success() throws JsonProcessingException, Exception {
		//Given Page of books
		Page<Book> bookPage = new PageImpl<>(books, VALID_PAGE, books.size() );
		
		
		//when & then
		when(bookServiceMock.retrieveBooksBySorting(EXISTING_FIELD_NAME, FIRST_PAGE)).thenReturn(bookPage);
		
		RequestBuilder request = MockMvcRequestBuilders.get(URL)
														.contentType(MediaType.APPLICATION_JSON);
		mockMvc.perform(request)
				.andExpect(content()
						.json(toJson(bookPage)))
				.andExpect(status().isOk());
		
		//Verifying the retrieveBooksBySorting method call
		verify(bookServiceMock).retrieveBooksBySorting(EXISTING_FIELD_NAME, FIRST_PAGE);
		
		
	}
//	@Test
	void testGetAllBooksWithSorting_UsingNonExistingField_ReturnBadRequest() throws JsonProcessingException, Exception {
		//Given Page of books
		
		String getReqLink = "/books?field="+NON_EXISTING_FIELD_NAME+"&&pageNumber="+ FIRST_PAGE;
		String errorMessage = NON_EXISTING_FIELD_NAME + " is not a valid field";
		String expectedBodyJsonResponce = "{message:\""+ errorMessage +"\",details:\"uri=/books\"}";
		
		//when & then
		when(bookServiceMock.retrieveBooksBySorting(NON_EXISTING_FIELD_NAME, FIRST_PAGE))
				.thenThrow(
						new NotAFieldException(errorMessage));
		
		RequestBuilder request = MockMvcRequestBuilders.get(getReqLink)
														.contentType(MediaType.APPLICATION_JSON);
		var result = mockMvc.perform(request)
							.andExpect(content()
									.json(expectedBodyJsonResponce))
							.andExpect(status().isBadRequest());
		
		//Verifying the retrieveBooksBySorting method call
		verify(bookServiceMock).retrieveBooksBySorting(NON_EXISTING_FIELD_NAME, FIRST_PAGE);
	}
	
//	@Test
	void testGetAllBooksWithSorting_NonExistingPageNumber_ReturnBadRequest() throws JsonProcessingException, Exception {
		//Given Page of books
		
		String getReqLink = "/books?field="+EXISTING_FIELD_NAME+"&&pageNumber="+ NON_EXISTING_PAGENUMBER;
		
		String errorMessage = NON_EXISTING_PAGENUMBER + " is not a valid page number";
		String expectedBodyJsonResponce = "{message:\""+ errorMessage + "\",details:\"uri=/books\"}";
		
		//when & then
		when(bookServiceMock.retrieveBooksBySorting(EXISTING_FIELD_NAME, NON_EXISTING_PAGENUMBER))
				.thenThrow(
						new InvalidPageNumberException(errorMessage));
		
		RequestBuilder request = MockMvcRequestBuilders.get(getReqLink)
														.contentType(MediaType.APPLICATION_JSON);
		var result = mockMvc.perform(request)
							.andExpect(content()
									.json(expectedBodyJsonResponce))
							.andExpect(status().isBadRequest());
		
		//Verifying the retrieveBooksBySorting method call
		verify(bookServiceMock).retrieveBooksBySorting(EXISTING_FIELD_NAME, NON_EXISTING_PAGENUMBER);
	}


	@Test
	void testSearchBook_Success() throws JsonProcessingException, Exception {
		//Given Page of books
		var keyword = "to";
		var booksContainKeyword  = books.stream()
											.filter((book)-> 
														book.getAuthor().toLowerCase().contains(keyword) || 
														book.getTitle().toLowerCase().contains(keyword))
											.toList();
//		Page<Book> bookPage = new PageImpl<>(booksContainKeyword, VALID_PAGE, booksContainKeyword.size() );
		
		String requestLink = URL + "/search?keyword="+ keyword;

		//when & then
		when(bookServiceMock.searchBook(keyword)).thenReturn(booksContainKeyword);
		
		RequestBuilder request = MockMvcRequestBuilders.get(requestLink)
														.contentType(MediaType.APPLICATION_JSON);
		mockMvc.perform(request)
				.andExpect(content()
						.json(toJson(booksContainKeyword)))
				.andExpect(status().isOk());
		
		//Verifying the retrieveBooksBySorting method call
		verify(bookServiceMock).searchBook(keyword);
	}
//	@Test
	void testSearchBook_UsingNonExistingField_ReturnBadRequest() throws JsonProcessingException, Exception {
		//Given Page of books
		var keyword = "to";
		String getReqLink = "/books/search?keyword="+keyword+"&&field="+NON_EXISTING_FIELD_NAME+"&&pageNumber="+ FIRST_PAGE;
		String errorMessage = NON_EXISTING_FIELD_NAME + " is not a valid field";
		String expectedBodyJsonResponce = "{message:\""+ errorMessage +"\",details:\"uri=/books/search\"}";
		
		//when & then
		when(bookServiceMock.searchBook(keyword, FIRST_PAGE, NON_EXISTING_FIELD_NAME))
				.thenThrow(
						new NotAFieldException(errorMessage));
		
		RequestBuilder request = MockMvcRequestBuilders.get(getReqLink)
														.contentType(MediaType.APPLICATION_JSON);
		var result = mockMvc.perform(request)
							.andExpect(content()
									.json(expectedBodyJsonResponce))
							.andExpect(status().isBadRequest());
		
		//Verifying the retrieveBooksBySorting method call
		verify(bookServiceMock).searchBook(keyword, FIRST_PAGE, NON_EXISTING_FIELD_NAME);
	}
	
//	@Test
	void testSearchBook_NonExistingPageNumber_ReturnBadRequest() throws JsonProcessingException, Exception {
		//Given Page of books
		
		String keyword = "to";
		String getReqLink = "/books/search?keyword="+keyword +"&&field="+EXISTING_FIELD_NAME+"&&pageNumber="+ NON_EXISTING_PAGENUMBER;
		
		String errorMessage = NON_EXISTING_PAGENUMBER + " is not a valid page number";
		String expectedBodyJsonResponce = "{message:\""+ errorMessage + "\",details:\"uri=/books/search\"}";
		
		//when & then
		when(bookServiceMock.searchBook(keyword, NON_EXISTING_PAGENUMBER,  EXISTING_FIELD_NAME))
				.thenThrow(
						new InvalidPageNumberException(errorMessage));
		
		RequestBuilder request = MockMvcRequestBuilders.get(getReqLink)
														.contentType(MediaType.APPLICATION_JSON);
		var result = mockMvc.perform(request)
							.andExpect(content()
									.json(expectedBodyJsonResponce))
							.andExpect(status().isBadRequest());
		
		//Verifying the retrieveBooksBySorting method call
		verify(bookServiceMock).searchBook(keyword, NON_EXISTING_PAGENUMBER,  EXISTING_FIELD_NAME);
	}

	@Test
	void testDeleteBook_Success() throws Exception {
		
		
		//when & then
		
		RequestBuilder request = 
				MockMvcRequestBuilders.delete(URL+"/{id}", VALID_BOOK_ID)
										.with(SecurityMockMvcRequestPostProcessors.csrf())
										.contentType(MediaType.APPLICATION_JSON);
		var result = mockMvc.perform(request)
							.andExpect(status().isNoContent());
		// Verify
		verify(bookServiceMock).deleteBook(VALID_BOOK_ID);
	}
	@Test
	void testDeleteBook_InvalidId_ReturnNotFound() throws Exception {
		
		
		String ReqLink = URL + "/{invalidId}";
		String errorMsg = "No book found with id=" + INVALID_BOOK_ID;
		String expectedJsonBodyResponce = "{message:\""+errorMsg+"\"}";
		//when & then
		doThrow(new BookNotFoundException(errorMsg)).when(bookServiceMock).deleteBook(INVALID_BOOK_ID);
		
		RequestBuilder request = 
				MockMvcRequestBuilders.delete(ReqLink, INVALID_BOOK_ID)
										.with(SecurityMockMvcRequestPostProcessors.csrf())
										.contentType(MediaType.APPLICATION_JSON);
		var result = mockMvc.perform(request)
							.andExpect(status().isNotFound())
							.andExpect(content().json(expectedJsonBodyResponce));
		// Verify
		verify(bookServiceMock).deleteBook(INVALID_BOOK_ID);
	}

	@Test
	void testAddBook_Success() throws Exception {
		// Given 
		book1.setBookId(VALID_BOOK_ID);

		// When & Then
		when(bookServiceMock.addBook(any(BookRequestDTO.class))).thenReturn(book1);
		
		RequestBuilder request =  
				MockMvcRequestBuilders.post(URL+"/add")
										.with(SecurityMockMvcRequestPostProcessors.csrf())
										.contentType(MediaType.APPLICATION_JSON)
										.content(toJson(bookRequest));
		String expectedHeaderName = "Location";
		String expectedHeaderValue = "http://localhost"+ URL +"/add/"+VALID_BOOK_ID;
		MvcResult result = mockMvc.perform(request)
								.andExpect(status().isCreated())
								.andExpect(header().string(expectedHeaderName ,expectedHeaderValue ))
								.andReturn();
		//Verify
		verify(bookServiceMock).addBook(any(BookRequestDTO.class));
	}
	
	@Test
	void testAddBook_ArgumentValidationException_ReturnBadRequest() throws Exception {
		// Given 
		BookRequestDTO invalidBookRequest = new BookRequestDTO();

		// When & Then
		when(bookServiceMock.addBook(any(BookRequestDTO.class))).thenThrow(new RuntimeException(DUMMY_ERROR_MSG));
		
		RequestBuilder request =  MockMvcRequestBuilders.post(URL + "/add")
				.with(SecurityMockMvcRequestPostProcessors.csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(toJson(invalidBookRequest));
		
		MvcResult result = mockMvc.perform(request)
								.andExpect(status().isBadRequest())
								.andReturn();
		//Verify
		verify(bookServiceMock, never()).addBook(any(BookRequestDTO.class));
	}

	@Test
	void testUpdateBook_Success() throws Exception , JsonProcessingException {
		// Given 
		book1.setBookId(VALID_BOOK_ID);
		
		// When & Then
		RequestBuilder request =  MockMvcRequestBuilders.put(URL + "/update/{id}", VALID_BOOK_ID)
				.with(SecurityMockMvcRequestPostProcessors.csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(toJson(bookRequest));
		
		MvcResult result = mockMvc.perform(request)
								.andExpect(status().isOk())
								.andReturn();
		//Verify
		verify(bookServiceMock).updateBook(any(BookRequestDTO.class), eq(VALID_BOOK_ID));
	}
	
	@Test
	void testGetBookById_Success() throws Exception {
		// Given
		book1.setBookId(VALID_BOOK_ID);
		
		//  When  && Then
		when(bookServiceMock.getBookById(VALID_BOOK_ID)).thenReturn(book1);
		RequestBuilder request =  
				MockMvcRequestBuilders.get(URL + "/get/{id}" , VALID_BOOK_ID)
									.contentType(MediaType.APPLICATION_JSON);
		
		MvcResult result = mockMvc.perform(request)
								.andExpect(status().isOk())
//								.andExpect(content().json(toJson(book1)))
								.andReturn();
		//Verify
		verify(bookServiceMock).getBookById(VALID_BOOK_ID);
	}
	@Test
	void testGetBookById_ReturnNotFoundResponce() throws Exception {
		// Given		
		book1.setBookId(INVALID_BOOK_ID);
		
		String expectedJsonContent = "{message:\""+DUMMY_ERROR_MSG+"\"}";
		
		//  When  && Then
		doThrow(new BookNotFoundException(DUMMY_ERROR_MSG)).when(bookServiceMock).getBookById(INVALID_BOOK_ID);
		RequestBuilder request =  MockMvcRequestBuilders.get(URL + "/get/{id}" , INVALID_BOOK_ID)
				.contentType(MediaType.APPLICATION_JSON);
		
		MvcResult result = mockMvc.perform(request)
								.andExpect(status().isNotFound())
								.andExpect(content().json(expectedJsonContent))
								.andReturn();
		//Verify
		verify(bookServiceMock).getBookById(INVALID_BOOK_ID);
	}
	@Test
	void testGetBookByIsbn_Success() throws JsonProcessingException, Exception {
		// Given
		book1.setBookId(VALID_BOOK_ID);
		
		//  When  && Then
		when(bookServiceMock.getByIsbn(ISBN.replace("-", ""))).thenReturn(book1);
		RequestBuilder request =  
				MockMvcRequestBuilders.get(URL + "/" + ISBN)
										.contentType(MediaType.APPLICATION_JSON);
		
		MvcResult result = mockMvc.perform(request)
								.andExpect(status().isOk())
								.andExpect(content().json(toJson(book1)))
								.andReturn();
		//Verify
		verify(bookServiceMock).getByIsbn(ISBN.replace("-", ""));
	}
	@Test
	void testGetBookByIsbn_ReturnNotFoundResponce() throws Exception {
		// Given
		String expectedJsonContent = "{message:\""+DUMMY_ERROR_MSG+"\"}";
		
		//  When  && Then
		doThrow(new BookNotFoundException(DUMMY_ERROR_MSG)).when(bookServiceMock).getByIsbn(ISBN.replace("-", ""));
		RequestBuilder request =  
				MockMvcRequestBuilders.get(URL + "/{isbn}" , ISBN)
										.contentType(MediaType.APPLICATION_JSON);
		
		MvcResult result = mockMvc.perform(request)
								.andExpect(status().isNotFound())
								.andExpect(content().json(expectedJsonContent))
								.andReturn();
		//Verify
		verify(bookServiceMock).getByIsbn(ISBN.replace("-", ""));
	}
	private String toJson(Object obj) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(obj);
	}
}
