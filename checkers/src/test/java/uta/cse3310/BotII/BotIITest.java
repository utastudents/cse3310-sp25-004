package uta.cse3310.BotII;
import java.util.ArrayList;

//import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
//import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import uta.cse3310.Bot.BotII.BotII;
import uta.cse3310.GamePlay.Board;
import uta.cse3310.GamePlay.Checker;
import uta.cse3310.GamePlay.Color;
import uta.cse3310.GamePlay.Cord;
//import uta.cse3310.GamePlay.GamePlay;



public class BotIITest {

    @Test
    public void TestDefendPieces() {
        // Create empty board
        Board board = new Board();
        clearBoard(board);
        
        // Black piece at column 3, row 4 (x=3, y=4) - bottom area
        Checker blackChecker = new Checker(new Cord(3, 4), Color.BLACK);
        board.checkerBoard[4][3] = blackChecker;

        Checker blackChecker2 = new Checker(new Cord(3, 6), Color.BLACK);
        board.checkerBoard[6][3] = blackChecker2;

        // Red piece at column 2, row 3 (x=2, y=3) - above black (can jump down)
        Checker redChecker1 = new Checker(new Cord(2, 3), Color.RED);
        board.checkerBoard[3][2] = redChecker1;

        Checker redChecker2 = new Checker(new Cord(4, 3), Color.RED);
        board.checkerBoard[3][4] = redChecker2;

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
        Checker redChecker = new Checker(new Cord(4, 5), Color.RED);
        board.checkerBoard[5][4] = redChecker;
        redChecker.setKing(true);

        // Empty landing spot at column 4, row 5 (x=4, y=5)
        // board.checkerBoard[5][4] = null;

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
        board.checkerBoard[2][1] = null;
        Checker redChecker = new Checker(new Cord(2, 3), Color.RED);
        board.checkerBoard[3][2] = redChecker;
        printBoardWithCoordinates(board);
        BotII.Move bestMove = BotII.makeBestMove(board);
        assertTrue(bestMove != null, "An offensive move should be found");
        System.out.println("Black move from" + bestMove.piece.getCord() + " -> to: " + bestMove.destination);
    }

    @Test
    public void testBlackPieceCapture() {
        Board board = new Board();
        clearBoard(board);
        
        // Black piece at (3,3) with red at (4,4)
        board.checkerBoard[4][4] = new Checker(new Cord(4, 4), Color.BLACK);
        board.checkerBoard[3][3] = new Checker(new Cord(3, 3), Color.RED); // Opponent
        printBoardWithCoordinates(board);
        
        BotII.Move move = BotII.capturePiece(board);
        assertNotNull(move);
        //assertEquals(new Cord(2, 2), move.destination); // Should capture downward
        System.out.println("From: (4, 4) To: " + move.destination);
    }

    @Test
    public void testBlackKingCapture() {
        Board board = new Board();
        clearBoard(board);
        
        // Black king at (2,2) with red at (1,1)
        Checker king = new Checker(new Cord(0, 0), Color.BLACK);
        king.setKing(true);
        board.checkerBoard[0][0] = king;
        board.checkerBoard[1][1] = new Checker(new Cord(1, 1), Color.RED);
        printBoardWithCoordinates(board);
        
        BotII.Move move = BotII.capturePiece(board);
        assertNotNull(move);
        //assertEquals(new Cord(2, 2), move.destination); // King can capture upward
        System.out.println("From: " + king.getCord() + " To: " + move.destination);
    }

    @Test
    public void testMakeKingMove() {
        Board board = new Board();
        clearBoard(board);
        
        Checker king = new Checker(new Cord(1, 1), Color.BLACK);
        king.setKing(true);
        board.checkerBoard[1][1] = king;
        board.checkerBoard[4][2] = new Checker(new Cord(2, 4), Color.RED);
        board.checkerBoard[6][6] = new Checker(new Cord(6, 6), Color.BLACK);

        printBoardWithCoordinates(board);
        
        BotII.Move move = BotII.makeBestMove(board);
        //System.out.println("Move" + moves.get(1));
        // for (Cord move : moves) {
        //     System.out.println("Move: " + move);
        // }
        //assertNotNull(moves);
        assertTrue(move != null);
    }

}