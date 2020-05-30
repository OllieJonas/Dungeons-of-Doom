package map;

import main.Location;
import main.Token;

import java.util.Random;

/**
 * Contains the map for the game, as well as the LOOK command.
 */
public class Map {

    private String name;
    private int width;
    private int height;

    private Token[][] mapArr;
    private int goldRequired;

    /**
     * Builds the map.
     *
     * @param name The maps name
     * @param goldRequired The amount of gold required to win
     * @param width The width of the map
     * @param height The height of the map
     * @param mapArr The token array for the map
     */
    public Map(String name, int goldRequired, int width, int height, Token[][] mapArr) {
        this.name = name;
        this.goldRequired = goldRequired;
        this.width = width;
        this.height = height;

        this.mapArr = mapArr;
    }


    /**
     * Finds the character at the location at the x,y coordinates.
     *
     * @param location Coordinates on the map (formatted like x,y)
     * @return The char at the location if it exists, else return a wall.
     */
    public final Token getTokenAtLocation(Location location){
        int x = location.getCol();
        int y = location.getRow();
        return (x > 0 || y > 0 || x < width || y < height) ? mapArr[y][x] : new Token('#');
    }

    /**
     * Executes the LOOK Command for the player.
     * Prints out the subsection of the map, whilst also overlaying both players location, if it applies.
     *
     * @param humanLocation Human Players main.Location
     * @param botLocation Bot Players main.Location
     * @param radius Radius of the subsection of the map
     */
    public void executeLookCommand(Location humanLocation, Location botLocation, int radius) {
        int currentPlayerX = humanLocation.getCol();
        int currentPlayerY = humanLocation.getRow();

        for (int y = 0; y < radius; y++) {
            for (int x = 0; x < radius; x++) {

                // Gets radius around player for both X and Y
                int adjustedX = currentPlayerX - (radius / 2) + x;
                int adjustedY = currentPlayerY - (radius / 2) + y;

                Location adjustedLocation = new Location(adjustedX, adjustedY);

                if (adjustedLocation.outOfBounds(width, height)) {
                    System.out.print('#');  // If player is at the edge of the map, add more wall chars

                } else if (adjustedLocation.equals(humanLocation)) {  // Display human player on map
                    System.out.print('P');

                } else if (adjustedLocation.equals(botLocation)) { // Display bot player on map
                    System.out.print('B');

                } else {
                    System.out.print(mapArr[adjustedY][adjustedX].getChar());  // Else add the appropriate char
                }
                System.out.print(" "); // Creates nice spacing
            }
            System.out.println();
        }
    }

    /**
     * Prints out the entire map. Used mainly for debugging.
     * Overlays the bot and human over the map.
     *
     * @param humanLocation The humans location
     * @param botLocation The bots location
     */
    public void print(Location humanLocation, Location botLocation) {
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                Location location = new Location(col, row);

                if (humanLocation.equals(location)) {
                    System.out.print('P');

                } else if (botLocation.equals(location)) {
                    System.out.print('B');

                } else {
                    System.out.print(mapArr[row][col].getChar());
                }
                System.out.print(" "); // Creates nice spacing
            }
            System.out.println();
        }
    }

    /**
     * Executes the HELLO command which prints out how much gold is required on the map to win.
     * Note: This does not print the remaining gold, but simply the total amount of gold that's required.
     */
    public void executeHelloCommand() {
        System.out.println("Gold to win: " + goldRequired);
    }

    /**
     * Checks if you can walk on the next location (ie. it's not a wall).
     *
     * @param location The tile you want to check.
     * @return If it's a wall
     */

    public boolean isWalkable(Location location) {
        return mapArr[location.getRow()][location.getCol()].isWalkable();
    }

    /**
     * Checks if the player can pickup gold at this location, then removes it from the map if they can.
     *
     * @param humanLocation The location which the gold should be picked up at
     * @return If the player picked up gold
     */
    public boolean pickupGoldAt(Location humanLocation) {
        if (getTokenAtLocation(humanLocation).canPickup()) {
            replaceCharAtLocation(humanLocation, new Token('.'));
            return true;
        } else {
            return false;
        }
    }


    /**
     * Replaces a char at the specified location.
     *
     * @param location : main.Location of the char
     * @param token : The token you want to replace it with
     */
    public void replaceCharAtLocation(Location location, Token token) {
        mapArr[location.getRow()][location.getCol()] = token;
    }

    /**
     * Generates a random spawn location from the map size and performs recursion if doesn't return a valid location.
     * @return Spawn main.Location
     */
    public final Location generateSpawnLocation() {
        Random random = new Random();

        int randomX = random.nextInt(width); // Starting X
        int randomY = random.nextInt(height); // Starting Y

        Location location = new Location(randomX, randomY);

        if (getTokenAtLocation(location).canSpawnOn()) {
            return location;
        } else {
            return generateSpawnLocation();  // Recurse if invalid spawn location
        }
    }

    /**
     * Gets total gold required as defined in the metadata of the map file.
     *
     * @return Gold Required
     */
    public int getGoldRequired() {
        return goldRequired;
    }

    /**
     * Gets the map name as defined in the metadata of the map file.
     *
     * @return map.Map Name
     */
    public String getName() {
        return name;
    }
}


