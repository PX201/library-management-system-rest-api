package com.lahmamsi.librarymanagementsystem.book;

import static com.lahmamsi.librarymanagementsystem.utilities.URIUtils.buildResourceUri;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

/**
 * @author Aiman
 *  Manage Restful endpoint's related to The Genre entity operations.
 * It handles requests for retrieving, adding, updating and deleting genres */
@RestController
@RequestMapping("/api/v1/genres")
public class GenreResources {

	@Autowired
	GenreService service;

	/**
	 * Retrieves All genres
	 * @return A list of all genres
	 */
	@GetMapping("")
	public List<Genre> retrieveAll() {
		return service.getAllGenres();
	}

	/**
	 * Retrieves a specific genre by ID
	 * @param id the ID of the genre to retrieve
	 * @return the genre coresponding to the given ID
	 */
	@GetMapping("/{id}")
	public Genre getGenre(@PathVariable int id) {
		return service.getGenreById(id);
	}

	/**
	 * Adding a new Genre.
	 * @param genre the genre to add
	 * @param request the HTTP request
	 * @return ResponseEntity indicating the status of the operation
	 */
	@PostMapping("")
	public ResponseEntity<Void> addGenre(@Valid @RequestBody Genre genre, HttpServletRequest request) {
		try {
			var addedGenre = service.addGenre(genre);
			URI uri = buildResourceUri(request, addedGenre.getGenreId());
			return ResponseEntity.created(uri).build();
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}

	}

	/**
	 * Update an existing genre
	 * @param genre the updated genre information
	 * @param id the existing genre ID
	 * @return ResponseEntity indicate the status of the operation 
	 */
	@PutMapping("/{id}")
	public ResponseEntity<Void> updateGenre(@RequestBody Genre genre, @PathVariable int id) {
		service.updateGenre(genre, id);
		return ResponseEntity.ok().build();
	}

	/**
	 * Delete a genre by ID.
	 * @param id the genre ID
	 * @return ResponseEntity indicating the status of the operation
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteGenre(@PathVariable int id) {
		service.deleteGenre(id);
		return ResponseEntity.noContent().build();
	}

}
