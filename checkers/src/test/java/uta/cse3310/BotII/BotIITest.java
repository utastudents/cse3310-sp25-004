package uta.cse3310.BotII;
import java.util.ArrayList;

//import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
//import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import uta.cse3310.Bot.BotII.BotII;
import uta.cse3310.GamePlay.Board;
import uta.cse3310.GamePlay.Checker;
import uta.cse3310.GamePlay.Color;
import uta.cse3310.GamePlay.Cord;



public class BotIITest {

    @Test
    public void TestDefendPieces() {
        // Create empty board
        Board board = new Board();
        clearBoard(board);
        
        // Black piece at column 3, row 4 (x=3, y=4) - bottom area
        Checker blackChecker = new Checker(new Cord(3, 4), Color.BLACK);
        board.checkerBoard[4][3] = blackChecker;

        Checker blackChecker2 = new Checker(new Cord(5, 6), Color.BLACK);
        board.checkerBoard[6][5] = blackChecker2;

        // Red piece at column 2, row 3 (x=2, y=3) - above black (can jump down)
        Checker redChecker1 = new Checker(new Cord(2, 3), Color.RED);
        board.checkerBoard[3][2] = redChecker1;

        System.out.println("Board state in TestDefendPieces:");
        printBoardWithCoordinates(board);  // pirnt board with cords
        System.out.println("Black piece at: " + blackChecker.getCord());
        System.out.println("Red piece at: " + redChecker1.getCord());

        BotII.Move bestMove = BotII.defendPieces(board);
        assertTrue(bestMove != null, "A defensice move should be found");
        printBoardWithCoordinates(board);
        
        if (bestMove != null) {
            System.out.println("Defensive move found: " + bestMove.piece.getCord() + 
                            " -> " + bestMove.destination);
        }
    }

    @Test
    public void testIsInDanger() {
        // Create empty board
        Board board = new Board();
        clearBoard(board);

        // Black piece at column 3, row 4 (x=3, y=4)
        Checker blackChecker = new Checker(new Cord(3, 4), Color.BLACK);
        board.checkerBoard[4][3] = blackChecker;

        // Red piece at column 2, row 3 (x=2, y=3) - can jump downward
        Checker redChecker = new Checker(new Cord(2, 3), Color.RED);
        board.checkerBoard[3][2] = redChecker;

        // Empty landing spot at column 4, row 5 (x=4, y=5)
        board.checkerBoard[5][4] = null;

        System.out.println("\nBoard state in testIsInDanger:");
        printBoardWithCoordinates(board);
        System.out.println("Black piece at: " + blackChecker.getCord());
        System.out.println("Red piece at: " + redChecker.getCord());

        boolean result = BotII.isInDanger(blackChecker, board);
        System.out.println("Is black piece in danger? " + result);
        assertTrue(result, "Checker should be in danger");
    }

    // Helper method to print board with coordinates
    private void printBoardWithCoordinates(Board board) {
        System.out.println("  0 1 2 3 4 5 6 7  <- X axis");
        for (int y = 0; y < 8; y++) {
            System.out.print(y + " ");
            for (int x = 0; x < 8; x++) {
                Checker c = board.checkerBoard[y][x];
                System.out.print(c == null ? "·" : 
                            c.getColor() == Color.BLACK ? "B" : "R");
                System.out.print(" ");
            }
            System.out.println();
        }
        System.out.println("Y axis");
    }

    private void clearBoard(Board board) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board.checkerBoard[i][j] = null;
            }
        }
    }


    @Test
    public void testGetSafeMoves() {
        // Create empty board
        Board board = new Board();
        clearBoard(board);

        // Place a black checker at (3, 3)
        Checker blackChecker = new Checker(new Cord(1, 3), Color.BLACK);
        board.checkerBoard[3][1] = blackChecker;

        // Place a red checker at (2,2)
        Checker redChecker = new Checker(new Cord(0, 2), Color.RED);
        board.checkerBoard[2][0] = redChecker;    

        System.out.println("\nBoard state in testGetSafeMoves:");
        printBoardWithCoordinates(board);
        System.out.println("Black piece at: " + blackChecker.getCord());
        System.out.println("Red piece at: " + redChecker.getCord());

        // Get safe moves for black checker
        ArrayList<Cord> safeMoves = BotII.getSafeMoves(blackChecker, board);

        // Print the safe moves found
        System.out.println("Safe moves found:");
        for (Cord move : safeMoves) {
            System.out.println(move);
        }

        assertTrue(!safeMoves.isEmpty(), "There should be at least one safe move");
    }
    
    //@Disabled("Disabled until we can fix this test")
    @Test
    public void testMakeValidMove() {
        Board board = new Board();
        printBoardWithCoordinates(board);
        BotII.Move bestMove = BotII.makeValidMove(board);
        System.out.println("Black move from" + bestMove.piece.getCord() + " -> to: " + bestMove.destination);
        assertTrue(bestMove != null, "An offensive move should be found");
    }

    @Test
    public void testMakeBestMove() {
        Board board = new Board();
        printBoardWithCoordinates(board);
        BotII.Move bestMove = BotII.makeBestMove(board);
        assertTrue(bestMove != null, "An offensive move should be found");
        System.out.println("Black move from" + bestMove.piece.getCord() + " -> to: " + bestMove.destination);
    }
}