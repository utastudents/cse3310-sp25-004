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

public class BotITest {

    private BotI bot;
    private Game game;
    private Board board;
    private GamePlay gameplay;

    @Before
    public void setUp() {
        gameplay = new GamePlay(1); // Pass game ID
        board = gameplay.getBoard();
        game = new Game(1, null, null);
        game.startGame(game);
        bot = new BotI(game, Color.BLACK);
    }

    @Test
    public void testMakeMove() {
        board = new Board();
        
        Checker blackPiece = new Checker(new Cord(1, 1), Color.BLACK);
        board.updatePosition(blackPiece, new Cord(1, 1));
        
        Checker redPiece = new Checker(new Cord(2, 2), Color.RED);
        board.updatePosition(redPiece, new Cord(2, 2));
        
        bot.updateBoard(GameState.GAME_DISPLAY);
        
        boolean moveResult = bot.makeMove(GameState.GAME_DISPLAY);
        assertTrue("Bot should make a valid move", moveResult);
        
        Checker pieceAtOriginalPosition = board.checkSpace(new Cord(1, 1));
        assertNull("Black piece should no longer be at the original position", pieceAtOriginalPosition);
    }

    @Test
    public void testUpdateBoard() {
        board = new Board();
        
        Checker blackPiece = new Checker(new Cord(1, 1), Color.BLACK);
        board.updatePosition(blackPiece, new Cord(1, 1));
        
        boolean updateResult = bot.updateBoard(GameState.GAME_DISPLAY);
        assertTrue("Board update should be successful", updateResult);
        
        boolean moveResult = bot.makeMove(GameState.GAME_DISPLAY);
        assertTrue("Bot should be able to make a move after board update", moveResult);
    }

    @Test
    public void testEndGame() {
        board = new Board();
        
        Checker blackPiece = new Checker(new Cord(1, 1), Color.BLACK);
        board.updatePosition(blackPiece, new Cord(1, 1));
        
        bot.updateBoard(GameState.GAME_DISPLAY);
        
        boolean endGameResult = bot.endGame(GameState.SUMMARY);
        assertTrue("End game should be successful", endGameResult);
        
        boolean moveResult = bot.makeMove(GameState.GAME_DISPLAY);
        assertTrue("Bot should be able to make a move after game ends", moveResult);
    }
} 
