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

	public Checker checkerBoard [][] = new Checker[8][8]; // 2D array of checkers on the board
	//NOTE: A NULL value in the array means that the square is empty.

	public Board()
	{
        //Initialize class with board array and initialize checker positions

		initCheckers();
	}

	private Checker checkSpace(Cord Cord) // Checks to see what checker if any occupies a space 
	// Returns null if no checker is found
	{
		return checkerBoard[Cord.getX()][Cord.getY()]; 
	}
	
	
	private void initCheckers() // Initializes the checkers on the board at there starting positions
	// TODO Refactor initCheckers function to use loops instead of hardcoding the values
	{
		int nums[] = {1,3,5,7};


		// Set the whole board to null
		for (int i = 0; i < 8; i++)
		{
			for(int j = 0; j < 8; j++)
			{
				checkerBoard[i][j] = null; // Initialize the board with null values
			}
		}

		for(int y = 0; y < 3; y++)
		{
			for(int x = 0; x < 8; x+=2)
			{
				if(y%2 == 0)
				{
					checkerBoard[x+1][y] = new Checker(new Cord(x+1, y), Color.BLACK);
				}
				else
				{
					checkerBoard[x][y] = new Checker(new Cord(x, y), Color.BLACK);
				}
			}
		}

		for(int y = 5; y < 8; y++)
		{
			for(int x = 0; x < 8; x+=2)
			{
				if(y%2 == 1)
				{
					checkerBoard[x+1][y] = new Checker(new Cord(x+1, y), Color.RED);
				}
				else
				{
					checkerBoard[x][y] = new Checker(new Cord(x, y), Color.RED);
				}
			}
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

		if(dest.getX() < 0 || dest.getX() > 7 || dest.getY() < 0 || dest.getY() > 7)
		{
			return false; // Invalid move, out of bounds
		}

		if(checkerBoard[dest.getY()][dest.getX()] == null)
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

		if(dest.getX() < 0 || dest.getX() > 7 || dest.getY() < 0 || dest.getY() > 7)
		{
			return false; // Invalid move, out of bounds
		}

		if(checkerBoard[dest.getY()][dest.getX()] == null)
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
	
	private ArrayList<Cord> getPossibleForwardJump(Checker piece) 
	{
		ArrayList<Cord> jumpList = new ArrayList<Cord>();
		int originX = piece.getCord().getX();
		int originY = piece.getCord().getY();
		//First check if the jump to the left is in bounds
		if ((originX-2 > 0 && originX-2 <=8) && (originY+2 > 0 && originY+2 <=8))
		{
			//Add to list of jumps if space is free
			if (checkerBoard[originX-2][originY+2] == null)
			{
				jumpList.add(new Cord(originX-2, originY+2));
			}
		}
		//Same as above but checking to the right
		if ((originX+2 > 0 && originX+2 <=8) && (originY+2 > 0 && originY+2 <=8))
		{
			if (checkerBoard[originX+2][originY+2] == null)
			{
				jumpList.add(new Cord(originX+2, originY+2));
			}
		}
		return jumpList;
	}

	public static int moveValidation(Checker piece, Cord dest)
	// The main logic for movement. 
	// This functions call the jump/move functions in order to determine if the passed in move is can should be allowed. 

	{
		// TODO: Finish Board moveValidation function
		int result = 0; // 0 = invalid move, 1 = jump required, 2 = valid move



		return result;
	}

}
