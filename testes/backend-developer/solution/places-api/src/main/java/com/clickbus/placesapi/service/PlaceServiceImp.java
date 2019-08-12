package com.clickbus.placesapi.service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.clickbus.placesapi.dto.PlaceDTO;
import com.clickbus.placesapi.exception.PlaceNotFoundExeption;
import com.clickbus.placesapi.exception.SlugAlreadyExistException;
import com.clickbus.placesapi.model.Place;
import com.clickbus.placesapi.repository.PlaceRepository;

@Service
public class PlaceServiceImp implements PlaceService {

	@Autowired
	private PlaceRepository placeRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private Clock clock;
	
	public PlaceDTO create(PlaceDTO place) {
		
		Place placeToSave = placeRepository.findBySlug(place.getSlug());
		
		if(placeToSave != null)
			throw new SlugAlreadyExistException();
		
		placeToSave = modelMapper.map(place, Place.class);
		Place savedPlace = placeRepository.save(placeToSave);
		return modelMapper.map(savedPlace, PlaceDTO.class);
	}
	
	public PlaceDTO update(PlaceDTO place) {
		
		Optional<Place> placeToUpdate = placeRepository.findById(place.getId());
		if(!placeToUpdate.isPresent())
			throw new PlaceNotFoundExeption();
		
		place.setUpdatedAt(LocalDateTime.now(clock));
		Place updatedPlace = modelMapper.map(place, Place.class);
		updatedPlace = placeRepository.save(updatedPlace);
		return modelMapper.map(updatedPlace, PlaceDTO.class);
		
	}
	
}
