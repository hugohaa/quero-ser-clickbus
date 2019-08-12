package com.clickbus.placesapi.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import com.clickbus.placesapi.dto.PlaceDTO;
import com.clickbus.placesapi.exception.PlaceNotFoundExeption;
import com.clickbus.placesapi.exception.SlugAlreadyExistException;
import com.clickbus.placesapi.model.Place;
import com.clickbus.placesapi.repository.PlaceRepository;

@RunWith(SpringRunner.class)
public class PlaceServiceTest {
	
	
	@TestConfiguration
	static class PlaceServiceContextConfiguration {
		
		@Bean
		public PlaceService createService() {
			return new PlaceServiceImp();
		}
	
		@Bean
		public ModelMapper createModelMapper() {
			return new ModelMapper();
		}
		
	}
	
	@Autowired
	private PlaceService placeService;
	
	
	@MockBean
	private PlaceRepository placeRepository;
	
	@MockBean
	private Clock clock;
	
	private Clock fixedClock = Clock.fixed(Instant.now(), ZoneId.systemDefault());
	
	
	@Test
	public void whenCreate_ThenReturnCreatedWithId() {
	
		
		LocalDateTime now = LocalDateTime.now();
		
		Place placeToTest = new Place(null,"DEFAULT NAME","DEFAULT SLUG","DEFAULT CITY","DEFAULT STATE",now,now);
		Place placeToTestWithId = new Place(1L,"DEFAULT NAME","DEFAULT SLUG","DEFAULT CITY","DEFAULT STATE",now,now);
		Mockito.when(placeRepository.save(placeToTest)).thenReturn(placeToTestWithId);
		
		PlaceDTO placeDTOToTest = new PlaceDTO(null,"DEFAULT NAME","DEFAULT SLUG","DEFAULT CITY","DEFAULT STATE",now,now);
		PlaceDTO savedPlace = placeService.create(placeDTOToTest);
		
		assertEquals(1L, savedPlace.getId().longValue());
		assertEquals("DEFAULT NAME", savedPlace.getName());
		assertEquals("DEFAULT SLUG", savedPlace.getSlug());
	}
	
	@Test(expected = SlugAlreadyExistException.class)
	public void whenCreateExistentSlugPlace_ThenThrowSlugAlreadyExistException() {
		
		LocalDateTime now = LocalDateTime.now();
		
		Place placeToTestWithId = new Place(1L,"DEFAULT NAME","DEFAULT SLUG","DEFAULT CITY","DEFAULT STATE",now,now);
		
		Mockito.when(placeRepository.findBySlug("DEFAULT SLUG")).thenReturn(placeToTestWithId);
		
		PlaceDTO placeDTOToTest = new PlaceDTO(null,"DEFAULT NAME","DEFAULT SLUG","DEFAULT CITY","DEFAULT STATE",now,now);
		placeService.create(placeDTOToTest);
		
		fail();
		
	}
	
	@Test
	public void whenUpdatePlace_ThenOk() {
		
		LocalDateTime now = LocalDateTime.now();
		Place placeToUpdateWithId = new Place(1L,"DEFAULT NAME","DEFAULT SLUG","DEFAULT CITY","DEFAULT STATE",now,now);
		
		Mockito.when(placeRepository.findById(1L)).thenReturn(Optional.of(placeToUpdateWithId));
		Mockito.when(clock.instant()).thenReturn(fixedClock.instant());
		Mockito.when(clock.getZone()).thenReturn(fixedClock.getZone());

		LocalDateTime updatedTime = LocalDateTime.now(fixedClock);
		
		PlaceDTO placeDTOWithUpdatedFields = new PlaceDTO(1L,"NEW NAME","NEW SLUG","NEW CITY","NEW STATE",now,now);
		Place placeWithUpdatedFields = new Place(1L,"NEW NAME","NEW SLUG","NEW CITY","NEW STATE",now,updatedTime);

		Mockito.when(placeRepository.save(placeWithUpdatedFields)).thenReturn(placeWithUpdatedFields);
		PlaceDTO updatedPlace = placeService.update(placeDTOWithUpdatedFields);
		
		assertEquals(1, updatedPlace.getId().longValue());
		assertEquals("NEW NAME", updatedPlace.getName());
		assertEquals("NEW SLUG", updatedPlace.getSlug());
		assertEquals("NEW CITY", updatedPlace.getCity());
		assertEquals("NEW STATE", updatedPlace.getState());
		assertEquals(now,updatedPlace.getCreatedAt());
		assertEquals(updatedTime,updatedPlace.getUpdatedAt());
		
		
	}
	
	@Test(expected = PlaceNotFoundExeption.class)
	public void whenUpdatePlaceWithInexistentId_ThenThrowPlaceNotFoundExeption() {
		LocalDateTime now = LocalDateTime.now();
		PlaceDTO placeDTO = new PlaceDTO(1L,"DEFAULT NAME","DEFAULT SLUG","DEFAULT CITY","DEFAULT STATE",now,now);
		
		Mockito.when(placeRepository.findById(1L)).thenReturn(Optional.empty());
		placeService.update(placeDTO);
		
		fail();
		
	}
	
	
}
