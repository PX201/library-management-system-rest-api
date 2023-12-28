package com.lahmamsi.librarymanagementsystem.book;

import static com.lahmamsi.librarymanagementsystem.utilities.ParamValidationUtils.checkFieldValidity;
import static com.lahmamsi.librarymanagementsystem.utilities.ParamValidationUtils.checkPageNumberValidity;
import static com.lahmamsi.librarymanagementsystem.utilities.Utils.page;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lahmamsi.librarymanagementsystem.exception.BookNotFoundException;
import com.lahmamsi.librarymanagementsystem.exception.GenreNotFoundException;
import com.lahmamsi.librarymanagementsystem.exception.LibraryManagmentException;

/**
 * @author Aiman
 * BookService manages book-related operations and interactions with repositories.
 */
@Service
public class BookService {
	
	private static final int PAGE_SIZE = 50;

	private final BookRepository bookRepo;
	private final GenreRepository genreRepo;

	public BookService(BookRepository bookRepo, GenreRepository genreRepo) {
		this.bookRepo = bookRepo;
		this.genreRepo = genreRepo;
	}


	/**
     * Retrieves a book by its ID.
     *
     * @param id The ID of the book
     * @return The book corresponding to the given ID
     * @throws BookNotFoundException If no book is found with the given ID
     */
	public Book getBookById(long id) {
		return bookRepo.findById(id).orElseThrow(() -> new BookNotFoundException("Can't find Book with id: " + id));
	}
	/**
     * Retrieves a book by its ISBN.
     *
     * @param id The ID of the book
     * @return The book corresponding to the given ISBN
     * @throws BookNotFoundException If no book is found with the given ISBN
     */
	public Book getByIsbn(String isbn) {

		return bookRepo.findByIsbn(isbn)
				.orElseThrow(() -> new BookNotFoundException("No book found with this isbn = " + isbn));
	}

	 /**
     * Adds a new book based on the provided BookRequestDTO.
     *
     * @param bookDTO The BookRequestDTO containing book details
     * @return The added book
     * @throws GenreNotFoundException If one or more genres are not found
     */
	@Transactional
	public Book addBook(BookRequestDTO bookDTO) {
		List<Genre> genres = genreRepo.findAllById(bookDTO.getGenreIds());
		if (genres.isEmpty())
			throw new GenreNotFoundException(
					"One Or more genners not found with IDs: " + bookDTO.getGenreIds().toString());

		var book = new Book();
		book.setAuthor(bookDTO.getAuthor());
		book.setTitle(bookDTO.getTitle());
		book.setDescription(bookDTO.getDescription());
		book.setGenres(genres);
		book.setIsbn(bookDTO.getIsbn().replace("-", ""));//
		book.setCopiesNumber(bookDTO.getCopiesNumber());
		book.setPublicationYear(bookDTO.getPublicationYear());
		book.setShelfAddress(bookDTO.getShelfAddress());

		return bookRepo.save(book);
	}
	
	 /**
     * Deletes a book by its ID.
     *
     * @param id The ID of the book to delete
     * @throws BookNotFoundException If no book is found with the given ID
     */
	@Transactional
	public void deleteBook(long id) {
		if (!bookRepo.existsById(id))
			throw new BookNotFoundException("Book with id: " + id + " doesn't exist");
		bookRepo.deleteById(id);
	}
	
	 /**
     * Updating book based on provided BookRequestDTO and its ID.
     *
     * @param bookDTO The BookRequestDTO contains updated book details
     * @param id The ID of the book to delete
     * @throws BookNotFoundException If no book is found with he given ID
     * @throws GenreNotFoundException If one or more genres is found with the given Genre IDs
     */
	@Transactional
	public void updateBook(BookRequestDTO bookDTO, long id) {
		if (!bookRepo.existsById(id))
			throw new BookNotFoundException("Book doesn't exist, id: " + id);

		List<Genre> genres = genreRepo.findAllById(bookDTO.getGenreIds());
		if (genres.isEmpty())
			throw new GenreNotFoundException(
					"One Or more genners not found with IDs: " + bookDTO.getGenreIds().toString());

		var book = new Book(bookDTO.getTitle(), bookDTO.getAuthor(), bookDTO.getDescription(),
				bookDTO.getPublicationYear(), bookDTO.getIsbn(), bookDTO.getShelfAddress(), genres,
				bookDTO.getCopiesNumber(), bookDTO.getSignedOutCopies(), Boolean.FALSE);
		book.setBookId(id);
		bookRepo.save(book);
	}



	/**
     * Borrows a book based on the provided book ID.
     *
     * @param bookId The ID of the book to borrow
     * @throws BookNotFoundException      If no book is found with the given ID
     * @throws LibraryManagmentException If all copies of the book are currently borrowed
     */
	@Transactional
	public void borrowBook(long bookId) {
		Book book = bookRepo.findById(bookId)
				.orElseThrow(() -> new BookNotFoundException("Can't find Book with id: " + bookId));
		if (!book.isAllSignedOut()) {
			book.setSignedOutCopies(book.getSignedOutCopies() + 1);
			book.setAllSignedOut(book.getCopiesNumber() == book.getSignedOutCopies());
			bookRepo.save(book);
		} else {
			throw new LibraryManagmentException("All copies of this book are currently borrowed.");
		}
	}
	
	
	 /**
     * Returns a borrowed book based on the provided book ID.
     *
     * @param bookId The ID of the book to return
     * @throws BookNotFoundException     If no book is found with the given ID
     * @throws LibraryManagmentException If the book was not borrowed
     */
	@Transactional
	public void returnBook(long bookId) {
		Book book = bookRepo.findById(bookId)
				.orElseThrow(() -> new BookNotFoundException("Can't find Book with id: " + bookId));

		if (book.getSignedOutCopies() > 0) {
			book.setSignedOutCopies(book.getSignedOutCopies() - 1);
			book.setAllSignedOut(book.getCopiesNumber() == book.getSignedOutCopies());
			bookRepo.save(book);
		} else {
			throw new LibraryManagmentException("This book was not borrowed.");
		}
	}


	/**
	 * Retrieves all books available
	 * @return a list of books
	 */
	public List<Book> retrieveAllBooks() {
		return bookRepo.findAll();
	}
	
	/**
	 * Searches for books based on a keyword in title, author, or ISBN.
	 * 
	 * @param keyword the search keyword
	 * @return list of books matching the search criteria
	 */
	public List<Book> searchBook(String keyword) {
		return bookRepo.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCaseOrIsbn(keyword, keyword, keyword);

	}

    /**
     * Adds a book to the Database.
     *
     * @param book The book to add
     * @return The added book
     */
	@Transactional
	public Book addBook(Book book) {
		return bookRepo.save(book);
	}

	/**
     * Updates a book in the Database.
     *
     * @param book The updated book information
     * @param id   The ID of the book to update
     * @throws BookNotFoundException If no book is found with the given ID
     */
	@Transactional
	public void updateBook(Book book, long id) {
		if (!bookRepo.existsById(id))
			throw new BookNotFoundException("Book doesn't exist, id: " + id);
		bookRepo.save(book);
	}
	
	
	
	
//	@PostConstruct
//	private void fillBooks() {
//		List<Genre> genres = IntStream
//				.range(0, 10)
//				.mapToObj(i -> new Genre("genre" + i)).toList();
//		genreRepo.saveAll(genres);
//		List<Genre> bookGenres = IntStream.range(1, 8)
//				.mapToObj(i -> genreRepo.getById(i).get()).toList();
//		List<Book> books = new ArrayList<>();
////		= IntStream.range(0, 300)
////								.mapToObj(i -> 
////								new Book("book00" + i, "Aiman & " + i, "new book @" + i,2023 - i,"ISBN "+i,"Shelf DS0" + i,bookGenres, 5, 2, false))
////								.toList();
//		books.add(new Book("OnePiece", "Togachi" , "anime book",1999 ,"ISBN X34","Shelf DS077",bookGenres, 5, 2, false));
//		books.add(new Book("onepiece", "Togachi new & " , "anime book",2003 ,"ISBN 344","Shelf DS077",bookGenres, 5, 2, false));
//		books.add(new Book("book ay", "Ay " , "chi",2023 ,"ISBN ","Shelf DS0",bookGenres, 2, 2, true));
//		books.add(new Book("ayman book", "ayman & " , "new book @",2023 ,"ISBN ","Shelf DS0",bookGenres, 5, 2, false));
//
//		bookRepo.saveAll(books);
//	}

	
	/***Down here there's Methods Using Pagination Not Used Anymore***/

    // Method to retrieve books sorted by field and paginated
	public Page<Book> retrieveBooksBySorting(String field, int pageNumber) {
		//Validate the field and the page number
		checkFieldValidity(BookFields.class, field);
		checkPageNumberValidity(PAGE_SIZE, pageNumber, bookRepo.count());
		
		//Fetshing books using pagination
		Pageable pageable = page(PAGE_SIZE, pageNumber, field);
		var books = bookRepo.findAll(pageable);
		if (books.isEmpty())
			throw new LibraryManagmentException("Empty Library");
		return books;
	}
	
	//Used to search books based on the author or title depending on what the keyword refers to
	public Page<Book> searchBook(String keyword, int pageNumber, String field) {
		// Validating field and page number
		checkFieldValidity(BookFields.class, field);
		checkPageNumberValidity(PAGE_SIZE, pageNumber,
				bookRepo.countByAuthorContainingIgnoreCaseOrTitleContainingIgnoreCase(keyword, keyword));

		// Pageable pageable = PageRequest.of(pageNumber, PAGE_SIZE,
		// Sort.by(Direction.ASC, field)); -> Utils.page(pageSize, pageNumber, field)
        
		// Fetching books by title, author, or ISBN using pagination
		var books = bookRepo.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(keyword, keyword,
				page(PAGE_SIZE, pageNumber, field));
		if (books.isEmpty())
			throw new LibraryManagmentException("Can't find any book related to search keywords");
		return books;

	}

//	public Page<Book> retrieveAllBooks(int pageNumber) {
//		checkPageNumberValidity(PAGE_SIZE, pageNumber, bookRepo.count());
//		return  bookRepo.findAll(PageRequest.of(pageNumber, PAGE_SIZE));
//	}
	
    // Enum defining field names for validation
	private enum BookFields {
		bookId, title, author, description, publicationYear, ISBN, shelfAddress, genres, copiesNumber, signedOutCopies,
		isAllSignedOut;
	}

}
