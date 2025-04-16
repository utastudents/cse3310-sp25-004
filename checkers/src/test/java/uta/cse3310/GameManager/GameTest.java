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

        @Override
        public boolean endGame(GameState gs){
            return true;
        }
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