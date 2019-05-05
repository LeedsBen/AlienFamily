package com.example.alienfamily.alien;

import com.example.alienfamily.exception.AlienException;
import org.junit.jupiter.api.Test;

import java.util.List;

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
        AlienException ace = assertThrows(AlienException.class, () -> {
            Alien alien = new Alien();
        });
        assertEquals("Aliens cannot appear out of the ether!", ace.getMessage());
    }

    /**
     * Test to initialise first alien.
     */
    @Test
    public void initialiseAlienTest() throws AlienException {
        Alien adam = Alien.initialise("Adam", AlienType.ALPHA, "Omicron");
        assertNotNull(adam);
        assertAlien(adam, "Adam", AlienType.ALPHA, "Omicron");
        assertNull(adam.getParent());
    }

    /**
     * Test to add a child
     */
    @Test
    public void addChildTest() {
        assertDoesNotThrow(() -> {
            Alien adam = Alien.initialise("Adam", AlienType.ALPHA, "Omicron");
            adam.addChild("Vexorg", AlienType.ALPHA, "Omicron");
            List<Alien> adamsKids = adam.getChildren();
            assertEquals(1, adamsKids.size());
            assertAlien(adamsKids.get(0), "Vexorg", AlienType.ALPHA, "Omicron");
        });
    }

    /**
     * Test to check name can't be over 50 characters
     */
    @Test
    public void nameLengthTest() {
        AlienException ace = assertThrows(AlienException.class, () -> {
            Alien adam = Alien.initialise("Adam with a super long name that is actually way way way over the limit and more than the permitted fifty characters", AlienType.ALPHA, "Omicron");
        });
        assertEquals("Name and home planet cannot be over 50 characters", ace.getMessage());

        AlienException ace2 = assertThrows(AlienException.class, () -> {
            Alien adam = Alien.initialise("Adam", AlienType.ALPHA, "Omicron");
            adam.addChild("Vexorg the destroyer of worlds and devourer of sworn enemies", AlienType.ALPHA, "Omicron");

        });
        assertEquals("Name and home planet cannot be over 50 characters", ace2.getMessage());

        AlienException ace3 = assertThrows(AlienException.class, () -> {
            Alien adam = Alien.initialise("Adam", AlienType.ALPHA, "Omicron");
            adam.setName("Adam with a super long name that is actually way way way over the limit and more than the permitted fifty characters");
        });
        assertEquals("Name and home planet cannot be over 50 characters", ace3.getMessage());
    }

    /**
     * Test to check home planet can't be over 50 characters
     */
    @Test
    public void homePlanetLengthTest() {
        AlienException ace = assertThrows(AlienException.class, () -> {
            Alien adam = Alien.initialise("Adam", AlienType.ALPHA, "Omicron the home of the Adam, the God Alien and destroyer of Worlds, father of Vexorg the Magnificent");
        });
        assertEquals("Name and home planet cannot be over 50 characters", ace.getMessage());

        AlienException ace2 = assertThrows(AlienException.class, () -> {
            Alien adam = Alien.initialise("Adam", AlienType.ALPHA, "Omicron the home of the Vexorg the Magnificent, son of Adam destroyer of Worlds");
            adam.addChild("Vexorg", AlienType.ALPHA, "Omicron");

        });
        assertEquals("Name and home planet cannot be over 50 characters", ace2.getMessage());

        AlienException ace3 = assertThrows(AlienException.class, () -> {
            Alien adam2 = Alien.initialise("Adam", AlienType.ALPHA, "Omicron");
            adam2.setHomePlanet("Omicron the home of the Adam, the God Alien and destroyer of Worlds, father of Vexorg the Magnificent");
        });
        assertEquals("Name and home planet cannot be over 50 characters", ace3.getMessage());
    }

    /**
     * Test to check betas & gammas can't have kids
     */
    @Test
    public void cannotAddChildTest() throws AlienException {
        Alien adamBeta = Alien.initialise("AdamBeta", AlienType.BETA, "Omicron");
        AlienException aceB = assertThrows(AlienException.class, () -> {
            adamBeta.addChild("someName", AlienType.ALPHA, "somePlanet");
        });
        assertEquals("Only Alpha aliens can reproduce. AdamBeta is of type BETA", aceB.getMessage());
        AlienException aceB2 = assertThrows(AlienException.class, () -> {
            adamBeta.getChildren();
        });
        assertEquals("Only alphas have children", aceB2.getMessage());

        Alien adamGamma = Alien.initialise("AdamGamma", AlienType.GAMMA, "Omicron");
        AlienException aceG = assertThrows(AlienException.class, () -> {
            adamGamma.addChild("someName", AlienType.ALPHA, "somePlanet");
        });
        assertEquals("Only Alpha aliens can reproduce. AdamGamma is of type GAMMA", aceG.getMessage());
        AlienException aceG2 = assertThrows(AlienException.class, () -> {
            adamGamma.getChildren();
        });
        assertEquals("Only alphas have children", aceG2.getMessage());
    }

    /**
     * Test to ensure an alien can have no more than two children
     */
    @Test
    public void addTooManyChildrenTest() {
        AlienException ace = assertThrows(AlienException.class, () -> {
            Alien adam = Alien.initialise("Adam", AlienType.ALPHA, "Omicron");
            adam.addChild("Vexorg", AlienType.ALPHA, "Omicron");
            adam.addChild("Braxtarg", AlienType.GAMMA, "Persei");
            List<Alien> adamsKids = adam.getChildren();
            assertEquals(2, adamsKids.size());
            assertAlien(adamsKids.get(0), "Vexorg", AlienType.ALPHA, "Omicron");
            assertAlien(adamsKids.get(1), "Braxtarg", AlienType.GAMMA, "Persei");
            adam.addChild("Proxigord", AlienType.BETA, "Persei");
        });
        assertEquals("Alien Adam has already had two children", ace.getMessage());
    }

    /**
     * Test to ensure Alien cannot have more than 2 kids when kids are removed/deleted
     */
    @Test
    public void addKidsAfterKidsRemovedTest() {
        AlienException ace = assertThrows(AlienException.class, () -> {
            Alien adam = Alien.initialise("Adam", AlienType.ALPHA, "Omicron");
            adam.addChild("Vexorg", AlienType.ALPHA, "Omicron");
            adam.addChild("Braxtarg", AlienType.GAMMA, "Persei");
            List<Alien> adamsKids = adam.getChildren();
            assertEquals(2, adamsKids.size());
            adam.removeKids();
            List<Alien> adamsKidsAfterRemoval = adam.getChildren();
            assertEquals(0, adamsKidsAfterRemoval.size());
            adam.addChild("Proxigord", AlienType.BETA, "Persei");
        });
        assertEquals("Alien Adam has already had two children", ace.getMessage());
    }

    private void assertAlien(Alien alien, String expectedName, AlienType expectedType, String expectedPlanet) {
        assertEquals(expectedName, alien.getName());
        assertEquals(expectedType, alien.getType());
        assertEquals(expectedPlanet, alien.getHomePlanet());
    }
}
