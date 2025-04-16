package uta.cse3310.GamePlay;

/*
 * Class for checker object
 */
public class Checker 
{
	private boolean isKing = false;
	private Cord cord;
	private Color color;
	private int playerID;
	
	public Checker(Cord cord, Color color)
	{
		this.cord = cord;
		this.color = color;
	}

	public boolean isKing() 
	{
		return isKing;
	}

	public void setKing(boolean promo) 
	{
		// false = not a king, true = king
		this.isKing = promo;
	}

	public Cord getCord()
	{
		return cord;
	}

	public void setCord(int x, int y)
	{
		this.cord.setCord(x, y);
	}

	public Color getColor()
	{
		return color;
	}

	public int getPlayerID(){
		return playerID;
	}


}
