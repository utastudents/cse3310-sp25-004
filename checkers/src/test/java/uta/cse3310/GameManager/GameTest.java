package uta.cse3310.GameManager;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.Assert.assertTrue;

import uta.cse3310.GameState;
import uta.cse3310.PairUp.PairUp;
import uta.cse3310.PairUp.Player;

/**
 * These are the Unit tests for the Game class to ensure proper behavior of
 * constructors, getters, and setters related to player IDs and game ID.
 */
public class GameTest {
    // mock implementation of player
    private static class MockPlayer extends Player {
        public MockPlayer(int id) {
            this.playerId = id;
        }

        @Override
        public boolean makeMove(GameState gs) {
            return true;
        }

        @Override
        public boolean updateBoard(GameState gs) {
            return true;
        }
    }

    /**
     * Testing that the constructor correctly sets the game ID and player1 ID.
     */
    @Test
    void testConstructorAndGetters() {
        int expectedGameID = 123;
        Player player1 = new MockPlayer(42);
        Player player2 = new MockPlayer(77);

        // Creating a new Game object with the specified IDs
        Game game = new Game(expectedGameID, player1, player2);

        // Verifying that the game ID and player1 ID were correctly assigned
        assertEquals(expectedGameID, game.getGameID(),
                "Game ID should match the value set in the constructor");

        assertEquals(player1, game.getPlayer1(),
                "Player 1 ID should match the value set in the constructor");
    }

    /**
     * Tests that setPlayer2() correctly sets and retrieves player2 ID.
     */
    @Test
    void testSetPlayer2() {

        Player player1 = new MockPlayer(10);
        Player player2 = new MockPlayer(20);

        Game game = new Game(200, player1, null); // Creating a Game with dummy IDs

        // Setting player 2 using the setter method
        game.setPlayer2(player2);

        // Verifying that the player2 ID was set correctly
        assertEquals(player2, game.getPlayer2(),
                "Player 2 ID should match the value set by setPlayer2()");
    }

    /**
     * Tests that setPlayer1() correctly updates and retrieves the player1 ID.
     */
    @Test
    void testSetPlayer1() {

        Player original = new MockPlayer(1);
        Player newPlayer = new MockPlayer(77);

        Game game = new Game(300, original, null); // Initial ID doesn't matter here

        // Set a new player1 ID
        game.setPlayer1(newPlayer);

        // Check if the new player1 ID is correctly updated
        assertEquals(newPlayer, game.getPlayer1(),
                "Player 1 ID should match the value set by setPlayer1()");
    }

    @Test
    void testCreateGame() {
        // Making game manager object
        GameManager manager = new GameManager();
        //manager.initializeGames(); //Moved initialization into constructor

        // Player objects
        Player p1 = new MockPlayer(0);
        Player p2 = new MockPlayer(1);

        // Setting to boolean
        boolean result = manager.createGame(p1, p2);

        // Will return true if executed correctly
        assertTrue("Game was created successfully!", result); //Causes an error
    }

    @Test
    void testRemoveGame(){
        // Making game manager object
        GameManager manager = new GameManager();

        manager.pu = new PairUp(manager);

        // Player objects
        Player p1 = new MockPlayer(0);
        Player p2 = new MockPlayer(1);

        // Setting to boolean
        boolean result = manager.createGame(p1, p2);
        
        // Will return true if executed correctly
        assertTrue("Game was created successfully!", result); //Causes an error
        assertEquals(9, manager.getNumOfAvailableGames(), "Game is now active after creation!");

        Game currentGame = null;
        for (Game g : manager.getGames()) {
            if (g != null && g.getPlayer1() == p1 && g.getPlayer2() == p2) {
                currentGame = g;
                break;
            }
        }
        manager.removeGame(currentGame);

        assertEquals(10, manager.getNumOfAvailableGames(), "Game was removed successfully!");
    }
}