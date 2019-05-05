package com.example.alienfamily.alien;

import java.util.Optional;

/**
 * Object class to represent an alien being.
 *
 * An instance of this class is one alien
 */
public class Alien {

    /**
     * Name of the Alien, max 50 chars
     */
    private String name;

    /**
     * Type of the Alien, Alpha, Beta or Gamma
     */
    private AlienType type;

    /**
     * Home planet, max 50 chars
     */
    private Optional<String> homePlanet;

    /**
     * The parent of this alien.
     */
    private Alien parent;

    /**
     * The first child of this alien.
     *
     * (Alphas only)
     */
    private Alien childOne;

    /**
     * The second child of this alien.
     *
     * (Alphas only)
     */
    private Alien childTwo;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AlienType getType() {
        return type;
    }

    public void setType(AlienType type) {
        this.type = type;
    }

    public Optional<String> getHomePlanet() {
        return homePlanet;
    }

    public void setHomePlanet(Optional<String> homePlanet) {
        this.homePlanet = homePlanet;
    }

    public Alien getParent() {
        return parent;
    }

    public void setParent(Alien parent) {
        this.parent = parent;
    }

    public Alien getChildOne() {
        return childOne;
    }

    public void setChildOne(Alien childOne) {
        this.childOne = childOne;
    }

    public Alien getChildTwo() {
        return childTwo;
    }

    public void setChildTwo(Alien childTwo) {
        this.childTwo = childTwo;
    }
}
