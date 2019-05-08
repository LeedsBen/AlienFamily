# AlienFamily

This application models an Alien Colony.

An Alien has the following features:
Business rules:
 - A name no longer than 50 characters (mandatory)
 - A type of either ALPHA, BETA or GAMMA (mandatory)
 - A home planet (max 50 chars)
 - Alpha aliens can have up to two children in their lifetime
Technical constraints / common sense rules:
 - An alien must have a unique name (future enhancement: introduce UUIDs so aliens can share names)
 - Aliens cannot simply be created, all aliens must be a child of an existing parent
 - A special initialise operation is available to create the first alien in a colony.

A service is exposed in order to support basic CRUD operations on a colony
 - A colony is initialised, which creates the first alien, this is the only alien permitted not to have a parent
 - Aliens can be added as children of existing aliens
 - An alien's name and home planet can be updated
 - Aliens can be deleted from the colony. If an alien is deleted, its reference from its parent is also deleted, but any children retain a record of them
 - Data about an alien can be retrieved passing in its name as a reference
