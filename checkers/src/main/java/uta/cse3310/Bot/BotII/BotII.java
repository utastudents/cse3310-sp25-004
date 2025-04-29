package uta.cse3310.Bot.BotII;

import java.util.ArrayList;

import uta.cse3310.Bot.Bot;
import uta.cse3310.GameManager.Game;
import uta.cse3310.GamePlay.Board;
import uta.cse3310.GamePlay.Checker;
import uta.cse3310.GamePlay.Color;
import uta.cse3310.GamePlay.Cord;
import uta.cse3310.GamePlay.GamePlay;
import uta.cse3310.PageManager.GameMove;
import uta.cse3310.PageManager.PageManager;
import java.lang.Math;
//import uta.cse3310.GameState;

public class BotII extends Bot {

    public BotII() {
        this.playerId = -2; //Bots are -1, -2. Might change this later
    }

	// private Game game; // Game game is declared in the abstract Player super class

    //private static Color botColor = Color.BLACK; // Initializing with a default value
    //private static boolean beAggressive = false; // Flag to determine if the bot should be aggressive
    private static boolean attackSide = false; // True will be for left and False for right side
    
    public static Move makeValidMove(Board board) {
        Move bestMove = null;
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                Checker checker = board.checkerBoard[y][x];
                if (bestMove == null && checker != null && checker.isKing() && 
                    checker.getColor() == Color.BLACK) {
                    //stops failing since there was no null check before
                    ArrayList<Cord> kingMoves = getKingMoves(board, checker);
                    if (kingMoves != null) {
                        for (Cord move : kingMoves) {
                            if (bestMove == null) {
                                bestMove = new Move(checker, move);
                                System.out.println("adding move to king move logic");
                            }
                        }
                    }
                    else {
                        System.out.println("kingMoves is null");;
                    }
                }
                else if (checker != null && checker.getColor() == Color.BLACK && !checker.isKing()) {
                    ArrayList<Cord> safeMoves = getSafeMoves(checker, board);
                    for (Cord move : safeMoves) {
                        if (bestMove == null && wouldBeInDangerAfterMove(checker, move, board) == false) {
                            bestMove = new Move(checker, move);
                            System.out.println("adding move to man move logic");
                        }
                        // else if (bestMove == null && wouldBeInDangerAfterMove(checker, move, board)) {
                        //     bestMove = new Move(checker, move);
                        // }
                    }
                }
            }
        }
        return bestMove;
    }

    public static ArrayList<Cord> getKingMoves (Board board, Checker king) {
        ArrayList<Cord> kingMoves = new ArrayList<>();
        ArrayList<Cord> allRedPieces = new ArrayList<>();
        Checker redChecker = null;
        Cord priorityPiece = null;
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                redChecker = board.checkerBoard[y][x];
                if (redChecker != null && redChecker.getColor() == Color.RED) {
                    allRedPieces.add(redChecker.getCord());
                    System.out.println("RedChecker: " + redChecker.getCord());
                }
            }
        }
        int[] p = new int[12];
        int i = 0; 
        for (Cord priority : allRedPieces) {
            int x1 = priority.getX();
            int y1 = priority.getY();
            int x2 = king.getCord().getX();
            int y2 = king.getCord().getY();
            x2 -= x1;
            y2 -= y1;
            x2 = Math.abs(x2);
            y2 = Math.abs(y2);

            p[i] = x2 > y2 ? x2 : y2;

            i++;
        }
        int priority = 8;
        for (int a = 0; a < i; a++) {
            if (p[a] < priority) {
                priority = p[a];
                priorityPiece = allRedPieces.get(a);
            } 
        }

        int[][] directions = {
            // direction of man pieces relative to our piece
            {-1, -1},   // bottom-right
            {1, -1},   // bottom-left
            // direction of king pieces relative to our piece
            {-1, 1},   // top-right
            {1, 1}   // top-left
        };
        int cordx = king.getCord().getX();
        int cordy = king.getCord().getY();
        for (int[] dir : directions) { 
            int jumpX = cordx - dir[0];
            int jumpY = cordy - dir[1];
            int x1 = priorityPiece.getX();
            int y1 = priorityPiece.getY();

            Cord kCord = new Cord(jumpX, jumpY);

            if (priority > 2 && inBounds(jumpX, jumpY) && wouldBeInDangerAfterMove(king, kCord, board) == false) {
                boolean jumpSpaceEmpty = board.checkerBoard[jumpY][jumpX] == null;
                if (jumpSpaceEmpty) {
                    System.out.println("Space is empty: " + kCord);
                }
                jumpX -= x1;
                jumpY -= y1;
                jumpX = Math.abs(jumpX);
                jumpY = Math.abs(jumpY);
                int tempPriority = jumpX > jumpY ? jumpX : jumpY;
                System.out.println("New Priority: " + tempPriority);
                System.out.println("Old priority: " + priority);
                if (jumpSpaceEmpty && tempPriority < priority) {
                    kingMoves.add(kCord);
                    System.out.println("A coordinate was added" + kCord);
                }
                else {
                    System.out.println("Not this move: " + kCord);
                }
            }
            else {
                System.out.println("Not this move: " + kCord);
            }
        }
        return kingMoves;
    }

    public static Move defendPieces(Board board) {
        // TODO: Prioritize defending own pieces over offense
        // TODO: Try to block opponent from advancing or jumping
        Move bestMove = null;
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                Checker checker = board.checkerBoard[y][x];
                Checker blockingChecker = null;
                if (y < 6) {
                    blockingChecker = board.checkerBoard[y+2][x];
                }
                if (checker != null && checker.getColor() == Color.BLACK) {
                    if (isInDanger(checker, board)) {
                        ArrayList<Cord> safeMoves = getSafeMoves(checker, board);
                        for (Cord move : safeMoves) {
                            System.out.println(y);
                            if (bestMove == null && !wouldBeInDangerAfterMove(checker, move, board)) {
                                bestMove = new Move(checker, move);
                            } 
                        }
                        if (bestMove == null && blockingChecker != null) {
                            bestMove = blockAttack(blockingChecker);
                        }
                    }
                }
            }
        }
        return bestMove;
    }

    public static Move blockAttack(Checker checker) {
        int x = checker.getCord().getX();
        int y = checker.getCord().getY();
        
        if (attackSide && inBounds(x+1, y-1)) {
            Cord dest = new Cord(x+1, y-1);
            return new Move(checker, dest);
        }
        else if (!attackSide && inBounds(x-1, y-1)) {
            Cord dest = new Cord(x-1, y-1);
            return new Move(checker, dest);
        }
        
        return null;
    }
    
    /**
     * Checks if the checker can be jumped by any red piece.
     */
    public static boolean isInDanger(Checker checker, Board board) {
        // get the coordinates from the specific piece we are looking at
        int x = checker.getCord().getX();
        int y = checker.getCord().getY();
        
    
        // Check all four possible jump directions incase of king pices
        int[][] directions = {
            // direction of man pieces relative to our piece
            {-1, -1},   // bottom-right
            {1, -1},   // bottom-left
            // direction of king pieces relative to our piece
            {-1, 1},   // top-right
            {1, 1}   // top-left
        };
        // created an iterator in order to differentiate the pieces between kings and mans
        int it = 0;
    
        for (int[] dir : directions) {
            int attackX = x + dir[0];
            int attackY = y + dir[1];
            int jumpX = x - dir[0];
            int jumpY = y - dir[1];
    
            // Check bounds
            if (inBounds(attackX, attackY) && inBounds(jumpX, jumpY)) {
                // Check if there's an opponent piece that can jump us
                Checker attacker = board.checkerBoard[attackY][attackX];
                boolean jumpSpaceEmpty = board.checkerBoard[jumpY][jumpX] == null;
    
                if (attacker != null && attacker.getColor() == Color.RED && jumpSpaceEmpty && it < 2) {
                    switch (it) {
                        case 0:
                            attackSide = true;
                            break;
                        case 1:
                            attackSide = false;
                            break;
                        case 2: 
                            attackSide = true;
                            break;
                        case 3: 
                            attackSide = false;
                            break;
                    }
                    System.out.println("The attacker is on the left side: " + attackSide);
                    return true;
                }
                if (attacker != null && attacker.getColor() == Color.RED && jumpSpaceEmpty && attacker.isKing()) {
                    switch (it) {
                        case 0:
                            attackSide = true;
                            break;
                        case 1:
                            attackSide = false;
                            break;
                        case 2: 
                            attackSide = true;
                            break;
                        case 3: 
                            attackSide = false;
                            break;
                    }
                    return true;
                }
            }
            it++;
        }
        System.out.println("Not in danger");
        return false;
    }

    /**
     * Finds all legal safe moves for the checker.
     */
    public static ArrayList<Cord> getSafeMoves(Checker checker, Board board) {
        ArrayList<Cord> safeMoves = new ArrayList<>();
        int x = checker.getCord().getX();
        int y = checker.getCord().getY();
        int temp = 0;

        // For BLACK pieces, safe moves are backward
        if(checker.getColor() == Color.BLACK) {
            int[][] moves = {{-1,-1}, {1,-1}, {-1, 1}, {1, 1}}; // all sides
            
            for(int[] m : moves) {
                int nx = x + m[0];
                int ny = y + m[1];
                if (inBounds(nx, ny) && board.checkerBoard[ny][nx] == null && temp < 2) {
                    safeMoves.add(new Cord(nx, ny));
                }
                if (checker.isKing() && temp >= 2) {
                    safeMoves.add(new Cord(nx, ny));
                }
                temp++;
            }
        }
        return safeMoves;
    }

    /**
     * Checks if moving to a given destination would put the checker in danger.
     */
    private static boolean wouldBeInDangerAfterMove(Checker piece, Cord dest, Board board) {
        Checker temp = new Checker(dest, Color.BLACK);
        System.out.println("Checking...");
        return isInDanger(temp, board);
    }

        /** Utility to check if board coordinates are valid. */
    private static boolean inBounds(int x, int y) {
        if (x >= 0 && x < 8 && y >= 0 && y < 8) {
            System.out.println("move is in bounds");
        }
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
    
    public static Move capturePiece(Board board) {
        Move bestCapture = null;
        
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                Checker piece = board.checkerBoard[y][x];
                if (piece != null && piece.getColor() == Color.BLACK) {
                    ArrayList<Cord> jumps = getPossibleJumps(piece, board);
                    if (!jumps.isEmpty()) {
                        bestCapture = new Move(piece, jumps.get(0)); // Take first available jump
                        break; // Prioritize earliest found capture
                    }
                }
            }
        }
        return bestCapture;
    }
    
    private static ArrayList<Cord> getPossibleJumps(Checker piece, Board board) {
        ArrayList<Cord> jumps = new ArrayList<>();
        int x = piece.getCord().getX();
        int y = piece.getCord().getY();

        // BLACK pieces move upward (y decreases)
        int[][] directions = piece.isKing() ? 
            new int[][]{{-1,-1}, {1,-1}, {-1,1}, {1,1}} : // Kings can move all directions
            new int[][]{{-1,-1}, {1,-1}}; // Regular pieces move upward only

        for (int[] dir : directions) {
            int newX = x + dir[0]*2;
            int newY = y + dir[1]*2;
            
            if (isValidJump(board, x, y, dir[0], dir[1])) {
                jumps.add(new Cord(newX, newY));
            }
        }
        return jumps;
    }

    private static boolean isValidJump(Board board, int x, int y, int dx, int dy) {
        int midX = x + dx;
        int midY = y + dy;
        int destX = x + dx*2;
        int destY = y + dy*2;

        return inBounds(destX, destY) &&
               board.checkerBoard[midY][midX] != null && 
               board.checkerBoard[midY][midX].getColor() != Color.BLACK && 
               board.checkerBoard[destY][destX] == null;
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

    // public static boolean adjustStrategy(Board board) {
    //     // When the opponent has 3 points more than us, adjustStrategy changes to more offensive
    //     // TODO: Change strategy based on early, mid, or late game
    //     // Early: Moving first row pieces?
    //     // Second: A King comes into play?
    //     // Late: A select # of pieces left on the board?
    //     //Board board = game.getBoard().getBoard();
    //     int myCount = 0;
    //     int oppCount = 0;

    //     for (int y = 0; y < 8; y++) {
    //         for (int x = 0; x < 8; x++) {
    //             Checker c = board.checkerBoard[y][x];
    //             if (c != null) {
    //                 if (c.getColor() == botColor) {
    //                     myCount++;
    //                 } else {
    //                     oppCount++;
    //                 }
    //             }
    //         }
    //     }

    //     // if opponent has 3 or more pieces than us, go aggressive
    //     beAggressive = oppCount - myCount >= 3;

    //     // (Optional) print for debugging
    //     System.out.println("BotII strategy: " + (beAggressive ? "Aggressive" : "Defensive"));
    //     return beAggressive;
    // }

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

    public static Move makeBestMove (Board board) {
        Move bestMove = null;
        boolean fJump = board.hasJump(Color.BLACK);
        
        if (fJump) {
            bestMove = capturePiece(board);
        }
        else if (bestMove == null) {
            bestMove = defendPieces(board);
        }
        if (bestMove == null) {
            bestMove = makeValidMove(board);
        }
        return bestMove;
    }
    
    private GameMove finalMove(GamePlay gp) {
        // Omar: dont forget to update the board
        Board board = game.getBoard().getBoard();
        Move fM = makeBestMove(board);
        Cord from = fM.piece.getCord();
        Cord to = fM.destination;
        return new GameMove(this.playerId, this.game.getGameID(), from.getX(), from.getY(), to.getX(), to.getY(), "black");
    }

    @Override
    public boolean makeMove(GamePlay gp) {

        GameMove move = finalMove(gp);

        try {
            Thread.sleep(1000); // Sleep 100 milliseconds to break the infinite recursion
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
       
        PageManager.Gm.processMove(move, gp);
        return true;
    }

    @Override
    public boolean updateBoard(GamePlay gs) {
        //Can be ignored
        return true;
    }
    @Override
    public boolean startGame(Game g) {
        this.game = g;
        return true;
    }

    @Override
    public boolean endGame(GamePlay gs) {
        return true; //Can be ignored
    }

}
