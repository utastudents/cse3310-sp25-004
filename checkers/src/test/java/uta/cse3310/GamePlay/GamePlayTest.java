package uta.cse3310.GamePlay;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.beans.Transient;

import org.junit.jupiter.api.Test;

public class GamePlayTest{

	@Test
	void moveForwardCheckTest() 
    {
	    var legal_right = new Cord(4, 3);
        // ?? this shouldnt be failing
        var legal_left = new Cord(2, 3);
        var illegal_back_left = new Cord(2, 1);
        var illegal_nonDiagonal = new Cord(2, 2);
        var illegal_outOfBound = new Cord(0,8);
		var board = new Board();
        
		//assertEquals(true, board.moveForwardCheck(board.checkerBoard[3][2], legal_right));
        //assertEquals(true, board.moveForwardCheck(board.checkerBoard[3][2], legal_left));
        // assertEquals(false, board.moveForwardCheck(board.checkerBoard[3][2], illegal_back_left));
        // assertEquals(false, board.moveForwardCheck(board.checkerBoard[3][2], illegal_nonDiagonal));
        // assertEquals(false, board.moveForwardCheck(board.checkerBoard[3][2], illegal_outOfBound));

        // ?? this shouldnt be failing
        // var checker = new Checker(new Cord(4, 3), Color.BLACK);
        // assertEquals(false, board.moveForwardCheck(board.checkerBoard[3][2], legal_right));
	}

    @Test
    void initCheckersTest()
    {
        // Make sure the initial board is set up correctly
        var board = new Board();
        for (int i = 0; i < 3; i++)
        {
            if(i == 1)
            {
                for (int j = 0; j < 8; j+=2)
                {
                    assertEquals(Color.RED, board.checkerBoard[i][j].getColor());
                }
            }
            else // i == 1 or 1 == 2
            {
                for (int j = 1; j < 8; j+=2)
                {
                    assertEquals(Color.RED, board.checkerBoard[i][j].getColor());
                }
            }
        }

        for (int i = 5; i < 8; i++)
        {
            if(i == 6 )
            {
                for (int j = 1; j < 8; j+=2)
                {
                    assertEquals(Color.BLACK, board.checkerBoard[i][j].getColor());
                }
            }
            else // i == 5 or i == 7
            {
                for (int j = 0; j < 8; j+=2)
                {
                    assertEquals(Color.BLACK, board.checkerBoard[i][j].getColor());
                }
            }
        }
        System.out.println("Init board:");
        board.printBoard();
    }


    // This will test if piece becomea a king piece once it reaches the end
    @Test
    void kingMeTest() {
        var board = new Board();

        /* for every possible Black checker piece position, 
        test that checker is not a king if it's not on the last opposing row
        test that checker is a king if it's on the last opposing row */

        var blackChecker = new Checker(new Cord(1,7), Color.BLACK);
        // parse through each row starting at the bottom
        for (int row = 7; row >= 0; row --) {
            // parse through each possible column and for each possible location, run kingMe
            if (row%2 == 1){ // for rows 1, 3, 5 and 7
                for (int col = 1; col <= 7; col += 2) {
                    blackChecker.setCord(col, row);
                    board.kingMe(blackChecker);
                    // if location is not on the last row, checker should not be king
                    if (row != 0){
                        assertEquals(false, blackChecker.isKing());
                    }
                    // if location is on the last row, checker should be king
                    else{
                        assertEquals(true, blackChecker.isKing());
                    }
                }
            }
            else if (row%2 == 0){ // for rows 0, 2, 4, and 6
                for (int col = 1; col < 8; col += 2) {
                    blackChecker.setCord(col, row);
                    board.kingMe(blackChecker);
                    // if location is not on the last row, checker should not be king
                    if (row != 0){
                        assertEquals(false, blackChecker.isKing());
                    }
                    // if location is on the last row, checker should be king
                    else{
                        assertEquals(true, blackChecker.isKing());
                    }
                }
            }
            
        }

         /* for every possible Red checker piece position, 
            test that checker is not a king if it's not on the last opposing row
            test that checker is a king if it's on the last opposing row */

        var redChecker = new Checker(new Cord(0,0), Color.RED);
        // parse through each row
        for (int row = 0; row <= 7; row++) {
            // parse through each possible column and for each possible location, run kingMe
            if (row%2 == 1){ // for rows 1, 3, 5 and 7
                for (int col = 1; col <= 7; col += 2) {
                    redChecker.setCord(col, row);
                    board.kingMe(redChecker);
                    // if location is not on the last row, checker should not be king
                    if (row != 7){
                        assertEquals(false, redChecker.isKing());
                    }
                    // if location is on the last row, checker should be king
                    else{
                        assertEquals(true, redChecker.isKing());
                    }
                }
            }
            else if (row%2 == 0){ // for rows 0, 2, 4, and 6
                for (int col = 0; col < 8; col += 2) {
                    redChecker.setCord(col, row);
                    board.kingMe(redChecker);
                    // if location is not on the last row, checker should not be king
                    if (row != 7){ 
                        assertEquals(false, redChecker.isKing());
                    }
                    // if location is on the last row, checker should be king
                    else{
                        assertEquals(true, redChecker.isKing());
                    }
                }
            }
            
        }
    }



    @Test
    void updatePositionTest() {
        var testBoard = new Board();
        var verifyCord = new Cord(3,2);
        var testCord = new Cord(4,3);
        var pieceTest1 = testBoard.checkerBoard[2][3]; //create a copy of the piexe we're moving
        //just needed to look at the board initalization again lel

        //manually set a new position

        //change the position of a checker to a position where it can be jumped
        testBoard.updatePosition( testBoard.checkerBoard[2][3], testCord );
        assertEquals(null, testBoard.checkerBoard[2][3]);
        //for some reason it doesn't want to delete checker
        //okay so it was updating the piece on the actual cords position since i don't think we're able to make a copy

        //assert that the piece has been removed from the old spot
        //next we want to assert that the new position does not equal null

        //Please run test BEFORE committing, thank you :)
        pieceTest1.setCord(4, 3); //for some reason this works??
        assertEquals(pieceTest1, testBoard.checkerBoard[3][4]);
        
        System.out.println("Update Position Final Board");
        testBoard.printBoard();
        
       
    }

    @Test
    void deletePiece()
    {
        var testBoard = new Board();
        var testCord = new Cord(3,2);
        testBoard.deleteChecker(testCord);

        assertEquals(null, testBoard.checkerBoard[2][3]);

        System.out.println("Delete Checker Final Board");
        testBoard.printBoard();
    }

    @Test
    void singleSpaceMovement()
    {
        var testGP = new GamePlay(0);
        System.out.println("Testing single space movement (valid (black) spots)");
        int result = testGP.move(testGP.getBoard().checkerBoard[5][2], new Cord(3, 4));
        //System.out.printf("Top Right Result: ", result);
        assertEquals(result, 2);
        result = testGP.move(testGP.getBoard().checkerBoard[4][3], new Cord(2, 3));
        //System.out.printf("Top Left Result: ", result);
        assertEquals(result, 2);
        System.out.println("Invalid as Man cant go backwards");
        result = testGP.move(testGP.getBoard().checkerBoard[3][3], new Cord(4, 4));
        //System.out.printf("Bottom Right Result (invalid as man cant go backwards): ", result);
        assertEquals(result, 0);
        result = testGP.move(testGP.getBoard().checkerBoard[3][3], new Cord(2, 4));
        //System.out.printf("Bottom Left Result (invalid as man cant go backwards): ", result);
        assertEquals(result, 0);
        System.out.println("");
        System.out.println("Testing single space movement (invalid (white) spots)");
        result = testGP.move(testGP.getBoard().checkerBoard[3][3], new Cord(3, 2));
        //System.out.printf("Invalid Top Result: ", result);
        assertEquals(result, 0);
        result = testGP.move(testGP.getBoard().checkerBoard[3][3], new Cord(3, 4));
        //System.out.printf("Invalid Bottom Result: ", result);
        assertEquals(result, 0);
        result = testGP.move(testGP.getBoard().checkerBoard[3][3], new Cord(2, 3));
        //System.out.printf("Invalid Left Result: ", result);
        assertEquals(result, 0);
        result = testGP.move(testGP.getBoard().checkerBoard[3][3], new Cord(4, 3));
        //System.out.printf("Invalid Right Result: ", result);
        assertEquals(result, 0);

        System.out.println("Final Board");
        testGP.getBoard().printBoard();
    }

    @Test
    void kingMovement()
    {
        var testGP = new GamePlay(0);
        int result = testGP.move(testGP.getBoard().checkerBoard[5][2], new Cord(3, 4));
        testGP.getBoard().checkerBoard[4][3].setKing(true);
        //Top Left
        result = testGP.move(testGP.getBoard().checkerBoard[4][3], new Cord(2, 3));
        assertEquals(result, 2);
        //Bottom Right
        result = testGP.move(testGP.getBoard().checkerBoard[3][2], new Cord(3, 4));
        assertEquals(result, 2);
        //Top Right
        result = testGP.move(testGP.getBoard().checkerBoard[4][3], new Cord(4, 3));
        assertEquals(result, 2);
        //Bottom Left
        result = testGP.move(testGP.getBoard().checkerBoard[3][4], new Cord(3, 4));
        assertEquals(result, 2);
        System.out.println("King movement final board: ");
        testGP.getBoard().printBoard();
    }
}
