package ui;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Allow for the selection for the custom difficulty part for the CUSTOM difficulty.
 */
public class CustomDifficultySelector extends AbstractSelector {

    private int customWeighting;

    protected CustomDifficultySelector(BufferedReader bufferedReader) {
        super(bufferedReader);
    }

    /**
     * Takes the input as a string, converts it to an integer and then sets the weighting to this.
     *
     * @param input The user's input
     */

    @Override
    protected void handleInput(String input) throws NumberFormatException {
            setCustomWeighting(Integer.parseInt(input));
    }

    /**
     * Opens the Custom Difficulty menu.
     */
    @Override
    protected void select() {
        System.out.println("Please type in a number from 1 to 10 to change the difficulty: ");
        try {
            handleInput(getConsoleInput());
        } catch (IOException e) {
            System.out.println("Error: Unable to read input");
        }
    }

    /**
     * Sets the custom weighting, ensuring that it's between 1 and 10.
     *
     * @param customWeighting Custom weighting
     */
    private void setCustomWeighting(int customWeighting) {
        if (customWeighting > 0 && customWeighting < 11) {
            this.customWeighting = customWeighting;
        } else {
            System.out.println("Please make sure to type in a difficulty between 1 and 10! Please try again...");
            select();
        }
    }

    /**
     * Returns the custom weighting
     *
     * @return Custom weighting
     */
    public int getCustomWeighting() {
        return customWeighting;
    }
}
