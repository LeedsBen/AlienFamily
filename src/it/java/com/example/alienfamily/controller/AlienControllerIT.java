package com.example.alienfamily.controller;

import com.example.alienfamily.alien.AlienType;
import com.example.alienfamily.service.AlienService;
import org.junit.FixMethodOrder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.util.UriComponentsBuilder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Integration Test class for Alien Controller.
 *
 * Tests named alphabetically to guarantee order.
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@DisplayName("Alien Controller Integration Test")
public class AlienControllerIT {

    @LocalServerPort
    private int localServerPort;

    @Autowired
    private AlienService alienService;

    /**
     * Simple test to check Spring Context loads
     */
    @Test
    @DisplayName("Basic test to check the Spring Context")
    public void aBasicIT() {
        assertTrue(true);
    }

    /**
     * Test to start a colony
     */
    @Test
    @DisplayName("Hit the start endpoint")
    public void bStartIT() {
        TestRestTemplate restTemplate = new TestRestTemplate();
        String url = "http://localhost:" + localServerPort;

        // Hit start
        HttpEntity<String> entity = new HttpEntity<String>(null, new HttpHeaders());
        UriComponentsBuilder uriStartColony = UriComponentsBuilder.fromHttpUrl(url + "/aliencolony/start")
                .queryParam("name", "Vexorg")
                .queryParam("homePlanet", "Omicron");
        ResponseEntity<Void> startColonyResponse = restTemplate.exchange(uriStartColony.build().toUri(), HttpMethod.POST, entity, Void.class);
        assertEquals(200, startColonyResponse.getStatusCodeValue());
    }

    /**
     * Test to build a colony.
     *
     * Adds some aliens, gets some, updates one and deletes one
     */
    @Test
    @DisplayName("Build full colony test")
    public void cBuildColonyIT() {
        TestRestTemplate restTemplate = new TestRestTemplate();
        String url = "http://localhost:" + localServerPort;
        HttpEntity<String> entity = new HttpEntity<String>(null, new HttpHeaders());

        // Hit start
        UriComponentsBuilder uriStartColony = UriComponentsBuilder.fromHttpUrl(url + "/aliencolony/start")
                .queryParam("name", "Vexorg")
                .queryParam("homePlanet", "Omicron");
        ResponseEntity<Void> startColonyResponse = restTemplate.exchange(uriStartColony.build().toUri(), HttpMethod.POST, entity, Void.class);
        assertEquals(200, startColonyResponse.getStatusCodeValue());

        // Add aliens
        UriComponentsBuilder uriAddAlien = UriComponentsBuilder.fromHttpUrl(url + "/aliencolony/addAlien")
                .queryParam("parentName", "Vexorg")
                .queryParam("childName", "Braxtarg")
                .queryParam("type", AlienType.ALPHA)
                .queryParam("homePlanet", "Persei");
        ResponseEntity<Void> addBraxtarg = restTemplate.exchange(uriAddAlien.build().toUri(), HttpMethod.POST, entity, Void.class);
        assertEquals(200, addBraxtarg.getStatusCodeValue());

        UriComponentsBuilder uriAddAlien2 = UriComponentsBuilder.fromHttpUrl(url + "/aliencolony/addAlien")
                .queryParam("parentName", "Vexorg")
                .queryParam("childName", "Proxigord")
                .queryParam("type", AlienType.BETA)
                .queryParam("homePlanet", "Omicron");
        ResponseEntity<Void> addProxigord = restTemplate.exchange(uriAddAlien2.build().toUri(), HttpMethod.POST, entity, Void.class);
        assertEquals(200, addProxigord.getStatusCodeValue());

        // Get alien
        UriComponentsBuilder uriGetAlien = UriComponentsBuilder.fromHttpUrl(url + "/aliencolony/getAlien")
                .queryParam("name", "Proxigord");
        ResponseEntity<String> getProxigord = restTemplate.exchange(uriGetAlien.build().toUri(), HttpMethod.GET, entity, String.class);
        assertEquals(200, getProxigord.getStatusCodeValue());
        String proxi = getProxigord.getBody();
        assertTrue(proxi.contains("Proxigord") && proxi.contains("BETA") && proxi.contains("Omicron") && proxi.contains("Vexorg"));

        // Update alien
        UriComponentsBuilder uriUpdateAlien = UriComponentsBuilder.fromHttpUrl(url + "/aliencolony/updateAlien")
                .queryParam("oldName", "Proxigord")
                .queryParam("newName", "Frank")
                .queryParam("newPlanet", "Grimsby");
        ResponseEntity<Void> updateProxigord = restTemplate.exchange(uriUpdateAlien.build().toUri(), HttpMethod.POST, entity, Void.class);
        assertEquals(200, updateProxigord.getStatusCodeValue());

        // Get updated alien
        UriComponentsBuilder uriGetAlien2 = UriComponentsBuilder.fromHttpUrl(url + "/aliencolony/getAlien")
                .queryParam("name", "Frank");
        ResponseEntity<String> getFrank = restTemplate.exchange(uriGetAlien2.build().toUri(), HttpMethod.GET, entity, String.class);
        assertEquals(200, getFrank.getStatusCodeValue());
        String frank = getFrank.getBody();
        assertTrue(!frank.contains("Proxigord") && frank.contains("Frank") && frank.contains("BETA") && !frank.contains("Omicron") && frank.contains("Grimsby") && frank.contains("Vexorg"));

        // Try to get old alien
        UriComponentsBuilder uriGetAlien3 = UriComponentsBuilder.fromHttpUrl(url + "/aliencolony/getAlien")
                .queryParam("name", "Proxigord");
        ResponseEntity<String> dontGetProxigord = restTemplate.exchange(uriGetAlien3.build().toUri(), HttpMethod.GET, entity, String.class);
        assertEquals(404, dontGetProxigord.getStatusCodeValue());
        assertEquals("Alien Proxigord not found", dontGetProxigord.getBody());

        // Delete alien
        UriComponentsBuilder uriDeleteAlien = UriComponentsBuilder.fromHttpUrl(url + "/aliencolony/deleteAlien")
                .queryParam("name", "Braxtarg");
        ResponseEntity<Void> deleteBraxtarg = restTemplate.exchange(uriDeleteAlien.build().toUri(), HttpMethod.DELETE, entity, Void.class);
        assertEquals(200, deleteBraxtarg.getStatusCodeValue());

        // Check deleted alien is gone
        UriComponentsBuilder uriGetAlien4 = UriComponentsBuilder.fromHttpUrl(url + "/aliencolony/getAlien")
                .queryParam("name", "Braxtarg");
        ResponseEntity<String> dontGetBraxtarg = restTemplate.exchange(uriGetAlien4.build().toUri(), HttpMethod.GET, entity, String.class);
        assertEquals(404, dontGetBraxtarg.getStatusCodeValue());
        assertEquals("Alien Braxtarg not found", dontGetBraxtarg.getBody());
    }

    /**
     * Test to check error handling
     */
    @Test
    @DisplayName("Check error handling")
    public void dBadRequestsIT() {
        // Reset Alien Colony
        alienService.startColony("Vexorg", "Omicron");
        alienService.deleteAlien("Vexorg");
        TestRestTemplate restTemplate = new TestRestTemplate();
        String url = "http://localhost:" + localServerPort;
        HttpEntity<String> entity = new HttpEntity<String>(null, new HttpHeaders());

        // Test an operation before we've started
        UriComponentsBuilder uriGetAlienBeforeStart = UriComponentsBuilder.fromHttpUrl(url + "/aliencolony/getAlien")
                .queryParam("name", "Braxtarg");
        ResponseEntity<String> getNonExistent = restTemplate.exchange(uriGetAlienBeforeStart.build().toUri(), HttpMethod.GET, entity, String.class);
        assertEquals(400, getNonExistent.getStatusCodeValue());
        assertEquals("No aliens! Please start a new colony.", getNonExistent.getBody());

        // Start a new colony
        UriComponentsBuilder uriStartColony = UriComponentsBuilder.fromHttpUrl(url + "/aliencolony/start")
                .queryParam("name", "Vexorg")
                .queryParam("homePlanet", "Omicron");
        ResponseEntity<Void> startColonyResponse = restTemplate.exchange(uriStartColony.build().toUri(), HttpMethod.POST, entity, Void.class);
        assertEquals(200, startColonyResponse.getStatusCodeValue());

        // Try to add an alien with an unknown parent
        UriComponentsBuilder uriAddAlien = UriComponentsBuilder.fromHttpUrl(url + "/aliencolony/addAlien")
                .queryParam("parentName", "Mr Pants")
                .queryParam("childName", "Braxtarg")
                .queryParam("type", AlienType.ALPHA)
                .queryParam("homePlanet", "Persei");
        ResponseEntity<String> addBraxtarg = restTemplate.exchange(uriAddAlien.build().toUri(), HttpMethod.POST, entity,String.class);
        assertEquals(404, addBraxtarg.getStatusCodeValue());
        assertEquals("Alien Mr Pants does not exist, Child not added.", addBraxtarg.getBody());

        // Try to update alien that doesn't exist
        UriComponentsBuilder uriUpdateAlien = UriComponentsBuilder.fromHttpUrl(url + "/aliencolony/updateAlien")
                .queryParam("oldName", "Mr Pants")
                .queryParam("newName", "Frank")
                .queryParam("newPlanet", "Grimsby");
        ResponseEntity<String> updateMrPants = restTemplate.exchange(uriUpdateAlien.build().toUri(), HttpMethod.POST, entity, String.class);
        assertEquals(404, updateMrPants.getStatusCodeValue());
        assertEquals("Alien Mr Pants not updated as they do not exist", updateMrPants.getBody());

        // Try to delete an alien that doesn't exist
        UriComponentsBuilder uriDeleteAlien = UriComponentsBuilder.fromHttpUrl(url + "/aliencolony/deleteAlien")
                .queryParam("name", "Mr Pants");
        ResponseEntity<String> deleteMrPants = restTemplate.exchange(uriDeleteAlien.build().toUri(), HttpMethod.DELETE, entity, String.class);
        assertEquals(404, deleteMrPants.getStatusCodeValue());
        assertEquals("Alien Mr Pants not removed as it does not exist.", deleteMrPants.getBody());

        // Try to get an alien that doesn't exist
        UriComponentsBuilder uriGetAlien = UriComponentsBuilder.fromHttpUrl(url + "/aliencolony/getAlien")
                .queryParam("name", "Mr Pants");
        ResponseEntity<String> getMrPants = restTemplate.exchange(uriGetAlien.build().toUri(), HttpMethod.GET, entity, String.class);
        assertEquals(404, getMrPants.getStatusCodeValue());
        assertEquals("Alien Mr Pants not found", getMrPants.getBody());
    }
}
