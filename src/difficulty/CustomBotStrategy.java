package difficulty;

import main.DungeonsOfDoom;
import main.Location;
import map.Map;
import player.BotPlayer;

import java.util.Random;

/**
 * A custom bot difficulty, which contains a weighted random. If the player has been seen, then the weighting is updated.
 * It then gets the predicted location from that and weights it to that predicted location. The higher the multiplier,
 * the more likely it is to move in the predicted direction.
 *
 * To work out probabilities, let us take the rather elementary and trivial example of a weighting of 3.
 *
 * - If the player hasn't been seen, then there is a 1/4 chance of either direction being called.
 *
 * - With a weighting of 3 being added, a random integer of bounds 4 * weighting is generated, so in this case
 *   between 0 and 11 (12).
 *
 * - The original random generated things stays the same (0,1,2,3 giving a 1/4 possibility of any given direction),
 *   however the default if it isn't any of those numbers is now going to return the predicted location.
 *
 * - This means that the probability of it now moving in any given predicted direction is:
 *   1/12 (New chance of getting the right direction out of 0,1,2,3) + (1 - 4/12) (Not hitting any of 0,1,2,3)
 *   = 9/12 = 3/4 chance of moving in the correct direction.
 *
 * - From this, we can deduce the following formula: 1 / 4n + (1 - (4 / 4n)), where n is the weighting multiplier.
 *
 * Note: The MEDIUM difficulty is created from this with a weighting of 3
 */

public class CustomBotStrategy implements BotStrategy {

    private int weighting = 1;
    private int weightingMultiplier;

    private int lookTurns = 0;

    private BotPlayer botPlayer;

    private Location currentHumanLocation;

    /**
     * Takes an instance of the player.BotPlayer to execute the commands as well as the weighting multiplier, w
     *
     * @param botPlayer The bot
     * @param weightingMultiplier The weighting you'd like to give it.
     */

    public CustomBotStrategy(BotPlayer botPlayer, int weightingMultiplier) {
        this.botPlayer = botPlayer;
        this.weightingMultiplier= weightingMultiplier;
    }

    /**
     * Works out the next move with LOOK command
     * @param map The map
     * @param humanLocation The humans location
     */
    @Override
    public void nextMove(Map map, Location humanLocation) {

        if (lookTurns % 4 == 0) { // Simulating the LOOK command every 4 turns
            if (botPlayer.getLocation().isNear(humanLocation, DungeonsOfDoom.LOOK_RADIUS)) {
                currentHumanLocation = humanLocation; // Updates location the bot thinks the human is at
                weighting = weightingMultiplier;
            }

        } else { // Move command
            botPlayer.executeMoveCommand(map, nextDirection());
        }
        lookTurns++;
    }

    /**
     * Moves towards the player with the probability being based on a weighting that's
     * pre-determined by the player at the setup phase.
     *
     * If the player hasn't been seen, then it will move randomly.
     *
     * @return The next direction the BOT will move in
     */
    @Override
    public Location.Direction nextDirection() {
        Random random = new Random();

        if (botPlayer.getLocation() == currentHumanLocation) {
            weighting = 1;
        }

        switch (random.nextInt(4 * weighting)) { // Random with weighting towards the predictedDirection
            case 0:
                return Location.Direction.N;
            case 1:
                return Location.Direction.E;
            case 2:
                return Location.Direction.S;
            case 3:
                return Location.Direction.W;
            default:  // This won't be called if the weighting is 1
                return botPlayer.getLocation().getDirectionTo(currentHumanLocation); // If it's anything else but the normal direction, then it's weighted.
        }
    }
}
