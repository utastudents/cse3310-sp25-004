package uta.cse3310.BotI;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import uta.cse3310.Bot.BotI.BotI;
import uta.cse3310.GameManager.Game;
import uta.cse3310.GamePlay.Board;
import uta.cse3310.GamePlay.Checker;
import uta.cse3310.GamePlay.Color;
import uta.cse3310.GamePlay.Cord;
import uta.cse3310.GamePlay.GamePlay;
import uta.cse3310.GameState;

public class Bot1Test {

    private BotI bot;
    private Game game;
    private Board board;
    private GamePlay gameplay;

    @Before
    public void setUp() {
        gameplay = new GamePlay();
        board = gameplay.getBoard();
        game = new Game(1, null, null);
        game.startGame(game);
        bot = new BotI(game, Color.BLACK);
    }

    @Test
    public void testMakeMove() {
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                board.removePiece(new Cord(x, y));
            }
        }
        
        Checker blackPiece = new Checker(Color.BLACK, new Cord(1, 1));
        board.placePiece(blackPiece);
        
        Checker redPiece = new Checker(Color.RED, new Cord(2, 2));
        board.placePiece(redPiece);
        
        bot.updateBoard(GameState.GAME_DISPLAY);
        
        boolean moveResult = bot.makeMove(GameState.GAME_DISPLAY);
        assertTrue("Bot should make a valid move", moveResult);
        
        Checker pieceAtOriginalPosition = board.checkSpace(new Cord(1, 1));
        assertNull("Black piece should no longer be at the original position", pieceAtOriginalPosition);
    }

    @Test
    public void testUpdateBoard() {
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                board.removePiece(new Cord(x, y));
            }
        }
        
        Checker blackPiece = new Checker(Color.BLACK, new Cord(1, 1));
        board.placePiece(blackPiece);
        
        boolean updateResult = bot.updateBoard(GameState.GAME_DISPLAY);
        assertTrue("Board update should be successful", updateResult);
        
        boolean moveResult = bot.makeMove(GameState.GAME_DISPLAY);
        assertTrue("Bot should be able to make a move after board update", moveResult);
    }

    @Test
    public void testEndGame() {
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                board.removePiece(new Cord(x, y));
            }
        }
        
        Checker blackPiece = new Checker(Color.BLACK, new Cord(1, 1));
        board.placePiece(blackPiece);
        
        bot.updateBoard(GameState.GAME_DISPLAY);
        
        boolean endGameResult = bot.endGame(GameState.SUMMARY);
        assertTrue("End game should be successful", endGameResult);
        
        boolean moveResult = bot.makeMove(GameState.GAME_DISPLAY);
        assertTrue("Bot should be able to make a move after game ends", moveResult);
    }
} 