package difficulty;

import main.DungeonsOfDoom;
import main.Location;
import map.Map;
import player.BotPlayer;

import java.util.Random;

/**
 * The Hard Bot Setting, which tracks down the player if they've been seen with 100% accuracy.
 */
public class HardBotStrategy implements BotStrategy {

    private int lookTurns;

    private BotPlayer botPlayer;

    private boolean seen;
    private Location currentHumanLocation;

    public HardBotStrategy(BotPlayer botPlayer) {
        this.botPlayer = botPlayer;
    }

    /**
     * Gets the next direction based on whether the player has been seen before.
     *
     * @return Their Next Direction
     */

    @Override
    public Location.Direction nextDirection() {
        if (seen) {
            return moveTowardsPlayer();
        } else {
            return move();
        }
    }

    /**
     * Gets a random direction.
     *
     * @return A random direction
     */
    private Location.Direction move() {
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

    /**
     * Finds the next direction with 100% accuracy.
     *
     * @return The move towards the player
     */
    private Location.Direction moveTowardsPlayer() {
        if (botPlayer.getLocation() != currentHumanLocation) {
            return botPlayer.getLocation().getDirectionTo(currentHumanLocation);
        } else {
            seen = false;
            return move();
        }
    }

    /**
     * Executes the next move, either LOOK every 4 turns or moves in the next direction.
     *
     * @param map The entire map
     * @param humanLocation Human main.Location
     */
    @Override
    public void nextMove(Map map, Location humanLocation) {
        if (lookTurns % 4 == 0) {
            if (botPlayer.getLocation().isNear(humanLocation, DungeonsOfDoom.LOOK_RADIUS)) {
                seen = true;
                currentHumanLocation = humanLocation;
            }
        } else {
            botPlayer.move(map, nextDirection());
        }
        lookTurns++;
    }
}
