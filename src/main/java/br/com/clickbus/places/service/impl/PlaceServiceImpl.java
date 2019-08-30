package br.com.clickbus.places.service.impl;

import java.time.LocalDateTime;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import br.com.clickbus.places.dto.PlaceDto;
import br.com.clickbus.places.model.Place;
import br.com.clickbus.places.repository.PlaceRepository;
import br.com.clickbus.places.service.IPlaceService;

@Service
public class PlaceServiceImpl implements IPlaceService{

	@Autowired
	private PlaceRepository placeRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public PlaceDto listById(Long id) {
		Optional<Place> optional = placeRepository.findById(id);

		if (optional.isPresent()) {
			return convertToDTO(optional.get());
		} else {
			throw new EntityNotFoundException();
		}
	}

	@Override
	public PlaceDto updatePlace(PlaceDto placeDto) {
		Optional.of(placeDto)
			.map(PlaceDto::getId)
			.filter(id -> id > 0)
			.orElseThrow(() -> new IllegalStateException("Id should be a valid number."));
		
		Optional<Place> optional = placeRepository.findById(placeDto.getId());

		if (optional.isPresent()) {
			Place place = convertToEntity(placeDto);

			place.setCreationDate(optional.get().getCreationDate());
			place.setUpdateDate(LocalDateTime.now());
			
			place = placeRepository.save(place);

			return convertToDTO(place);
		} else {
			throw new EntityNotFoundException();
		}
	}

	@Override
	public Page<PlaceDto> list(String placeName, Pageable pageable) {
		Page<Place> places;
		
		if(StringUtils.isEmpty(placeName)) {
			places = placeRepository.findAll(pageable);
		} else {
			places = placeRepository.findByNameContaining(placeName, pageable);
		}
		
		return places.map(this::convertToDTO);
	}

	@Override
	public void removePlace(Long id) {
		Optional<Place> optional = placeRepository.findById(id);

		if (optional.isPresent()) {
			 placeRepository.delete(optional.get());
		} else {
			throw new EntityNotFoundException();
		}
	}

	@Override
	public PlaceDto savePlace(PlaceDto placeDto) {
		Place place = convertToEntity(placeDto);
		place.setCreationDate(LocalDateTime.now());
		
		placeRepository.save(place);
		
		return convertToDTO(place);
	}

	private PlaceDto convertToDTO(Place place) {
		return modelMapper.map(place, PlaceDto.class);
	}

	private Place convertToEntity(PlaceDto placeDto) {
		return modelMapper.map(placeDto, Place.class);
	}
}
