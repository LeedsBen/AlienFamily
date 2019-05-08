package com.example.alienfamily.service;

import com.example.alienfamily.alien.AlienType;
import com.example.alienfamily.exception.AlienException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Alien Service
 */
public class AlienServiceTest {

    /**
     * Test to ensure colony can only be started with a new alien with a non-null name
     *
     * Also test that ethods cannot be called before a colony has been started
     */
    @Test
    public void startColonyTest() {
        AlienService alienService = new AlienService();
        AlienException ae = assertThrows(AlienException.class, () -> {
           alienService.addAlien(null, null, null, null);
        });
        assertEquals("No aliens! Please start a new colony.", ae.getMessage());
        ae = assertThrows(AlienException.class, () -> {
            alienService.updateAlien(null, null, null);
        });
        assertEquals("No aliens! Please start a new colony.", ae.getMessage());
        ae = assertThrows(AlienException.class, () -> {
            alienService.deleteAlien(null);
        });
        assertEquals("No aliens! Please start a new colony.", ae.getMessage());
        ae = assertThrows(AlienException.class, () -> {
            alienService.getAlien(null);
        });
        assertEquals("No aliens! Please start a new colony.", ae.getMessage());
        ae = assertThrows(AlienException.class, () -> {
            alienService.startColony(null, "Omicron");
        });
        assertEquals("Aliens must have a name and a type", ae.getMessage());
        assertDoesNotThrow(() -> {
            alienService.startColony("Vexorg", "Omicron");
        });
        assertDoesNotThrow(() -> {
            String vexorg = alienService.getAlien("Vexorg");
            assertTrue(vexorg.contains("Vexorg") && vexorg.contains("Omicron") && vexorg.contains("ALPHA"));
        });
    }

    /**
     * Test to add aliens.
     *
     * Check we can't add null values
     *
     * Check we can't add aliens that already exist
     *
     * Check we can't add to non-existent parents
     *
     * Check we can't add to non-ALPHA aliens
     */
    @Test
    public void addAlienTest() {
        AlienService alienService = new AlienService();
        alienService.startColony("Vexorg", "Omicron");
        AlienException ae = assertThrows(AlienException.class, () -> {
            alienService.addAlien(null, null, null, null);
        });
        assertEquals("Please specify a parent for this alien.", ae.getMessage());
        ae = assertThrows(AlienException.class, () -> {
            alienService.addAlien("Vexorg", null, null, null);
        });
        assertEquals("Aliens must have a name and a type", ae.getMessage());
        ae = assertThrows(AlienException.class, () -> {
            alienService.addAlien("Vexorg", "Braxtarg", null, null);
        });
        assertEquals("Aliens must have a name and a type", ae.getMessage());
        assertDoesNotThrow(() -> {
            alienService.addAlien("Vexorg", "Braxtarg", AlienType.BETA, "Omicron");
        });
        assertEquals(2, alienService.getAlienColony().size());
        ae = assertThrows(AlienException.class, () -> {
            alienService.addAlien("Vexorg", "Braxtarg", null, null);
        });
        assertEquals("Alien Braxtarg already exists. Aliens must have unique names.", ae.getMessage());
        ae = assertThrows(AlienException.class, () -> {
            alienService.addAlien("Mr Pants", "Proxigord", null, null);
        });
        assertEquals("Alien Mr Pants does not exist, Child not added.", ae.getMessage());
        ae = assertThrows(AlienException.class, () -> {
            alienService.addAlien("Braxtarg", "Proxigord", null, null);
        });
        assertEquals("Only Alpha aliens can reproduce. Braxtarg is of type BETA", ae.getMessage());
    }
}
