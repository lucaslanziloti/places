package br.com.clickbus.places.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.clickbus.places.model.Place;


@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {
	
	Page<Place> findByNameContaining(String name, Pageable pageable);

}
