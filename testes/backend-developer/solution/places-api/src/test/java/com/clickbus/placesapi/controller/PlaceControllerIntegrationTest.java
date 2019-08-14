package com.clickbus.placesapi.controller;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

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
	
	private Place place1,place2,place3;
	
	@Before 
	public void setUp() { 
		this.now = LocalDateTime.now(); 
		
		place1 = new Place(null,"DEFAULT NAME","DEFAULT SLUG","DEFAULT CITY","DEFAULT STATE",now,now);
		place2 = new Place(null,"ANOTHER NAME","ANOTHER SLUG","ANOTHER CITY","ANOTHER STATE",now,now);
		place3 = new Place(null,"WEIRD","WEIRD","WEIRD","WEIRD",now,now);
		place1 = placeRepository.save(place1);
		place2 = placeRepository.save(place2);
		place3 = placeRepository.save(place3);
		
	}
	 
	
	@Test
	public void whenSavePlace_ThenReturnPlaceAnd201() throws Exception {
		
		long countBeforeInsert = placeRepository.count();
		PlaceDTO placeDTOToTest = new PlaceDTO(null,"NAME","SLUG","CITY","STATE",null,null);
		
		mvc.perform(
					post("/api/places")
					.contentType(MediaType.APPLICATION_JSON)
					.content(asJsonString(placeDTOToTest)))
		.andExpect(status().isCreated())
		.andExpect(jsonPath("$.id", notNullValue()))
		.andExpect(jsonPath("$.name", is("NAME")))
		.andExpect(jsonPath("$.slug", is("SLUG")))
		.andExpect(jsonPath("$.city", is("CITY")))
		.andExpect(jsonPath("$.state", is("STATE")));
		
		long countAfterInsert = placeRepository.count();
		
		assertEquals(countAfterInsert,countBeforeInsert + 1);
		
	}
	
	
	@Test
	public void whenSaveAlreadyExistentSlug_Then409() throws Exception {

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
		
		long countBeforeUpdate = placeRepository.count();
		PlaceDTO placeDTOToTest = new PlaceDTO(null,"NAME","SLUG","CITY","STATE",null,null);
		
		mvc.perform(
					put("/api/places/{id}",place1.getId())
					.contentType(MediaType.APPLICATION_JSON)
					.content(asJsonString(placeDTOToTest)))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.id", is(place1.getId().intValue())))
		.andExpect(jsonPath("$.name", is("NAME")))
		.andExpect(jsonPath("$.slug", is("SLUG")))
		.andExpect(jsonPath("$.city", is("CITY")))
		.andExpect(jsonPath("$.state", is("STATE")));
		
				
		long countAfterInsert = placeRepository.count();
		
		assertEquals(countAfterInsert,countBeforeUpdate);
		
	}
	
	
	@Test
	public void whenUpdateInexistentPlace_Then400() throws Exception {
		
		
		long countBeforeUpdate = placeRepository.count();
		PlaceDTO placeDTOToTest = new PlaceDTO(null,"ANOTHER NAME","ANOTHER SLUG","ANOTHER CITY","ANOTHER STATE",null,null);
		
		mvc.perform(
					put("/api/places/{id}",10)
					.contentType(MediaType.APPLICATION_JSON)
					.content(asJsonString(placeDTOToTest)))
		.andExpect(status().isBadRequest());
		
		long countAfterInsert = placeRepository.count();
		
		assertEquals(countAfterInsert,countBeforeUpdate);

		
		
	}
	
	@Test
	public void whenFindExistentPlace_ThenReturnPlaceAnd200() throws Exception {
		
		mvc.perform(get("/api/places/{id}",place1.getId()))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id", is(place1.getId().intValue())))
			.andExpect(jsonPath("$.name", is("DEFAULT NAME")))
			.andExpect(jsonPath("$.slug", is("DEFAULT SLUG")))
			.andExpect(jsonPath("$.city", is("DEFAULT CITY")))
			.andExpect(jsonPath("$.state", is("DEFAULT STATE")));
		
	}
	
	@Test
	public void whenFindInexistentPlace_Then400() throws Exception {
		
		mvc.perform(get("/api/places/{id}",10))
		.andExpect(status().isBadRequest());
	}
	
	@Test
	public void whenFindWithFilter_ThenReturnFilteredPlaceAnd200() throws Exception {
		
		mvc.perform(get("/api/places?name={name}","NAME"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.length()", is(2)))
			.andExpect(jsonPath("$[0].name", is("DEFAULT NAME")))
			.andExpect(jsonPath("$[0].slug", is("DEFAULT SLUG")))
			.andExpect(jsonPath("$[0].city", is("DEFAULT CITY")))
			.andExpect(jsonPath("$[0].state", is("DEFAULT STATE")))
			.andExpect(jsonPath("$[1].name", is("ANOTHER NAME")))
			.andExpect(jsonPath("$[1].slug", is("ANOTHER SLUG")))
			.andExpect(jsonPath("$[1].city", is("ANOTHER CITY")))
			.andExpect(jsonPath("$[1].state", is("ANOTHER STATE")));
		
	}
	
	@Test
	public void whenFindWithoutFilter_ThenReturnAllAnd200() throws Exception {
		mvc.perform(get("/api/places"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.length()", is(3)))
		.andExpect(jsonPath("$[0].name", is("DEFAULT NAME")))
		.andExpect(jsonPath("$[0].slug", is("DEFAULT SLUG")))
		.andExpect(jsonPath("$[0].city", is("DEFAULT CITY")))
		.andExpect(jsonPath("$[0].state", is("DEFAULT STATE")))
		.andExpect(jsonPath("$[1].name", is("ANOTHER NAME")))
		.andExpect(jsonPath("$[1].slug", is("ANOTHER SLUG")))
		.andExpect(jsonPath("$[1].city", is("ANOTHER CITY")))
		.andExpect(jsonPath("$[1].state", is("ANOTHER STATE")))
		.andExpect(jsonPath("$[2].name", is("WEIRD")))
		.andExpect(jsonPath("$[2].slug", is("WEIRD")))
		.andExpect(jsonPath("$[2].city", is("WEIRD")))
		.andExpect(jsonPath("$[2].state", is("WEIRD")));
		
	}
	
	@Test
	public void whenFindWithFilterAndInexistentName_ThenReturnEmptyAnd200() throws Exception {
		
		
		mvc.perform(get("/api/places?name={name}","WEIRDOOO"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.length()", is(0)));
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
