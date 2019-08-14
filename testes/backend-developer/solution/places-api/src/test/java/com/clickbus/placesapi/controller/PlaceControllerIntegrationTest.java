package com.clickbus.placesapi.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.clickbus.placesapi.PlacesApiApplication;
import com.clickbus.placesapi.dto.PlaceDTO;
import com.clickbus.placesapi.model.Place;
import com.clickbus.placesapi.repository.PlaceRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PlacesApiApplication.class)
@AutoConfigureMockMvc
public class PlaceControllerIntegrationTest {

	@Rule
	public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");
	
    private MockMvc mvc;
	
	@Autowired
	private PlaceRepository placeRepository;
	
	@Autowired
	private WebApplicationContext context;
	
	private LocalDateTime now;
	
	private Place place1,place2,place3;
	
	@Before 
	public void setUp() { 
		
		this.mvc = MockMvcBuilders.webAppContextSetup(this.context)
			      .apply(documentationConfiguration(this.restDocumentation))
			      .build();
		
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
		
		FieldDescriptor[] placeDescriptor = getPlaceFieldDescriptor();
		
		mvc.perform(
					post("/api/places")
					.contentType(MediaType.APPLICATION_JSON)
					.content(asJsonString(placeDTOToTest)))
		.andExpect(status().isCreated())
		.andExpect(jsonPath("$.id", notNullValue()))
		.andExpect(jsonPath("$.name", is("NAME")))
		.andExpect(jsonPath("$.slug", is("SLUG")))
		.andExpect(jsonPath("$.city", is("CITY")))
		.andExpect(jsonPath("$.state", is("STATE")))
		.andDo(document("save-place",
				requestFields(placeDescriptor),
				responseFields(placeDescriptor)		  
		));
		
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
		
		FieldDescriptor[] placeDescriptor = getPlaceFieldDescriptor();
		
		mvc.perform(
					put("/api/places/{id}",place1.getId())
					.contentType(MediaType.APPLICATION_JSON)
					.content(asJsonString(placeDTOToTest)))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.id", is(place1.getId().intValue())))
		.andExpect(jsonPath("$.name", is("NAME")))
		.andExpect(jsonPath("$.slug", is("SLUG")))
		.andExpect(jsonPath("$.city", is("CITY")))
		.andExpect(jsonPath("$.state", is("STATE")))
		.andDo(document("update-place",
				pathParameters(
						parameterWithName("id").description("The Place's ID")	
				),
				requestFields(placeDescriptor),
				responseFields(placeDescriptor)
		));
		
				
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
		
		FieldDescriptor[] placeDescriptor = getPlaceFieldDescriptor();
		
		mvc.perform(get("/api/places/{id}",place1.getId()))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id", is(place1.getId().intValue())))
			.andExpect(jsonPath("$.name", is("DEFAULT NAME")))
			.andExpect(jsonPath("$.slug", is("DEFAULT SLUG")))
			.andExpect(jsonPath("$.city", is("DEFAULT CITY")))
			.andExpect(jsonPath("$.state", is("DEFAULT STATE")))
			.andDo(document("get-place-by-id",
					pathParameters(
							parameterWithName("id").description("The Place's ID")	
					),
					responseFields(placeDescriptor)
			));
	}
	
	@Test
	public void whenFindInexistentPlace_Then400() throws Exception {
		
		mvc.perform(get("/api/places/{id}",10))
		.andExpect(status().isBadRequest());
	}
	
	@Test
	public void whenFindWithFilter_ThenReturnFilteredPlaceAnd200() throws Exception {
		
		FieldDescriptor[] placeDescriptor = getPlaceFieldDescriptor();
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
			.andExpect(jsonPath("$[1].state", is("ANOTHER STATE")))
			.andDo(document("filter-places-by-name",
					requestParameters(
							parameterWithName("name").description("The Place's name to filter")	
					),
					responseFields(fieldWithPath("[]").description("An array of places"))
					.andWithPrefix("[].", placeDescriptor)
			));
			
	}
	
	@Test
	public void whenFindWithoutFilter_ThenReturnAllAnd200() throws Exception {
		FieldDescriptor[] placeDescriptor = getPlaceFieldDescriptor();
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
		.andExpect(jsonPath("$[2].state", is("WEIRD")))
		.andDo(document("get-all-places",
				responseFields(fieldWithPath("[]").description("An array of places"))
					.andWithPrefix("[].", placeDescriptor)
			
		));
		
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
	
	private FieldDescriptor[] getPlaceFieldDescriptor() {
		return new FieldDescriptor[] {
				fieldWithPath("id").description("ID of the place"),
				fieldWithPath("name").description("Name of the place"),
				fieldWithPath("slug").description("Slug of the place"),
				fieldWithPath("city").description("City of the place"),
				fieldWithPath("state").description("State of the place"),
				fieldWithPath("createdAt").description("Record creation time"),
				fieldWithPath("updatedAt").description("Record update time")
			};
	}
	
}
