package com.lahmamsi.librarymanagementsystem.book;

import java.util.List;

import org.hibernate.validator.constraints.ISBN;
import org.hibernate.validator.constraints.ISBN.Type;
import org.springframework.stereotype.Component;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * @author Aiman 
 * BookRequestDTO serves as a data transfer object specifically designed to convey book details
 * within the system's endpoints. It acts as a safe intermediary structure for transmitting 
 * book information, preventing exposure of sensitive data.
 */
@Component
public class BookRequestDTO {

	@NotBlank
	@Size(min = 2, max = 50)
	private String title;
	@NotBlank
	@Size(min = 2, max = 50)
	private String author;

	@NotBlank
	@Size(min = 3)
	private String description;
	// @Past()
	private int publicationYear;

	@ISBN(type = Type.ANY)
	@NotBlank
	private String isbn;

	@NotBlank
	private String shelfAddress;

	private List<Integer> genreIds;
	@NotNull
	@Min(value = 1)
	private int copiesNumber;

	private int signedOutCopies;
	private boolean isAllSignedOut;

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

	public List<Integer> getGenreIds() {
		return genreIds;
	}

	public void setGenreIds(List<Integer> genreIds) {
		this.genreIds = genreIds;
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

	@Override
	public String toString() {
		return "BookRequestDTO [title=" + title + ", author=" + author + ", description=" + description
				+ ", publicationYear=" + publicationYear + ", ISBN=" + isbn + ", shelfAddress=" + shelfAddress
				+ ", genreIds=" + genreIds + ", copiesNumber=" + copiesNumber + ", signedOutCopies=" + signedOutCopies
				+ ", isAllSignedOut=" + isAllSignedOut + "]";
	}

}
