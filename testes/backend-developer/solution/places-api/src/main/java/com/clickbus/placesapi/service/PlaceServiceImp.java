package com.clickbus.placesapi.service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
		
		place.setCreatedAt(LocalDateTime.now(clock));
		place.setUpdatedAt(LocalDateTime.now(clock));
		
		placeToSave = modelMapper.map(place, Place.class);
		Place savedPlace = placeRepository.save(placeToSave);
		return modelMapper.map(savedPlace, PlaceDTO.class);
	}
	
	public PlaceDTO update(Long id, PlaceDTO place) {
		
		Optional<Place> placeToUpdate = placeRepository.findById(id);
		if(!placeToUpdate.isPresent())
			throw new PlaceNotFoundExeption();
		
		place.setId(id);
		place.setUpdatedAt(LocalDateTime.now(clock));
		
		Place updatedPlace = modelMapper.map(place, Place.class);
		updatedPlace = placeRepository.save(updatedPlace);
		return modelMapper.map(updatedPlace, PlaceDTO.class);
		
	}
	
	public PlaceDTO find(Long id) {
		
		Optional<Place> placeToFind = placeRepository.findById(id);
		if(!placeToFind.isPresent())
			throw new PlaceNotFoundExeption();
		
		return modelMapper.map(placeToFind.get(), PlaceDTO.class);
	}
	
	public List<PlaceDTO> findAll(){
		List<Place> allPlaces = placeRepository.findAll();
		
		return allPlaces.stream().map(place -> {
			return modelMapper.map(place, PlaceDTO.class);
		}).collect(Collectors.toList());
		
	}
	
	public List<PlaceDTO> findByName(String name){
		List<Place> filteredPlaces = placeRepository.findByNameIgnoreCaseContaining(name);
		
		return filteredPlaces.stream().map(place -> {
			return modelMapper.map(place, PlaceDTO.class);
		}).collect(Collectors.toList());
		
	}
	
	
}
