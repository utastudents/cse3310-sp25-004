package uta.cse3310.GamePlay;
import java.util.ArrayList;
/*
 * this class creates and changes the checker board
 * checks if moves are valid
 */
public class Board 
{
    //Variable for 2D array board
	
	
	public Board()
	{
        //Initialize class with board array and inital checker positions

	}
	
	/*
	 * checks if a move is valid
	 * piece is the selected checker piece
	 * dest is selected square
	 */
	public boolean isValidMove(Checker piece, Cord dest) 
	{
		boolean isValid = false; //base case
		//Check if the piece becomes a king

		//CODE
		
		return isValid;
	}
	
	/*
	 * called by isValidMove to check for more
	 * possible jumps
	 */
	public Cord isValidJump(Checker piece, Cord dest, Cord jump) 
	{
		//CODE
		
		return jump;
	}
	
	

}
