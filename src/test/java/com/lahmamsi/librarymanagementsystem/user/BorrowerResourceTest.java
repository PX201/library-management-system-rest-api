package com.lahmamsi.librarymanagementsystem.user;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.lahmamsi.librarymanagementsystem.borrowers.Borrower;
import com.lahmamsi.librarymanagementsystem.borrowers.BorrowerResource;
import com.lahmamsi.librarymanagementsystem.borrowers.BorrowerService;
import com.lahmamsi.librarymanagementsystem.config.JwtService;

@WebMvcTest(BorrowerResource.class)
@WithMockUser(username = "Aiman", roles = {"LIBRARIAN", "USER"})
class BorrowerResourceTest {
	private static final String ADDRESS = "560 perry St Auburn, AL";
	private static final String PHONE = "3345591653";
	private static final String LAST_NAME = "Lahmamsi";
	private static final String FIRST_NAME = "Aima";
	private static final String EMAIL = "aymanhm09@gmail.com";
	private static final LocalDate DATE = LocalDate.of(1999, 10, 27);
	
	
	
	Borrower borrower;
	@Autowired
	MockMvc mockMvc;
	@MockBean BorrowerService borrowerServiceMock;
	@MockBean JwtService jwtService;
	
	private static final String URL  = "/api/v1/borrowers";
	
	String testBorrower = "{\r\n"
			+ "		    \"firstName\": \"TesterBorrower\",\r\n"
			+ "		    \"lastName\": \"tester\",\r\n"
			+ "		    \"email\": \"testerBorrower@Test.com\",\r\n"
			+ "		    \"phone\": \"1234567891\",\r\n"
			+ "		    \"address\": \"test Adress 33 35, San tester GA, 3347\",\r\n"
			+ "		    \"birthDate\": \"1997-10-10\",\r\n"
			+ "		    \"membershipStatus\": \"ACTIVE\"\r\n"
			+ "		}";
	
	@BeforeEach
	void setUp() throws Exception {
//		ObjectMapper objectMapper = new ObjectMapper();
//		objectMapper.registerModule(new JavaTimeModule());
//		mockMvc = MockMvcBuilders.standaloneSetup(new BorrowerResource())
//		                        .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
//		                        .build();
		borrower = new Borrower(FIRST_NAME, LAST_NAME, EMAIL, PHONE, ADDRESS, DATE);

	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testRetrieveAll_Success() throws Exception {
		List<Borrower> list = List.of(new Borrower());
		when(borrowerServiceMock.getAllBorrowers()).thenReturn(list);
		
		RequestBuilder request = MockMvcRequestBuilders.get(URL)
											.contentType(MediaType.APPLICATION_JSON);
		mockMvc.perform(request )
				.andExpect(content()
						.json(toJson(list)))
				.andExpect(status().isOk());
	}

	@Test
	void testSearchByKeyword_Success_UsingFourDigitStr() throws JsonProcessingException, Exception {
		//GIven
		String uri = URL +  "/search";
		//When
		List<Borrower> list = List.of(new Borrower());
		when(borrowerServiceMock.getByLastFourDigits(anyString())).thenReturn(list);
		
		String keyword = "0000";
		RequestBuilder request = MockMvcRequestBuilders.get(uri)
											.param("keyword", keyword)
											.contentType(MediaType.APPLICATION_JSON);
		mockMvc.perform(request )
				.andExpect(content()
						.json(toJson(list)))
				.andExpect(status().isOk());
		verify(borrowerServiceMock, never()).getBorrowerByEmail(anyString());
		verify(borrowerServiceMock, never()).getBorrowerByPhone(anyString());
		verify(borrowerServiceMock ).getByLastFourDigits(anyString());

	}
	
	@Test
	void testSearchByKeyword_Success_UsingPhone() throws JsonProcessingException, Exception {
		//GIven
		String uri = URL + "/search";
		//When
		when(borrowerServiceMock.getBorrowerByPhone(anyString())).thenReturn(new Borrower());
		
		String keyword = "0000000000";
		RequestBuilder request = MockMvcRequestBuilders.get(uri)
											.param("keyword", keyword)
											.contentType(MediaType.APPLICATION_JSON);
		mockMvc.perform(request )
				.andExpect(content()
						.json(toJson(List.of(new Borrower()))))
				.andExpect(status().isOk());
		verify(borrowerServiceMock, never()).getBorrowerByEmail(anyString());
		verify(borrowerServiceMock).getBorrowerByPhone(anyString());
		verify(borrowerServiceMock, never()).getByLastFourDigits(anyString());

	}
	
	@Test
	void testSearchByKeyword_Success_UsingEmail() throws JsonProcessingException, Exception {
		//GIven
		String uri = URL + "/search";
		//When
		List<Borrower> list = List.of(new Borrower());
		when(borrowerServiceMock.getBorrowerByEmail(anyString())).thenReturn(new Borrower());
		
		String keyword = "aymanhm@gmail.com";
		RequestBuilder request = MockMvcRequestBuilders.get(uri)
											.param("keyword", keyword)
											.contentType(MediaType.APPLICATION_JSON);
		mockMvc.perform(request )
				.andExpect(content()
						.json(toJson(list)))
				.andExpect(status().isOk());
		verify(borrowerServiceMock).getBorrowerByEmail(anyString());
		verify(borrowerServiceMock, never()).getBorrowerByPhone(anyString());
		verify(borrowerServiceMock, never()).getByLastFourDigits(anyString());

	}
	
	@Test
	void testSearchByKeyword_Success_PassingNonValidKeyWord() throws JsonProcessingException, Exception {
		//GIven
		String uri = URL + "/search";
		//When
		
		String keyword = "aymanhm9200";
		RequestBuilder request = MockMvcRequestBuilders.get(uri)
											.param("keyword", keyword)
											.contentType(MediaType.APPLICATION_JSON);
		mockMvc.perform(request )
				.andExpect(content()
						.json(toJson(new ArrayList<Borrower>())))
				.andExpect(status().isOk());
		verify(borrowerServiceMock, never()).getBorrowerByEmail(anyString());
		verify(borrowerServiceMock, never()).getBorrowerByPhone(anyString());
		verify(borrowerServiceMock, never()).getByLastFourDigits(anyString());

	}

	@Test
	void testGetBorrower() throws JsonProcessingException, Exception {
		//Given
		long id = 1001L;
		borrower.setBorrowerId(id);

		//When
		//Old >> getBorrowerByID(id)
		when(borrowerServiceMock.getBorrowerByBorrowerId(id)).thenReturn(borrower);
		RequestBuilder request = MockMvcRequestBuilders.get(URL + "/{id}", id)
											.contentType(MediaType.APPLICATION_JSON);
		mockMvc.perform(request )
				.andExpect(content()
						.json(toJson(borrower)))
				.andExpect(status().isOk());
		//Old >> getBorrowerByID(id)
		verify(borrowerServiceMock).getBorrowerByBorrowerId(id);

	}
	
	@Test
	void testAddBorrowerBorrowerHttpServletRequest_Success() throws Exception {
		//GIven
		long id = 1001L;
		var expectedBorrower = new Borrower("TesterBorrower", "tester", "testerBorrower@Test.com", "1234567891", "test Adress 33 35, San tester GA, 3347",  LocalDate.of(1997, 10, 10));

		//When
		when(borrowerServiceMock.addBorrower(any())).thenReturn(expectedBorrower);
		RequestBuilder request = MockMvcRequestBuilders.post(URL)
											.with(SecurityMockMvcRequestPostProcessors.csrf())
											.contentType(MediaType.APPLICATION_JSON)
											.content(testBorrower);
		String expectedHeaderName = "Location";
		String expectedHeadervalue = "http://localhost"+ URL +"/"+ expectedBorrower.getBorrowerId();
		mockMvc.perform(request)
				.andExpect(status().isCreated())
				.andExpect(header().string(expectedHeaderName , expectedHeadervalue ));
		
		verify(borrowerServiceMock).addBorrower(any());
	}
	@Test
	void testAddBorrowerBorrowerHttpServletRequest_ReturnInternelError() throws Exception {
		//Given

		//When
		doThrow(RuntimeException.class).when(borrowerServiceMock).addBorrower(any());
		RequestBuilder request = MockMvcRequestBuilders.post(URL)
											.with(SecurityMockMvcRequestPostProcessors.csrf())
											.contentType(MediaType.APPLICATION_JSON)
											.content(testBorrower);
		mockMvc.perform(request)
				.andExpect(status().isInternalServerError());
		verify(borrowerServiceMock).addBorrower(any());
	}
	@Test
	void testAddBorrowerBorrowerHttpServletRequest_PassingInvalidArgument_ReturnBadREquest() throws Exception {
		//Given
		long id = 1001L;
		var emptuBrorrower = new Borrower();

		//When
		doThrow(RuntimeException.class).when(borrowerServiceMock).addBorrower(any());
		RequestBuilder request = MockMvcRequestBuilders.post(URL)
											.with(SecurityMockMvcRequestPostProcessors.csrf())
											.contentType(MediaType.APPLICATION_JSON)
											.content(toJson(emptuBrorrower));
		
		mockMvc.perform(request)
				.andExpect(status().isBadRequest());
		verify(borrowerServiceMock, never()).addBorrower(any());
	}

	@Test
	void testupdateBorrower_PassingValidArgument_RetunOKRespose() throws Exception {
		
		long id = 1001L;
		
		RequestBuilder request = MockMvcRequestBuilders.put(URL + "/{id}" , id )
														.with(SecurityMockMvcRequestPostProcessors.csrf())
														.contentType(MediaType.APPLICATION_JSON)
														.content(testBorrower);
		MvcResult result = mockMvc.perform(request)
				.andExpect(status().isOk())
				.andReturn();
		verify(borrowerServiceMock).updateBorrower(any(Borrower.class), anyLong());

		
//		ArgumentCaptor<Borrower> borrowerCaptor = ArgumentCaptor.forClass(Borrower.class);
//		ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
//		verify(borrowerServiceMock).updateBorrower(borrowerCaptor.capture(), idCaptor.capture());
//		assertEquals(borrower, borrowerCaptor.getValue());
//		assertEquals(id, idCaptor.getValue());
	}

	@Test
	void testDeleteBorrower_DeleteSuccess_retunrnNocontent() throws Exception {
		long id = 1001L;
		
		RequestBuilder request = MockMvcRequestBuilders.delete(URL + "/{id}" , id )
														.with(SecurityMockMvcRequestPostProcessors.csrf())
														.contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(request)
				.andExpect(status().isNoContent())
				.andReturn();
		verify(borrowerServiceMock).deleteBorrower(id);

	}

	private String toJson(Object obj) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		var javaTimeModule = new JavaTimeModule();
		javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ISO_DATE));
		mapper.registerModule(javaTimeModule);
		return mapper.writeValueAsString(obj);
	}
	
}
