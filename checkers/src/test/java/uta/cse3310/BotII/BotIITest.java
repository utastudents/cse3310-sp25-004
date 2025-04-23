package uta.cse3310.BotII;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.*;
import java.util.ArrayList;

import org.junit.Test;
import org.mockito.internal.matchers.Null;

import uta.cse3310.Bot.BotII.BotII;
import uta.cse3310.GamePlay.Board;
import uta.cse3310.GamePlay.Checker;
import uta.cse3310.GamePlay.Color;
import uta.cse3310.GamePlay.Cord;



public class BotIITest {

    @Test
    public void testInDanger() {
        // 1. Setup the board
        Board board = new Board();
    
        // Place pieces
        Checker blackPiece = new Checker(new Cord(2, 2), Color.BLACK);
        Checker redPiece = new Checker(new Cord(3, 3), Color.RED);
        board.checkerBoard[2][2] = blackPiece;  
        board.checkerBoard[3][3] = redPiece;    
        
        // 2. Verify the danger condition
        boolean inDanger = BotII.isInDanger(blackPiece, board);
        
        
        // 4. Assertions
        assertTrue("Black piece at (2,2) should be in danger from red at (3,3)", inDanger);
        
        // 5. Test defensive move generation
        BotII.Move defensiveMove = BotII.defendPieces(board);
        assertNotNull("Should find a defensive move", defensiveMove);
        assertTrue("Move should be to higher Y coordinate (backward for BLACK)",
                  defensiveMove.destination.getY() > blackPiece.getCord().getY());
    }



    @Test
    public void TestDefendPieces() {

        // Set up the board with a black checker
        Board board = new Board();
        Checker blackChecker = new Checker(new Cord(3, 3), Color.BLACK);
        board.checkerBoard[3][3] = blackChecker;

        // Simulate a red piece in a position that can jump the black piece
        Checker redChecker = new Checker(new Cord(4, 4), Color.RED);
        board.checkerBoard[4][4] = redChecker;

        // Call the defendPieces method to find a safe move
        BotII.Move bestMove = BotII.defendPieces(board);

        // Check if the bot finds a valid move
        assertTrue("A defensive move should be found", bestMove != null);

    }
    /* 
    @Test
    void TestcapturePiece() {
	//Arange
 	// Set up the board with a black checker

    
  	Board board = new Board();
        boolean AT, HM;
     	Checker blackChecker = new Checker(new Cord(2, 2), Color.BLACK);
      	board.checkerBoard[2][2] = blackChecker;

	//Simulate a red piece in a position that can get jumped by the black piece
       Checker redChecker = new Checker(new Cord(3, 3), Color.RED);
       board.checkerBoard[3][3] = redChecker;

	//Checks to see if the landing spot is empty
    	board.checkerBoard[4][4] = null;
     
     	BotII botII = new BotII(null, null);
        //Capture the piece
      	botII.capturePiece(board);
	    
	//assert
        if (board.checkerBoard[3][3] == null) {
            AT = true;
            assertTrue("Red piece should have been captured", AT);
        }
        Checker moved = board.checkerBoard[4][4];
        if (moved != null) {
            HM = true;
            assertTrue("Black piece should have moved to new location", HM);
        }
    } */

    @Test
    public void testIsInDanger() {
        // Set up a simple test scenario
        Board board = new Board();
        Checker blackChecker = new Checker(new Cord(3, 3), Color.BLACK);
        board.checkerBoard[3][3] = blackChecker;

        // Simulate a red piece that could jump the black piece
        Checker redChecker = new Checker(new Cord(4, 4), Color.RED);
        board.checkerBoard[4][4] = redChecker;

        // Run the method and check if the checker is in danger
        boolean result = BotII.isInDanger(blackChecker, board);
        assertTrue("Checker should be in danger", result);
    }


    @Test
    public void testGetSafeMoves() {
        // Set up the board and a black checker
        Board board = new Board();
        Checker blackChecker = new Checker(new Cord(3, 3), Color.BLACK);
        board.checkerBoard[3][3] = blackChecker;

        // Get the safe moves for the black checker
        ArrayList<Cord> safeMoves = BotII.getSafeMoves(blackChecker, board);

        // Assert that there are safe moves available
        assertTrue("There should be at least one safe move", !safeMoves.isEmpty());
    }
    
}
