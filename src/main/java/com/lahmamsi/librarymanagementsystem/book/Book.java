package com.lahmamsi.librarymanagementsystem.book;

import java.util.List;

import org.hibernate.validator.constraints.ISBN;
import org.hibernate.validator.constraints.ISBN.Type;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * Book entity.
 * @author Aiman
 */
@Entity
@Table(name = "book")
public class Book {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long bookId;
	
	private String title;
	
	private String author;
	
	
	private String description;
	
	private int publicationYear;
	
	@ISBN(type = Type.ANY)
	@Column(unique = true)
	private String isbn;
	
	private String shelfAddress;

	@ManyToMany
	@JoinColumn(name = "genre_id") 
	private List<Genre> genres;
	
	private int copiesNumber;
	@NotNull
	@Min(value = 0)
	@Column(columnDefinition = "int default 0")
	private int signedOutCopies;
	
	@Column(columnDefinition = "boolean default false")
	private boolean isAllSignedOut; 

	public Book() {

	}

	public Book(String title, String author, String description, int publicationYear,
			String isbn, String shelfAddress, int copiesNumber, int signedOutCopies, boolean isAllSignedOut) {
		super();
		this.title = title;
		this.author = author;
		this.description = description;
		this.publicationYear = publicationYear;
		this.isbn = isbn;
		this.shelfAddress = shelfAddress;
		this.copiesNumber = copiesNumber;
		this.signedOutCopies = signedOutCopies;
		this.isAllSignedOut = isAllSignedOut;
	}

	public Book(String title, String author, String description, int publicationYear, String isbn, String shelfAddress,
			List<Genre> genres, int copiesNumber, int signedOutCopies, boolean isAllSignedOut) {
		super();
		this.title = title;
		this.author = author;
		this.description = description;
		this.publicationYear = publicationYear;
		this.isbn = isbn;
		this.shelfAddress = shelfAddress;
		this.genres = genres;
		this.copiesNumber = copiesNumber;
		this.signedOutCopies = signedOutCopies;
		this.isAllSignedOut = isAllSignedOut;
	}

	public long getBookId() {
		return bookId;
	}

	public void setBookId(long bookId) {
		this.bookId = bookId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getPublicationYear() {
		return publicationYear;
	}

	public void setPublicationYear(int publicationYear) {
		this.publicationYear = publicationYear;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getShelfAddress() {
		return shelfAddress;
	}

	public void setShelfAddress(String shelfAddress) {
		this.shelfAddress = shelfAddress;
	}

	public List<Genre> getGenres() {
		return genres;
	}

	public void setGenres(List<Genre> genres) {
		this.genres = genres;
	}

	public int getCopiesNumber() {
		return copiesNumber;
	}

	public void setCopiesNumber(int copiesNumber) {
		this.copiesNumber = copiesNumber;
	}

	public int getSignedOutCopies() {
		return signedOutCopies;
	}

	public void setSignedOutCopies(int signedOutCopies) {
		this.signedOutCopies = signedOutCopies;
	}

	public boolean isAllSignedOut() {
		return isAllSignedOut;
	}

	public void setAllSignedOut(boolean isAllSignedOut) {
		this.isAllSignedOut = isAllSignedOut;
	}

}
