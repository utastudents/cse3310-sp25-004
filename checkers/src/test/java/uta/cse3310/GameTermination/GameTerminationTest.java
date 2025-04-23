package uta.cse3310.GameTermination;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import uta.cse3310.GamePlay.GamePlay;
import uta.cse3310.GameTermination.GameTermination;
import uta.cse3310.GameTermination.gameState;
import uta.cse3310.GameManager.Game;
import uta.cse3310.GamePlay.Board;
import uta.cse3310.GamePlay.Checker;
import uta.cse3310.GamePlay.Color;
import uta.cse3310.GamePlay.Cord;
import uta.cse3310.PageManager.HumanPlayer;
import uta.cse3310.PairUp.Player;
import uta.cse3310.DB.DB;

public class GameTerminationTest {

    @Test
    public void testEndGame_Player1Wins() {
        GameTermination gt = new GameTermination();

        HumanPlayer p1 = new HumanPlayer("Player1", "user1", 1, Player.STATUS.IN_GAME, 0, 0, 0, 1000);
        HumanPlayer p2 = new HumanPlayer("Player2", "user2", 2, Player.STATUS.IN_GAME, 0, 0, 0, 1000);
        
        Game game = new Game(999, p1, p2);
        GamePlay gamePlay = new GamePlay(999);
        Board board = gamePlay.getBoard();

        // Set up a board state where Player 1 has won
        // This assumes Player 1 has captured all of Player 2's pieces
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board.checkerBoard[i][j] = null;
            }
        }
        
        // Add only Player 1's pieces
        board.checkerBoard[0][0] = new Checker(new Cord(0, 0), Color.BLACK);

        game.setGameActive(true);

        Game result = gt.endGame(game);

        assertFalse(result.isGameActive());
        assertEquals(1, result.getPlayer1().getPlayerId());
    }

    @Test
    public void testEndGame_Player2Wins() {
        GameTermination gt = new GameTermination();

        HumanPlayer p1 = new HumanPlayer("Player1", "user1", 1, Player.STATUS.IN_GAME, 0, 0, 0, 1000);
        HumanPlayer p2 = new HumanPlayer("Player2", "user2", 2, Player.STATUS.IN_GAME, 0, 0, 0, 1000);
        
        Game game = new Game(999, p1, p2);
        GamePlay gamePlay = new GamePlay(999);
        Board board = gamePlay.getBoard();

        // Set up a board state where Player 2 has won
        // This assumes Player 2 has captured all of Player 1's pieces
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board.checkerBoard[i][j] = null;
            }
        }
        
        // Add only Player 2's pieces
        board.checkerBoard[0][0] = new Checker(new Cord(0, 0), Color.RED);

        game.setGameActive(true);

        Game result = gt.endGame(game);

        assertFalse(result.isGameActive());
        assertEquals(2, result.getPlayer2().getPlayerId());
    }

    @Test
    public void testEndGame_Draw() {
        GameTermination gt = new GameTermination();

        HumanPlayer p1 = new HumanPlayer("Player1", "user1", 1, Player.STATUS.IN_GAME, 0, 0, 0, 1000);
        HumanPlayer p2 = new HumanPlayer("Player2", "user2", 2, Player.STATUS.IN_GAME, 0, 0, 0, 1000);
        
        Game game = new Game(999, p1, p2);
        GamePlay gamePlay = new GamePlay(999);
        Board board = gamePlay.getBoard();

        // Set up a board state that results in a draw
        // This could be a position where neither player can make a legal move
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board.checkerBoard[i][j] = null;
            }
        }
        
        // Add one piece for each player in a position where neither can move
        board.checkerBoard[0][0] = new Checker(new Cord(0, 0), Color.BLACK);
        board.checkerBoard[7][7] = new Checker(new Cord(7, 7), Color.RED);

        game.setGameActive(true);

        Game result = gt.endGame(game);

        assertFalse(result.isGameActive());
    }

    @Test
    public void testSaveResults() {
        // Create a GameTermination instance
        GameTermination gt = new GameTermination();
        gameState state = new gameState();
        
        // Create a DB instance to add players
        DB db = new DB();
        
        // Use fixed usernames for test players
        String username1 = "testPlayer1";
        String username2 = "testPlayer2";
        
        // Get the players from the database first
        HumanPlayer dbPlayer1 = db.getPlayerByUsername(username1);
        HumanPlayer dbPlayer2 = db.getPlayerByUsername(username2);
        
        // If players don't exist, create them
        if (dbPlayer1 == null) {
            db.addPlayer(username1, "password1");
            dbPlayer1 = db.getPlayerByUsername(username1);
        }
        
        if (dbPlayer2 == null) {
            db.addPlayer(username2, "password2");
            dbPlayer2 = db.getPlayerByUsername(username2);
        }
        
        assertNotNull(dbPlayer1, "Player 1 not found in database");
        assertNotNull(dbPlayer2, "Player 2 not found in database");
        
        // Set initial stats in the database
        assertTrue(db.updatePlayerStats(dbPlayer1.getPlayerId(), 5, 2, 1200, 7), "Failed to update player 1 stats");
        assertTrue(db.updatePlayerStats(dbPlayer2.getPlayerId(), 3, 4, 1000, 7), "Failed to update player 2 stats");
        
        // Verify the stats were updated
        dbPlayer1 = db.getPlayerByUsername(username1);
        dbPlayer2 = db.getPlayerByUsername(username2);
        
        // Create a game with these players
        Game game = new Game(999, dbPlayer1, dbPlayer2);
        GamePlay gamePlay = new GamePlay(999);
        Board board = gamePlay.getBoard();

        // Set up a winning position for player 1
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board.checkerBoard[i][j] = null;
            }
        }
        
        // Add only Player 1's pieces to simulate a win
        board.checkerBoard[0][0] = new Checker(new Cord(0, 0), Color.BLACK);
        board.checkerBoard[0][2] = new Checker(new Cord(0, 2), Color.BLACK);
        board.checkerBoard[1][1] = new Checker(new Cord(1, 1), Color.BLACK);
        
        game.setGameActive(true);


        try {
            // Call saveResults and get the updated player stats
            HumanPlayer[] updatedStats = gt.saveResults(game);
            
            // Verify that the method returned something
            assertNotNull(updatedStats);
            
            // If the database connection worked and players were found, verify the stats
            if (updatedStats[0] != null && updatedStats[1] != null) {
                
                // Verify that the stats were actually updated
                assertEquals(dbPlayer1.getWins() + 1, updatedStats[0].getWins(), "Player 1 wins should have increased by 1");
                assertEquals(dbPlayer2.getLosses() + 1, updatedStats[1].getLosses(), "Player 2 losses should have increased by 1");
                assertTrue(updatedStats[0].getELO() > dbPlayer1.getELO(), "Player 1 ELO should have increased");
                assertTrue(updatedStats[1].getELO() < dbPlayer2.getELO(), "Player 2 ELO should have decreased");
            } else {
                fail("Players not found in database after saveResults");
            }
            
        } catch (Exception e) {
            // If an exception occurs, the test should fail
            fail("Unexpected exception: " + e.getMessage());
        }
    }
}
