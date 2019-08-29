package br.com.clickbus.places.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.clickbus.places.dto.PlaceDto;
import br.com.clickbus.places.model.Place;
import br.com.clickbus.places.repository.PlaceRepository;
import br.com.clickbus.places.service.impl.PlaceServiceImpl;

@RunWith(SpringRunner.class)
public class PlaceServiceTest {

	@Mock
	private PlaceRepository placeRepository;

	@Mock
	private ModelMapper modelMapper;

	@InjectMocks
	private PlaceServiceImpl placeServiceImpl;

	private PlaceDto placeDto;

	private Place place;

	private LocalDateTime localDateTime;

	@Before
	public void setup() {
		localDateTime = LocalDateTime.now();
		
		placeDto = new PlaceDto(1L, "Test 1", "slug 1", "city 1", "state 1", localDateTime, localDateTime);
		place = this.convertToEntity(placeDto);
	}

	@Test
	public void shouldSavePlace() {
		when(modelMapper.map(Mockito.eq(placeDto), Mockito.any())).thenReturn(place);
		
		when(placeRepository.save(Mockito.eq(place))).thenReturn(place);
		
		when(modelMapper.map(Mockito.eq(place), Mockito.any())).thenReturn(placeDto);
		
		placeDto = placeServiceImpl.savePlace(placeDto);
		
		assertSame(place.getId(), placeDto.getId());
		assertSame(place.getCity(), placeDto.getCity());
		assertSame(place.getSlug(), placeDto.getSlug());
		assertSame(place.getState(), placeDto.getState());
		assertSame(place.getName(), placeDto.getName());
		assertNotNull(place.getCreationDate());
	}

	@Test
	public void shouldUpdatePlace() {
		place.setCreationDate(localDateTime);
		
		when(placeRepository.findById(Mockito.eq(1L))).thenReturn(Optional.of(place));
		
		when(modelMapper.map(Mockito.eq(placeDto), Mockito.any())).thenReturn(place);
		
		when(placeRepository.save(Mockito.eq(place))).thenReturn(place);
		
		when(modelMapper.map(Mockito.eq(place), Mockito.any())).thenReturn(placeDto);
		
		placeDto = placeServiceImpl.updatePlace(placeDto);
		
		assertSame(place.getId(), placeDto.getId());
		assertSame(place.getCity(), placeDto.getCity());
		assertSame(place.getSlug(), placeDto.getSlug());
		assertSame(place.getState(), placeDto.getState());
		assertSame(place.getName(), placeDto.getName());
		assertNotNull(place.getCreationDate());
		assertNotNull(place.getUpdateDate());
	}

	@Test
	public void shouldListById() {
		when(placeRepository.findById(Mockito.eq(1L))).thenReturn(Optional.of(place));
		
		when(modelMapper.map(Mockito.eq(place), Mockito.any())).thenReturn(placeDto);
		
		placeDto = placeServiceImpl.listById(1L);
		
		assertSame(place.getId(), placeDto.getId());
		assertSame(place.getCity(), placeDto.getCity());
		assertSame(place.getSlug(), placeDto.getSlug());
		assertSame(place.getState(), placeDto.getState());
		assertSame(place.getName(), placeDto.getName());
	}
	
	@Test(expected = EntityNotFoundException.class)
	public void shouldThrowEntityNotFoundException() {
		when(placeRepository.findById(Mockito.eq(1L))).thenThrow(EntityNotFoundException.class);
		
		placeDto = placeServiceImpl.updatePlace(placeDto);
	}

	private Place convertToEntity(PlaceDto placeDto) {
		return new ModelMapper().map(placeDto, Place.class);
	}

}
