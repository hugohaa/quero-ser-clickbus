package com.clickbus.placesapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
	
	
	
}
