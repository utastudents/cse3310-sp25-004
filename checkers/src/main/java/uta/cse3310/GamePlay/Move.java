package uta.cse3310.GamePlay;

public class Move {
	public Checker piece;
    public Cord from;
	public Cord dest;
	public Move(Checker p, Cord f, Cord d) {
		this.piece = p;
        this.from = f;
		this.dest = d;
	}

    public String toString() {
        return piece + " from " + from + " to " + dest;
    }
}