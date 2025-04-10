package uta.cse3310.GameManager;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Game class to ensure proper behavior of constructors, getters, and setters related to player IDs and game ID.
 */
class GameTest {

    /**
     * Tests that the constructor correctly sets the game ID and player1 ID.
     */
    @Test
    void testConstructorAndGetters() {
        int expectedGameID = 123;        // Simulated unique game ID
        int expectedPlayer1ID = 42;      // Simulated player 1 ID

        // Creating a new Game object with the specified IDs
        Game game = new Game(expectedGameID, expectedPlayer1ID);

        // Verifing that the game ID and player1 ID were correctly assigned
        assertEquals(expectedGameID, game.getGameID(), 
            "Game ID should match the value set in the constructor");

        assertEquals(expectedPlayer1ID, game.getPlayer1(), 
            "Player 1 ID should match the value set in the constructor");
    }

    // Testing the setPlayer2() method properly sets and retrieves player2 ID.
    @Test
    void testSetPlayer2() {
        Game game = new Game(200, 10);   // Creating a Game with dummy IDs
        int expectedPlayer2ID = 20;      // Simulating player 2 ID

        // Setting player 2 using the setter method
        game.setPlayer2(expectedPlayer2ID);

        // Verifing that the player2 ID was set correctly
        assertEquals(expectedPlayer2ID, game.getPlayer2(), 
            "Player 2 ID should match the value set by setPlayer2()");
    }
}
