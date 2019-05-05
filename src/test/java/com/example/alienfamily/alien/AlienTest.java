package com.example.alienfamily.alien;

import com.example.alienfamily.exception.AlienChildException;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class to test basic alien functionality
 */
public class AlienTest {

    /**
     * Test constructor throws exception.
     */
    @Test
    public void createAlienTest() {
        AlienChildException ace = assertThrows(AlienChildException.class, () -> {
            Alien alien = new Alien();
        });
        assertEquals("Aliens cannot appear out of the ether!", ace.getMessage());
    }

    /**
     * Test to initialise first alien.
     */
    @Test
    public void initialiseAlienTest() {
        Alien adam = Alien.initialise("Adam", AlienType.ALPHA, Optional.of("Omicron"));
        assertNotNull(adam);
        assertAlien(adam, "Adam", AlienType.ALPHA, "Omicron");
        assertNull(adam.getParent());
    }

    /**
     * Test to add a child
     */
    @Test
    public void addChildTest() {
        Alien adam = Alien.initialise("Adam", AlienType.ALPHA, Optional.of("Omicron"));
        assertDoesNotThrow(() -> {
            adam.addChild("Vexorg", AlienType.ALPHA, Optional.of("Omicron"));
            List<Alien> adamsKids = adam.getChildren();
            assertEquals(1, adamsKids.size());
            assertAlien(adamsKids.get(0), "Vexorg", AlienType.ALPHA, "Omicron");
        });
    }

    /**
     * Test to check betas & gammas can't have kids
     */
    @Test
    public void cannotAddChildTest() {
        Alien adamBeta = Alien.initialise("AdamBeta", AlienType.BETA, Optional.of("Omicron"));
        AlienChildException aceB = assertThrows(AlienChildException.class, () -> {
            adamBeta.addChild("someName", AlienType.ALPHA, Optional.of("somePlanet"));
        });
        assertEquals("Only Alpha aliens can reproduce. AdamBeta is of type BETA", aceB.getMessage());
        AlienChildException aceB2 = assertThrows(AlienChildException.class, () -> {
            adamBeta.getChildren();
        });
        assertEquals("Only alphas have children", aceB2.getMessage());

        Alien adamGamma = Alien.initialise("AdamGamma", AlienType.GAMMA, Optional.of("Omicron"));
        AlienChildException aceG = assertThrows(AlienChildException.class, () -> {
            adamGamma.addChild("someName", AlienType.ALPHA, Optional.of("somePlanet"));
        });
        assertEquals("Only Alpha aliens can reproduce. AdamGamma is of type GAMMA", aceG.getMessage());
        AlienChildException aceG2 = assertThrows(AlienChildException.class, () -> {
            adamGamma.getChildren();
        });
        assertEquals("Only alphas have children", aceG2.getMessage());
    }

    /**
     * Test to ensure an alien can have no more than two children
     */
    @Test
    public void addTooManyChildrenTest() {
        Alien adam = Alien.initialise("Adam", AlienType.ALPHA, Optional.of("Omicron"));
        AlienChildException ace = assertThrows(AlienChildException.class, () -> {
            adam.addChild("Vexorg", AlienType.ALPHA, Optional.of("Omicron"));
            adam.addChild("Braxtarg", AlienType.GAMMA, Optional.of("Persei"));
            List<Alien> adamsKids = adam.getChildren();
            assertEquals(2, adamsKids.size());
            assertAlien(adamsKids.get(0), "Vexorg", AlienType.ALPHA, "Omicron");
            assertAlien(adamsKids.get(1), "Braxtarg", AlienType.GAMMA, "Persei");
            adam.addChild("Proxigord", AlienType.BETA, Optional.of("Persei"));
        });
        assertEquals("Alien Adam has already had two children", ace.getMessage());
    }

    /**
     * Test to ensure Alien cannot have more than 2 kids when kids are removed/deleted
     */
    @Test
    public void addKidsAfterKidsRemovedTest() {
        Alien adam = Alien.initialise("Adam", AlienType.ALPHA, Optional.of("Omicron"));
        AlienChildException ace = assertThrows(AlienChildException.class, () -> {
            adam.addChild("Vexorg", AlienType.ALPHA, Optional.of("Omicron"));
            adam.addChild("Braxtarg", AlienType.GAMMA, Optional.of("Persei"));
            List<Alien> adamsKids = adam.getChildren();
            assertEquals(2, adamsKids.size());
            adam.removeKids();
            List<Alien> adamsKidsAfterRemoval = adam.getChildren();
            assertEquals(0, adamsKidsAfterRemoval.size());
            adam.addChild("Proxigord", AlienType.BETA, Optional.of("Persei"));
        });
        assertEquals("Alien Adam has already had two children", ace.getMessage());
    }

    private void assertAlien(Alien alien, String expectedName, AlienType expectedType, String expectedPlanet) {
        assertEquals(expectedName, alien.getName());
        assertEquals(expectedType, alien.getType());
        assertEquals(Optional.of(expectedPlanet), alien.getHomePlanet());
    }
}
