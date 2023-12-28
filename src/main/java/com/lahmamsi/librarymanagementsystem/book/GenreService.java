package com.lahmamsi.librarymanagementsystem.book;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lahmamsi.librarymanagementsystem.exception.GenreNotFoundException;

/**
 * 
 * @author Aiman
 *
 */
@Service
public class GenreService {

	@Autowired
	private GenreRepository genreRepo;
	
	public List<Genre> getAllGenres() {
		var genres = genreRepo.findAll();
		return genres;
	}
	
	public Genre getGenreById(int id) {
		return genreRepo.findById(id).orElseThrow(() -> 
					new GenreNotFoundException("Can't find any Genre with id= "+ id));
	}

	public Genre addGenre(Genre genre) {
		return genreRepo.save(genre);
		
	}

	/**
     * Updates an existing genre by its ID.
     *
     * @param genre The updated genre information
     * @param id    The ID of the genre to update
     * @return The updated genre
     * @throws GenreNotFoundException if no genre is found with the given ID
     */
	public Genre updateGenre(Genre genre, int id) {
		if(!genreRepo.existsById(id))
			throw  new GenreNotFoundException("Can't update Genre, because didn't find any Genre id=" + id);
		genre.setGenreId(id);
		return genreRepo.save(genre);
	}

	/**
     * Deletes a genre by its ID.
     *
     * @param id The ID of the genre to delete
     * @throws GenreNotFoundException if no genre is found with the given ID
     */
	public void deleteGenre(int id) {
		if(!genreRepo.existsById(id))
			throw  new GenreNotFoundException("No Genre with id=" + id);
		genreRepo.deleteById(id);
	}
	
	
	

}
