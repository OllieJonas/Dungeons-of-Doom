package difficulty;

import main.Location;
import map.Map;

/**
 * Interface which dictates how to set out a Difficulty setting.
 *
 * Note: The name of this class is in reference to the Strategy design pattern, as opposed to the bot's strategy.
 * https://refactoring.guru/design-patterns/strategy
 */

public interface BotStrategy {

    /**
     * Finds the next direction that the bot is going to move in.
     *
     * @return Direction which the player is going to move
     */
    Location.Direction nextDirection();

    /**
     * Executes the next move based on the difficulty setting.
     * This could include just moving, or occasionally executing the LOOK command (a simulated version).
     *
     * @param map
     * @param humanLocation
     */
    void nextMove(Map map, Location humanLocation);

}