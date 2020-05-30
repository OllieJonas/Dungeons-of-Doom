package map;

import main.Token;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapBuilder {

    private Token[][] mapArr;
    private int goldRequired;
    private String name;
    private int width;
    private int height;

    private int filledSpace;

    public MapBuilder(String fileName) {
        try (
                BufferedReader bufferedReader = new BufferedReader(new FileReader(new File("maps", fileName)))
        ) {

            ArrayList<String> file = loadFile(bufferedReader);

            List<String> metadataList = file.subList(0, 2);
            List<String> map = file.subList(2, file.size());

            parseMetadata(metadataList);

            setDimensions(map);
            mapArr = parseMap(map);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map build() {
        return new Map(name, goldRequired, width, height, mapArr);
    }

    private ArrayList<String> loadFile(BufferedReader bufferedReader) throws IOException {
        ArrayList<String> fileList = new ArrayList<>();

        while (bufferedReader.ready()) {
            fileList.add(bufferedReader.readLine());
        }

        return fileList;
    }

    private void setDimensions(List<String> map) {
        int maxWidth = 0;

        for (String next : map) {
            if (next.length() >= maxWidth) {
                maxWidth = next.length();
            }
        }
        this.width = maxWidth;
        this.height = map.size();
    }

    private void parseMetadata(List<String> arrList) {
        name = parseMapName(arrList.get(0));
        goldRequired = parseGoldRequired(arrList.get(1));
    }


    private Token[][] parseMap(List<String> file) {
        Token[][] mapArr = new Token[height][width];

        if (file.isEmpty()) {
            System.out.println("Error: Unable to find map in text file!");
            System.exit(1);
        } else if (!hasValidEdges(file)) {
            System.out.println("Error: Doesn't have valid sides!");
            System.exit(1);
        }

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (x >= file.get(y).length()) {  // If map has uneven sides, fill out right side with walls.
                    mapArr[y][x] = new Token('#');
                    filledSpace++;  // Keeps track of extra rows being added for printing purposes
                } else {
                    char token = file.get(y).charAt(x);

                    if (!isValidToken(token)) {
                        System.out.println("Error: Invalid token \'" + token + "\' on map at coordinates: (" + x + ", " + y + ")!");
                        System.exit(1);
                    } else {
                        mapArr[y][x] = new Token(token);
                    }
                }
            }
        }
        return mapArr;
    }

    private boolean hasValidEdges(List<String> map) {
        boolean isValidMap = true;

        for (int i = 0; i < map.size(); i++) {

            if (i == 0 || i == map.size() - 1) {  // Top or bottom line of map file must all be Wall token
                for (char tokens : map.get(i).toCharArray()) {
                    if (tokens != '#') {
                        isValidMap = false;
                        break;
                    }
                }
            } else {
                if (map.get(i).charAt(0) != '#' ||
                        map.get(i).charAt(width - 1) != '#') {  // First and last char need to be Wall tokens for each line
                    isValidMap = false;
                }
            }
        }

        return isValidMap;
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

    private boolean isValidToken(char token) {
        return token == '#' || token == '.' || token == 'E' || token == 'G';
    }
}
