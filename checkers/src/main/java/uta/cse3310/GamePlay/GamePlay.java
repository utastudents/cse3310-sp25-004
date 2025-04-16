package uta.cse3310.GamePlay;


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

    /*public int gameTermCheck(Board board)
    {

        //return 0 if game does not end
        //call for endGame method
        if ()

        //could do a try and catch method if they decide to use a throw to make terminate


    } */
   //we don't use the game object so we'll let manager handle this
    

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

                    // requested destination is a valid jump
                    if (cordIter != -1)
                    {
                        Cord newPos = possibleJumpsForward.get(cordIter);
                        board.removeJumpedChecker(piece, newPos);
                        board.updatePosition(piece, newPos);
                        concurrentJumps(piece);
                        board.kingMe(piece);
                    }  
                    else
                    {
                        result = 1;
                    } 
                }

                if(piece.getColor() == Color.RED ) // Red Man piece jump code
                {
                    int cordIter = board.checkBackwardJump(possibleJumpsBackward, dest);

                    if (cordIter != -1)
                    {
                        Cord newPos = possibleJumpsBackward.get(cordIter);
                        board.removeJumpedChecker(piece, newPos);
                        board.updatePosition(piece, newPos);
                        concurrentJumps(piece);
                        board.kingMe(piece);
                    }   
                    else
                    {
                        result = 1;
                    }
                }
            }
            else
            {
                int cordIndexfwd = board.checkForwardJump(possibleJumpsForward, dest); // Index of forward jump if found
                int cordIndexbwd = board.checkBackwardJump(possibleJumpsBackward, dest); // Index of backward jump if found

                if(cordIndexfwd != -1)
                {
                    Cord newPos = possibleJumpsForward.get(cordIndexfwd);
                    board.removeJumpedChecker(piece, newPos);
                    board.updatePosition(piece, newPos);
                    concurrentJumps(piece);
                }
                else if (cordIndexbwd != -1)
                {
                    Cord newPos = possibleJumpsBackward.get(cordIndexbwd);
                    board.removeJumpedChecker(piece, newPos);
                    board.updatePosition(piece, newPos);
                    concurrentJumps(piece);
                }
                else
                {
                    result = 1; 
                }   
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
                        board.kingMe(piece);
                        result = 2;
                    }
                    else
                    {
                        result = 0;
                    }
                }
                else if(piece.getColor() == Color.RED) // Red piece move code
                {
                    if(board.moveBackwardCheck(piece, dest) == true)
                    {
                        board.updatePosition(piece, dest);
                        board.kingMe(piece);
                        result = 2;
                    }
                    else
                    {
                        result = 0;
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
                        result = 0;
                    }
                }
            }
        }
        //TODO: Send Game Termination end board
        return result;
    }
    //Allows the piece to automatically take direct jumps after a jump by the player
    public int concurrentJumps(Checker piece)
    {
        ArrayList<Cord> possibleJumpsForward;
        ArrayList<Cord> possibleJumpsBackward;
        int result = 0;
        if (piece.isKing())
        {
            possibleJumpsForward = board.getPossibleForwardJump(piece);
            possibleJumpsBackward = board.getPossibleBackwardJump(piece);
            if (possibleJumpsForward.size() + possibleJumpsBackward.size() > 1)
            {
                return 1;
            }
            else if (possibleJumpsForward.size() == 1)
            {
                move(piece, possibleJumpsForward.get(0));
            }
            else if (possibleJumpsBackward.size() == 1)
            {
                move(piece, possibleJumpsBackward.get(0));
            }
            else
            {
                return 0;
            }
        }
        else if (piece.getColor() == Color.BLACK)
        {
            possibleJumpsForward = board.getPossibleForwardJump(piece);
            if (possibleJumpsForward.size() > 1)
            {
                return 1;
            }
            else if (possibleJumpsForward.size() == 1)
            {
                move(piece, possibleJumpsForward.get(0));
            }
            else
            {
                return 0;
            }
        }
        else if (piece.getColor() == Color.RED)
        {
            possibleJumpsBackward = board.getPossibleBackwardJump(piece);
            if (possibleJumpsBackward.size() > 1)
            {
                return 1;
            }
            else if (possibleJumpsBackward.size() == 1)
            {
                move(piece, possibleJumpsBackward.get(0));
            }
            else
            {
                return 0;
            }
        }
        return 0;
    }

    public Board getBoard() 
    {
        //Return Board
        return board;
    }
}
