package br.com.clickbus.places.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import br.com.clickbus.places.dto.PlaceDto;
import br.com.clickbus.places.service.IPlaceService;

@RunWith(SpringRunner.class)
@WebMvcTest(PlaceController.class)
public class PlaceControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private IPlaceService iPlaceService;

	private LocalDateTime localDateTime;
	private static final String CONTENT_LIST = "$.content";
	private static final String ENDPOINT = "/places";
	
	@Before
	public void setup() {
        MockitoAnnotations.initMocks(this);
        
		localDateTime = LocalDateTime.now();
	}

	@Test
	public void shouldReturnOnePlaceById() throws Exception {
		PlaceDto placeDto = createPlaceList().get(0);
		
		when(iPlaceService.listById(1L)).thenReturn(placeDto);
		
		this.mockMvc.perform(get(ENDPOINT + "/1"))
				.andDo(print())
				.andExpect(status().isOk());
		
		Mockito.verify(iPlaceService, Mockito.times(1)).listById(1L);
	}

	@Test
	public void shouldReturnNotFoundOnFindById() throws Exception {
		when(iPlaceService.listById(1L)).thenThrow(EntityNotFoundException.class);
		
		this.mockMvc.perform(get(ENDPOINT + "/1"))
				.andDo(print())
				.andExpect(status().isNotFound());
		
		Mockito.verify(iPlaceService, Mockito.times(1)).listById(1L);
	}

	@Test
	public void shouldReturnAllItems() throws Exception {
		when(iPlaceService.list(Mockito.nullable(String.class), Mockito.any(Pageable.class))).thenReturn(new PageImpl<>(createPlaceList()));

		this.mockMvc.perform(get(ENDPOINT)).andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath(CONTENT_LIST).isArray())
				.andExpect(jsonPath(CONTENT_LIST).isNotEmpty())
				.andExpect(jsonPath(CONTENT_LIST, hasSize(createPlaceList().size())));
	}

	@Test
	public void shouldSavePlace() throws Exception {
		PlaceDto placeDto = createPlaceList().get(0);
		
		when(iPlaceService.savePlace(Mockito.any(PlaceDto.class))).thenReturn(placeDto);
		
		this.mockMvc.perform(post(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(convertObjectToJson(placeDto)))
				.andDo(print())
				.andExpect(status().isCreated());
		
		Mockito.verify(iPlaceService, Mockito.times(1)).savePlace(placeDto);
	}

	@Test
	public void shouldUpdatePlace() throws Exception {
		PlaceDto placeDto = createPlaceList().get(0);
		
		when(iPlaceService.updatePlace(Mockito.any(PlaceDto.class))).thenReturn(placeDto);
		
		this.mockMvc.perform(put(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(convertObjectToJson(placeDto)))
				.andDo(print())
				.andExpect(status().isOk());
		
		Mockito.verify(iPlaceService, Mockito.times(1)).updatePlace(placeDto);
	}

	@Test
	public void shouldDeletePlace() throws Exception {
		doNothing().when(iPlaceService).removePlace(Mockito.any(Long.class));
		
		this.mockMvc.perform(delete(ENDPOINT + "/1"))
				.andDo(print())
				.andExpect(status().isOk());
		
		Mockito.verify(iPlaceService, Mockito.times(1)).removePlace(1L);
	}

	@Test
	public void shouldThrowNotFoundOnUpdate() throws Exception {
		PlaceDto placeDto = createPlaceList().get(0);
		
		when(iPlaceService.updatePlace(placeDto)).thenThrow(EntityNotFoundException.class);
		
		this.mockMvc.perform(put(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(convertObjectToJson(placeDto)))
				.andDo(print())
				.andExpect(status().isNotFound());
		
		Mockito.verify(iPlaceService, Mockito.times(1)).updatePlace(placeDto);
	}

	@Test
	public void shouldThrowNotFoundOnDelete() throws Exception {
		doThrow(EntityNotFoundException.class).when(iPlaceService).removePlace(Mockito.any(Long.class));
		
		this.mockMvc.perform(delete(ENDPOINT + "/1"))
				.andDo(print())
				.andExpect(status().isNotFound());
		
		Mockito.verify(iPlaceService, Mockito.times(1)).removePlace(1L);
	}

	private List<PlaceDto> createPlaceList() {
		List<PlaceDto> listPopulated = new ArrayList<>();

		listPopulated.add(new PlaceDto(1L, "Test 1", "slug 1", "city 1", "state 1", localDateTime, localDateTime));
		listPopulated.add(new PlaceDto(2L, "Test 2", "slug 2", "city 2", "state 2", localDateTime, localDateTime));
		listPopulated.add(new PlaceDto(3L, "Test 3", "slug 3", "city 3", "state 3", localDateTime, localDateTime));
		listPopulated.add(new PlaceDto(4L, "Test 4", "slug 4", "city 4", "state 4", localDateTime, localDateTime));

		return listPopulated;
	}
	
	public String convertObjectToJson(Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper.writeValueAsString(object);
    }
}
