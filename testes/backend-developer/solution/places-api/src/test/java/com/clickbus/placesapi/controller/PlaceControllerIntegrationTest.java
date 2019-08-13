package com.clickbus.placesapi.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Clock;
import java.time.LocalDateTime;

import org.hamcrest.text.IsEmptyString;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.clickbus.placesapi.PlacesApiApplication;
import com.clickbus.placesapi.dto.PlaceDTO;
import com.clickbus.placesapi.model.Place;
import com.clickbus.placesapi.repository.PlaceRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PlacesApiApplication.class)
@AutoConfigureMockMvc
public class PlaceControllerIntegrationTest {

	@Autowired
    private MockMvc mvc;
	
	@Autowired
	private PlaceRepository placeRepository;
	
	private LocalDateTime now;
	
	@Before 
	public void setUp() { 
		this.now = LocalDateTime.now(); 
	}
	 
	
	@Test
	public void whenSavePlace_ThenReturnPlaceAnd201() throws Exception {
		
		long countBeforeInsert = placeRepository.count();
		PlaceDTO placeDTOToTest = new PlaceDTO(null,"DEFAULT NAME","DEFAULT SLUG","DEFAULT CITY","DEFAULT STATE",null,null);
		
		mvc.perform(
					post("/api/places")
					.contentType(MediaType.APPLICATION_JSON)
					.content(asJsonString(placeDTOToTest)))
		.andExpect(status().isCreated())
		.andExpect(jsonPath("$.id", notNullValue()))
		.andExpect(jsonPath("$.name", is("DEFAULT NAME")))
		.andExpect(jsonPath("$.slug", is("DEFAULT SLUG")))
		.andExpect(jsonPath("$.city", is("DEFAULT CITY")))
		.andExpect(jsonPath("$.state", is("DEFAULT STATE")));
		
		long countAfterInsert = placeRepository.count();
		
		assertEquals(countAfterInsert,countBeforeInsert + 1);
		
	}
	
	
	@Test
	public void whenSaveAlreadyExistentSlug_Then409() throws Exception {
		Place equalsPlace = new Place(null,"DEFAULT NAME","DEFAULT SLUG","DEFAULT CITY","DEFAULT STATE",now,now);
		placeRepository.save(equalsPlace);
		long countBeforeInsert = placeRepository.count();

		
		PlaceDTO placeDTOToTest = new PlaceDTO(null,"DEFAULT NAME","DEFAULT SLUG","DEFAULT CITY","DEFAULT STATE",null,null);
		mvc.perform(
					post("/api/places")
					.contentType(MediaType.APPLICATION_JSON)
					.content(asJsonString(placeDTOToTest)))
		.andExpect(status().is(409));
		
		long countAfterInsert = placeRepository.count();
		
		assertEquals(countAfterInsert,countBeforeInsert);
	}
	
	
	@Test
	public void whenUpdatePlace_ThenReturnPlaceAnd200() throws Exception {
		Place startPlace = new Place(null,"DEFAULT NAME","DEFAULT SLUG","DEFAULT CITY","DEFAULT STATE",now,now);
		startPlace = placeRepository.save(startPlace);
		
		
		long countBeforeUpdate = placeRepository.count();
		PlaceDTO placeDTOToTest = new PlaceDTO(null,"ANOTHER NAME","ANOTHER SLUG","ANOTHER CITY","ANOTHER STATE",null,null);
		
		mvc.perform(
					put("/api/places/{id}",startPlace.getId())
					.contentType(MediaType.APPLICATION_JSON)
					.content(asJsonString(placeDTOToTest)))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.id", is(startPlace.getId().intValue())))
		.andExpect(jsonPath("$.name", is("ANOTHER NAME")))
		.andExpect(jsonPath("$.slug", is("ANOTHER SLUG")))
		.andExpect(jsonPath("$.city", is("ANOTHER CITY")))
		.andExpect(jsonPath("$.state", is("ANOTHER STATE")));
		
				
		long countAfterInsert = placeRepository.count();
		
		assertEquals(countAfterInsert,countBeforeUpdate);
		
	}
	
	
	@Test
	public void whenUpdateInexistentPlace_Then400() throws Exception {
		Place startPlace = new Place(null,"DEFAULT NAME","DEFAULT SLUG","DEFAULT CITY","DEFAULT STATE",now,now);
		startPlace = placeRepository.save(startPlace);
		
		
		long countBeforeUpdate = placeRepository.count();
		PlaceDTO placeDTOToTest = new PlaceDTO(null,"ANOTHER NAME","ANOTHER SLUG","ANOTHER CITY","ANOTHER STATE",null,null);
		
		mvc.perform(
					put("/api/places/{id}",10)
					.contentType(MediaType.APPLICATION_JSON)
					.content(asJsonString(placeDTOToTest)))
		.andExpect(status().isBadRequest());
		
		long countAfterInsert = placeRepository.count();
		startPlace = placeRepository.findById(startPlace.getId()).get();
		
		assertEquals(countAfterInsert,countBeforeUpdate);
		assertEquals("DEFAULT NAME", startPlace.getName());
		assertEquals("DEFAULT SLUG", startPlace.getSlug());
		assertEquals("DEFAULT CITY", startPlace.getCity());
		assertEquals("DEFAULT STATE", startPlace.getState());
		
		
	}
	
	
	
	@After
	public void tearDown() {
		placeRepository.deleteAll();
	}
	
	private String asJsonString(final Object obj) {
	    try {
	        return new ObjectMapper().writeValueAsString(obj);
	    } catch (Exception e) {
	        throw new RuntimeException(e);
	    }
	}
	
}
