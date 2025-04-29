package uta.cse3310.Bot.BotI; 


import java.util.ArrayList; 
import java.util.Random;

import uta.cse3310.Bot.Bot;
import uta.cse3310.GameManager.Game;
import uta.cse3310.GamePlay.Board; 
import uta.cse3310.GamePlay.Checker;
import uta.cse3310.GamePlay.Color;
import uta.cse3310.GamePlay.Cord; 
import uta.cse3310.GamePlay.GamePlay; 
import uta.cse3310.PageManager.GameMove;
import uta.cse3310.PageManager.PageManager;

public class BotI extends Bot {
    
    private Board board;
    private Color color;
    private Game game;
    private Random random;
    private boolean gameEnded = false;
    
    // if there are multiple moves the bot can make during its turn and 
    // these moves are all equally "beneficial" i guess, it randomly chooses one of those moves
    public BotI() {
        super();
        this.random = new Random();
        this.playerId = -1;
    } 

     //sets up the botn with a specific and color - like red or black
    public BotI(Game game, Color color){
        super();
        this.game = game;
        this.color = color;
        this.board = game.getBoard().getBoard();
        this.random = new Random();
        this.playerId = -1;
    }
    
     //this method checks what move to make, its made strategically, 
    // it goes through different checks/options and then makes it decisions, explained below
    @Override
    public boolean makeMove(GamePlay gs){
        //if game ends bot cant make a move
        if (gameEnded) {
            return false;
        }

        //checks the available peices 
        ArrayList<Checker> availableCheckers = getAvailableCheckers(); 
        if (availableCheckers.isEmpty()) { 
            return false; 
        } 
        
        //looks for jump moves that capture opponent
        ArrayList<Move> jumpMoves = getAllJumpMoves(availableCheckers); 
        if (!jumpMoves.isEmpty()) { 
            Move bestJumpMove = selectBestJumpMove(jumpMoves); //picks the best jump option if its available
            if (bestJumpMove.jumpSequence != null && bestJumpMove.jumpSequence.size() > 2) {
                for (int i = 0; i < bestJumpMove.jumpSequence.size() - 1; i++) {
                    Cord start = bestJumpMove.jumpSequence.get(i);
                    Cord end = bestJumpMove.jumpSequence.get(i + 1);
                    GameMove move = new GameMove(this.playerId, this.game.getGameID(), start.getX(), start.getY(),
                                                end.getX(), end.getY(), "black");
                    PageManager.Gm.processMove(move, gs);
                }
                return true;
            } else {
                GameMove move = new GameMove(this.playerId, this.game.getGameID(), bestJumpMove.piece.getCord().getX(), 
                                            bestJumpMove.piece.getCord().getY(), bestJumpMove.destination.getX(), 
                                            bestJumpMove.destination.getY(), "black");
                PageManager.Gm.processMove(move, gs);
                return true;
            }
        }
         
         //if theres no jump moves,  just look for regular moves
         ArrayList<Move> moves = getAllMoves(availableCheckers); 
         if (!moves.isEmpty()) { 
            //picks the best move for regular moves
            Move bestMove = selectBestMove(moves); 
            GameMove move =  new GameMove( this.playerId, this.game.getGameID(), 
                                    bestMove.piece.getCord().getX(), bestMove.piece.getCord().getY(), 
                                    bestMove.destination.getX(), bestMove.destination.getY(), "black");
            PageManager.Gm.processMove(move, gs); 
            return true; 
        } 
        return false; //if theres no moves at all
    } 
       



    //updates where the peices are on the board
    @Override 
    public boolean updateBoard(GamePlay gs) {
        
        if (game != null) {
            this.board = game.getBoard().getBoard();
        }
        return true;
        
    } 

    //this function is called when the game is over
    @Override 
    public boolean endGame(GamePlay gs) {
        
        if (game != null) {
            this.board = game.getBoard().getBoard();
        }
        gameEnded = true;
        return true;
        
    } 
    



    //finds all checkers that are available on the board
    protected ArrayList<Checker> getAvailableCheckers() { 
        //creates a new list to store our checkers
        ArrayList<Checker> checkers = new ArrayList<>(); 
        if (board == null) { //if the board doesnt exist, return empty list
            return checkers; 
        } 

        //just checking the board
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

    

    // Finds all possible jump moves (capturing opponent pieces)
    private ArrayList<Move> getAllJumpMoves(ArrayList<Checker> checkers) { 

        ArrayList<Move> jumpMoves = new ArrayList<>(); 
        if (board == null) { 
            return jumpMoves; 
        } 

        for (Checker checker : checkers) { 
            ArrayList<Cord> jumps; 

            //jumps are based on checker color, black - moves forward, and red moves backward
            if (checker.getColor() == Color.BLACK) { 
                jumps = board.getPossibleForwardJump(checker); 
            } else { 
                jumps = board.getPossibleBackwardJump(checker); 
            } 

            //looks for multiple jumps
            for (Cord jump : jumps) { 
                ArrayList<Cord> jumpSequence = new ArrayList<>();
                jumpSequence.add(checker.getCord());
                jumpSequence.add(jump);
                findMultJumps(checker, jump, jumpSequence, jumpMoves);
            } 
        } 
        
        return jumpMoves; 
    } 

    private void findMultJumps(Checker checker, Cord currentPos, ArrayList<Cord> jumpSequence, ArrayList<Move> jumpMoves) {
        jumpMoves.add(new Move(checker, jumpSequence.get(jumpSequence.size()-1), true, new ArrayList<>(jumpSequence)));

        Checker tempChecker = new Checker(currentPos, checker.getColor());
        tempChecker.setKing(checker.isKing());

        ArrayList<Cord> nextJumps;
        if (tempChecker.getColor() == Color.BLACK) {
            nextJumps = board.getPossibleForwardJump(tempChecker);
        } else {
            nextJumps = board.getPossibleBackwardJump(tempChecker);
        } 

        if (tempChecker.isKing()) {
            ArrayList<Cord> kingJumps;
            if (tempChecker.getColor() == Color.BLACK) {
                kingJumps = board.getPossibleBackwardJump(tempChecker);
            } else {
                kingJumps = board.getPossibleForwardJump(tempChecker);
            }
            nextJumps.addAll(kingJumps);
        }

        for (Cord nextJump : nextJumps) {
            if (!jumpSequence.contains(nextJump)) { 
                jumpSequence.add(nextJump);
                findMultJumps(checker, nextJump, jumpSequence, jumpMoves);
                jumpSequence.remove(jumpSequence.size()-1);
            }
        }
    }
   




    // finds all possible moves
    private ArrayList<Move> getAllMoves(ArrayList<Checker> checkers){
	    ArrayList<Move> moves = new ArrayList<>(); 

        if (board == null) { //returns empty list if the board is null
            return moves; 
        } 


        for (Checker checker : checkers) { 
            //get all possible moves
            ArrayList<Cord> possibleMoves = getPossibleMoves(checker); 
            for (Cord move : possibleMoves) { 
                moves.add(new Move(checker, move, false)); //adds each possible destination as a move
            } 
        } 
        return moves; 
    } 

 
    // gets all possible mvoes for a single checker piece
    private ArrayList<Cord> getPossibleMoves(Checker piece) { 
        ArrayList<Cord> moves = new ArrayList<>(); 

        if (board == null) { 
            return moves; 
        } 

        int x = piece.getCord().getX(); 
        int y = piece.getCord().getY(); 


        //checks if the piece is a king -- meaning it can mov both forward and backward
        if (piece.isKing()) { 
            //checks forward-right move
            if (board.moveForwardCheck(piece, new Cord(x + 1, y + 1))) { 
                moves.add(new Cord(x + 1, y + 1)); 
            } 
            //checks forward-left
            if (board.moveForwardCheck(piece, new Cord(x - 1, y + 1))) { 
                moves.add(new Cord(x - 1, y + 1)); 
            } 

            
            //checks backward-riht
            if (board.moveBackwardCheck(piece, new Cord(x + 1, y - 1))) { 
                moves.add(new Cord(x + 1, y - 1)); 
            } 
            //chcks backward-left
            if (board.moveBackwardCheck(piece, new Cord(x - 1, y - 1))) { 
                moves.add(new Cord(x - 1, y - 1)); 
            } 

        }  

       //handles all non king peices- meaning the regular ones that cna only move iin one direction
        else { 
            //check moves for black pieces - forward moves
            if (piece.getColor() == Color.BLACK) { 
                //checks forward-right move
                if (board.moveForwardCheck(piece, new Cord(x + 1, y + 1))) { 
                    moves.add(new Cord(x + 1, y + 1)); 
                }
                //checks forward-left move
                if (board.moveForwardCheck(piece, new Cord(x - 1, y + 1))) { 
                    moves.add(new Cord(x - 1, y + 1)); 
                } 

            } else { 
                //checks backward-right move
                if (board.moveBackwardCheck(piece, new Cord(x + 1, y - 1))) { 
                    moves.add(new Cord(x + 1, y - 1)); 
                } 
                //checks backward-left move
                if (board.moveBackwardCheck(piece, new Cord(x - 1, y - 1))) { 
                    moves.add(new Cord(x - 1, y - 1)); 
                } 
            } 
        } 
        return moves; 
    } 

  
        
    // Picks best jump move - prefers making kings and capturing kings
    private Move selectBestJumpMove(ArrayList<Move> jumpMoves) {
        if (jumpMoves.size() == 1) {
            return jumpMoves.get(0);
        }

        int maxJumps = 0;
        ArrayList<Move> longJumps = new ArrayList<>();
        for (Move move : jumpMoves) {
            int jumps;
            if (move.jumpSequence != null) {
                jumps = move.jumpSequence.size() - 1;
            } else {
                jumps = 1;
            }
            
            if (jumps > maxJumps) {
                maxJumps = jumps;
                longJumps.clear();
                longJumps.add(move);
            } else if (jumps == maxJumps) {
                longJumps.add(move);
            }
        }
        
        ArrayList<Move> kingJumps = new ArrayList<>();
        for (Move move : longJumps) {
            if (wouldBecomeKing(move)) {
                kingJumps.add(move);
            }
        }

        if (!kingJumps.isEmpty()) {
            return kingJumps.get(random.nextInt(kingJumps.size()));
        }
        
        ArrayList<Move> kingCaptureJumps = new ArrayList<>();
        for (Move move : longJumps) {
            if (capturesKing(move)) {
                kingCaptureJumps.add(move);
            }
        }

        if (!kingCaptureJumps.isEmpty()) {
            return kingCaptureJumps.get(random.nextInt(kingCaptureJumps.size()));
        }
        
        return longJumps.get(random.nextInt(longJumps.size()));
    }
 
    
    // Picks best regular move - prefers making kings, staying safe, and advancing
    private Move selectBestMove(ArrayList<Move> moves) {
	// If there's only one move, it'll just do that one
        if (moves.size() == 1) {
            return moves.get(0);
        }
       
	//First check for moves that make us a king choose the best option! 
        ArrayList<Move> kingMoves = new ArrayList<>();
        for (Move move : moves) {
            if (wouldBecomeKing(move)) {
                kingMoves.add(move);
            }
        }
       
	// If we found king moves, pick one randomly 
        if (!kingMoves.isEmpty()) {
            return kingMoves.get(random.nextInt(kingMoves.size()));
        }
       
	// Next look for safe moves where we won't get captured right away 
        ArrayList<Move> safeMoves = new ArrayList<>();
        for (Move move : moves) {
            if (isSafeMove(move)) {
                safeMoves.add(move);
            }
        }
       
	// If safe moves exist, pick one randomly 
        if (!safeMoves.isEmpty()) {
            return safeMoves.get(random.nextInt(safeMoves.size()));
        }
       
	// Then check for moves that move us forward toward being king 
        ArrayList<Move> advancingMoves = new ArrayList<>();
        for (Move move : moves) {
            if (isAdvancingMove(move)) {
                advancingMoves.add(move);
            }
        }
       
	// If we have advancing moves, pick one randomly 
        if (!advancingMoves.isEmpty()) {
            return advancingMoves.get(random.nextInt(advancingMoves.size()));
        }

	// If all else fails, just pick any random move
        return moves.get(random.nextInt(moves.size()));
    }
 
    
    // Checks if a move would make the piece a king
    private boolean wouldBecomeKing(Move move) {
	// Can't become king if already a king
        if (move.piece.isKing()) {
            return false;
        }

	// Black pieces become kings at the top row (y=7)
        if (move.piece.getColor() == Color.BLACK && move.destination.getY() == 7) {
            return true;
        }

	// Red pieces become kings at the bottom row (y=0)
        if (move.piece.getColor() == Color.RED && move.destination.getY() == 0) {
            return true;
        }
        return false;
    }


    // Checks if a jump would capture an opponent's king
    private boolean capturesKing(Move move) 
    { 
        if (board == null) 
        { 
        return false; 
        }
	 
        if (move.jumpSequence != null && move.jumpSequence.size() > 2) {

            for (int i = 0; i < move.jumpSequence.size() - 1; i++) {
                Cord start = move.jumpSequence.get(i);
                Cord end = move.jumpSequence.get(i + 1);
                int capturedX = (start.getX() + end.getX()) / 2;
                int capturedY = (start.getY() + end.getY()) / 2;
                Checker capturedPiece = board.checkSpace(new Cord(capturedX, capturedY));
                if (capturedPiece != null && capturedPiece.isKing()) {
                    return true;
                }
            }
            return false;
        } else {
            int capturedX = (move.piece.getCord().getX() + move.destination.getX()) / 2;
            int capturedY = (move.piece.getCord().getY() + move.destination.getY()) / 2;
            Checker capturedPiece = board.checkSpace(new Cord(capturedX, capturedY));
            return capturedPiece != null && capturedPiece.isKing();
        }
    } 

    // Checks if a move is safe from being captured
    private boolean isSafeMove(Move move) 
    { 
        if (board == null)
        { 
        return true; 
        }
	// Look at all four diagonal squares around new position 
        for (int dy = -1; dy <= 1; dy += 2)
        { 
            for (int dx = -1; dx <= 1; dx += 2)
            { 
                int x = move.destination.getX() + dx; 
                int y = move.destination.getY() + dy; 
                if (x >= 0 && x < 8 && y >= 0 && y < 8)
                { 
                    Checker adjacentPiece = board.checkSpace(new Cord(x, y));
			
			// If there's an enemy piece that can capture us 
                    if (adjacentPiece != null && adjacentPiece.getColor() != color)
                    {
			// Kings can capture any direction 
                        if (adjacentPiece.isKing() || 
			// Black can only capture downward 
                        (adjacentPiece.getColor() == Color.BLACK && y < move.destination.getY()) ||
			// Red can only capture upward 
                        (adjacentPiece.getColor() == Color.RED && y > move.destination.getY())) 
                        {
		        	// Check if we have a friend behind us to block 
                            int behindX = move.destination.getX() - dx; 
                            int behindY = move.destination.getY() - dy; 
                            if (behindX >= 0 && behindX < 8 && behindY >= 0 && behindY < 8)
                            { 
                                Checker behindPiece = board.checkSpace(new Cord(behindX, behindY)); 
                                if (behindPiece != null && behindPiece.getColor() == color) 
                                { 
                                    return true; // Return's true when protected 
                                } 
                            } 
                            return false;  // Return's false if in danger 
                        } 
                    } 
                } 
            } 
        } 
        return true; // No threats found 
    }
          
    // Checks if a move helps advance toward opponent's side
    private boolean isAdvancingMove(Move move)
    {
	// Kings don't need to advance 
        if (move.piece.isKing()) {
            return false; 
        }
	// Black moves up the board (higher y values) 
        if (move.piece.getColor() == Color.BLACK)  {
            return move.destination.getY() > move.piece.getCord().getY(); 
        }
	// Red moves down the board (lower y values) 
        else { 
            return move.destination.getY() < move.piece.getCord().getY(); 
        } 
    }  

    // Keeps track of move information (which piece, where it's going, if it's a jump)
    private class Move
    { 
        Checker piece;  // The piece being moved 
        Cord destination; // Where it's going 
        boolean isJump; // True if it's a jump/capture
        ArrayList<Cord> jumpSequence;

        Move(Checker piece, Cord destination, boolean isJump){
            this(piece, destination, isJump, null);
        }
 
        Move(Checker piece, Cord destination, boolean isJump, ArrayList<Cord> jumpSequence) { 
            this.piece = piece; 
            this.destination = destination; 
            this.isJump = isJump; 
            this.jumpSequence = jumpSequence;
        } 
    } 
}
    























