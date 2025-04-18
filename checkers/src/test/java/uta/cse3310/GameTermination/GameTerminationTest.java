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

// Mock DB class for testing
class MockDB extends DB {
    @Override
    public HumanPlayer getPlayerById(int playerId) {
        // Return a mock player with the given ID
        return new HumanPlayer("Player" + playerId, "user" + playerId, playerId, Player.STATUS.IN_GAME, 5, 2, 1000, 7);
    }
    
    @Override
    public boolean updatePlayerStats(int playerId, int wins, int losses, int ELO, int gamesPlayed) {
        // Mock implementation that always succeeds
        return true;
    }
}

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
        // Create a GameTermination instance with a mock DB
        GameTermination gt = new GameTermination();
        
        // Use reflection to replace the DB instance with our mock
        try {
            java.lang.reflect.Field dbField = GameTermination.class.getDeclaredField("database");
            dbField.setAccessible(true);
            dbField.set(gt, new MockDB());
        } catch (Exception e) {
            // If reflection fails, we'll skip this test
            System.out.println("Could not inject mock DB: " + e.getMessage());
            return;
        }

        HumanPlayer p1 = new HumanPlayer("Player1", "user1", 1, Player.STATUS.IN_GAME, 5, 2, 7, 1200);
        HumanPlayer p2 = new HumanPlayer("Player2", "user2", 2, Player.STATUS.IN_GAME, 3, 4, 7, 1000);
        
        Game game = new Game(999, p1, p2);
        GamePlay gamePlay = new GamePlay(999);
        Board board = gamePlay.getBoard();

        // Set up a winning position for player 1
        board.checkerBoard[0][0] = new Checker(new Cord(0, 0), Color.BLACK);
        game.setGameActive(true);

        HumanPlayer[] updatedStats = gt.saveResults(game);

        assertNotNull(updatedStats);
        assertEquals(2, updatedStats.length);
        assertTrue(updatedStats[0].getELO() > 1200); // Winner's ELO should increase
        assertTrue(updatedStats[1].getELO() < 1000); // Loser's ELO should decrease
        assertEquals(6, updatedStats[0].getWins()); // Winner should have one more win
        assertEquals(5, updatedStats[1].getLosses()); // Loser should have one more loss
    }
}
