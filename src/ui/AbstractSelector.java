package ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

/**
 *This class contains a framework for building Selector Menus for the Console, for example the map.Map Selector.
 */
public abstract class AbstractSelector {

    /**
     * These are the values used across the program when using a selector, although it is possible to change these
     * in the methods.
     */
    protected static final int MENU_TEXT_LENGTH = 100;
    protected static final char MENU_BORDER_CHAR = '=';

    protected BufferedReader bufferedReader;

    protected AbstractSelector(BufferedReader bufferedReader) {
        this.bufferedReader = bufferedReader;
    }

    /**
     * Take the user input from getConsoleInput, then handles it and sets the appropriate values in the selector.
     *
     * @param input The user's input
     */
    protected abstract void handleInput(String input);

    /**
     * Opens the menu to allow users to select the various settings in the menu.
     */
    protected abstract void select();

    /**
     * Gets console input from the user for the menu.
     *
     * @return The users input
     * @throws IOException If the input cannot be read
     */
    protected String getConsoleInput() throws IOException {
        return bufferedReader.readLine();
    }

    /**
     * Formats text and border characters into a displayable menu.
     * It takes the border character and adds a header and footer with a size of the length,
     * From there it prints out a centred and numbered version of the list inputted.
     *
     * @param text The text in the middle
     * @param borderChar The character that surrounds the centre text
     * @param length The length of each string
     */
    protected void printNumberedMenu(List<String> text, char borderChar, int length) {
        String border = generateBorder(borderChar, length);
        System.out.println(border);

        for (int i = 0; i < text.size(); i++) {
            System.out.println(centre((i + 1) + ". " + text.get(i), length)); // Prints centred version with numberings. EG: EASY -> 1. EASY (centred)
        }

        System.out.println(border);
    }

    /**
     * Takes a string input and adds spaces before it until it's centred in the middle of the size.
     * It does this by iterating, adding spaces until it reaches the centre offset by the string, then adds the offset to the string.
     * Used for the printMenu.
     *
     * @param str Input string to centre
     * @param size The total size of the entire line.
     * @return A centred string based on the size
     */
    private String centre(String str, int size) {
        return " ".repeat((size - str.length()) / 2) + str;
    }

    /**
     * Generates a border around the text.
     * Used for printMenu.
     *
     * @param token The char you want the border to be
     * @param size The size of the border
     * @return The token repeated size times.
     */
    private String generateBorder(char token, int size) {
        return String.valueOf(token).repeat(size); // Keeps repeating
    }
}
