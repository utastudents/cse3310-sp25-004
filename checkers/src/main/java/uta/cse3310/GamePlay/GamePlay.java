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
        int result = 0;
        ArrayList<Cord> possibleJumpsForward = board.getPossibleForwardJump(piece);
        ArrayList<Cord> possibleJumpsBackward = board.getPossibleBackwardJump(piece); //create arrays for possible jumps for that piece

        //checks if moves are valid
        if(board.moveForwardCheck(piece, dest) == true || board.moveBackwardCheck(piece, dest)  == true || board.checkForwardJump(possibleJumpsForward, dest)  != -1 || board.checkBackwardJump(possibleJumpsBackward, dest) != -1)
        {
            //free arrays lists and update a temp checker piece with new cord and create new arraylist to check for additional jumps



            //check if there are valid jumps that can be done or what not
            if(board.checkForwardJump(possibleJumpsForward, dest) != -1 || board.checkBackwardJump(possibleJumpsBackward, dest) != -1)
            {
                result=1;
            }
            else
            {
                result=2;
            }
        }

        else
        {
            result=0;
        }

        //Call Board's move action based on type then forcefully check for jumps
        //Send Game Termination end board
        return result;
    }

    public Board getBoard() 
    {
        //Return Board
        return board;
    }
}
