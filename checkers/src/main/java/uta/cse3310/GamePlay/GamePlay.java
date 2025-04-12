package uta.cse3310.GamePlay;
//import uta.cse3310.GameTermination.GameTermination;

import java.util.ArrayList;

//Main Class holding everything
public class GamePlay 
{
    private int GameID;
    private Board board;
    //private GameTermination termination;

    public GamePlay(int id) 
    {
        this.GameID = id;
        this.board= new Board();
        //Initialize class with GameID and create starting board
    }

    /*public idkyet gameTermCheck(Board board)
    {
        //call for endGame method
        var return=endGame(board);

        //could do a try and catch method if they decide to use a throw to make terminate


    }
    */

    public int move(Checker piece, Cord dest) 
    {
        // Return values
        // 2 = Move is valid and piece was moved. Any pieces jumped are removed from the board.
        // 1 = Invalid move. The player must choose a valid jump.
        // 0 = Invalid move for any other reason

        int result = 0;
        boolean forceJump = false; // Set To true if a jump is found. 
        //create two ArrayLists for possible jumps
        ArrayList<Cord> possibleJumpsForward = null;
        ArrayList<Cord> possibleJumpsBackward = null;

        if(piece.getColor() == Color.BLACK || piece.isKing())
        {
            possibleJumpsForward = board.getPossibleForwardJump(piece);
            if(possibleJumpsForward.size() > 0)
            {
                forceJump = true;
            }
        }
        
        if(piece.getColor() == Color.RED || piece.isKing())
        {
            possibleJumpsBackward = board.getPossibleBackwardJump(piece);
            if(possibleJumpsBackward.size() > 0)
            {
                forceJump = true; 
            }
        }
    
        if(forceJump == true) // If the piece can jump
        {
            if(piece.isKing() == false) // Man piece jump code 
            {
                if(piece.getColor() == Color.BLACK ) // Black Man piece jump code
                {
                    int cordIter = board.checkForwardJump(possibleJumpsForward, dest);
    
                    if (cordIter != -1)
                    {
                        Cord newPos = possibleJumpsForward.get(cordIter);
                        board.updatePosition(piece, newPos);
    
                       // TODO: Remove jumped piece / pieces
                    }   
                }
                else
                {
                    result = 1;
                }

                if(piece.getColor() == Color.RED ) // Red Man piece jump code
                {
                    int cordIter = board.checkBackwardJump(possibleJumpsBackward, dest);
    
                    if (cordIter != -1)
                    {
                        Cord newPos = possibleJumpsBackward.get(cordIter);
                        int x = piece.getCord().getX();
                        int y = piece.getCord().getY();
    
                        board.updatePosition(piece, newPos);
    
                       // TODO: Remove jumped piece / pieces
                    }   
                }
                else
                {
                    result = 1;
                }
            }

            int cordIndexfwd = board.checkForwardJump(possibleJumpsForward, dest); // Index of forward jump if found
            int cordIndexbwd = board.checkBackwardJump(possibleJumpsBackward, dest); // Index of backward jump if found

            if(cordIndexfwd != -1)
            {
                Cord newPos = possibleJumpsForward.get(cordIndexfwd);
                board.updatePosition(piece, newPos);

               // TODO: Remove jumped piece / pieces
            }
            else if (cordIndexbwd != -1)
            {
                Cord newPos = possibleJumpsBackward.get(cordIndexbwd);
                board.updatePosition(piece, newPos);

                // TODO: Remove jumped piece / pieces
            }
            else
            {
                result = 1; 
            }
        }
        else // If there are no jumps to be made.
        {
            if(piece.isKing() == false) // Man piece move code 
            {
                if(piece.getColor() == Color.BLACK) // Black piece move code
                {
                    if(board.moveForwardCheck(piece, dest) == true)
                    {
                        board.updatePosition(piece, dest);
                        result = 2;
                    }
                    else
                    {
                        result = 1;
                    }
                }
                else if(piece.getColor() == Color.RED) // Red piece move code
                {
                    if(board.moveBackwardCheck(piece, dest) == true)
                    {
                        board.updatePosition(piece, dest);
                        result = 2;
                    }
                    else
                    {
                        result = 1;
                    }
                }
            }
            else // King piece move code
            {
                if(piece.getColor() == Color.BLACK) // Black King piece move code
                {
                    if(board.moveForwardCheck(piece, dest) == true || board.moveBackwardCheck(piece, dest) == true)
                    {
                        board.updatePosition(piece, dest);
                        result = 2;
                    }
                    else
                    {
                        result = 1;
                    }
                }
            }
        }
        //TODO: Send Game Termination end board
        return result;
    }

    public Board getBoard() 
    {
        //Return Board
        return board;
    }
}
