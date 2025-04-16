package uta.cse3310.GamePlay;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class GamePlayTest{

	@Test
	void moveForwardCheckTest() {
		var checker = new Checker(new Cord(3, 2), Color.BLACK);
		var cord = new Cord(4, 3);
		var board = new Board();
		
		assertEquals(true, board.moveForwardCheck(checker, cord));
	}

    // This will test if piece becomea a king piece once it reaches the end
    @Test
    void kingMeTest() {
        var board = new Board();

        /* for every possible Black checker piece position, 
            test that checker is not a king if it's not on the last opposing row
            test that checker is a king if it's on the last opposing row */

        var blackChecker = new Checker(new Cord(0,0), Color.BLACK);
        // parse through each row
        for (int y = 0; y < 8; y++) {
            // parse through each possible column and for each possible location, run kingMe
            if (y%2 == 1){
                for (int x = 0; x < 8; x += 2) {
                    blackChecker.setCord(x, y);
                    board.kingMe(blackChecker);
                }
            }
            else if (y%2 == 0){
                for (int x = 1; x < 8; x += 2) {
                    blackChecker.setCord(x, y);
                    board.kingMe(blackChecker);
                }
            }
            // if location is not on the last row, checker should not be king
            if (y != 7){
                assertEquals(false, blackChecker.isKing());
            }
            // if location is on the last row, checker should be king
            else{
                assertEquals(true, blackChecker.isKing());
            }
            
        }

         /* for every possible Red checker piece position, 
            test that checker is not a king if it's not on the last opposing row
            test that checker is a king if it's on the last opposing row */

        var redChecker = new Checker(new Cord(0,0), Color.RED);
        // parse through each row
        for (int y = 7; y >= 0; y--) {
            // parse through each possible column and for each possible location, run kingMe
            if (y%2 == 1){
                for (int x = 0; x < 8; x += 2) {
                    redChecker.setCord(x, y);
                    board.kingMe(redChecker);
                }
            }
            else if (y%2 == 0){
                for (int x = 1; x < 8; x += 2) {
                    redChecker.setCord(x, y);
                    board.kingMe(redChecker);
                }
            }
            // if location is not on the last row, checker should not be king
            if (y != 0){
                assertEquals(false, redChecker.isKing());
            }
            // if location is on the last row, checker should be king
            else{
                assertEquals(true, redChecker.isKing());
            }
            
        }
    }

    @Test
    void removeJumpedCheckerTest() {
        var testBoard = new Board();
        var testCord = new Cord(2,4);

        //manually set new board positions somehow

        //change the position of a checker to a position where it can be jumped
        testBoard.updatePosition( testBoard.checkerBoard[2][2], testCord );
        //assertEquals(null, testBoard.checkerBoard[2][2]);   
        //NOTE: need to implement way to set old position to NULL
        
        //assert that the piece has been removed from the old spot

        
        //testBoard.removeJumpedChecker( testBoard.checkBoard[1][5], new Cord(x:3, y:3));
        //assertEquals(null, testBoard.checkerBoard[2][4]);
        //test if the spot is null? There is no exact return type to test
        
       
    }

}
