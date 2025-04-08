package uta.cse3310.GamePlay;
import java.util.ArrayList;
/*
 * this class creates and changes the checker board
 * checks if moves are valid
 */
public class Board 
{
    //Variable for 2D array board

	private ArrayList<ArrayList<Checker>> board; // List of checkers on the board split by color
	private ArrayList<Checker> redCheckers; // list of red checkers
	private ArrayList<Checker> blackCheckers; // list of black checkers

	public Board()
	{
        //Initialize class with board array and inital checker positions

		initCheckers(Color.RED);
		initCheckers(Color.BLACK);
		board.add(redCheckers);
		board.add(blackCheckers);
	}
	
	private void initCheckers(Color color) // Takes in a color and adds the inital checker of that color
	// TODO Refactor initCheckers function to use loops instead of hardcoding the values
	{
		if (color == Color.RED) 
		{
			redCheckers.add(new Checker(new Cord(1, 0), color));
			redCheckers.add(new Checker(new Cord(3, 0), color));
			redCheckers.add(new Checker(new Cord(5, 0), color));
			redCheckers.add(new Checker(new Cord(7, 0), color));
			redCheckers.add(new Checker(new Cord(0, 1), color));
			redCheckers.add(new Checker(new Cord(2, 1), color));
			redCheckers.add(new Checker(new Cord(4, 1), color));
			redCheckers.add(new Checker(new Cord(6, 1), color));
			redCheckers.add(new Checker(new Cord(1, 2), color));
			redCheckers.add(new Checker(new Cord(3, 2), color));
			redCheckers.add(new Checker(new Cord(5, 2), color));
			redCheckers.add(new Checker(new Cord(7, 2), color));

		} 
		else if (color == Color.BLACK) 
		{
			blackCheckers.add(new Checker(new Cord(1, 5), color));
			blackCheckers.add(new Checker(new Cord(3, 5), color));
			blackCheckers.add(new Checker(new Cord(5, 5), color));
			blackCheckers.add(new Checker(new Cord(7, 5), color));
			blackCheckers.add(new Checker(new Cord(0, 6), color));
			blackCheckers.add(new Checker(new Cord(2, 6), color));
			blackCheckers.add(new Checker(new Cord(4, 6), color));
			blackCheckers.add(new Checker(new Cord(6, 6), color));
			blackCheckers.add(new Checker(new Cord(1, 7), color));
			blackCheckers.add(new Checker(new Cord(3, 7), color));
			blackCheckers.add(new Checker(new Cord(5, 7), color));
			blackCheckers.add(new Checker(new Cord(7, 7), color));
		}

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
