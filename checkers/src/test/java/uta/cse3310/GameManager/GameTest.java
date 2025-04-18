package uta.cse3310.GameManager;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.Assert.assertTrue;

import uta.cse3310.GameState;
import uta.cse3310.PairUp.PairUp;
import uta.cse3310.PairUp.Player;
import uta.cse3310.PageManager.GameMove;
import uta.cse3310.PageManager.GameUpdate;
import uta.cse3310.GamePlay.Board;
import uta.cse3310.GamePlay.Checker;
import uta.cse3310.GamePlay.GamePlay;
import uta.cse3310.GamePlay.Color;
import uta.cse3310.GamePlay.Cord;

public class GameTest {
    // Mock implementation of player
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
        public boolean endGame(GameState gs) {
            return true;
        }
    }

    @Test
    void testCreateGame() {
        // Making game manager object
        GameManager manager = new GameManager();
        // manager.initializeGames(); //Moved initialization into constructor

        // Player objects
        Player p1 = new MockPlayer(0);
        Player p2 = new MockPlayer(1);

        // Setting to boolean
        boolean result = manager.createGame(p1, p2);

        // Will return true if executed correctly
        assertTrue("Game was created successfully!", result); // Causes an error
    }

    @Test
    void testRemoveGame() {
        // Making game manager object
        GameManager manager = new GameManager();

        manager.pu = new PairUp(manager);

        // Player objects
        Player p1 = new MockPlayer(0);
        Player p2 = new MockPlayer(1);

        // Setting to boolean
        boolean result = manager.createGame(p1, p2);

        // Will return true if executed correctly
        assertTrue("Game was created successfully!", result); // Causes an error
        assertEquals(9, manager.getNumOfAvailableGames(), "Game is now active after creation!");

        Game currentGame = null;
        for (Game g : manager.getGames()) {
            if (g != null && g.getPlayer1() == p1 && g.getPlayer2() == p2) {
                currentGame = g;
                break;
            }
        }

        Board board = new Board();

        if (currentGame.getBoard() == null) {
            currentGame.getBoard(); // or whatever constructor you have
        }
        manager.removeGame(currentGame);

        assertEquals(10, manager.getNumOfAvailableGames(), "Game was removed successfully!");
    }

    @Test
    void testProcessMove() {

        GameManager manager = new GameManager();
        // Using dummy values for testing purposes
        GameMove move = new GameMove(1, 3, 2, 6, 3, "King");
        move.getFromPosition_X();
        move.getFromPosition_Y();
        move.getToPosition_X();
        move.getToPosition_Y();
        move.getClietId();
        // Making objects
        GamePlay gamePlay = new GamePlay(1);
        Cord cord = new Cord(3, 2);
        Checker piece = new Checker(cord, Color.RED);
        gamePlay.getBoard().checkerBoard[2][3] = piece;

        GameUpdate update = manager.processMove(move, gamePlay);

        // will return equals and true if executed
        assertEquals("In Progress", update.getGameStatus());
        assertTrue(update.getCapturedPosition().contains("Playerid"));
    }
}
