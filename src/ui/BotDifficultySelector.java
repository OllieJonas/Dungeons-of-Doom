package ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Allows you to choose and change the difficulty of the bot before the game starts.
 */
public class BotDifficultySelector extends AbstractSelector {

    /**
     * Stores the descriptions of each difficulty with a small explanation.
     */
    private static ArrayList<String> difficultyDescriptions = new ArrayList<>();

    private Difficulty selectedDifficulty;
    private int customWeighting = 0;

    /**
     * The difficulties as an enum
     */
    public enum Difficulty {
        EASY,
        MEDIUM,
        HARD,
        GODLIKE,
        CUSTOM;
    }

    static {
        // Difficulty Descriptions
        difficultyDescriptions.add("EASY - Randomly moves around the map.");
        difficultyDescriptions.add("MEDIUM - Attempts to chase you when executing the LOOK command.");
        difficultyDescriptions.add("HARD - The bot will chase you with 100% accuracy when it finds you using the LOOK command.");
        difficultyDescriptions.add("GODLIKE - The bot knows where you are at all times and tries to chase you.");
        difficultyDescriptions.add("CUSTOM - Customise the difficulty level for you!");
    }

    /**
     * Gets the BufferedReader through and prints out the appropriate header messages to choose a difficulty.
     *
     * @param reader The console input
     */
    public BotDifficultySelector(BufferedReader reader) {
        super(reader);

        System.out.println("Please select a bot difficulty by typing in the corresponding number: ");
    }

    /**
     * Takes in console input and uses this to assign the appropriate difficulty enum,
     * which is then handled in the DoD class.
     */
    @Override
    public void select() {

        printNumberedMenu(difficultyDescriptions, AbstractSelector.MENU_BORDER_CHAR, AbstractSelector.MENU_TEXT_LENGTH);
        try {
            handleInput(getConsoleInput());
        } catch (NumberFormatException e) {
            System.out.println("Error: Please input a number!");
            select();
        } catch (IOException e) {
            System.out.println("Error: Unable to read input");
        }
    }

    /**
     * Takes the input (should be an integer between 1 and the number of difficulties) and returns the appropriate
     * difficulty based on their selection.
     *
     * @param input The user input
     */
    @Override
    protected void handleInput(String input) throws NumberFormatException {
        switch (Integer.parseInt(input)) {
            case 1:
                this.selectedDifficulty = Difficulty.EASY;
                break;

            case 2:
                this.selectedDifficulty = Difficulty.MEDIUM;
                break;

            case 3:
                this.selectedDifficulty = Difficulty.HARD;
                break;

            case 4:
                this.selectedDifficulty = Difficulty.GODLIKE;
                break;

            case 5:
                CustomDifficultySelector customSel = new CustomDifficultySelector(bufferedReader);
                customSel.select();

                this.customWeighting = customSel.getCustomWeighting(); // Assigns the number that the user chose to the weighting.
                this.selectedDifficulty = Difficulty.CUSTOM;
                break;

            default:
                System.out.println("Error: Unable to read difficulty! Please select again...");
                select();
        }
    }

    /**
     * Gets the difficulty that the user chooses after the input
     *
     * @return The selected difficulty
     */
    public Difficulty getSelectedDifficulty() {
        return selectedDifficulty;
    }

    /**
     * Gets the weighting multiplier for custom, if applicable.
     *
     * @return Custom Weighting set in the menu.
     */
    public int getCustomWeighting() {
        return customWeighting;
    }
}
