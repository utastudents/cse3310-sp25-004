package uta.cse3310.BotII;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Test;

import uta.cse3310.Bot.BotII.BotII;
import uta.cse3310.GamePlay.Board;
import uta.cse3310.GamePlay.Checker;
import uta.cse3310.GamePlay.Color;
import uta.cse3310.GamePlay.Cord;

public class BotIITest {

  /*  @Test
	void Makemove() {
		
	}*/

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

  /*  @Test
    void TestcapturePiece() {
	//Arange
	/*
 	// Set up the board with a black checker
  	Board board = new Board();
     	Checker blackChecker = new Checker(new Cord(2, 2), Color.BLACK);
      	board.checkerBoard[2][2] = blackChecker;

	//Simulate a red piece in a position that can get jumped by the black piece
       Checker redChecker = new Checker(new Cord(3, 3), Color.RED);
       board.checkerBoard[3][3] = redChecker;

	//Checks to see if the landing spot is empty
    	board.checkerBoard[4][4] = null;
     
     	//Capture the piece
      	BotII.capturePiece(board);

       //
     

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
