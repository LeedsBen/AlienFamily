package com.example.alienfamily.alien;

import com.example.alienfamily.exception.AlienChildException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

    /**
     * Private boolean flag to indicate whether this alien has had one child.
     * (Aliens can be deleted, but an alpha alien can only have 2 children)
     */
    private boolean hasChildOne = false;

    /**
     * Private boolean flag to indicate whether this alien has had two children.
     * (Aliens can be deleted, but an alpha alien can only have 2 children)
     */
    private boolean hasChildTwo = false;

    /**
     * Constructor - throws exception, use initialise
     * @throws AlienChildException
     */
    public Alien() throws AlienChildException {
        throw new AlienChildException("Aliens cannot appear out of the ether!");
    }

    /**
     * Initialise method to create very first alien with no parent
     *
     * @param name
     * @param type
     * @param homePlanet
     * @return
     */
    public static Alien initialise(String name, AlienType type, Optional<String> homePlanet) {
        return new Alien(name, type, homePlanet);
    }

    /**
     * private constructor for creating very first alien
     *
     * @param name
     * @param type
     * @param homePlanet
     */
    private Alien(String name, AlienType type, Optional<String> homePlanet) {
        this.name = name;
        this.type = type;
        this.homePlanet = homePlanet;
    }

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

    /**
     * Method to return children.
     *
     * Returns all children or an empty list
     *
     * @return
     * @throws AlienChildException - if this alien is not an alpha
     */
    public List<Alien> getChildren() throws AlienChildException{
        if (!AlienType.ALPHA.equals(this.type)) {
            throw new AlienChildException("Only alphas have children");
        }
        List<Alien> children = new ArrayList<>();
        if (this.childOne != null) {
            children.add(this.childOne);
        }
        if (this.childTwo != null) {
            children.add(this.childTwo);
        }
        return children;
    }

    /**
     * Method to add a child. Only supported for Alpha type aliens.
     *
     * Adds as first child if this alien has never had a child.
     *
     * Adds as second child if this alien has already had one child.
     *
     * Otherwise throws exception
     *
     * @param name
     * @param type
     * @param homePlanet
     * @throws AlienChildException
     */
    public void addChild(String name, AlienType type, Optional<String> homePlanet) throws AlienChildException {
        if (!AlienType.ALPHA.equals(this.type)) {
            // Not an alpha, can't have children
            throw new AlienChildException("Only Alpha aliens can reproduce. " + this.name + " is of type " + this.type);
        }

        Alien child = new Alien(name, type, homePlanet);
        child.setParent(this);
        if (!hasChildOne) {
            // never had a first child, add as first child
            this.childOne = child;
            hasChildOne = true;
        } else if (!hasChildTwo) {
            // has a first child, but never a second, add as second child
            this.childTwo = child;
            hasChildTwo = true;
        } else {
            // This alien has already had two children
            throw new AlienChildException("Alien " + this.name + " has already had two children");
        }
    }

    /**
     * Protected method for testing, remove any kids
     */
    protected void removeKids() {
        this.childOne = null;
        this.childTwo = null;
    }
}
