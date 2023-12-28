package com.lahmamsi.librarymanagementsystem.book;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
import com.lahmamsi.librarymanagementsystem.exception.GenreNotFoundException;

@WebMvcTest(GenreResources.class)
@WithMockUser(username = "Aiman", roles = {"LIBRARIAN", "USER" })
class GenreResourcesTest {
	@Autowired
	public MockMvc mockMvc;
	@MockBean
	public GenreService genreServiceMock;
	@MockBean 
	private JwtService jwtService;

	private static final String BASE_URL = "http://localhost";
	private static final String URL = "/api/v1/genres";
	private static URI uri = URI.create(BASE_URL+URL);
	
	private static List<Genre> genres;
//	static {
//		genres = new ArrayList<>();
//		genres.add(new Genre("Romance"));
//		genres.add(new Genre("Technology"));
//		genres.add(new Genre("Sience"));
//	}

	@BeforeEach
	public void setup() {
		genres = new ArrayList<>();
		genres.add(new Genre("Romance"));
		genres.add(new Genre("Technology"));
		genres.add(new Genre("Sience"));
	}

	@Test
	void testRetrieveAll_ReturnListOfGenres() throws Exception {
		when(genreServiceMock.getAllGenres()).thenReturn(genres);
		RequestBuilder request = MockMvcRequestBuilders.get("/api/v1/genres").contentType(MediaType.APPLICATION_JSON);
		MvcResult mvcResult = mockMvc.perform(request)
				.andExpect(status().isOk())
				.andExpect(content().json(toJson(genres)))
				.andReturn();
	}

	@Test
	void testRetrieveAll_ReturnEmptyListOfGenres() throws Exception {
		when(genreServiceMock.getAllGenres()).thenReturn(new ArrayList<Genre>());
		RequestBuilder request = MockMvcRequestBuilders.get(URL).contentType(MediaType.APPLICATION_JSON);
		MvcResult mvcResult = mockMvc.perform(request)
				.andExpect(status().isOk())
				.andExpect(content().json("[]"))
				.andReturn();
	}

	@Test
	void testGetGenre_ReturnGenre() throws Exception {
		// Given
		var genre = new Genre("Action");

		// When & then
		when(genreServiceMock.getGenreById(anyInt())).thenReturn(genre);

		RequestBuilder request = MockMvcRequestBuilders.get(URL + "/0").contentType(MediaType.APPLICATION_JSON);
		MvcResult mvcResult = mockMvc.perform(request).andExpect(status().isOk())
				.andExpect(content().json(toJson(genre))).andReturn();
	}

	@Test
	void testGetGenre_ReturnNotFoundException() throws Exception {
		// Given
		String expetedJsonContent = "{message:\"Can't find any Genre with id= 0\",details:\"uri=" + URL + "/0\"}";

		// When and Then
		doThrow(new GenreNotFoundException("Can't find any Genre with id= 0")).when(genreServiceMock).getGenreById(0);
		RequestBuilder request = 
				MockMvcRequestBuilders.get(URL + "/0")
					.with(SecurityMockMvcRequestPostProcessors.csrf())
					.contentType(MediaType.APPLICATION_JSON);
		MvcResult mvcResult = mockMvc
									.perform(request)
									.andExpect(status().isNotFound())
									.andExpect(content().json(expetedJsonContent)).andReturn();
		
		verify(genreServiceMock).getGenreById(anyInt());
	}

	@Test
	void testAddGenre_ResponceCreatedWithLocationHeader() throws Exception {
		// Given
		var genre = new Genre("Action");

		var expectedGenre = genre;
		expectedGenre.setGenreId(0);

		String expectedHeaderName = "Location";
		String expectedHeaderValue = BASE_URL + URL + "/" + expectedGenre.getGenreId();

		// When & then
		when(genreServiceMock.addGenre(any(Genre.class))).thenReturn(expectedGenre);

		RequestBuilder request = MockMvcRequestBuilders.post(URL)
				.with(SecurityMockMvcRequestPostProcessors.csrf())
				.content(toJson(genre))
				.contentType(MediaType.APPLICATION_JSON);
		MvcResult mvcResult = mockMvc.perform(request).andExpect(status().isCreated())
				.andExpect(header().string(expectedHeaderName, expectedHeaderValue)).andReturn();
		// assertEquals( expectedHeaderValue
		// ,mvcResult.getRequest().getHeader(expectedHeaderName));

		verify(genreServiceMock, times(1)).addGenre(any(Genre.class));
	}

	@Test
	void testAddGenre_CatchAnyUnexpectedException_ReturnInternelServerError() throws Exception {
		// Given
		Genre genre = new Genre("Action");

		var expectedGenre = genre;
		// expectedGenre.setGenreId(0);
		// When & then
		when(genreServiceMock.addGenre(any(Genre.class))).thenThrow(new RuntimeException("Errrr..."));

		RequestBuilder request = 
				MockMvcRequestBuilders.post(URL)
										.with(SecurityMockMvcRequestPostProcessors.csrf())
										.content(toJson(genre))
										.contentType(MediaType.APPLICATION_JSON);
		MvcResult mvcResult = mockMvc.perform(request).andExpect(status().isInternalServerError()).andReturn();

	}

	@Test
	void testAddGenre_PassABlankORNullStringInTheGenreName_ReturnBadRequest() throws Exception {
		// Given
		String BLANK_STRING = "   ";
		String NULL_STRING = null;

		Genre genre1 = new Genre(NULL_STRING);
		Genre genre2 = new Genre(BLANK_STRING);

		// expectedGenre.setGenreId(0);
		// When & then
		// when(genreServiceMock.addGenre(any(Genre.class))).thenThrow(new
		// RuntimeException("My Errrr..."));

		RequestBuilder request1 = 
				MockMvcRequestBuilders.post(URL)
										.with(SecurityMockMvcRequestPostProcessors.csrf())
										.content(toJson(genre1))
										.contentType(MediaType.APPLICATION_JSON);
		MvcResult mvcResult1 = mockMvc.perform(request1).andExpect(status().isBadRequest()).andReturn();
		RequestBuilder request2 = 
				MockMvcRequestBuilders.post(URL)
										.with(SecurityMockMvcRequestPostProcessors.csrf())
										.content(toJson(genre2))
										.contentType(MediaType.APPLICATION_JSON);
		MvcResult mvcResult2 = mockMvc.perform(request2).andExpect(status().isBadRequest()).andReturn();
	}

	@Test
	void testUpdateGenre_UpdatingSuccessfully() throws Exception {
		// Given
		var genre = new Genre("Action");
		int id = 0;
		Genre expectedGenre = genre;
		expectedGenre.setGenreId(10);
		// When & then
		when(genreServiceMock.updateGenre(any(Genre.class), anyInt())).thenReturn(expectedGenre);
		RequestBuilder request = 
				MockMvcRequestBuilders.put(URL+"/{id}", id)
										.with(SecurityMockMvcRequestPostProcessors.csrf())
										.contentType(MediaType.APPLICATION_JSON)
										.content(toJson(genre));

		MvcResult result = mockMvc.perform(request).andExpect(status().isOk()).andReturn();

		// verify(genreServiceMock).updateGenre(any(Genre.class),anyInt());
		// Verify the input arguments using ArgumentCaptor
		ArgumentCaptor<Genre> genreCaptor = ArgumentCaptor.forClass(Genre.class);
		ArgumentCaptor<Integer> idCaptor = ArgumentCaptor.forClass(Integer.class);
		verify(genreServiceMock, times(1)).updateGenre(genreCaptor.capture(), idCaptor.capture());

		assertEquals(genre.getGenreName(), genreCaptor.getValue().getGenreName());
		assertEquals(id, idCaptor.getValue().intValue());
	}

	@Test
	void testUpdateGenre_IdNotExsist_shouldReturnGenreNotFoundResponce() throws Exception {
		// Given
		var genre = new Genre("Action");

		Genre expectedGenre = genre;
		expectedGenre.setGenreId(0);
		// When & then
		when(genreServiceMock.updateGenre(any(Genre.class), anyInt())).thenThrow(
				new GenreNotFoundException("Can't update Genre, because didn't find any Genre id=" + anyInt()));

		RequestBuilder request = 
				MockMvcRequestBuilders.put(URL+"/0", anyInt())
										.with(SecurityMockMvcRequestPostProcessors.csrf())
										.content(toJson(genre))
										.contentType(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(request).andExpect(status().isNotFound()).andExpect(content()
				.json("{message:\"Can't update Genre, because didn't find any Genre id=0\",details:\"uri="+URL+"/0\"}"))
				.andReturn();
		verify(genreServiceMock).updateGenre(any(Genre.class), anyInt());
	}

	@Test
	void testdeleteGenre_ByIdInt_ReturnOk() throws Exception {
		// Given
		int id = 0;
		//When
		
		RequestBuilder request = 
				MockMvcRequestBuilders.delete( URL + "/{id}", id)
										.with(SecurityMockMvcRequestPostProcessors.csrf())
										.contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(request).andExpect(status().isNoContent()).andReturn();

		verify(genreServiceMock).deleteGenre(id);

	}

	@Test
	void testdeleteGenre_ByIdInt_ReturnBadRequest() throws Exception {
		// Given
		int nonExistenceId = 9999;

		// when && then
		doThrow(new GenreNotFoundException("No Genre with id=" + nonExistenceId)).when(genreServiceMock)
				.deleteGenre(nonExistenceId);
		RequestBuilder request = 
				MockMvcRequestBuilders.delete(URL + "/{nonExistenceId}", nonExistenceId)
											.with(SecurityMockMvcRequestPostProcessors.csrf())
											.contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(request).andExpect(status().isNotFound()).andExpect(content().json(
				"{message:\"No Genre with id=" + nonExistenceId + "\",details:\"uri="+ URL +"/" + nonExistenceId + "\"}"))
				.andReturn();

		verify(genreServiceMock).deleteGenre(nonExistenceId);

	}

	private String toJson(Object obj) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(obj);

	}
}
