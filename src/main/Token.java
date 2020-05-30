package main;

/**
 * Handles tiles on the map and their various properties.
 *
 * This class is mainly here for expandability, and makes it easier for future developers to easily add new features
 * to the game (For example: Emeralds to collect?)
 */
public class Token {

    private char character;

    /**
     * Makes it clear to see what is represented by what character.
     */
    private enum TokenType {
        WALL('#'),
        GOLD('G'),
        FLOOR('.'),
        EXIT('E');

        private char character;

        TokenType(char character) {
            this.character = character;
        }

        /**
         * Returns the character associated with the TokenType.
         *
         * @return Character associated with the TokenType
         */
        public char getChar() {
            return character;
        }


    }
    /**
     * Default constructor, assigns a char to the main.Token.
     *
     * @param character The represented char on the map
     */
    public Token(char character) {
        setChar(character);
    }

    /**
     * Gets if you can walk on the tile (so not a Wall).
     *
     * @return If the char isn't a wall
     */
    public boolean isWalkable() {
        return character != TokenType.WALL.getChar();
    }

    /**
     * Gets if the human can pickup this token (so GOLD).
     *
     * @return If the token can be picked up,
     */
    public boolean canPickup() {
        return character == TokenType.GOLD.getChar();
    }

    /**
     * Returns if the player can spawn on this token (so a FLOOR or an EXIT).
     *
     * @return If you can spawn on this token
     */
    public boolean canSpawnOn() {
        return character == TokenType.FLOOR.getChar() || character == TokenType.EXIT.getChar();
    }

    /**
     * Gets if the current token is an EXIT token.
     *
     * @return If the current token is an exit token
     */
    public boolean isExit() {
        return character == TokenType.EXIT.getChar();
    }

    /**
     * Gets if the current token is a WALL token.
     *
     * @return If the current token is a wall token
     */
    public boolean isWall() {
        return character == TokenType.WALL.getChar();
    }

    /**
     * Gets if the current token is a GOLD token.
     *
     * @return If the current token is a gold token
     */
    public boolean isGold() {
        return character == TokenType.GOLD.getChar();
    }

    /**
     * Sets the character on the map, and checks if it's a valid token.
     *
     * @param character The character we'd like to assign to this token
     */
    private void setChar(char character) {
        if (isValidToken(character)) {
            this.character = character;
        } else {
            System.out.println(String.format("Error! Invalid main.Token %c on map! ", character));
            System.exit(1);
        }
    }

    /**
     * Gets the tokens character.
     *
     * @return The main.Token character
     */
    public char getChar() {
        return character;
    }

    /**
     * Checks if the token placed is valid.
     *
     * @param character The token to be checked
     * @return If it's a valid token
     */
    private boolean isValidToken(char character) {
        return character == TokenType.WALL.getChar() || character == TokenType.EXIT.getChar()
                || character == TokenType.FLOOR.getChar() || character == TokenType.GOLD.getChar();
    }
}
