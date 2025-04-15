package uta.cse3310.Bot.BotII;

import uta.cse3310.GameState;
import uta.cse3310.Bot.Bot;
import uta.cse3310.GameManager.Game;
//import uta.cse3310.GamePlay.GamePlay;
import uta.cse3310.GamePlay.Board;
import uta.cse3310.GamePlay.Checker;
import uta.cse3310.GamePlay.Color;
import uta.cse3310.GamePlay.Cord;
import java.util.ArrayList;

/*enum Color {
    BLACK
}*/
public class BotII extends Bot {
	
	//private GamePlay gamePlay;
    //private Color botColor = Color.BLACK; // Default color for the bot

    /*
    public BotII(GamePlay gamePlay, Color botColor) {
        this.gamePlay = gamePlay; // Initialize the game play
        this.botColor = botColor; // Set the bot's color
    }
    */
    
    public void makeValidMove() {
        // TODO: Logic to choose and make a legal move
    }
    public void promoteToKing() {
        // TODO: Check if a piece reached the end and promote to king / Allow more than one king on board
    }
        public static Move defendPieces(Board board) {
        // TODO: Prioritize defending own pieces over offense
        // TODO: Try to block opponent from advancing or jumping
        Move bestMove = null;

        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                Checker checker = board.checkerBoard[y][x];

                if (checker != null && checker.getColor() == Color.BLACK) {
                    if (isInDanger(checker, board)) {
                        ArrayList<Cord> safeMoves = getSafeMoves(checker, board);

                        for (Cord move : safeMoves) {
                            if (bestMove == null) {
                                bestMove = new Move(checker, move);
                            } else {
                                // Prioritize backward movement if near being jumped
                                if (move.getY() > checker.getCord().getY()) {
                                    bestMove = new Move(checker, move);
                                }
                            }
                        }
                    }
                }
            }
        }
        return bestMove;
    }
    /**
     * Checks if the checker can be jumped by any red piece.
     */
    private static boolean isInDanger(Checker checker, Board board) {
        int x = checker.getCord().getX();
        int y = checker.getCord().getY();

        // Check if a red piece could jump this checker
        int[][] dirs = { {-1, -1}, {1, -1} };
        for (int[] d : dirs) {
            int rx = x + d[0];
            int ry = y + d[1];
            int jx = x - d[0];
            int jy = y - d[1];

            if (inBounds(rx, ry) && inBounds(jx, jy)) {
                Checker red = board.checkerBoard[ry][rx];
                if (red != null && red.getColor() == Color.RED && board.checkerBoard[jy][jx] == null) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Finds all legal safe moves for the checker.
     */
    private static ArrayList<Cord> getSafeMoves(Checker checker, Board board) {
        ArrayList<Cord> safeMoves = new ArrayList<>();
        int x = checker.getCord().getX();
        int y = checker.getCord().getY();

        int[][] moves = { {-1, 1}, {1, 1} }; // forward directions for black

        for (int[] m : moves) {
            int nx = x + m[0];
            int ny = y + m[1];
            if (inBounds(nx, ny) && board.checkerBoard[ny][nx] == null) {
                // Simulate move and check if it leads to danger
                Cord simulatedCord = new Cord(nx, ny);
                if (!wouldBeInDangerAfterMove(checker, simulatedCord, board)) {
                    safeMoves.add(simulatedCord);
                }
            }
        }

        return safeMoves;
    }

    /**
     * Checks if moving to a given destination would put the checker in danger.
     */
    private static boolean wouldBeInDangerAfterMove(Checker piece, Cord dest, Board board) {
        Checker temp = new Checker(dest, piece.getColor());
        return isInDanger(temp, board);
    }

    /** Utility to check if board coordinates are valid. */
    private static boolean inBounds(int x, int y) {
        return x >= 0 && x < 8 && y >= 0 && y < 8;
    }

    /**
     * Simple data structure to store a move.
     */
    public static class Move {
        public Checker piece;
        public Cord destination;

        public Move(Checker piece, Cord destination) {
            this.piece = piece;
            this.destination = destination;
        }
    }
    public void capturePiece() {
        // TODO: Capture opponent piece by jumping diagonally (Call method)
    }
    public void checkMultipleJumps() {
        // TODO: If multiple jumps are possible, do them
    }
    public void moveChecker() {
        // TODO: Move a non-king piece diagonally one square
    }
    public void moveKing() {
        // TODO: Move a king piece diagonally one square in any direction
    }
    public boolean isMoveLegal() {
        // TODO: Validate if a move is legal
        return true;
    }
    public void waitForOpponent() {
        // TODO: Wait for opponent to make a move before acting
    }
      public void adjustStrategy() {
        // When the opponent has 3 points more than us, adjustStrategy changes to more offensive
        // TODO: Change strategy based on early, mid, or late game
        // Early: Moving first row pieces?
        // Second: A King comes into play?
        // Late: A select # of pieces left on the board?
    }
    public void findOffensiveMove() {
        // TODO: Decide if it's safe and smart to attack
    }
     public boolean isPieceCaptured(int pieceId) {
        // TODO: Check if a piece has been captured
        return false;
    }
    public void protectBackLine() {
        // TODO: Avoid moving back row pieces unless necessary to stop other player from getting king pieces
    }

    @Override
    public boolean makeMove(GameState gs) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'makeMove'");
    }
    @Override
    public boolean updateBoard(GameState gs) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateBoard'");
    }
    @Override
    public boolean startGame(Game g) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'startGame'");
    }

}
