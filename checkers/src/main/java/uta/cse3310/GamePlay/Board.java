package uta.cse3310.GamePlay;
import java.util.ArrayList;
/*
 * this class creates and changes the checker board
 * checks if moves are valid
 * Forward is defined a moving up the board from black to red
 * Backward is defined as moving down the board from red to black
 */
public class Board 
{
    //Variable for 2D array board

	private ArrayList<ArrayList<Checker>> board; // List of checkers on the board split by color
	private ArrayList<Checker> redCheckers; // list of red checkers
	private ArrayList<Checker> blackCheckers; // list of black checkers

	public Board()
	{
        //Initialize class with board array and initialize checker positions

		initCheckers(Color.RED);
		initCheckers(Color.BLACK);
		board.add(redCheckers);
		board.add(blackCheckers);
	}

	public Checker checkSpace(Cord Cord) // Checks to see what checker if any occupies a space 
	// Returns null if no checker is found
	{
		// Check if the space has a red checker

		for(Checker c : redCheckers)
		{
			if(c.getCord().equals(Cord))
			{
				return c;
			}
		}

		// Check if the space has a black checker

		for(Checker c : redCheckers)
		{
			if(c.getCord().equals(Cord))
			{
				return c;
			}
		}
	
		// If no checker is found, return null

		return null; 
	}
	
	private void initCheckers(Color color) // Takes in a color and adds the checkers of that color in there starting position.
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


	private boolean moveForwardCheck(Checker piece, Cord dest) 
	// Returns true if the piece can move diagonally forward to the destination square. 
	// Does not check Jumps
	{
		Checker destPiece = checkSpace(dest); // Check if the destination square is occupied by another piece

		if(dest.getX() < 0 || dest.getX() > 7 || dest.getY() < 0 || dest.getY() > 7)
		{
			return false; // Invalid move, out of bounds
		}

		if(destPiece != null) 
		{
			return false; // Cannot move to a square already occupied by another piece
		}
		else
		{
			int xDiff = Math.abs(dest.getX() - piece.getCord().getX());
			int yDiff = (dest.getY() - piece.getCord().getY());

			if(xDiff == 1 && yDiff == 1)
			{
				return true;
			}
		}

		return false; // Should not reach. If it does something is wrong and the move is invalid
	}

	private boolean moveBackwardCheck(Checker piece, Cord dest) 
	// Returns true if the piece can move diagonally backward to the destination square. 
	// Does not check Jumps
	{
		Checker destPiece = checkSpace(dest); // Check if the destination square is occupied by another piece

		if(dest.getX() < 0 || dest.getX() > 7 || dest.getY() < 0 || dest.getY() > 7)
		{
			return false; // Invalid move, out of bounds
		}

		if(destPiece != null) 
		{
			return false; // Cannot move to a square already occupied by another piece
		}
		else
		{
			int xDiff = Math.abs(dest.getX() - piece.getCord().getX());
			int yDiff = (dest.getY() - piece.getCord().getY());

			if(xDiff == 1 && yDiff == -1)
			{
				return true;
			}
		}

		return false; // Should not reach. If it does something is wrong and the move is invalid
	}
	

}
