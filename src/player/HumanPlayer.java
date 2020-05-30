package player;

import main.Location;
import map.Map;

/**
 * The Human player.Player. This class is responsible for most of the commands that have something to do with the human.
 * These include: MOVE, PICKUP, QUIT/EXIT and GOLD.
 */
public class HumanPlayer extends Player {

    private int goldCollected;

    private final String FAIL_MSG = "FAIL";
    private final String SUCCESS_MSG = "SUCCESS";

    /**
     * Creates a new player
     *
     * @param location Their starting location.
     */
    public HumanPlayer(Location location) {
        super(location);
        this.goldCollected = 0;
    }

    /**
     * Executes the MOVE command, - moving the player around the map based on the direction they input.
     *
     * @param map The entire map
     * @param direction The direction they'd like to move in
     */
    @Override
    public void executeMoveCommand(Map map, Location.Direction direction) {
        if (super.move(map, direction)) {
            System.out.println(SUCCESS_MSG);
        } else {
            System.out.println(FAIL_MSG);
        }
    }

    /**
     * Executes the PICKUP command - picks up any gold on the tile the player is standing on, if there is any there.
     *
     * @param map The entire map
     */
    public void executePickupCommand(Map map) {
        if (map.pickupGoldAt(location)) {
            goldCollected++;
            System.out.println(SUCCESS_MSG + ". Gold owned: " + goldCollected);
        } else {
            System.out.println(FAIL_MSG);
        }
    }

    /**
     * Executes the QUIT / EXIT command - Quits the game, if they have enough gold and are standing on the exit square
     * then they win, otherwise they lose. The game quits regardless of whether they have met the pre-conditions to win.
     *
     * @param map The map
     * @return If the player has valid exit conditions.
     */
    public boolean executeQuitCommand(Map map) {
        return map.getTokenAtLocation(location).isExit() && map.getGoldRequired() <= goldCollected;
    }

    /**
     * Executes the GOLD command - Prints out how much gold the player has.
     */
    public void executeGoldCommand() {
        System.out.println("Gold owned: " + goldCollected);
    }
}