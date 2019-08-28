package br.com.clickbus.places.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.clickbus.places.dto.PlaceDto;

public interface IPlaceService {

	public PlaceDto savePlace(PlaceDto placeDto);

	public PlaceDto updatePlace(PlaceDto placeDto);

	public void removePlace(Long id);

	public Page<PlaceDto> list(String placeName, Pageable pageable);

	public PlaceDto listById(Long id);
}
