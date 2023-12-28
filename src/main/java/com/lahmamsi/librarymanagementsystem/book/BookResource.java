package com.lahmamsi.librarymanagementsystem.book;

import static com.lahmamsi.librarymanagementsystem.utilities.URIUtils.buildResourceUri;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

/**
 * @author Aiman
 *  Manage Restful endpoint's related to book operations.
 * It handles requests for retrieving, adding, updating and deleting books */
@RestController
@RequestMapping("/api/v1/books")
public class BookResource {

	@Autowired
	private BookService service;

	@GetMapping("/get/{id}")
	public Book getBookById(@PathVariable int id) {
		return service.getBookById(id);
	}
	@GetMapping("/{isbn}")
	public Book getBookByISBN( @Valid @PathVariable String isbn) {
		isbn = isbn.replace("-", "");
		return service.getByIsbn(isbn);
	}
	
	@GetMapping("")
	public List<Book> getAllBooks() {
		return service.retrieveAllBooks();
	}

	
	@GetMapping("/search")
	public List<Book> searchBook(
				@RequestParam(defaultValue = "") String keyword) {
		return service.searchBook(keyword);
	}
	@PostMapping("")
	public ResponseEntity<Book> addBook2(@Valid @RequestBody Book book, HttpServletRequest request) {
//		System.out.println("Recources BOOK>>>" + book);
		var addedBook = service.addBook(book);
		try {
			URI uri = buildResourceUri(request, addedBook.getBookId());
			return ResponseEntity.created(uri).body(addedBook);
		}catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@PostMapping("/add")
	public ResponseEntity<Book> addBook(@Valid @RequestBody BookRequestDTO bookDTO, HttpServletRequest request) {
//		System.out.println("Recources BOOK>>>" + bookDTO);
		var addedBook = service.addBook(bookDTO);
		try {
			URI uri = buildResourceUri(request, addedBook.getBookId());
			return ResponseEntity.created(uri).body(addedBook);
		}catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}

	}
	@PutMapping("/{id}")
	public ResponseEntity<Void> updateBook2(@PathVariable long id,@Valid @RequestBody Book book) {
		service.updateBook(book, id);
		return ResponseEntity.ok().build();
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<Void> updateBook(@PathVariable long id,@Valid @RequestBody BookRequestDTO bookDTO) {
			service.updateBook(bookDTO, id);
			return ResponseEntity.ok().build();
		
	}
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteBook(@PathVariable long id) {
		service.deleteBook(id);
		return ResponseEntity.noContent().build();
	}
	
	
//	@GetMapping("")
//	public Page<Book> getAllBooksWithSorting(@RequestParam(defaultValue = "title") String field, @RequestParam(defaultValue = "0") int pageNumber) {
//		return service.retrieveBooksBySorting(field, pageNumber);
//	}
//	@GetMapping("/search")
//	public Page<Book> searchBook(
//				@RequestParam(defaultValue = "") String keyword,
//				@RequestParam(defaultValue = "title") String field,
//				@RequestParam(defaultValue = "0") int pageNumber) {
//		return service.searchBook(keyword, pageNumber, field);
//	}


}
