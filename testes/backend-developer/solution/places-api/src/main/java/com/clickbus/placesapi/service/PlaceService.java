package com.clickbus.placesapi.service;

import java.util.List;

import com.clickbus.placesapi.dto.PlaceDTO;

public interface PlaceService {

	public PlaceDTO create(PlaceDTO place);

	public PlaceDTO update(Long id, PlaceDTO place);

	public PlaceDTO find(Long id);

	public List<PlaceDTO> findAll();

	public List<PlaceDTO> findByName(String name);

}
