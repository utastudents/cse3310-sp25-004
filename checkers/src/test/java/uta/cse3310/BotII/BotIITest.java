package uta.cse3310.BotII;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

import uta.cse3310.Bot.BotII.BotII;
import uta.cse3310.GamePlay.Board;
import uta.cse3310.GamePlay.Checker;
import uta.cse3310.GamePlay.Color;
import uta.cse3310.GamePlay.Cord;

public class BotIITest {

    @Test
	void Makemove() {
		
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

    @Test
    void capturePieces() {

    }
    
}
