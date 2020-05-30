package map;

import main.Token;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Parses in the map from a text file.
 *
 * This class works by splitting the metadata and the map into two separate parts, then dealing with them
 * appropriately. From this, it then has a .build() method which returns a generated map.Map from the parsed file.
 */
public class MapParser {

    private String fileName;

    private int goldRequired;
    private String name;

    private Token[][] mapArr;
    private int width;
    private int height;

    public MapParser(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Parses the map and puts the information into the appropriate fields.
     * @return The updated map.Map Parser.
     */
    public MapParser parse() {
        try {

            BufferedReader bufferedReader = new BufferedReader(new FileReader(new File("maps", fileName)));
            ArrayList<String> file = loadFile(bufferedReader);

            // Split the file into the metadata (name, gold) and the rest of the map.
            List<String> metadataList = file.subList(0, 2);
            List<String> map = file.subList(2, file.size());

            this.name = parseMapName(metadataList.get(0));
            this.goldRequired = parseGoldRequired(metadataList.get(1));

            this.height = parseHeight(map);
            this.width = parseWidth(map); // Sets the width as the length of the first line.
            this.mapArr = parseMap(map);
        } catch (IOException e) {
            System.out.println("Error: Unable to parse map");
        }
        return this;
    }

    /**
     * Creates a new map based on the fields in this class.
     * @return A new map using the fields in this class
     */
    public Map load() {
        return new Map(name, goldRequired, width, height, mapArr);
    }

    /**
     * Takes the file and adds the entire thing into an ArrayList.
     *
     * @param bufferedReader The reader
     * @return An ArrayList of everything in the map.
     * @throws IOException If unable to read the file
     */
    private ArrayList<String> loadFile(BufferedReader bufferedReader) throws IOException {
        ArrayList<String> fileList = new ArrayList<>();

        while (bufferedReader.ready()) {
            fileList.add(bufferedReader.readLine()); // Adds entire file to an ArrayList
        }
        return fileList;
    }

    /**
     * Takes the map part, then performs rudimentary checks to see if it's a valid map.
     *
     * @param map The map part of the file as a list
     * @return A token array
     */
    private Token[][] parseMap(List<String> map) {
        Token[][] mapArr = new Token[height][width];
        int goldCount = 0;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                String line = map.get(y);

                // One line that has a length that isn't equal to the first line
                if (line.length() != width) {
                    System.out.println(String.format("Error: Invalid line length %d on line %d!", x, y));
                    System.exit(1);
                } else {
                        mapArr[y][x] = new Token(map.get(y).charAt(x));

                        if (mapArr[y][x].isGold()) {
                            goldCount++; // Checking that it is possible to collect enough gold.
                        }
                }
            }
        }
        if (goldCount < goldRequired) {
            System.out.println("Error; Not enough gold in the map based on the gold required specified!");
            System.exit(1);
        }

        if (!hasValidEdges(mapArr)) {
            System.out.println("Error: This map doesn't have valid edges!");
            System.exit(1);
        }
        return mapArr;
    }

    /**
     * Gets if a map has wall tokens all around.
     *
     * @param map The main.Token Array
     * @return If it has valid edges
     */
    private boolean hasValidEdges(Token[][] map) {
        boolean hasValidEdges = true;
        for (int col = 0; col < width; col++) {
            if (!map[0][col].isWall() || !map[height - 1][col].isWall()) {
                hasValidEdges = false;
                break;
            }
        }

        for (int row = 0; row < height; row++) {
            if (!map[row][0].isWall() || !map[row][width - 1].isWall()) {
                hasValidEdges = false;
                break;
            }
        }
        return hasValidEdges;
    }

    /**
     * Reads and parses the width of the map.
     *
     * Note: The width of the map is determined by the length of the first line. This means that the map has to be the
     * same width based on this first line.
     *
     * @param map The map as a list
     * @return The width of the map
     */
    private int parseWidth(List<String> map) {
        if (map.isEmpty()) {
            System.out.println("Error: Unable to find map in text file!");
            System.exit(1);
        }
        return map.get(0).length();
    }

    /**
     * Reads and parses the height of the map.
     *
     * @param map The map as a list
     * @return The height of the map
     */
    private int parseHeight(List<String> map) {
        return map.size();
    }

    /**
     * Reads and parses what should be line 1 of the text file which details the map name.
     *
     * @param line : The line in the text file which contains the information concerning the map name (Line 1)
     * @return : The map name
     */
    private String parseMapName(String line) {
        return line.substring(line.indexOf(" ") + 1).trim();
    }


    /**
     * Reads and parses what should be Line 2 of the text file which details how much gold the player is required
     * to collect in order to be able to trigger the Exit.
     *
     * @param line : The line which contains the information concerning the number of gold required to pass (Line 2)
     * @return The total gold required to pass the level
     */
    private int parseGoldRequired(String line) {
        int gold = 0;
        String goldString = line.substring(line.indexOf(" ") + 1);

        try {
            gold = Integer.parseInt(goldString);
        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid gold amount " + goldString + ".");
            System.exit(1);
        }

        if (gold < 0) {
            System.out.println("Error: Cannot have negative gold!");
            System.exit(1);
        }
        return gold;
    }
}
