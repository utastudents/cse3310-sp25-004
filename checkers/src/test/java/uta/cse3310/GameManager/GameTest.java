package uta.cse3310.GameManager;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;

import uta.cse3310.GamePlay.Board;
import uta.cse3310.GamePlay.Checker;
import uta.cse3310.GamePlay.Color;
import uta.cse3310.GamePlay.Cord;
import uta.cse3310.GamePlay.GamePlay;
import uta.cse3310.PageManager.GameMove;
import uta.cse3310.PageManager.GameUpdate;
import uta.cse3310.PageManager.PageManager;
import uta.cse3310.PairUp.PairUp;
import uta.cse3310.PairUp.Player;
import uta.cse3310.PairUp.TestPlayer;

public class GameTest {
    // Mock implementation of player
    private static class MockPlayer extends Player {
        public MockPlayer(int id) {
            this.playerId = id;
        }

        @Override
        public boolean makeMove(GamePlay gs) {
            return true;
        }

        @Override
        public boolean updateBoard(GamePlay gs) {
            return true;
        }

        @Override
        public boolean endGame(GamePlay gs) {
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

        PairUp pu = new PairUp(manager);

        PageManager.pu = pu;

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

        manager.createGame(new TestPlayer(0), new TestPlayer(1));
        Game g = manager.getGames().get(0);

        // Using dummy values for testing purposes
        GameMove move = new GameMove(1, 0, 3, 2, 6, 3, "King");
        move.getFromPosition_X();
        move.getFromPosition_Y();
        move.getToPosition_X();
        move.getToPosition_Y();
        move.getClientId();
        // Making objects
        GamePlay gamePlay = g.getBoard();
        Cord cord = new Cord(3, 2);
        Checker piece = new Checker(cord, Color.RED);
        gamePlay.getBoard().checkerBoard[2][3] = piece;

        GameUpdate update = manager.processMove(move, gamePlay);

        // will return equals and true if executed
        assertEquals("In Progress", update.getGameStatus());
        assertTrue(update.getCapturedPosition().contains("Playerid"));
    }
    @Test
    void testSetGameActive() {

        Player player1 = new MockPlayer(1); 
        Player player2 = new MockPlayer(2);

        Game game = new Game(0, player1, player2);

        assertTrue("Game should initially be active", game.isGameActive());

        game.setGameActive(false);
        assertFalse("Game should be inactive after setting to false", game.isGameActive());

        game.setGameActive(true);
        assertTrue("Game should be active after setting to true again", game.isGameActive());
    }

    @Test
    void testInitializeGames(){
        GameManager manager = new GameManager(); //calling initializeGames() in the constructor

        // Getting the list of games after initialization
        var games = manager.getGames();

        assertEquals(10, games.size(), "There should be exactly 10 games initialized.");

        for (int i = 0; i < games.size(); i++) {
            Game game = games.get(i);
            assertNotNull(game, "Game at index " + i + " should not be null.");
            assertEquals(i, game.getGameID(), "Game ID at index " + i + " should be " + i + ".");
            assertNull(game.getPlayer1(), "Player 1 should initially be null.");
            assertNull(game.getPlayer2(), "Player 2 should initially be null.");
        }
    }

    @Test
    void testNumGamesAvailable() {
        //This test was created because the number client-side seems to not change, despite the number of active games changing
        GameManager gm = new GameManager();

        for (int i=0; i<10; i++) {
            assertEquals(gm.getNumOfAvailableGames(), GameManager.MAX_GAMES - i);
            gm.createGame(new TestPlayer(i*2), new TestPlayer(i*2 + 1));
        }
        
    }
}
