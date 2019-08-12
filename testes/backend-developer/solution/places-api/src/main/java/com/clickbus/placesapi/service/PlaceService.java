package com.clickbus.placesapi.service;

import com.clickbus.placesapi.dto.PlaceDTO;

public interface PlaceService {

	public PlaceDTO create(PlaceDTO place);

	public PlaceDTO update(PlaceDTO place);

}
