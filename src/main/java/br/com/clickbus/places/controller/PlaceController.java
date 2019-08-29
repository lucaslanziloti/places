package br.com.clickbus.places.controller;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.clickbus.places.dto.PlaceDto;
import br.com.clickbus.places.service.IPlaceService;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/places")
public class PlaceController {

	@Autowired
	private IPlaceService placeService;

	@GetMapping
	@ApiOperation(value = "Search places by name (Using paginator)")
	public Page<PlaceDto> list(@RequestParam(required = false) String placeName,
			@PageableDefault(sort = "creationDate", direction = Direction.DESC, page = 0, size = 10) Pageable pageable) {

		return placeService.list(placeName, pageable);
	}

	@GetMapping("/{id}")
	@ApiOperation(value = "Search places by id")
	public ResponseEntity<PlaceDto> listById(@PathVariable Long id) {
		try {
			PlaceDto placeDto = placeService.listById(id);

			return ResponseEntity.ok(placeDto);
		} catch (EntityNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@PutMapping
	@Transactional
	@ApiOperation(value = "Update a place")
	public ResponseEntity<PlaceDto> update(@RequestBody @Valid PlaceDto placeDto) {
		try {
			placeDto = placeService.updatePlace(placeDto);

			return ResponseEntity.ok(placeDto);
		} catch (EntityNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@DeleteMapping("/{id}")
	@Transactional
	@ApiOperation(value = "Remove a place")
	public ResponseEntity<?> remover(@PathVariable Long id) {
		try {
			placeService.removePlace(id);

			return ResponseEntity.ok().build();
		} catch (EntityNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@PostMapping
	@Transactional
	@ApiOperation(value = "Save a place")
	public ResponseEntity<PlaceDto> save(@RequestBody PlaceDto placeDto) {
		placeDto = placeService.savePlace(placeDto);

		return ResponseEntity.status(HttpStatus.CREATED).body(placeDto);
	}
}
