package br.com.clickbus.places.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PlaceDto {

	private Long id;
	
	private String name;
	
	private String slug;
	
	private String city;
	
	private String state;

	private LocalDateTime creationDate;

	private LocalDateTime updateDate;
}
