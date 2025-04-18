package uta.cse3310.Bot.BotI; 


import uta.cse3310.GameState; 
import uta.cse3310.Bot.Bot;
import uta.cse3310.Bot.BotII.BotII.Move;
import uta.cse3310.GameManager.Game; 
import uta.cse3310.GamePlay.Board; 
import uta.cse3310.GamePlay.Checker;
import uta.cse3310.GamePlay.Color;
import uta.cse3310.GamePlay.Cord; 
import uta.cse3310.GamePlay.GamePlay; 
import uta.cse3310.PairUp.Player; 
import java.util.ArrayList; 
import java.util.Random; 
import java.util.List; 

public class BotI extends Bot {
    
    private Board board;
    private Color color;
    private Game game;
    private Random random;
    
    public BotI() {
        super();
        this.random = new Random();
    } 

    public BotI(Game game, Color color){
        super();
        this.game = game;
        this.color = color;
        this.board = game.getBoard().getBoard();
        this.random = new Random();
    }
    
    @Override
    public boolean makeMove(GameState gs){
        /*
        ArrayList<Checker> availableCheckers = getAvailableCheckers(); 
        if (availableCheckers.isEmpty()) { 
            return false; 
        } 
        
         ArrayList<Move> jumpMoves = getAllJumpMoves(availableCheckers); 
         if (!jumpMoves.isEmpty()) { 
            Move bestJumpMove = selectBestJumpMove(jumpMoves); 
            executeMove(bestJumpMove); 
            return true; 
        } 
         
         ArrayList<Move> moves = getAllMoves(availableCheckers); 

         if (!moves.isEmpty()) { 
            Move bestMove = selectBestMove(moves); 
            executeMove(bestMove); 
            return true; 
        } 

        return false; 

    } 
        */
        return false;
    }


    @Override 
    public boolean updateBoard(GameState gs) {
        /*
        if (game != null) {
            this.board = game.getBoard().getBoard();
        }
        return true;
        */
        return false;
    } 

    @Override 
    public boolean endGame(GameState gs) {
        /*
        if (game != null) {
            this.board = game.getBoard().getBoard();
        }
        return true;
        */
        return false;
    } 
    /* 

    // Get all checkers of the bot's color 

    protected ArrayList<Checker> getAvailableCheckers() { 
        
        ArrayList<Checker> checkers = new ArrayList<>(); 
        
        // If board is null, return empty list 
        if (board == null) { 
            return checkers; 
        } 

        for (int y = 0; y < 8; y++) { 
            for (int x = 0; x < 8; x++) { 
                Checker checker = board.checkSpace(new Cord(x, y)); 
                if (checker != null && checker.getColor() == color) { 
                    checkers.add(checker); 
                } 
            } 
        } 
        return checkers; 
    }  

    // Get all possible jump moves for all checkers 

    private ArrayList<Move> getAllJumpMoves(ArrayList<Checker> checkers) { 

        ArrayList<Move> jumpMoves = new ArrayList<>(); 

        // If board is null, return empty list 

        if (board == null) { 

            return jumpMoves; 
        } 

        for (Checker checker : checkers) { 

            ArrayList<Cord> jumps; 

            if (checker.getColor() == Color.BLACK) { 

                jumps = board.getPossibleForwardJump(checker); 

            } else { 

                jumps = board.getPossibleBackwardJump(checker); 
            } 

            for (Cord jump : jumps) { 

                jumpMoves.add(new Move(checker, jump, true)); 
            } 
        } 
        
        return jumpMoves; 
    } 

   // Get all possible moves for all checkers
    private ArrayList<Move> getAllMoves(ArrayList<Checker> checkers){
	
	ArrayList<Move> moves = new ArrayList<>(); 
        
	// If board is null, return empty list 

        if (board == null) { 

            return moves; 
        } 

        for (Checker checker : checkers) { 

            ArrayList<Cord> possibleMoves = getPossibleMoves(checker); 

            for (Cord move : possibleMoves) { 

                moves.add(new Move(checker, move, false)); 

            } 

        } 
        return moves; 
} 

 // Get all possible moves for a single checker 

    private ArrayList<Cord> getPossibleMoves(Checker piece) { 

        ArrayList<Cord> moves = new ArrayList<>(); 

        // If board is null, return empty list 

        if (board == null) { 

            return moves; 

        } 

        int x = piece.getCord().getX(); 

        int y = piece.getCord().getY(); 

        // For kings, check both forward and backward moves 

        if (piece.isKing()) { 

            // Forward moves 

            if (board.moveForwardCheck(piece, new Cord(x + 1, y + 1))) { 

                moves.add(new Cord(x + 1, y + 1)); 

            } 

            if (board.moveForwardCheck(piece, new Cord(x - 1, y + 1))) { 

                moves.add(new Cord(x - 1, y + 1)); 

            } 

            // Backward moves 

            if (board.moveBackwardCheck(piece, new Cord(x + 1, y - 1))) { 

                moves.add(new Cord(x + 1, y - 1)); 

            } 

            if (board.moveBackwardCheck(piece, new Cord(x - 1, y - 1))) { 

                moves.add(new Cord(x - 1, y - 1)); 

            } 

        }  

        // For regular pieces, only check moves in their direction 

        else { 

            if (piece.getColor() == Color.BLACK) { 

                if (board.moveForwardCheck(piece, new Cord(x + 1, y + 1))) { 

                    moves.add(new Cord(x + 1, y + 1)); 

                } 

                if (board.moveForwardCheck(piece, new Cord(x - 1, y + 1))) { 

                    moves.add(new Cord(x - 1, y + 1)); 

                } 

            } else { 

                if (board.moveBackwardCheck(piece, new Cord(x + 1, y - 1))) { 

                    moves.add(new Cord(x + 1, y - 1)); 

                } 

                if (board.moveBackwardCheck(piece, new Cord(x - 1, y - 1))) { 

                    moves.add(new Cord(x - 1, y - 1)); 

                } 

            } 

        } 

        return moves; 
    } 

  
        // select the best jump move based on strategy
    private Move selectBestJumpMove(ArrayList<Move> jumpMoves) {
        // if there's only one jump move, return it
        if (jumpMoves.size() == 1) {
            return jumpMoves.get(0);
        }
        
        // prioritize jumps that lead to kings
        ArrayList<Move> kingJumps = new ArrayList<>();
        for (Move move : jumpMoves) {
            if (wouldBecomeKing(move)) {
                kingJumps.add(move);
            }
        }
        
        if (!kingJumps.isEmpty()) {
            return kingJumps.get(random.nextInt(kingJumps.size()));
        }
        
        // prioritize jumps that capture kings
        ArrayList<Move> kingCaptureJumps = new ArrayList<>();
        for (Move move : jumpMoves) {
            if (capturesKing(move)) {
                kingCaptureJumps.add(move);
            }
        }
        
        if (!kingCaptureJumps.isEmpty()) {
            return kingCaptureJumps.get(random.nextInt(kingCaptureJumps.size()));
        }
        
        // otherwise, choose a random jump move
        return jumpMoves.get(random.nextInt(jumpMoves.size()));
    }
 
    // select the best move based on strategy
    private Move selectBestMove(ArrayList<Move> moves) {
        // if there's only one move, return it
        if (moves.size() == 1) {
            return moves.get(0);
        }
        
        // prioritize moves that lead to kings
        ArrayList<Move> kingMoves = new ArrayList<>();
        for (Move move : moves) {
            if (wouldBecomeKing(move)) {
                kingMoves.add(move);
            }
        }
        
        if (!kingMoves.isEmpty()) {
            return kingMoves.get(random.nextInt(kingMoves.size()));
        }
        
        // prioritize moves that protect pieces
        ArrayList<Move> safeMoves = new ArrayList<>();
        for (Move move : moves) {
            if (isSafeMove(move)) {
                safeMoves.add(move);
            }
        }
        
        if (!safeMoves.isEmpty()) {
            return safeMoves.get(random.nextInt(safeMoves.size()));
        }
        
        // prioritize moves that advance pieces toward the opponent's side
        ArrayList<Move> advancingMoves = new ArrayList<>();
        for (Move move : moves) {
            if (isAdvancingMove(move)) {
                advancingMoves.add(move);
            }
        }
        
        if (!advancingMoves.isEmpty()) {
            return advancingMoves.get(random.nextInt(advancingMoves.size()));
        }
        
        // otherwise, choose a random move
        return moves.get(random.nextInt(moves.size()));
    }
 
    // check if a move would result in the piece becoming a king
    private boolean wouldBecomeKing(Move move) {
        if (move.piece.isKing()) {
            return false;
        }
        
        if (move.piece.getColor() == Color.BLACK && move.destination.getY() == 7) {
            return true;
        }
        
        if (move.piece.getColor() == Color.RED && move.destination.getY() == 0) {
            return true;
        }
        
        return false;
    }


    private boolean capturesKing(Move move) 
    {
     // If board is null, return false 
    if (board == null) 
    { 
     return false; 
    }  
    int capturedX = (move.piece.getCord().getX() + move.destination.getX()) / 2; 
    int capturedY = (move.piece.getCord().getY() + move.destination.getY()) / 2; 
    Checker capturedPiece = board.checkSpace(new Cord(capturedX, capturedY)); 
    return capturedPiece != null && capturedPiece.isKing(); 
    } 

    private boolean isSafeMove(Move move) 
    { 
        if (board == null)
        { 
        return true; 
        } 
        for (int dy = -1; dy <= 1; dy += 2)
        { 
            for (int dx = -1; dx <= 1; dx += 2)
            { 
                int x = move.destination.getX() + dx; 
                int y = move.destination.getY() + dy; 
                if (x >= 0 && x < 8 && y >= 0 && y < 8)
                { 
                Checker adjacentPiece = board.checkSpace(new Cord(x, y)); 
                if (adjacentPiece != null && adjacentPiece.getColor() != color)
                { 
                if (adjacentPiece.isKing() ||  
                (adjacentPiece.getColor() == Color.BLACK && y < move.destination.getY()) || 
                (adjacentPiece.getColor() == Color.RED && y > move.destination.getY())) 
                { 
                    int behindX = move.destination.getX() - dx; 
                    int behindY = move.destination.getY() - dy; 
                    if (behindX >= 0 && behindX < 8 && behindY >= 0 && behindY < 8)
                    { 
                        Checker behindPiece = board.checkSpace(new Cord(behindX, behindY)); 
                        if (behindPiece != null && behindPiece.getColor() == color) 
                        { 
                            return true; 
                        } 
                    } 
                    return false; 
                } 
                } 
                } 
            } 
        } 
            return true; 
        }  
private boolean isAdvancingMove(Move move)
{ 
    if (move.piece.isKing()) 
    { 
        return false; // Kings can move in any direction 
    } 
    if (move.piece.getColor() == Color.BLACK)
    { 
        return move.destination.getY() > move.piece.getCord().getY(); 
    } 
    else
    { 
        return move.destination.getY() < move.piece.getCord().getY(); 
    } 
}  
private void executeMove(Move move)
{ 
    if (board != null)
    { 
        board.updatePosition(move.piece, move.destination); 
        board.kingMe(move.piece); 
    } 
    } 
    private class Move 
    { 
        Checker piece; 
        Cord destination; 
        boolean isJump; 
        Move(Checker piece, Cord destination, boolean isJump)
        { 
            this.piece = piece; 
            this.destination = destination; 
            this.isJump = isJump; 
        } 
    } 
} 
private void executeMove(Move move)
{ 
    if (board != null)
    { 
        board.updatePosition(move.piece, move.destination); 
        board.kingMe(move.piece); 
    } 
} 
private class Move
{ 
    Checker piece; 
    Cord destination; 
    boolean isJump; 
    Move(Checker piece, Cord destination, boolean isJump)
    { 
        this.piece = piece; 
        this.destination = destination; 
        this.isJump = isJump; 
    } 
} 
} 
*/
}
