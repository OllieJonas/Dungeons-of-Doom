package main;

/**
 * Used for storing and comparing different x and y coords on the map.
 */
public class Location {

    private int col;
    private int row;

    /**
     * List of the 4 directions for movement.
     */
    public enum Direction {
        N,
        E,
        S,
        W;
    }

    /**
     * Input the X and Y coords.
     *
     * @param col X Coords of the place on the map
     * @param row Y Coords of the place on the map
     */
    public Location(int col, int row) {
        this.col = col;
        this.row = row;
    }

    /**
     * Finds the location adjacent to the current location.
     *
     * @param direction The direction they'd like to be adjacent to.
     * @return The adjacent location based on the direction.
     */
    public Location adjacentLocation(Direction direction) {
        Location newLoc = new Location(col, row);
        switch(direction) {
            case N:
                newLoc.modifyRow(-1);
                break;

            case E:
                newLoc.modifyCol(1);
                break;

            case S:
                newLoc.modifyRow(1);
                break;
            case W:
                newLoc.modifyCol(-1);
                break;

            default:
                throw new IllegalArgumentException(String.format("Error: Unrecognised direction: %s!", direction));
        }
        return newLoc;
    }

    /**
     * Gets if the current location and the new location are within the set radius.
     *
     * @param location The location to test if they're near.
     * @param radius The radius for how close they are.
     * @return If they're near
     */
    public boolean isNear(Location location, int radius) {
        return Math.sqrt(Math.pow((row - location.getRow()), 2) + Math.pow(col - location.getCol(), 2)) <= radius;
    }

    /**
     * Finds the direction to a location from this location.
     *
     * @param location The new location
     * @return The closest direction
     */
    public Direction getDirectionTo(Location location) {
        Direction direction;

        int xAxis = col - location.getCol();
        int yAxis = row - location.getRow();

        if (Math.abs(yAxis) > Math.abs(xAxis)) {
            direction = yAxis > 0 ? Direction.N : Direction.S;
        } else {
            direction = xAxis > 0 ? Direction.W : Direction.E;
        }
        return direction;
    }

    /**
     * Checks if a location is the same as another.
     *
     * @param location The location to check
     * @return Whether they're the same
     */
    public boolean equals(Location location) {
        return col == location.getCol() && row == location.getRow();
    }

    /**
     * Checks if the location is out of bounds from the map
     * @param width Width of the entire map
     * @param height Height of the entire map
     * @return If the current location is in the map
     */
    public boolean outOfBounds(int width, int height) {
        return col < 0 || row < 0 || col >= width || row >= height;
    }

    /**
     * Prints out the location in the form of coords, used mainly for debugging.
     */
    public void print() {
        System.out.println(String.format("(%d, %d)", col, row));
    }

    /**
     * Modifies the column amount.
     *
     * @param amount The amount you want to modify row
     */
    private void modifyCol(int amount) {
        col += amount;
    }

    /**
     * Modifies the row amount.
     *
     * @param amount The amount you want to modify the row.
     */
    private void modifyRow(int amount) {
        row += amount;
    }

    /**
     * Gets the column (X axis).
     */
    public int getCol() {
        return col;
    }

    /**
     * Gets the row (Y axis).
     */
    public int getRow() {
        return row;
    }

}
