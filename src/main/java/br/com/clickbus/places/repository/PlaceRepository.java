package br.com.clickbus.places.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.clickbus.places.model.Place;


@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {
	
	List<Place> findByNameContaining(String name);

}
