package br.com.clickbus.places.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author llanziloti
 *
 */
@Entity
@Data
@EqualsAndHashCode
public class Place {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	private String slug;

	private String city;

	private String state;

	private LocalDateTime creationDate;

	private LocalDateTime updateDate;
}
