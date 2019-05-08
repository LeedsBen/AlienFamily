package com.example.alienfamily.service;

import com.example.alienfamily.alien.Alien;
import com.example.alienfamily.alien.AlienType;
import com.example.alienfamily.exception.AlienException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Alien Service
 *
 * Models a colony of Aliens and CRUD operations to support it
 *
 */
@Service
public class AlienService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AlienService.class);

    /**
     * List of Aliens in the Colony
     */
    private List<Alien> alienColony;

    /**
     * Start Colony method.
     *
     * Starts a new Colony with the very first Alien.
     *
     * ASSUMPTION: First Alien is an Alpha type or we can't add anymore aliens
     *
     * @param name
     * @param birthPlanet
     * @throws AlienException
     */
    public void startColony(String name, String birthPlanet) {
        LOGGER.debug("Starting Colony...");
        Alien adam = Alien.initialise(name, AlienType.ALPHA, birthPlanet);
        alienColony = new ArrayList<>();
        alienColony.add(adam);
    }

    /**
     * Adds a new alien to the colony as a child of the parent alien.
     *
     * CAVEAT: Aliens must have unique names
     *
     * @param parentName
     * @param childName
     * @param type
     * @param homePlanet
     * @throws AlienException
     */
    public void addAlien(String parentName, String childName, AlienType type, String homePlanet) {
        checkColonyExists();
        if (parentName == null) {
            LOGGER.error("Alien " + childName + " not added due to null parent.");
            // No parent specified, throw exception
            throw new AlienException("Please specify a parent for this alien.");
        }
        if (alienColony.stream()
                .filter(alien -> alien.getName().equals(childName))
                .collect(Collectors.toList()).size() > 0) {
            // Alien must be unique
            LOGGER.error("Alien " + childName + " already exists.");
            throw new AlienException("Alien " + childName + " already exists. Aliens must have unique names.");
        }
        boolean parentFound = false;
        for (Alien alien : alienColony) {
            if (alien.getName().equals(parentName)) {
                LOGGER.debug("Parent alien " + parentName + " found for child " + childName);
                // add child to parent
                alien.addChild(childName, type, homePlanet);
                // add child to colony - seems a weird way to do it, but the Alien constructor is private for a good reason
                alienColony.addAll(alien.getChildren().stream()
                        .filter(child -> child.getName().equals(childName))
                        .collect(Collectors.toList()));
                parentFound = true;
                break;
            }
        }
        if (!parentFound) {
            LOGGER.error("Alien " + parentName + " does not exist");
            throw new AlienException("Alien " + parentName + " does not exist, Child not added.");
        }
    }

    /**
     * Method to retrieve and Alien
     *
     * @param name
     * @return
     */
    public String getAlien(String name) {
        checkColonyExists();
        for (Alien alien : alienColony) {
            if (alien.getName().equals(name)) {
                return alien.toString();
            }
        }
        LOGGER.error("Get Alien call failed for " + name);
        throw new AlienException("Alien " + name + " not found");
    }

    /**
     * Method to update an alien's name and/or home planet
     *
     * Fields are only updated for non-null arguments
     *
     * @param oldName
     * @param newName
     * @param newPlanet
     */
    public void updateAlien(String oldName, String newName, String newPlanet) {
        checkColonyExists();
        boolean alienFound = false;
        for (Alien alien : alienColony) {
            if (alien.getName().equals(oldName)) {
                if (newName != null) {
                    LOGGER.debug("Name changing from " + oldName + " to " + newName);
                    alien.setName(newName);
                }
                if (newPlanet != null) {
                    LOGGER.debug("Home planet of alien " + newName + " changing to " + newPlanet);
                    alien.setHomePlanet(newPlanet);
                }
                alienFound = true;
                break;
            }
        }
        if (!alienFound) {
            LOGGER.error("Update alien call failed as alien does not exist: " + oldName);
            throw new AlienException("Alien " + oldName + " not updated as they do not exist");
        }
    }

    /**
     * Method to delete an alien from the colony
     *
     * Deletes the Alien and removes it from its parent
     *
     * @param name
     */
    public void deleteAlien(String name) {
        checkColonyExists();
        // Check alien exists & remove if it does
        int oldSize = alienColony.size();
        List<Alien> newAlienColony = alienColony.stream()
                .filter(alien -> !alien.getName().equals(name))
                .collect(Collectors.toList());

        if (newAlienColony.size() == oldSize) {
            LOGGER.error("Delete call failed, alien does not exist: " + name);
            throw new AlienException("Alien " + name + " not removed as it does not exist.");
        }

        // Check to see if this is 'Adam' (first) alien, if it is, no need to remove it s a child
        List<Alien> adam = alienColony.stream()
                .filter(alien -> alien.getName().equals(name) && alien.getParent() == null)
                .collect(Collectors.toList());
        if (adam.size() > 0) {
            LOGGER.debug("Removing Adam alien: " + name);
            alienColony.removeAll(adam);
            return;
        }


        // Traverse the colony and find the parent
        alienColony.stream()
                .filter(alien -> alien.hasChild(name))
                .reduce((a, b) -> {
                    throw new IllegalStateException("This alien exists multiple times!");
                })
                .get()
                .removeChild(name);

        // Remove the alien
        alienColony = alienColony.stream()
                .filter(alien -> !alien.getName().equals(name))
                .collect(Collectors.toList());
    }

    /**
     * Private method to throw an exception if the colony has not been started.
     */
    private void checkColonyExists() {
        if (alienColony == null || alienColony.isEmpty()) {
            LOGGER.error("Colony not started");
            throw new AlienException("No aliens! Please start a new colony.");
        }
    }

    /**
     * Protected method for testing
     *
     * @return
     */
    protected List<Alien> getAlienColony() {
        return alienColony;
    }
}
