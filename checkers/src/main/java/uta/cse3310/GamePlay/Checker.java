package uta.cse3310.GamePlay;

/*
 * Class for checker object
 */
public class Checker 
{
	private boolean isKing = false;
	private Cord cord;
	private Color color;
	
	public Checker(Cord cord, Color color){
		this.cord = cord;
		this.color = color;
	}
}
