package player;

import main.Location;
import map.Map;

/**
 * Framework for a player.
 *
 * This class contains their location, as well as an implementation of MOVE as well as an executeMoveCommand, which
 * allows the human to print out SUCCESS or FAIL whereas the BOT doesn't need this.
 */
public abstract class Player {

    protected Location location;

    /**
     * Put in their starting position.
     *
     * @param location Their starting position
     */
    protected Player(Location location) {
        this.location = location;
    }

    /**
     * Allows for implementation of the MOVE command, mainly so that the human can print success or fail.
     *
     * @param map The entire map
     * @param direction The direction
     */
    protected abstract void executeMoveCommand(Map map, Location.Direction direction);

    /**
     * Actually moves the player from one tile to the next.
     *
     * @param map The entire map
     * @param direction The direction which you want the player to move
     * @return If the move was successful
     */
    public boolean move(Map map, Location.Direction direction) {
        if (map.isWalkable(location.adjacentLocation(direction))) {  // If you can walk on the adjacent location
            this.location = location.adjacentLocation(direction); // Then set their position to the adjacent location
            return true; // And it was a valid move
        } else {
            return false;
        }
    }

    /**
     * Gets their location.
     *
     * @return Their location
     */
    public Location getLocation() {
        return location;
    }
}
