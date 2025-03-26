package uta.cse3310.PairUp;

import java.util.Queue;

import uta.cse3310.DB.DB;

public class PairUp {
    private Queue<Challenge> playerQueue;

    public PairUp(DB db) {
        
    }

    public boolean addToQueue(Player p) {return false;} //Adds a player to the Queue
    public boolean challenge(Player p, Player c) {return false;} //Makes 2 players challenge eachother (or player & bot, or 2 bots)

    public boolean boardAvailable() {return false;} //Called by GameManager when a board becomes available (in case all were previously taken)

}

//Private class for handling challegnge queue
class Challenge {
    Player first;
    Player second;
    public Challenge(Player p) {
        first = p;
    }
    public Challenge(Player p, Player c) {
        first = p;
        second = c;
    }
}