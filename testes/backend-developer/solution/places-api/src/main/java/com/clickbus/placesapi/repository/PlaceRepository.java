package com.clickbus.placesapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.clickbus.placesapi.model.Place;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long>{

	public Place findBySlug(String slug);
	
}
