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

	// checkerBoard[y][x]
	public Checker[][] checkerBoard; // 2D array of checkers on the board
	//NOTE: A NULL value in the array means that the square is empty.

	public boolean turn; // false for red's turn, true for black's turn


	public Board()
	{
		this.checkerBoard = new Checker[8][8];
        //Initialize class with board array and initialize checker positions
		initCheckers();
	}

	public void printBoard()
    {
        for(int i = 0; i < 8; i++)
        {
            for(int j = 0; j < 8; j++)
            {
                if (checkerBoard[i][j] == null)
                {
                    System.out.print("0 ");
                }
                else if(checkerBoard[i][j].getColor() == Color.BLACK)
                {
                    System.out.print("B ");
                }
                else if(checkerBoard[i][j].getColor() == Color.RED)
                {
                    System.out.print("R ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }

	public void printBoard(Cord from, Cord to)
    {
        for(int i = 0; i < 8; i++)
        {
            for(int j = 0; j < 8; j++)
            {
				String space = "  ";
				if ((from.getX() == j && from.getY() == i)) {
					space = "f ";
				} else if (to.getX() == j && to.getY() == i) {
					space = "t ";
				}

                if (checkerBoard[i][j] == null)
                {
                    System.out.print("0" + space);
                }
                else if(checkerBoard[i][j].getColor() == Color.BLACK)
                {
                    System.out.print("B" + space);
                }
                else if(checkerBoard[i][j].getColor() == Color.RED)
                {
                    System.out.print("R" + space);
                }
            }
            System.out.println();
        }
        System.out.println();
    }

	public Checker checkSpace(Cord Cord) // Checks to see what checker if any occupies a space 
	// Returns null if no checker is found
	{
		return checkerBoard[Cord.getY()][Cord.getX()]; 
	}

	public void clearboard() // Set all spaces to null
	{
		for (int i = 0; i < 8; i++)
		{
			for(int j = 0; j < 8; j++)
			{
				checkerBoard[i][j] = null; // Initialize the board with null values
			}
		}
	}
	
	private void initCheckers() // Initializes the checkers on the board at there starting positions
	{
		clearboard();

		for(int i = 5; i < 8; i++) // row aka y coordinate
		{
			if(i == 6) // second
			{
				checkerBoard[i][1] = new Checker(new Cord(1, i), Color.BLACK);
				checkerBoard[i][3] = new Checker(new Cord(3, i), Color.BLACK);
				checkerBoard[i][5] = new Checker(new Cord(5, i), Color.BLACK);
				checkerBoard[i][7] = new Checker(new Cord(7, i), Color.BLACK);
			}
			else // first and third black row
			{
				checkerBoard[i][0] = new Checker(new Cord(0, i), Color.BLACK);
				checkerBoard[i][2] = new Checker(new Cord(2, i), Color.BLACK);
				checkerBoard[i][4] = new Checker(new Cord(4, i), Color.BLACK);
				checkerBoard[i][6] = new Checker(new Cord(6, i), Color.BLACK);
			}

		}

		for(int i = 0; i < 3; i++) // row aka y coordinate
		{
			if(i == 1) // second Red Row
			{
				checkerBoard[i][0] = new Checker(new Cord(0, i), Color.RED);
				checkerBoard[i][2] = new Checker(new Cord(2, i), Color.RED);
				checkerBoard[i][4] = new Checker(new Cord(4, i), Color.RED);
				checkerBoard[i][6] = new Checker(new Cord(6, i), Color.RED);
			}
			else // first and thire Red Row
			{
				checkerBoard[i][1] = new Checker(new Cord(1, i), Color.RED);
				checkerBoard[i][3] = new Checker(new Cord(3, i), Color.RED);
				checkerBoard[i][5] = new Checker(new Cord(5, i), Color.RED);
				checkerBoard[i][7] = new Checker(new Cord(7, i), Color.RED);
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
		if(piece.getColor() == Color.BLACK && piece.getCord().getY() == 0)
		{
			piece.setKing(true);
		}
		else if(piece.getColor() == Color.RED && piece.getCord().getY() == 7)
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
		if(dest.getX() < 0 || dest.getX() > 7 || dest.getY() < 0 || dest.getY() > 7)
		{
			System.out.println("Out of bounds");
			return false;
		}
		// verify destination square is not occupied by another piece
		if(checkerBoard[dest.getY()][dest.getX()] != null)
		{
			//System.out.println("Space occupied");
			return false;
		}
		// verify if destination is 1 square above and to the right/left of the chosen checker
		int xDiff = Math.abs(dest.getX() - piece.getCord().getX());
		int yDiff = (dest.getY() - piece.getCord().getY());
		
		//System.out.println("X/Y diffs: expected: (1,1) actual: (" + xDiff + ", " + yDiff + ")");

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
		if(dest.getX() < 0 || dest.getX() > 7 || dest.getY() < 0 || dest.getY() > 7)
		{
			System.out.println("Out of bounds");
			return false;
		}
		// verify destination square is not occupied by another piece	
		if(checkerBoard[dest.getY()][dest.getX()] != null)
		{
			//System.out.println("Space occupied");
			return false;
		}
		// verify if destination is 1 square below and to the right/left of the chosen checker
		int xDiff = Math.abs(dest.getX() - piece.getCord().getX());
		int yDiff = (dest.getY() - piece.getCord().getY());
		
		//System.out.println("X/Y diffs: expected: (1,-1) actual: (" + xDiff + ", " + yDiff + ")");

		return (xDiff == 1 && yDiff == -1);
	}

	public ArrayList<Move> getAllRegularMoves(Color pColor) {
		ArrayList<Move> moves = new ArrayList<>();
		// Brute force approach

		Checker c;
		Cord f;
		Cord d;
		
		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
				c = checkerBoard[row][col];
				f = new Cord(col, row);
				if (c != null && c.getColor() == pColor) {
					// Check every possible square
					for (int dr = 0; dr < 8; dr++) {
						for (int dc = 0; dc < 8; dc++) {
							d = new Cord(dc, dr);
							if (c.isKing() || pColor == Color.BLACK) {
								if (moveBackwardCheck(c, d)) {
									moves.add(new Move(c, f, d));
								}
							}
							if (c.isKing() || pColor == Color.RED) {
								if (moveForwardCheck(c, d)) {
									moves.add(new Move(c, f, d));
								}
							}
						}
					}
				}
			}
		}

		return moves;
	}

	public ArrayList<Move> getAllMoves() {
		ArrayList<Move> moves = new ArrayList<>();

		Color current = turn ? Color.BLACK : Color.RED;

		moves.addAll(getAllJumps(current));

		if (moves.size() > 0) {
			return moves;
		}

		return getAllRegularMoves(current);
	}
	
	public ArrayList<Cord> getPossibleBackwardJump(Checker piece) 
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
		if ((originX-2 >= 0 && originX-2 < 8) && (originY+2 >= 0 && originY+2 < 8))
		{
			//Check if the immediate diagonal is an enemy piece
			if (checkerBoard[originY+1][originX-1] != null && checkerBoard[originY+1][originX-1].getColor() != originColor)
			{
				//Add to list of jumps if space is free
				if (checkerBoard[originY+2][originX-2] == null)
				{
					jumpList.add(new Cord(originX-2, originY+2));

					//check for second leftward
					if ((originX-4 >= 0 && originX-4 < 8) && (originY+4 >= 0 && originY+4 < 8))
					{
						if (checkerBoard[originY+4][originX-4] == null)
						{
							if (checkerBoard[originY+3][originX-3] != null && checkerBoard[originY+3][originX-3].getColor() != originColor)
							{
								jumpList.add(new Cord(originX-4, originY+4));
							}
						}

					}
					
					//check for second rightward
					if ((originX >= 0 && originX < 8) && (originY+4 >= 0 && originY+4 < 8))
					{
						if (checkerBoard[originY+4][originX] == null)
						{
							if (checkerBoard[originY+3][originX+1] != null && checkerBoard[originY+3][originX+1].getColor() != originColor)
							{
								jumpList.add(new Cord(originX, originY+4));
							}
						}

					}
				}
			}
		}
		//Same as above but checking to the right
		if ((originX+2 >= 0 && originX+2 < 8) && (originY+2 >= 0 && originY+2 < 8))
		{
			if (checkerBoard[originY+1][originX+1] != null && checkerBoard[originY+1][originX+1].getColor() != originColor)
			{
				if (checkerBoard[originY+2][originX+2] == null)
				{
					jumpList.add(new Cord(originX+2, originY+2));
				}

				//check for second leftward jump
				if ((originX >= 0 && originX < 8) && (originY+4 >= 0 && originY+4 < 8))
				{
					if (checkerBoard[originY+4][originX] == null)
					{
						if (checkerBoard[originY+3][originX-1] != null && checkerBoard[originY+3][originX-1].getColor() != originColor)
						{
							jumpList.add(new Cord(originX-4, originY+4));
						}
					}

				}

				//check for second rightward jump
				if ((originX+4 >= 0 && originX+4 < 8) && (originY+4 >= 0 && originY+4 < 8))
				{
					if (checkerBoard[originY+4][originX+4] == null)
					{
						if (checkerBoard[originY+3][originX+3] != null && checkerBoard[originY+3][originX+3].getColor() != originColor)
						{
							jumpList.add(new Cord(originX+4, originY+4));
						}
					}

				}
			}
		}
		System.out.println(jumpList);
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
			if(jumpList.get(i).getX() == cord.getX() && jumpList.get(i).getY() == cord.getY())
			{
				check = true;
				break;
			}
		}
		System.out.println("CHECK JUMP RESULT");
		System.out.println(jumpList);
		System.out.println(cord);
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

	//Same as getPossibleBackwardJump but forwards
	public ArrayList<Cord> getPossibleForwardJump(Checker piece) 
	{
		ArrayList<Cord> jumpList = new ArrayList<Cord>();
		int originX = piece.getCord().getX();
		int originY = piece.getCord().getY();
		Color originColor = piece.getColor();
		if ((originX-2 >= 0 && originX-2 < 8) && (originY-2 >= 0 && originY-2 < 8))
		{
			if (checkerBoard[originY-1][originX-1] != null && checkerBoard[originY-1][originX-1].getColor() != originColor)
			{
				if (checkerBoard[originY-2][originX-2] == null)
				{
					jumpList.add(new Cord(originX-2, originY-2));

					//check for second jump to left
					if ((originX-4 >= 0 && originX-4 < 8) && (originY-4 >= 0 && originY-4 < 8))
					{
						if (checkerBoard[originY-4][originX-4] == null)
						{
							if (checkerBoard[originY-3][originX-3] != null && checkerBoard[originY-3][originX-3].getColor() != originColor)
							{
								jumpList.add(new Cord(originX-4, originY-4));
							}
						}

					}
					
					//check for second jump to the right
					if ((originX >= 0 && originX < 8) && (originY-4 >= 0 && originY-4 < 8))
					{
						if (checkerBoard[originY-4][originX] == null)
						{
							if (checkerBoard[originY-3][originX-1] != null && checkerBoard[originY-3][originX-1].getColor() != originColor)
							{
								jumpList.add(new Cord(originX, originY-4));
							}	
						}

					}
				}
			}
		}
		if ((originX+2 >= 0 && originX+2 < 8) && (originY-2 >= 0 && originY-2 < 8))
		{
			if (checkerBoard[originY-1][originX+1] != null && checkerBoard[originY-1][originX+1].getColor() != originColor)
			{
				if (checkerBoard[originY-2][originX+2] == null)
				{
					jumpList.add(new Cord(originX+2, originY-2));

					//check for second jump to left
					if ((originX >= 0 && originX < 8) && (originY-4 >= 0 && originY-4 < 8))
					{
						if (checkerBoard[originY-4][originX] == null)
						{
							if (checkerBoard[originY-3][originX+1] != null && checkerBoard[originY-3][originX+1].getColor() != originColor)
							{
								jumpList.add(new Cord(originX, originY-4));
							}
						}

					}

					//check for second jump to the right
					if ((originX+4 >= 0 && originX+4 < 8) && (originY-4 >= 0 && originY-4 < 8))
					{
						if (checkerBoard[originY-4][originX+4] == null)
						{
							if (checkerBoard[originY-3][originX+3] != null && checkerBoard[originY-3][originX+3].getColor() != originColor)
							{
								jumpList.add(new Cord(originX-4, originY-4));
							}
						}

					}
				}
			}
		}
		System.out.println(jumpList);
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
			if(jumpList.get(i).getX() == cord.getX() && jumpList.get(i).getY() == cord.getY())
			{
				check = true;
				break;
			}
		}
		System.out.println("CHECK JUMP RESULT");
		System.out.println(jumpList);
		System.out.println(cord);
		System.out.println(check);
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
		Cord oldCord = piece.getCord();
		deleteChecker(oldCord);
		int newX = dest.getX();
		int newY = dest.getY();
		checkerBoard[newY][newX] = piece; //test works with this format like how initialized
		piece.setCord(newX, newY);
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

		else if (origX == destX+4 && origY == destY+4)
		{
			deleteChecker(new Cord(destX+1, destY+1));
			deleteChecker(new Cord(destX+3, destY+3));
		}

		else if (origX == destX-4 && origY == destY-4)
		{
			deleteChecker(new Cord(destX-1, destY-1));
			deleteChecker(new Cord(destX-3, destY-3));
		}

		else if (origX == destX+4 && origY == destY-4)
		{
			deleteChecker(new Cord(destX+1, destY-1));
			deleteChecker(new Cord(destX+3, destY-3));
		}

		else if (origX == destX-4 && origY == destY+4)
		{
			deleteChecker(new Cord(destX+1, destY+1));
			deleteChecker(new Cord(destX+3, destY+3));
		}

		else if (origX == destX && origY == destY+4)
		{
			deleteChecker(new Cord(destX+1, destY+1));
			deleteChecker(new Cord(destX, destY+3));
		}

		else if (origX == destX && origY == destY-4)
		{
			deleteChecker(new Cord(destX+1, destY+1));
			deleteChecker(new Cord(destX, destY-3));
		}
	}
	
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

	public ArrayList<Move> getAllJumps(Color pColor) {
		ArrayList<Move> jumps = new ArrayList<>();

		for (int row = 0; row < 8; row++) 
		{
			for (int col = 0; col < 8; col++) 
			{
				Checker p = checkerBoard[row][col];
				Cord from = new Cord(col, row);
				if (p == null) // Skip to next piece if p is NULL
				{
					continue; 
				}
				else if(p.getColor() != pColor) // Skip to next piece if p's color doesn't match the player's color
				{
					continue;
				}
				else 
				{
					if(p.isKing() || p.getColor() == Color.BLACK) // Check if the piece is a king or black
					{
						assert p.getColor() == pColor; // Assert that the piece is the same color as the player
						ArrayList<Cord> dests = getPossibleForwardJump(p);
						for (Cord c : dests) {
							jumps.add(new Move(p, from, c));
						}
					}
					if(p.getColor() == Color.RED || p.isKing()) // Check if the piece is a king or red
					{
						assert p.getColor() == pColor; // Assert that the piece is the same color as the player
						ArrayList<Cord> dests = getPossibleBackwardJump(p);
						for (Cord c : dests) {
							jumps.add(new Move(p, from, c));
						}
					}
					/* The code should not add the opposite color's jumps to the list,
					because of the if(p.getColor() == pColor) check above that should prevent it.
					*/
				}
			}
		}
		return jumps;
	}

	public boolean hasJump(Color pColor) // Checks if a specific player (red or black) has any jumps
    {
        ArrayList<Move> jumps = getAllJumps(pColor);
		if(jumps.size() > 0)
		{
			return true; // Player has at least one jump
		}
		else
		{
			return false; // Player has no jumps
		}
	}

	public void printAvailableMoves() {
		ArrayList<Move> moves = getAllMoves();
		for (Move m : moves) {
			System.out.print(m);
		}
		System.out.println();
	}
}