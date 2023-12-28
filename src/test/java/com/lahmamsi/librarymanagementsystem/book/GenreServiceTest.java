package com.lahmamsi.librarymanagementsystem.book;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.lahmamsi.librarymanagementsystem.exception.GenreNotFoundException;


@ExtendWith(MockitoExtension.class)
class GenreServiceTest {
	private static final String RANDOM_GENRE_NAME = "Action";
	private static final String SAVE_OPERATION_FAILD_ERROR_MSG = "Save Operation faild!";
	
	@InjectMocks
	private GenreService underTest;

	@Mock GenreRepository genreRepoMock;
	
	
	
	@Test
	void testGetAllGenres_Basic() {
		//Given
		Genre genre1 = new Genre("Romance");
		genre1.setGenreId(1001);
		Genre genre2 = new Genre("Romance");
		genre2.setGenreId(1002);
		Genre genre3 = new Genre("Romance");
		genre3.setGenreId(1003);
		
		List<Genre> expectedGenres = List.of(genre1, genre2, genre3);
		
		//When
		when(genreRepoMock.findAll()).thenReturn(expectedGenres);
		
		var retrivedgenres = underTest.getAllGenres();
		
		//then
		assertEquals(expectedGenres, retrivedgenres);
		
		
	}
	
	@Test
	void testGetAllGenres_EmptyList() {
		//Given
		List<Genre> expectedGenres = List.of();
		
		//When
		when(genreRepoMock.findAll()).thenReturn(new ArrayList<Genre>());
		
		var retrivedgenres = underTest.getAllGenres();
		
		//then
		assertEquals(expectedGenres, retrivedgenres);
		assertNotNull(retrivedgenres);
		
		
	}

	@Test
	void testGetGenreById_ReturnGenreFromDataBase() {
		//given 
		var genreName = "Action";
		var expectedGenre = new Genre(genreName);
		
		//WHen
		when(genreRepoMock.findById(anyInt())).thenReturn(Optional.of(expectedGenre));
		
		var genre = underTest.getGenreById(0);
		
		// then
		assertEquals(expectedGenre, genre);
		verify(genreRepoMock).findById(anyInt());
	}
	
	@Test
	void testGetGenreById_ThrowGenreNotFountException() {
		//given 
		var genreName = "Action";
		var expectedGenre = new Genre(genreName);
		
		//WHen
		when(genreRepoMock.findById(anyInt())).thenReturn(Optional.empty());
		
		
		
		// then
		GenreNotFoundException exception = assertThrows( GenreNotFoundException.class,() ->{ 
						underTest.getGenreById(0);
					});
		String expexted_Error_MSG = "Can't find any Genre with id= "+ 0;
		assertEquals(expexted_Error_MSG , exception.getMessage());
		
		verify(genreRepoMock).findById(0);
	}

	@Test
	void testAddGenre_Success_ReturnTheGenreAfterSavingItIntoTheDataBase() {
		//given 
		var genreName = "Action";
		int randomNumber = 10001;
		
		var expectedGenre = new Genre(genreName);
		expectedGenre.setGenreId(randomNumber);
		
		//When 
		when(genreRepoMock.save(any(Genre.class))).thenReturn(expectedGenre);
		var result = underTest.addGenre(expectedGenre);
		//then
		assertEquals(expectedGenre, result);
		verify(genreRepoMock).save(expectedGenre);
	}

	@Test
	void testAddGenre_SaveException() {
		//given 
		var genre = new Genre("Science");
				
		//When && then
		when(genreRepoMock.save(genre)).thenThrow(new RuntimeException("Save operation faild"));
		assertThrows(RuntimeException.class	, () -> {
			underTest.addGenre(genre);
		});
	}

	

	@Test
	void testUpdateGenre_UpdatingSuccessfully() {
		//Given
		var id = 0;
		var genre = new Genre(RANDOM_GENRE_NAME);
		
		//When
		when(genreRepoMock.existsById(id)).thenReturn(true);
		when(genreRepoMock.save(any(Genre.class))).thenReturn(genre);
		
		var result = underTest.updateGenre(genre, id);
		//Then
		assertNotNull(result);
		assertEquals(genre, result);
		
		//Verify
		verify(genreRepoMock, times(1)).existsById(id);
		verify(genreRepoMock, times(1)).save(any(Genre.class));
	}
	@Test
	void testUpdateGenre_throwsGenreNotFountException() {
		//Given
		var id = 10002;
		var genre = new Genre(RANDOM_GENRE_NAME);
		
		//When & then
		when(genreRepoMock.existsById(id)).thenReturn(false);
		
		GenreNotFoundException exception = assertThrows(GenreNotFoundException.class, ()->{
			underTest.updateGenre(genre, id);
		});
		
		var expectedErrorMsg = "Can't update Genre, because didn't find any Genre id="+ id;
		
		assertEquals(expectedErrorMsg, exception.getMessage());
		// Verify
		verify(genreRepoMock, times(1)).existsById(id);
		verify(genreRepoMock, never()).save(any(Genre.class));
		
	}
	
	@Test
	void testUpdateGenre_SaveException() {
		//Given
		var id = 10002;
		var genre = new Genre(RANDOM_GENRE_NAME);
		
				
		//When && then
		when(genreRepoMock.existsById(id)).thenReturn(true);

		when(genreRepoMock.save(genre)).thenThrow(new RuntimeException(SAVE_OPERATION_FAILD_ERROR_MSG));
		
		var exception = assertThrows(RuntimeException.class	, () -> {
			underTest.updateGenre(genre, id);
		});
		
		assertEquals(SAVE_OPERATION_FAILD_ERROR_MSG, exception.getMessage());
	    // Verify existsById and save are called with the expected arguments
		verify(genreRepoMock).existsById(id);
		verify(genreRepoMock).save(genre);
		
	}

	@Test
	void testDeleteGenre_DeleteSuccessfully() {
		//Given
		var id = 99;
		//When
		when(genreRepoMock.existsById(id)).thenReturn(true);
		underTest.deleteGenre(id);
		//verify if the  methods existsById and deleteById called once
		verify(genreRepoMock).existsById(id);
		verify(genreRepoMock).deleteById(id);
		
		
	}
	@Test
	void testDeleteGenre_ThrowsGenreNotFountException() {
		//Given
		var id = 99;
		var genreNotFoundExceptionMessage = "No Genre with id=" + id;
		//When
		when(genreRepoMock.existsById(id)).thenReturn(false);
		GenreNotFoundException exception = assertThrows(GenreNotFoundException.class,() ->{
			underTest.deleteGenre(id);
		});
		
		assertEquals(genreNotFoundExceptionMessage, exception.getMessage());
		//verify if the  methods existsById and deleteById called once
		verify(genreRepoMock).existsById(id);
		verify(genreRepoMock, never()).deleteById(id);
		
		
	}
	
	@Test
	void testDeleteGenre_DeletionException() {
		//Given
		var id = 99;
		var errorMessage = "Delete operation fails" ;
		//When
		when(genreRepoMock.existsById(id)).thenReturn(true);
		doThrow(new RuntimeException(errorMessage)).when(genreRepoMock).deleteById(id);
		var exception = assertThrows(RuntimeException.class,() ->{
			underTest.deleteGenre(id);
		});
		
		assertEquals(errorMessage, exception.getMessage());
		//verify if the  methods existsById and deleteById called once
		verify(genreRepoMock).existsById(id);
		verify(genreRepoMock).deleteById(id);
		
		
	}

}
