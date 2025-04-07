package uta.cse3310.GamePlay;

/*
 * Class for checker object
 */
public class Checker 
{
	private boolean isKing = false;
	private Cord cord;
	private Color color;
	
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

	public Color getColor()
	{
		return color;
	}


}
