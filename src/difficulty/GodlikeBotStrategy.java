package difficulty;

import main.Location;
import map.Map;
import player.BotPlayer;

/**
 * The hardest difficulty
 * The bot will always move towards the player, irrespective of whether the bot can see the player with LOOK or not.
 */
public class GodlikeBotStrategy implements BotStrategy {

    private BotPlayer botPlayer;

    public GodlikeBotStrategy(BotPlayer botPlayer) {
        this.botPlayer = botPlayer;
    }

    /**
     * No need for this method here since it's always moving towards the Human.
     *
     * @return null
     */
    @Override
    public Location.Direction nextDirection() {
        return null;
    }

    /**
     * Performs the next move which is always moving towards the player.
     *
     * @param map The map
     * @param humanLocation Human's main.Location
     */
    @Override
    public void nextMove(Map map, Location humanLocation) {
        botPlayer.move(map, botPlayer.getLocation().getDirectionTo(humanLocation));
    }
}
