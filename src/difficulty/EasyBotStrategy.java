package difficulty;

import main.Location;
import map.Map;
import player.BotPlayer;

import java.util.Random;

/**
 * The easiest of the bot difficulties.
 * It just moves in a random direction regardless, and doesn't use the LOOK command.
 */

public class EasyBotStrategy implements BotStrategy {

    private BotPlayer botPlayer;

    public EasyBotStrategy(BotPlayer botPlayer) {
        this.botPlayer = botPlayer;
    }

    /**
     * Executes the next move for the bot.
     * The next move is always a MOVE command.
     *
     * @param map The entire map
     * @param humanLocation The humans location
     */
    @Override
    public void nextMove(Map map, Location humanLocation) {
        botPlayer.executeMoveCommand(map, nextDirection());
    }

    /**
     * Finds the next direction for the bot to move in by using a random number generator.
     * This is a completely random move.
     *
     * @return A random direction
     */
    @Override
    public Location.Direction nextDirection() {
        Random random = new Random();

        switch(random.nextInt(4)) {
            case 0:
                return Location.Direction.N;
            case 1:
                return Location.Direction.E;
            case 2:
                return Location.Direction.S;
            case 3:
                return Location.Direction.W;
            default:
                throw new IllegalStateException("Should never be called");
        }
    }
}
