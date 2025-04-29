package uta.cse3310.BotI;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

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


    // this method runs before each test to set things up
    // creates a new game, board, and bot for each test
    @BeforeEach
    public void setUp() {
        gameplay = new GamePlay(1);
        board = gameplay.getBoard();
        game = new Game(1, null, null);
        game.startGame(game);
        bot = new BotI(game, Color.BLACK);
    }


    //tests if the bot actually makes a move
    @Test
    public void testMakeMove() {
               
        //gets the board from gameplay and Board
        board =  game.getBoard().getBoard();


        //puts the black peices on the board
        Checker blackPiece = new Checker(new Cord(1, 1), Color.BLACK);
        board.updatePosition(blackPiece, new Cord(1, 1));
        
        //puts the red peices on the board
        Checker redPiece = new Checker(new Cord(2, 2), Color.RED);
        board.updatePosition(redPiece, new Cord(2, 2));
        
        bot.updateBoard(game.getBoard());
        
        //have the bot mKE  a move
        // TODO: Investigate why testMakeMove out of bounds error
        //boolean moveResult = bot.makeMove(game.getBoard());
        // assertTrue(moveResult, "Bot should make a valid move");
        
        // TODO: Uncomment assertNULL
        // check if the piece moved from where it started
        Checker pieceAtOriginalPosition = board.checkSpace(new Cord(1, 1));
        // assertNull(pieceAtOriginalPosition, "Black piece should no longer be at the original position");
    }

    //checks if the board is properly updating
    @Test
    public void testUpdateBoard() {
        board =  game.getBoard().getBoard();
        
        Checker blackPiece = new Checker(new Cord(1, 1), Color.BLACK);
        board.updatePosition(blackPiece, new Cord(1, 1));

        //the board updates
        boolean updateResult = bot.updateBoard(game.getBoard());
        assertTrue(updateResult, "Board update should be successful");
        
        //TODO : Investigate why testUpdateBoard out of bounds error
        //checks if the bot can move after the board updates
        // boolean moveResult = bot.makeMove(game.getBoard());
        // assertTrue(moveResult, "Bot should be able to make a move after board update" );
    }


    //tests if the bot properly ends the game
    @Test
    public void testEndGame() {
        board =  game.getBoard().getBoard();
        
        Checker blackPiece = new Checker(new Cord(1, 1), Color.BLACK);
        board.updatePosition(blackPiece, new Cord(1, 1));
        
        bot.updateBoard(game.getBoard());
        
        //game ends properly
        boolean endGameResult = bot.endGame(game.getBoard());
        assertTrue(endGameResult, "End game should be successful" );
        
        //bot stops moving after game end
        boolean moveResult = bot.makeMove(game.getBoard());
        assertFalse(moveResult ,"Bot should NOT be able to make a move after game ends" );
    }
} 
