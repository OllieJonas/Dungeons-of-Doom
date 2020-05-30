package ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Allows the user to select their chosen map.
 */
public class MapSelector extends AbstractSelector {

    private String fileName;

    private ArrayList<String> fileNames;

    /**
     * Starts the process of selecting a map.Map, by listing all .txt files in the directory.
     *
     * @param reader Console Input buffered reader.
     */
    public MapSelector(BufferedReader reader) {
        super(reader);

        System.out.println("Please select a map by either:");
        System.out.println("- Typing in the name of the .txt file");
        System.out.println("- Typing in the corresponding number");

        try {
            fileNames = filesInDir();
        } catch (IOException e) {
            System.out.println("Error: Unable to read files!");
            System.exit(1);
        }
    }

    /**
     * Selects the appropriate map based on input
     */
    @Override
    public void select()  {
        printNumberedMenu(fileNames, AbstractSelector.MENU_BORDER_CHAR, AbstractSelector.MENU_TEXT_LENGTH);
        try {
            handleInput(getConsoleInput());
        } catch (IOException e) {
            System.out.println("Error: Unable to read input");
        }
    }

    /**
     * Checks if the input is an integer or a string, and then deals with this accordingly.
     *
     * @param input The user's input
     */
    @Override
    protected void handleInput(String input) {
        try {
            handleNumberInput(Integer.parseInt(input));
        } catch(NumberFormatException e) {
            handleStringInput(input);
        }

        System.out.println("You have selected the map " + fileName);
    }

    /**
     * Handles selecting the file if the user types in an index from the list.
     *
     * @param fileIndex The number based on the list
     */
    private void handleNumberInput(int fileIndex) {
        if (fileIndex <= fileNames.size() && fileIndex > 0) {
            this.fileName = fileNames.get(fileIndex - 1);

        } else {
            System.out.println(String.format("The number %d isn't on the list! Please try again...", fileIndex));
            select();
        }
    }

    /**
     * Handles input based on the name of the file.
     *
     * @param fileName The input / name of the potential file
     */
    private void handleStringInput(String fileName) {
        if (fileNames.contains(fileName)) {
            this.fileName = fileName;
        } else {
            System.out.println(String.format("Error: Couldn't find the file with the name \"%s\"! Please try again...", fileName));
            select();
        }
    }

    /**
     * Gets a list of files in a direcory and adds them to an ArrayList
     *
     * @return ArrayList with all file names in the directory
     * @throws IOException When it can't add a file name to the list.
     */
    private ArrayList<String> filesInDir() throws IOException {
        ArrayList<String> files = new ArrayList<>();

        DirectoryStream<Path> paths = Files.newDirectoryStream(Paths.get("maps"));

        for (Path path : paths) {
            if (path.toString().endsWith(".txt")) {
                files.add(path.getFileName().toString());
            }
        }
        Collections.sort(files);
        return files;
    }

    /**
     * Gets the file name.
     *
     * @return File name
     */
    public String getFileName() {
        return fileName;
    }
}