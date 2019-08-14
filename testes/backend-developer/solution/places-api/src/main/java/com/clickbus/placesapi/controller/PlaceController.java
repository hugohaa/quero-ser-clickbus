package com.clickbus.placesapi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.clickbus.placesapi.dto.PlaceDTO;
import com.clickbus.placesapi.service.PlaceService;

@RestController
@RequestMapping("/api")

public class PlaceController {
	
	@Autowired
	private PlaceService placeService;
	
	@PostMapping("/places")
	public ResponseEntity<PlaceDTO> save(@RequestBody PlaceDTO placeToSave) {
		PlaceDTO savedPlace = placeService.create(placeToSave);
		return ResponseEntity.status(HttpStatus.CREATED).body(savedPlace);
		
	}
	
	@PutMapping("/places/{id}")
	public ResponseEntity<PlaceDTO> update(@PathVariable("id") Long id, @RequestBody PlaceDTO placeToUpdate){
		PlaceDTO updatedPlace = placeService.update(id, placeToUpdate);
		return ResponseEntity.status(HttpStatus.OK).body(updatedPlace);
	}
	
	@GetMapping("/places/{id}")
	public ResponseEntity<PlaceDTO> findById(@PathVariable("id") Long id){
		PlaceDTO foundedPlace = placeService.find(id);
		return ResponseEntity.status(HttpStatus.OK).body(foundedPlace);
	}
	
	@GetMapping("/places")
	public ResponseEntity<List<PlaceDTO>> findByName(@RequestParam(required = false, value = "name") String name){
		List<PlaceDTO> foundedPlaces;
		if(name != null)
			foundedPlaces = placeService.findByName(name);
		else
			foundedPlaces = placeService.findAll();
		return ResponseEntity.status(HttpStatus.OK).body(foundedPlaces);
	}
	
	
}
