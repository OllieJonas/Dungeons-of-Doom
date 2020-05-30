package main;

import difficulty.*;
import map.Map;
import map.MapParser;
import player.BotPlayer;
import player.HumanPlayer;
import ui.BotDifficultySelector;
import ui.MapSelector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

import static ui.BotDifficultySelector.*;
import static ui.BotDifficultySelector.Difficulty.*;
import static ui.BotDifficultySelector.Difficulty.EASY;

/**
 * Main class for the game. It loads the map and plays the game. Equivalent to the GameLogic class provided by Bath.
 */

public class DungeonsOfDoom {

    /**
     * The default error message for a Syntax Move Error
     */
    private static final String SYNTAX_MOVE_ERROR = "Syntax Error: Syntax should be MOVE <N / E / S / W>";
    /**
     * Radius when executing the LOOK command
     */
    public static final int LOOK_RADIUS = 5;



    private static String dodAscii = " ______            _        _______  _______  _______  _        _______    _______  _______    ______   _______  _______  _______ \n"+
            "(  __  \\ |\\     /|( (    /|(  ____ \\(  ____ \\(  ___  )( (    /|(  ____ \\  (  ___  )(  ____ \\  (  __  \\ (  ___  )(  ___  )(       )\n"+
            "| (  \\  )| )   ( ||  \\  ( || (    \\/| (    \\/| (   ) ||  \\  ( || (    \\/  | (   ) || (    \\/  | (  \\  )| (   ) || (   ) || () () |\n"+
            "| |   ) || |   | ||   \\ | || |      | (__    | |   | ||   \\ | || (_____   | |   | || (__      | |   ) || |   | || |   | || || || |\n"+
            "| |   | || |   | || (\\ \\) || | ____ |  __)   | |   | || (\\ \\) |(_____  )  | |   | ||  __)     | |   | || |   | || |   | || |(_)| |\n"+
            "| |   ) || |   | || | \\   || | \\_  )| (      | |   | || | \\   |      ) |  | |   | || (        | |   ) || |   | || |   | || |   | |\n"+
            "| (__/  )| (___) || )  \\  || (___) || (____/\\| (___) || )  \\  |/\\____) |  | (___) || )        | (__/  )| (___) || (___) || )   ( |\n"+
            "(______/ (_______)|/    )_)(_______)(_______/(_______)|/    )_)\\_______)  (_______)|/         (______/ (_______)(_______)|/     \\|\n"+
            "                                                                                                                                  \n"+
            "\n";

    /**
     * Main method for the game.
     *
     * @param args
     */
    public static void main(String[] args) {
        System.out.println(dodAscii);

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        new DungeonsOfDoom(reader);  // Initialise game
    }

    /**
     * Main instance of the game including setting bot difficulty, loading the map and the execution of the commands.
     *
     * @param reader Input reader for console
     */
    private DungeonsOfDoom(BufferedReader reader) {

        MapSelector mapSelector = new MapSelector(reader);
        mapSelector.select();

        String fileName = mapSelector.getFileName();

        Map map = new MapParser(fileName)
                .parse()
                .load(); // Parses then makes a new map object

        // Initialise the players
        HumanPlayer human = new HumanPlayer(map.generateSpawnLocation());
        BotPlayer bot = new BotPlayer(map.generateSpawnLocation());

        BotDifficultySelector difficultySelector = new BotDifficultySelector(reader);
        difficultySelector.select(); // Select a bot difficulty

        bot.setDifficulty(selectDifficulty(difficultySelector, bot));


        // Keep repeating until the player wins or loses
        System.out.println("You need " + map.getGoldRequired() + " gold to escape this dungeon...");
        System.out.println("Now be careful as you enter the " + map.getName().replaceAll("_", " ").toUpperCase().trim() + "!");

        boolean gameEnded = false;
        boolean gameWon = false;
        boolean caught = false;

        // Main execution of the game

        while (!caught && !gameEnded) {

            try {

                String[] input = reader.readLine().split(" "); // Takes the input and splits it by whitespace.
                String command = input[0].toUpperCase().trim(); // The first bit separated by whitespace is the command
                String[] args = Arrays.copyOfRange(input, 1, input.length); // Takes the rest of the input as the arguments.

                switch (command) {
                    case "GOLD":
                        human.executeGoldCommand();
                        break;

                    case "PICKUP":
                        human.executePickupCommand(map);
                        break;

                    case "QUIT":
                    case "EXIT": // The spec mentioned both QUIT and EXIT so I thought I'd put both in just in case.
                        gameWon = human.executeQuitCommand(map);
                        gameEnded = true; // The game ends whenever the user types QUIT/EXIT, regardless of whether the player should win or not
                        break;

                    case "MOVE":
                        if (args.length == 1) { // Only needs one argument (N,E,S,W)
                            try {
                                Location.Direction direction = Location.Direction.valueOf(args[0].toUpperCase()); // Converts string into enum if possible
                                human.executeMoveCommand(map, direction);
                            } catch (IllegalArgumentException e) {
                                System.out.println(SYNTAX_MOVE_ERROR); // If not N/E/S/W
                            }

                        } else {
                            System.out.println(SYNTAX_MOVE_ERROR);
                        }
                        break;

                    case "LOOK":
                        map.executeLookCommand(human.getLocation(), bot.getLocation(), LOOK_RADIUS);
                        break;

                    case "HELLO":
                        map.executeHelloCommand();
                        break;

                    default:
                        invalidCommand(command);
                }

                bot.callTurn(map, human.getLocation()); // Now it's the bots turn.
                caught = isCaught(human.getLocation(), bot.getLocation()); // If the bot has the same location

            } catch (IOException e) {
                System.out.println("Error: Unable to read command");
            }
        }

        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (gameWon) {
            // Game Won
            System.out.println("WIN");
            System.out.println("Congratulations! You escaped the bot!");
        } else {
            // Game Loss
            System.out.println("LOSE");
        }
    }

    /**
     * Takes the Selected Difficulty from the Selection menu and selects the appropriate class to run.
     *
     * @param selector The Difficulty Selector
     * @param bot The player.BotPlayer
     * @return The Selected Bot Strategy
     */
    private BotStrategy selectDifficulty(BotDifficultySelector selector, BotPlayer bot) {
        System.out.println("You have selected the " + selector.getSelectedDifficulty().toString() + " difficulty!");

        switch (selector.getSelectedDifficulty()) {
            case EASY:
                return new EasyBotStrategy(bot);
            case MEDIUM:
                return new CustomBotStrategy(bot, 3); // Medium is just the custom with a random weighting of 3.
            case HARD:
                return new HardBotStrategy(bot);
            case GODLIKE:
                return new GodlikeBotStrategy(bot);
            case CUSTOM:
                System.out.println("Custom difficulty level is set at " + selector.getCustomWeighting());
                return new CustomBotStrategy(bot, selector.getCustomWeighting());
            default:
                throw new IllegalStateException("Difficulty doesn't exist, shouldn't be called");
        }
    }

    /**
     * Prints out to the console that what the human typed was an invalid command.
     *
     * @param command The string they typed
     */
    private void invalidCommand(String command) {
        System.out.println(String.format("Invalid Command %s!", command));
    }

    /**
     * Gets if the player has been caught by the bot.
     *
     * @param humanLocation Humans current location
     * @param botLocation Bots current location
     * @return If the bot has the same location as the human.
     */
    private boolean isCaught(Location humanLocation, Location botLocation) {
        return botLocation.equals(humanLocation);
    }
}