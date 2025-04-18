package uta.cse3310.Bot.BotII;

import java.util.ArrayList;

import uta.cse3310.Bot.Bot;
import uta.cse3310.GameManager.Game;
import uta.cse3310.GamePlay.Board;
import uta.cse3310.GamePlay.Checker;
import uta.cse3310.GamePlay.Color;
import uta.cse3310.GamePlay.Cord;
import uta.cse3310.GamePlay.GamePlay;
import uta.cse3310.GameState;

public class BotII extends Bot {
	private GamePlay gamePlay;
	private Game game;
    private Color botColor = Color.BLACK; // Initializing with a default value
    private boolean beAggressive = false; // Flag to determine if the bot should be aggressive

    public BotII(GamePlay gamePlay, Color botColor) {
        this.gamePlay = gamePlay;
        this.botColor = botColor;
    }

    // Method to make a defensive move
    public void makeDefensiveMove() {
        Board board = gamePlay.getBoard();  // Get the current board
        ArrayList<Checker> botCheckers = getBotCheckers(board); // Get the bot's pieces
        boolean moveMade = false;

        for (Checker checker : botCheckers) {
            if (!moveMade) {
                // Try to move each piece defensively
                moveMade = attemptDefensiveMove(checker, board);
            }
        }
    }

    // Get all the Bot II's checkers (either RED or BLACK)
    private ArrayList<Checker> getBotCheckers(Board board) {
        ArrayList<Checker> botCheckers = new ArrayList<>();

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Checker checker = board.checkSpace(new Cord(col, row));
                if (checker != null && checker.getColor() == botColor) {
                    botCheckers.add(checker);
                }
            }
        }
        return botCheckers;
    }
	
    //tests with a test checker piece if move willput piece in danger by calling isInDanger
   /* private boolean isMoveSafe(Checker checker, Board board Cord dest) {
  	Checker testChecker= new Checker(dest, Color.BLACK);
   	return !isInDanger(testChecker, board)
    } */

    // Attempt to make a defensive move for a given checker
    private boolean attemptDefensiveMove(Checker checker, Board board) {
        ArrayList<Cord> possibleMoves = new ArrayList<>();

        // Generate possible defensive moves based on the piece type (man or king)
        if (checker.isKing()) {
            possibleMoves = getPossibleKingMoves(checker, board);
        } else {
            possibleMoves = getPossibleManMoves(checker, board);
        }

        // Try each move and choose the safest one
        for (Cord dest : possibleMoves) {
             // if (isMoveSafe(checker, dest, board)) { // doesn't compile
                board.updatePosition(checker, dest);  // Execute the move
                return true;
            //}
        }

        return false;
    }

    // Get possible moves for a king piece
    private ArrayList<Cord> getPossibleKingMoves(Checker checker, Board board) {
        /*
        ArrayList<Cord> possibleMoves = new ArrayList<>();
        Cord cord = checker.getCord();

        // Check both forward and backward diagonals
        for (int x = -1; x <= 1; x += 2) {
            for (int y = -1; y <= 1; y += 2) {
                Cord newCord = new Cord(cord.getX() + x, cord.getY() + y);
                if (board.moveForwardCheck(checker, newCord) || board.moveBackwardCheck(checker, newCord)) {
                    possibleMoves.add(newCord);
    */
        return null;
    }
    public BotII() {
        super(); // Calls the constructor of the parent class which is Bot
    }
    
    public void makeValidMove() {
    // try normal move if nothing else works
    Board board = game.getBoard().getBoard();

        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                Checker c = board.checkerBoard[y][x];
                if (c != null && c.getColor() == botColor) {
                    ArrayList<Cord> moves = new ArrayList<>();

                    if (botColor == Color.BLACK || c.isKing()) {
                        checkMove(board, c, -1, 1, moves);
                        checkMove(board, c, 1, 1, moves);
                    }
                    if (botColor == Color.RED || c.isKing()) {
                        checkMove(board, c, -1, -1, moves);
                        checkMove(board, c, 1, -1, moves);
                    }

                    if (!moves.isEmpty()) {
                        Cord dest = moves.get(0);
                        board.updatePosition(c, dest);
                        promoteToKing(); // crown if needed
                        return;
                    }
                }
            }
        }
    }

    public void promoteToKing() {
    // go through all my pieces and promote if they reached end
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                Checker c = game.getBoard().getBoard().checkerBoard[y][x];
                if (c != null && c.getColor() == botColor) {
                    if (!c.isKing()) {
                        if (botColor == Color.BLACK && c.getCord().getY() == 7) {
                            c.setKing(true);
                        }
                        if (botColor == Color.RED && c.getCord().getY() == 0) {
                            c.setKing(true);
                        }
                    }
                }
            }
        }
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
    public static boolean isInDanger(Checker checker, Board board) {
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
    public static ArrayList<Cord> getSafeMoves(Checker checker, Board board) {
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
    Board board = game.getBoard().getBoard();

    for (int y = 0; y < 8; y++) {
        for (int x = 0; x < 8; x++) {
            Checker piece = board.checkerBoard[y][x];
            if (piece != null && piece.getColor() == botColor) {
                ArrayList<Cord> jumps = new ArrayList<>();

                if (botColor == Color.BLACK || piece.isKing()) {
                    jumps.addAll(board.getPossibleForwardJump(piece));
                }
                if (botColor == Color.RED || piece.isKing()) {
                    jumps.addAll(board.getPossibleBackwardJump(piece));
                }

                if (!jumps.isEmpty()) {
                    Cord jumpDest = jumps.get(0);
                    board.updatePosition(piece, jumpDest); // Just jump
                    board.kingMe(piece);
                    return;
                }
            }
        }
    }
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
    Board board = game.getBoard().getBoard();
    int myCount = 0;
    int oppCount = 0;

    for (int y = 0; y < 8; y++) {
        for (int x = 0; x < 8; x++) {
            Checker c = board.checkerBoard[y][x];
            if (c != null) {
                if (c.getColor() == botColor) {
                    myCount++;
                } else {
                    oppCount++;
                }
            }
        }
    }

    // if opponent has 3 or more pieces than us, go aggressive
    if (oppCount - myCount >= 3) {
        beAggressive = true;
    } else {
        beAggressive = false;
    }

    // (Optional) print for debugging
    System.out.println("BotII strategy: " + (beAggressive ? "Aggressive" : "Defensive"));
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
        if (this.game == null) return false;

        Board board;
        try {
            board = this.game.getBoard().getBoard();
        } catch (Exception e) {
            return false;
        }

        if (board == null) return false;

        // get my pieces
        ArrayList<Checker> myPieces = new ArrayList<>();
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                Checker c = board.checkerBoard[y][x];
                if (c != null && c.getColor() == this.botColor) {
                    myPieces.add(c);
                }
            }
        }

        //TODO send possibleMoves to GameManager

        return true;
    }

    // Get possible moves for a man piece
    private ArrayList<Cord> getPossibleManMoves(Checker checker, Board board) {
        /* THIS CODE DOES NOT COMPILE
        ArrayList<Cord> possibleMoves = new ArrayList<>();
        Cord cord = checker.getCord();

        // If the piece is black, it moves forward (up the board)
        if (checker.getColor() == Color.BLACK) {
            Cord newCord = new Cord(cord.getX() + 1, cord.getY() - 1);
            if (board.moveForwardCheck(checker, newCord)) {
                possibleMoves.add(newCord);
            }
            newCord = new Cord(cord.getX() - 1, cord.getY() - 1);
            if (board.moveForwardCheck(checker, newCord)) {
                possibleMoves.add(newCord);
            }
        } 
        // If the piece is red, it moves backward (down the board)
        else if (checker.getColor() == Color.RED) {
            Cord newCord = new Cord(cord.getX() + 1, cord.getY() + 1);
            if (board.moveBackwardCheck(checker, newCord)) {
                possibleMoves.add(newCord);
            }
            newCord = new Cord(cord.getX() - 1, cord.getY() + 1);
            if (board.moveBackwardCheck(checker, newCord)) {
                possibleMoves.add(newCord);

        if (myPieces.isEmpty()) return;

        // check if jump possible
        for (Checker piece : myPieces) {
            ArrayList<Cord> jumps = new ArrayList<>();
            if (this.botColor == Color.BLACK || piece.isKing()) {
                jumps.addAll(board.getPossibleForwardJump(piece));
            }
            if (this.botColor == Color.RED || piece.isKing()) {
                jumps.addAll(board.getPossibleBackwardJump(piece));
            }

            if (!jumps.isEmpty()) {
                Cord jumpDest = jumps.get(0);
                board.updatePosition(piece, jumpDest);
                board.kingMe(piece);
                return;
            }
        }

        // try to defend
        Move move = defendPieces(board);
        if (move != null) {
            board.updatePosition(move.piece, move.destination);
            board.kingMe(move.piece);
            return;
        }

        // just move normally
        for (Checker piece : myPieces) {
            ArrayList<Cord> moves = new ArrayList<>();
            if (this.botColor == Color.BLACK || piece.isKing()) {
                checkMove(board, piece, -1, 1, moves);
                checkMove(board, piece, 1, 1, moves);
            }
            if (this.botColor == Color.RED || piece.isKing()) {
                checkMove(board, piece, -1, -1, moves);
                checkMove(board, piece, 1, -1, moves);
            }

            if (!moves.isEmpty()) {
                Cord dest = moves.get(0);
                board.updatePosition(piece, dest);
                board.kingMe(piece);
                return;
            }
        } */
        return null;
    }

        // Helper method to check if a move is valid and add it to the list
    private void checkMove(Board board, Checker checker, int dx, int dy, ArrayList<Cord> moves) {
    int newX = checker.getCord().getX() + dx;
    int newY = checker.getCord().getY() + dy;

    if (newX >= 0 && newX < 8 && newY >= 0 && newY < 8 && board.checkerBoard[newY][newX] == null) {
        moves.add(new Cord(newX, newY));
    }
    }

    /*
    @Override
    public boolean makeMove(GameState gs) {
        makeMove(); // call your actual move logic
        return true;
    }
     */

    @Override
    public boolean updateBoard(GameState gs) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateBoard'");
    }
    @Override
    public boolean startGame(Game g) {
        this.game = g;
        return true;
}

    @Override
    public boolean endGame(GameState gs) {
        return true; //Can be ignored
    }

}
