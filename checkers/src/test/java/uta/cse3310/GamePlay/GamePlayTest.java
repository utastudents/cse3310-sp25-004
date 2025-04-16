package uta.cse3310.GamePlay;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class GamePlayTest{

	@Test
	void moveForwardCheckTest() {
		// var checker = new Checker(new Cord(4, 1), Color.BLACK);
		// var cord = new Cord(5, 2);
		// var board = new Board();
		
		// assertEquals(true, board.moveForwardCheck(checker, cord));
	}

    // testing piece will become a king piece once it reaches the end
    @Test
    void kingMeTest() {
        var board = new Board();

        /* for every possible Black checker piece position, 
            test checker is not a king if not on the last row
            test checker is a king if on the last row */

        var checker = new Checker(new Cord(0,0), Color.BLACK);
        // parse through each row
        for (int y = 0; y < 8; y++) {
            // parse through each column and for each possible location, run kingMe
            if (y%2 == 1){
                // parse
                for (int x = 0; x < 8; x += 2) {
                    checker.setCord(x, y);
                    board.kingMe(checker);
                }
            }
            else if (y%2 == 0){
                for (int x = 1; x < 8; x += 2) {
                    checker.setCord(x, y);
                    board.kingMe(checker);
                }
            }
            // if location is not on the last row, checker should not be king
            if (y != 7){
                assertEquals(false, checker.isKing());
            }
            // if location is on the last row, checker should be king
            else{
                assertEquals(true, checker.isKing());
            }
            
        }
    }

    @Test
    void removeJumpedCheckerTest() {
        var testBoard = new Board();

        //manually set new board positions somehow

        //testBoard.updatePosition( testBoard.checkerBoard[2][2], new Cord(x:2, y:4) );
        //change the position of a checker to a position where it can eb jumped
        
        //assertEquals(testBoard.checkerBoard[2][4]==null, testBoard.removeJumpedChecker( testBoard.checkBoard[1][5], new Cord(x:3, y:3) ))
        //test if the spot is null? There is no exact return type to test
        

    }

}