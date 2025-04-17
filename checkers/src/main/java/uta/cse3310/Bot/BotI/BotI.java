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

    private ArrayList<Cord> getPossibleMoves(Checker piece){} 

    private Move selectBestJumpMove(ArrayList<Move> jumpMoves) {return jumpMoves;}
 
    private Move selectBestMove(ArrayList<Move> moves) {}
 
    private boolean wouldBecomeKing(Move move) {}

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
