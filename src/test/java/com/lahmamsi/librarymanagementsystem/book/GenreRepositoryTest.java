package com.lahmamsi.librarymanagementsystem.book;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class GenreRepositoryTest {
	@Autowired
	private GenreRepository underTest;
	
	
	@Test
	void itShouldretrieveGenreById() {
		// Given
		Genre historique = new Genre("historic");
		var genre = underTest.save(historique);
		//when
		Genre retrievedGenre = underTest.findById(genre.getGenreId()).get();

		//then
		assertNotNull(retrievedGenre);
		assertEquals(genre, retrievedGenre);
		
	}

}
