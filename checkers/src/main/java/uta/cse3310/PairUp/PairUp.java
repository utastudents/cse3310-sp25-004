package uta.cse3310.PairUp;

import java.util.Queue;

import uta.cse3310.DB.DB;
import uta.cse3310.Bot.BotI.BotI;
import uta.cse3310.Bot.BotII.BotII;

public class PairUp {
    private Queue<Challenge> playerQueue;

    public PairUp(DB db) {
        
    }

    public boolean addToQueue(Player p) {return false;} //Adds a player to the Queue
    public boolean challenge(Player p, Player c) {return false;} //Makes 2 players challenge eachother (or player & bot, or 2 bots)
    public boolean challengeBot(Player p, boolean botI) {
        //return challenge(p, botI ? new BotI() : new BotII()); //just calls challenge with a bot
        return false;
    }
    public boolean botVBot(boolean botI, boolean botII) {
        //return challenge(botI ? new BotI() : new BotII(), botI ? new BotI() : new BotII()); //just calls challenge with a bot
        return false;
    }

    public boolean boardAvailable() {return false;} //Called by GameManager when a board becomes available (in case all were previously taken)

}

//Private class for handling challenge queue
class Challenge {
    Player first;
    Player second;
    boolean hasJustOne;
    public Challenge(Player p) {
        first = p;
        hasJustOne = true;
    }
    public Challenge(Player p, Player c) {
        first = p;
        second = c;
        hasJustOne = false;
    }
}