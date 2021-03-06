package com.example.alienfamily.controller;

import com.example.alienfamily.alien.AlienType;
import com.example.alienfamily.service.AlienService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller to expose Alien Colony functionality as an HTTP API
 */
@RestController
public class AlienController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AlienController.class);

    /**
     * Alien Service
     */
    private AlienService alienService;

    /**
     * Constructor for Spring autowiring
     *
     * @param alienService
     */
    public AlienController(AlienService alienService) {
        this.alienService = alienService;
    }

    /**
     * @see com.example.alienfamily.service.AlienService#startColony(String, String)
     */
    @PostMapping("/aliencolony/start")
    public ResponseEntity<Void> start(@RequestParam String name, @RequestParam String homePlanet) {
        LOGGER.info("Colony starting with alien " + name);
        alienService.startColony(name, homePlanet);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    /**
     * @see com.example.alienfamily.service.AlienService#addAlien(String, String, AlienType, String)
     */
    @PostMapping("/aliencolony/addAlien")
    public ResponseEntity<Void> addAlien(@RequestParam String parentName, @RequestParam String childName, @RequestParam AlienType type, @RequestParam String homePlanet) {
        LOGGER.info("Adding alien " + childName + " as child of " + parentName);
        alienService.addAlien(parentName, childName, type, homePlanet);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    /**
     * @see com.example.alienfamily.service.AlienService#getAlien(String)
     */
    @GetMapping("/aliencolony/getAlien")
    public String getAlien(@RequestParam String name) {
        LOGGER.info("Getting alien " + name);
        return alienService.getAlien(name);
    }

    /**
     * @see com.example.alienfamily.service.AlienService#updateAlien(String, String, String)
     */
    @PostMapping("/aliencolony/updateAlien")
    public ResponseEntity<Void> updateAlien(@RequestParam String oldName, @RequestParam String newName, @RequestParam String newPlanet) {
        LOGGER.info("Updating alien " + oldName + " with new name " + newName + " and new planet " + newPlanet);
        alienService.updateAlien(oldName, newName, newPlanet);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    /**
     * @see com.example.alienfamily.service.AlienService#deleteAlien(String)
     */
    @DeleteMapping("/aliencolony/deleteAlien")
    public ResponseEntity<Void> deleteAlien(@RequestParam String name) {
        LOGGER.info("Deleting alien " + name);
        alienService.deleteAlien(name);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }
}
