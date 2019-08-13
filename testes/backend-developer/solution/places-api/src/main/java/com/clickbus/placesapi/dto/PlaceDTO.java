package com.clickbus.placesapi.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PlaceDTO {

	private Long id;
	private String name;
	private String slug;
	private String city;
	private String state;
	
	@JsonFormat(pattern = "dd.MM.yyyy HH:mm")
	private LocalDateTime createdAt;
	
	@JsonFormat(pattern = "dd.MM.yyyy HH:mm")
	private LocalDateTime updatedAt; 
	
}
