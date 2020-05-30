package player;

import difficulty.BotStrategy;
import main.Location;
import map.Map;

/**
 * This class creates and handles the BOT player. It calls its next turn based on the Bot Difficulty assigned to it
 * and also contains basic implementation of the MOVE command (so it doesn't print out SUCCESS/FAIL)
 */
public class BotPlayer extends Player {

    private BotStrategy botStrategy;

    /**
     * Creates a BOT, with their starting position.
     *
     * @param location Their starting position.
     */
    public BotPlayer(Location location) {
        super(location);
    }

    /**
     * Calls the bots next turn based on the difficulty.BotStrategy.
     *
     * @param map The entire map
     * @param humanLocation The humans location
     */
    public void callTurn(Map map, Location humanLocation) {
        botStrategy.nextMove(map, humanLocation);
    }

    /**
     * Runs the MOVE command.
     *
     * @param map The map
     * @param direction The direction
     */
    @Override
    public void executeMoveCommand(Map map, Location.Direction direction) {
        move(map, direction);
    }

    /**
     * Sets the difficulty for the bot.
     *
     * @param botStrategy The difficulty
     */
    public void setDifficulty(BotStrategy botStrategy) {
        this.botStrategy = botStrategy;
    }
}
