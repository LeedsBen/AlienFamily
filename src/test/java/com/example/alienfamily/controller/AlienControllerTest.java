package com.example.alienfamily.controller;

import com.example.alienfamily.alien.AlienType;
import com.example.alienfamily.service.AlienService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Simple unit test class for the controller
 */
@ExtendWith(MockitoExtension.class)
public class AlienControllerTest {

    @Mock
    private AlienService alienService;

    /**
     * Test to start the colony
     */
    @Test
    public void startControllerTest() {
        AlienController controller = new AlienController(alienService);
        ResponseEntity<Void> response = controller.start("Vexorg", "Omicron");
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    /**
     * Test to add an alien
     */
    @Test
    public void addAlienTest() {
        AlienController controller = new AlienController(alienService);
        ResponseEntity<Void> response = controller.addAlien("Vexorg", "Braxtarg", AlienType.ALPHA, "Persei");
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    /**
     * Test to update an alien
     */
    @Test
    public void updateAlienTest() {
        AlienController controller = new AlienController(alienService);
        ResponseEntity<Void> response = controller.updateAlien("Braxtarg", "Frank", "Grimsby");
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    /**
     * Test to get an alien
     */
    @Test
    public void getAlienTest() {
        when(alienService.getAlien(anyString())).thenReturn("Some details about an alien");
        AlienController controller = new AlienController(alienService);
        String alien = controller.getAlien("Vexorg");
        assertEquals("Some details about an alien", alien);
    }

    /**
     * Test to delete an alien
     */
    @Test
    public void deleteAlienTest() {
        AlienController controller = new AlienController(alienService);
        ResponseEntity<Void> response = controller.deleteAlien("Vexorg");
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
