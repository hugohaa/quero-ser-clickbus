package com.clickbus.placesapi.controller;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;

import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.clickbus.placesapi.service.PlaceService;

@RunWith(SpringRunner.class)
@WebMvcTest(PlaceController.class)
public class PlaceControllerTest {

	
	@Rule
	public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");

	@MockBean
	private PlaceService placeService;
	
	@Autowired
	private WebApplicationContext context;

	private MockMvc mock;
	
	@Before
	public void setUp(){
	    this.mock = MockMvcBuilders.webAppContextSetup(this.context)
	      .apply(documentationConfiguration(this.restDocumentation))
	      .build();
	}		
	
	
}
