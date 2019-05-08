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
     * Testing retrieve functionality
     */
    @Test
    public void retrieveAlienTest() {
        // Create multi-generational Alien Colony
        AlienService alienService = new AlienService();
        alienService.startColony("Vexorg", "Omicron");
        alienService.addAlien("Vexorg", "Braxtarg", AlienType.BETA, "Persei");
        alienService.addAlien("Vexorg", "Proxigord", AlienType.ALPHA, "Omicron");
        alienService.addAlien("Proxigord", "Venkagard", AlienType.GAMMA, "Molita");
        alienService.addAlien("Proxigord", "Tanqahorn", AlienType.ALPHA, "Persei");

        String vexorg = alienService.getAlien("Vexorg");
        assertTrue(vexorg.contains("Vexorg") && vexorg.contains("Braxtarg") && vexorg.contains("Proxigord") && vexorg.contains("ALPHA") && vexorg.contains("Omicron"));
        String tanqahorn = alienService.getAlien("Tanqahorn");
        assertTrue(tanqahorn.contains("Tanqahorn") && tanqahorn.contains("Proxigord") && tanqahorn.contains("ALPHA") && tanqahorn.contains("Persei"));
        String venkagard = alienService.getAlien("Venkagard");
        assertTrue(venkagard.contains("Venkagard") && venkagard.contains("Proxigord") && venkagard.contains("GAMMA") && venkagard.contains("Molita"));
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

    /**
     * Test for updating aliens
     *
     * Check we can't update non-existenet aliens
     *
     * Check we can update other aliens
     *
     */
    @Test
    public void updateAlienTest() {
        // Create multi-generational Alien Colony
        AlienService alienService = new AlienService();
        alienService.startColony("Vexorg", "Omicron");
        alienService.addAlien("Vexorg", "Braxtarg", AlienType.BETA, "Persei");
        alienService.addAlien("Vexorg", "Proxigord", AlienType.ALPHA, "Omicron");
        alienService.addAlien("Proxigord", "Venkagard", AlienType.GAMMA, "Molita");
        alienService.addAlien("Proxigord", "Tanqahorn", AlienType.ALPHA, "Persei");

        // Can't update Mr Pants
        AlienException ae = assertThrows(AlienException.class, () -> {
            alienService.updateAlien("Mr Pants", null, null);
        });
        assertEquals("Alien Mr Pants not updated as they do not exist", ae.getMessage());

        // Update Proxigorn to be Frank from Grimsby
        alienService.updateAlien("Proxigord", "Frank", "Grimsby");
        // Check we can no longer find Proxigord
        ae = assertThrows(AlienException.class, () -> {
            alienService.getAlien("Proxigord");
        });
        assertEquals("Alien Proxigord not found", ae.getMessage());
        // Check that Frank is from Grimsby and still has kids
        String frank = alienService.getAlien("Frank");
        assertTrue(frank.contains("Frank") && frank.contains("Venkagard") && !frank.contains("Proxigord"));

        // Check that Frank's kids now know him as Frank
        String venkagard = alienService.getAlien("Venkagard");
        assertTrue(venkagard.contains("Venkagard") && venkagard.contains("Frank") && !venkagard.contains("Proxigord"));
    }

    /**
     * Test for deleting aliens
     *
     * Check we can't delete from an empty colony
     *
     * Check we can't delete a non-existent alien
     *
     * Check we can delete other aliens
     */
    @Test
    public void deleteAlienTest() {
        AlienService alienService = new AlienService();
        alienService.startColony("Vexorg", "Omicron");

        // delete First alien and try to add another
        AlienException ae = assertThrows(AlienException.class, () -> {
            alienService.deleteAlien("Vexorg");
            alienService.addAlien("Vexorg", "Braxtarg", AlienType.BETA, "Persei");
        });
        assertEquals("No aliens! Please start a new colony.", ae.getMessage());

        // Create multi-generational Alien Colony
        alienService.startColony("Vexorg", "Omicron");
        alienService.addAlien("Vexorg", "Braxtarg", AlienType.BETA, "Persei");
        alienService.addAlien("Vexorg", "Proxigord", AlienType.ALPHA, "Omicron");
        alienService.addAlien("Proxigord", "Venkagard", AlienType.GAMMA, "Molita");
        alienService.addAlien("Proxigord", "Tanqahorn", AlienType.ALPHA, "Persei");
        alienService.addAlien("Tanqahorn", "Kranulla", AlienType.BETA, "Kwist 7");
        alienService.addAlien("Tanqahorn", "Sequanda", AlienType.GAMMA, "Molita");
        assertEquals(7, alienService.getAlienColony().size());

        // Try to delete non-existent alien
        ae = assertThrows(AlienException.class, () -> alienService.deleteAlien("Mr Pants"));
        assertEquals("Alien Mr Pants not removed as it does not exist.", ae.getMessage());

        // Delete bottom level alien, check they're gone and no longer a child of their parent
        alienService.deleteAlien("Sequanda");
        assertEquals(6, alienService.getAlienColony().size());
        ae = assertThrows(AlienException.class, () -> alienService.getAlien("Sequanda"));
        assertEquals("Alien Sequanda not found", ae.getMessage());
        String tanqahorn = alienService.getAlien("Tanqahorn");
        assertTrue(tanqahorn.contains("Tanqahorn") && tanqahorn.contains("Kranulla") && !tanqahorn.contains("Sequanda"));

        // Delete mid level alien, make sure it is gone as a child, but still referenced as a parent
        alienService.deleteAlien("Proxigord");
        assertEquals(5, alienService.getAlienColony().size());
        ae = assertThrows(AlienException.class, () -> alienService.getAlien("Proxigord"));
        assertEquals("Alien Proxigord not found", ae.getMessage());
        String vexorg = alienService.getAlien("Vexorg");
        assertTrue(vexorg.contains("Vexorg") && vexorg.contains("Braxtarg") && !vexorg.contains("Proxigord"));
        tanqahorn = alienService.getAlien("Tanqahorn");
        assertTrue(tanqahorn.contains("Tanqahorn") && tanqahorn.contains("Kranulla") && tanqahorn.contains("Proxigord"));
        String venkagard = alienService.getAlien("Venkagard");
        assertTrue(venkagard.contains("Venkagard") && venkagard.contains("Proxigord"));
    }
}
