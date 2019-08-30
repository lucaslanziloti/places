package br.com.clickbus.places;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.clickbus.places.dto.PlaceDto;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PlaceIntegrationTest {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	private StringBuilder endpoint = new StringBuilder();
	
	@Before
	public void setup() {
		endpoint.append("http://localhost:").append(port).append("/places");
	}

	@Test
	public void aShouldFindAPlaceById() {
		endpoint.append("/1");

		ResponseEntity<PlaceDto> result = this.restTemplate.getForEntity(endpoint.toString(), PlaceDto.class);

		assertEquals(200, result.getStatusCodeValue());
		assertEquals(result.getBody().getId().intValue(), 1);
	}

	@Test
	public void bShouldDeletePlace() {
		endpoint.append("/1");
		restTemplate.delete(endpoint.toString());

		ResponseEntity<PlaceDto> result = this.restTemplate.getForEntity(endpoint.toString(), PlaceDto.class);

		assertEquals(404, result.getStatusCodeValue());
	}

	@Test
	public void shouldSaveAPlace() throws URISyntaxException {
		PlaceDto placeDto = new PlaceDto();
		placeDto.setCity("City integration test");
		placeDto.setSlug("Slug integration test");
		placeDto.setState("State integration test");
		placeDto.setName("Name integration test");

		HttpHeaders headers = new HttpHeaders();

		HttpEntity<PlaceDto> request = new HttpEntity<>(placeDto, headers);

		ResponseEntity<PlaceDto> result = this.restTemplate.postForEntity(new URI(endpoint.toString()), request,
				PlaceDto.class);

		assertEquals(201, result.getStatusCodeValue());
		assertNotNull(result.getBody().getId());
		assertNotNull(result.getBody().getCreationDate());

		assertNull(result.getBody().getUpdateDate());

		assertEquals(result.getBody().getCity(), placeDto.getCity());
		assertEquals(result.getBody().getSlug(), placeDto.getSlug());
		assertEquals(result.getBody().getName(), placeDto.getName());
		assertEquals(result.getBody().getState(), placeDto.getState());
	}

	@Test
	public void shouldUpdateAPlace() {
		PlaceDto placeDto = new PlaceDto();
		placeDto.setId(2L);
		placeDto.setCity("City integration test updated");
		placeDto.setSlug("Slug integration test updated");
		placeDto.setState("State integration test updated");
		placeDto.setName("Name integration test updated");

		HttpHeaders headers = new HttpHeaders();

		HttpEntity<PlaceDto> request = new HttpEntity<>(placeDto, headers);

		ResponseEntity<PlaceDto> result = this.restTemplate.exchange(endpoint.toString(), HttpMethod.PUT, request,
				PlaceDto.class);

		assertEquals(200, result.getStatusCodeValue());
		assertNotNull(result.getBody().getCreationDate());
		assertNotNull(result.getBody().getUpdateDate());

		assertEquals(result.getBody().getId().intValue(), 2);
		assertEquals(result.getBody().getCity(), placeDto.getCity());
		assertEquals(result.getBody().getSlug(), placeDto.getSlug());
		assertEquals(result.getBody().getName(), placeDto.getName());
		assertEquals(result.getBody().getState(), placeDto.getState());
	}
}
