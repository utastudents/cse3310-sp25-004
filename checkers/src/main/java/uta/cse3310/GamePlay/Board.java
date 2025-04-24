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

	public Checker[][] checkerBoard; // 2D array of checkers on the board
	//NOTE: A NULL value in the array means that the square is empty.


	public Board()
	{
		this.checkerBoard = new Checker[8][8];
        //Initialize class with board array and initialize checker positions

		initCheckers();
	}

	public Checker checkSpace(Cord Cord) // Checks to see what checker if any occupies a space 
	// Returns null if no checker is found
	{
		return checkerBoard[Cord.getX()][Cord.getY()]; 
	}
	
	
	private void initCheckers() // Initializes the checkers on the board at there starting positions
	{
		// Set the whole board to null
		for (int i = 0; i < 8; i++)
		{
			for(int j = 0; j < 8; j++)
			{
				checkerBoard[i][j] = null; // Initialize the board with null values
			}
		}

		// Set the black checkers on the board on row 0, 1, 2
		for(int y = 0; y < 3; y++)
		{
			/*
			MIGHT NEED CHANGING
   			IN DISPLAY CHECKERS IN R0 ARE ON COLUMNS 0, 2 ,4 ,6
   			*/
			for(int x = 1; x < 8; x+=2)
			{
				if(y%2 == 0)// Row 0 and 2 have the black checkers on squares 1,3,5,7
				{
					// ?? I feel like x+1 would initialize a checker piece for 1, 4, and 7
					// ?? potential fix: just make another for loop
					checkerBoard[y][x] = new Checker(new Cord(x+1, y), Color.BLACK);
				}
			}
			/*
   			CAN BE COMBINED WITH ABOVE ^ FOR LOOP
   			*/
			for(int x = 1; x < 8; x+=2)
			{
				if(y%2 == 1)// Row 0 and 2 have the black checkers on squares 1,3,5,7
				{
					// ?? I feel like x+1 would initialize a checker piece for 1, 4, and 7
					// ?? potential fix: just make another for loop
					checkerBoard[y][x] = new Checker(new Cord(x+1, y), Color.RED);
				}
			}
		}

		// Set the red checkers on the board on row 5, 6, 7
		for(int y = 5; y < 8; y++)
		{
			for(int x = 0; x < 8; x+=2)
			{
				if(y%2 == 1) // Row 5 and 7 have the red checkers on squares 0,2,4,6
				{
					checkerBoard[y][x] = new Checker(new Cord(x+1, y), Color.RED);
				}
				else // Row 6 has the red checkers on squares 1,3,5,7
				{
					checkerBoard[y][x] = new Checker(new Cord(x, y), Color.RED);
				}
			}
		}

	}

	/* 
		Definition: kingMe sets a checker piece to be a king
			- called after every move

		Arguments:
			piece: checker piece that was moved

		Returns:
			void
	*/
	public void kingMe(Checker piece)
	{
		/*
  		BLACK STARTS ON R7 SO 0 AND 7 SHOULD BE SWITCHED
  		*/
		if(piece.getColor() == Color.BLACK && piece.getCord().getY() == 7)
		{
			piece.setKing(true);
		}
		else if(piece.getColor() == Color.RED && piece.getCord().getY() == 0)
		{
			piece.setKing(true);
		}
	}

	/* 
		Definition: moveForwardCheck checks if the requested destination by the user is a valid simple move
			- for forward move only (black and king pieces)
			- Does not check Jumps

		Arguments:
			piece : checker piece chosen by the player
			dest : destination cord that the player click/request to move to

		Returns:
			true : piece can move diagonally forward to the destination square
	*/
	public boolean moveForwardCheck(Checker piece, Cord dest) 
	{
		// verify destination square is in bound
		if(dest.getX() <= 0 || dest.getX() >= 7 || dest.getY() <= 0 || dest.getY() >= 7)
		{
			return false;
		}
		// verify destination square is not occupied by another piece
		if(checkerBoard[dest.getY()][dest.getX()] != null)
		{
			return false;
		}
		/*
  		COULD ADD COLOR CHECK TO VERIFY THE CORRECT DIRECTION
    		ALSO COULD BE ADDED IN A DIFFERENT METHOD
    		*/
		// verify if destination is 1 square above and to the right/left of the chosen checker
		int xDiff = Math.abs(dest.getX() - piece.getCord().getX());
		int yDiff = (dest.getY() - piece.getCord().getY());
		
		return (xDiff == 1 && yDiff == 1);
	}

	/* 
		Definition: moveBackwardCheck checks if the requested destination by the user is a valid simple move
			- for backward move only (red and king pieces)
			- Does not check Jumps

		Arguments:
			piece : checker piece chosen by the player
			dest : destination cord that the player click/request to move to

		Returns:
			true : piece can move diagonally backward to the destination square
	*/
	public boolean moveBackwardCheck(Checker piece, Cord dest) 
	{
		// verify destination square is in bound
		if(dest.getX() <= 0 || dest.getX() >= 7 || dest.getY() <= 0 || dest.getY() >= 7)
		{
			return false;
		}
		// verify destination square is not occupied by another piece	
		if(checkerBoard[dest.getY()][dest.getX()] == null)
		{
			return false;
		}
		/*
  		COLOR CHECK
    		UNLESS IT'S IN ANOTHER METHOD
    		*/
		// verify if destination is 1 square below and to the right/left of the chosen checker
		int xDiff = Math.abs(dest.getX() - piece.getCord().getX());
		int yDiff = (dest.getY() - piece.getCord().getY());
		
		return (xDiff == 1 && yDiff == -1);
	}
	
	public ArrayList<Cord> getPossibleForwardJump(Checker piece) 
	{
		ArrayList<Cord> jumpList = new ArrayList<Cord>();
		int originX = piece.getCord().getX();
		int originY = piece.getCord().getY();
		Color originColor = piece.getColor();
		/*
  		COLOR CHECK UNLESS IN A DIFFERENT METHOD
    		NEED COLOR FOR DIRECTION
  		*/
		//First check if the jump to the left is in bounds
		if ((originX-2 > 0 && originX-2 <=8) && (originY+2 > 0 && originY+2 <=8))
		{
			//Check if the immediate diagonal is an enemy piece
			if (checkerBoard[originX-1][originY+1] != null && checkerBoard[originX-1][originY+1].getColor() != originColor)
			{
				//Add to list of jumps if space is free
				if (checkerBoard[originX-2][originY+2] == null)
				{
					jumpList.add(new Cord(originX-2, originY+2));
				}
			}
		}
		//Same as above but checking to the right
		if ((originX+2 > 0 && originX+2 <=8) && (originY+2 > 0 && originY+2 <=8))
		{
			if (checkerBoard[originX+1][originY+1] != null && checkerBoard[originX+1][originY+1].getColor() != originColor)
			{
				if (checkerBoard[originX+2][originY+2] == null)
				{
					jumpList.add(new Cord(originX+2, originY+2));
				}
			}
		}
		return jumpList;
	}
	
	//searches the jump list for the requested move
	int checkForwardJump(ArrayList<Cord> jumpList, Cord cord)
	{
		//base case for our check
		//checks if our cord was found
		boolean check = false;
		//iterator
		int i;
		
		//iterates over the jump list
		for(i = 0; i<jumpList.size(); i++)
		{
			//checks if cords are equal
			//might need a separate equals methods to actually compare the x-y positions
			if(jumpList.get(i).equals(cord))
			{
				check = true;
				break;
			}
		}
		
		//if true return index of cord in list
		//else return -1, same as null
		if(check)
		{
			return i;
		}
		else
		{
			return -1;
		}
	}

	//Same as getPossibleForwardJump but backwards
	public ArrayList<Cord> getPossibleBackwardJump(Checker piece) 
	{
		ArrayList<Cord> jumpList = new ArrayList<Cord>();
		int originX = piece.getCord().getX();
		int originY = piece.getCord().getY();
		Color originColor = piece.getColor();
		if ((originX-2 > 0 && originX-2 <=8) && (originY-2 > 0 && originY-2 <=8))
		{
			if (checkerBoard[originX-1][originY-1] != null && checkerBoard[originX-1][originY-1].getColor() != originColor)
			{
				if (checkerBoard[originX-2][originY-2] == null)
				{
					jumpList.add(new Cord(originX-2, originY-2));
				}
			}
		}
		if ((originX+2 > 0 && originX+2 <=8) && (originY-2 > 0 && originY-2 <=8))
		{
			if (checkerBoard[originX+1][originY-1] != null && checkerBoard[originX+1][originY-1].getColor() != originColor)
			{
				if (checkerBoard[originX+2][originY-2] == null)
				{
					jumpList.add(new Cord(originX+2, originY-2));
				}
			}
		}
		return jumpList;
	}

	/* 
		Definition: checkBackwardJump checks if the requested destination by the user is a valid jump
		Arguments:
			jumpList : List of possible backwards jumps gathered after running getPossibleBackwardJump
			cord : destination cord that the player click/request to move to

		Returns:
			i (int) : index of the cord in the list of possible backward jumps
			-1 : cord is not found in list and is not a valid backward jump
	*/
	int checkBackwardJump(ArrayList<Cord> jumpList, Cord cord)
	{
		//base case for our check
		//checks if our cord was found
		boolean check = false;
		//iterator
		int i;
		
		//iterates over the jump list
		for(i = 0; i<jumpList.size(); i++)
		{
			//checks if cords are equal
			//might need a separate equals methods to actually compare the x-y positions
			if(jumpList.get(i).equals(cord))
			{
				check = true;
				break;
			}
		}
		
		//if found return index of cord in list
		//if cord is not found in possibleBackwardJumps list, return -1 (same as null)
		if(check)
		{
			return i;
		}
		else
		{
			return -1;
		}
	}
	
	//deletes a checker at a given cord
	//can be changed to have x and y as the parameters
	void deleteChecker(Cord cord)
	{
		checkerBoard[cord.getY()][cord.getX()] = null;
	}

	// updatePosition updates the chosen checker piece with the chosen destination
	public void updatePosition(Checker piece, Cord dest)
	{
		int newX = dest.getX();
		int newY = dest.getY();
		Cord oldCord = piece.getCord();
		int oldX = oldCord.getX();
		int oldY = oldCord.getY();
		checkerBoard[oldX][oldY] = null;
		piece.setCord(newX, newY);
		checkerBoard[newX][newY] = piece; //test works with this format like how initialized
	}
	
	/* 
		Definition: 
		Arguments:
			piece : checker piece chosen by the player, the piece that is jumping
			dest : new destination of the chosen checker piece after the jump
	*/
	void removeJumpedChecker(Checker piece, Cord dest)
	{
		Cord originalCord = piece.getCord();
		int origX = originalCord.getX();
		int origY = originalCord.getY();

		int destX = dest.getX();
		int destY = dest.getY();

		/*
 		COULD ADD COLOR AND/OR KING CHECKS TO MAKE CODE CLEANER
  		*/
		// check which location piece moved too by comparing the cords piece original location the location of the jump
		// remove piece in the bottom left (in terms of dest)
		if (origX == destX-2 && origY == destY-2)
		{
			deleteChecker(new Cord(destX-1, destY-1));
		}
		// remove piece in the bottom right (in terms of dest)
		else if (origX == destX+2 && origY == destY-2)
		{
			deleteChecker(new Cord(destX+1, destY-1));
		}
		// remove piece in the top left (in terms of dest)
		else if (origX == destX-2 && origY == destY+2)
		{
			deleteChecker(new Cord(destX-1, destY+1));
		}
		// remove piece in the top right (in terms of dest)
		else if (origX == destX+2 && origY == destY+2)
		{
			deleteChecker(new Cord(destX+1, destY+1));
		}
	}

	//UNUSED
	// public static int moveValidation(Checker piece, Cord dest)
	// // The main logic for movement. 
	// // This functions call the jump/move functions in order to determine if the passed in move is can should be allowed. 

	// {
	// 	// TODO: Finish Board moveValidation function
	// 	int result = 0; // 0 = invalid move, 1 = jump required, 2 = valid move



	// 	return result;
	// }

	public String toString() {
		String toReturn = "";
		for (Checker[] row : checkerBoard) {
			for (Checker c : row) {
				toReturn += Checker.shortString(c) + " ";
			}
			toReturn += "\n";
		}
		return toReturn;
	}
}
