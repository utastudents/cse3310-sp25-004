package uta.cse3310.PairUp;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import uta.cse3310.Bot.BotI.BotI;
import uta.cse3310.Bot.BotII.BotII;

import uta.cse3310.DB.DB;
import uta.cse3310.PageManager.HumanPlayer;


public class PairUp {
    private Queue<Challenge> playerQueue;
    private DB db;
    private int numPlayersInQueue;

    /**
     * Create a new PairUp object. Should only be called ONCE, and only by Page Manager.
     * @param db
     */
    public PairUp(DB db) {
        this.playerQueue = new LinkedList<>();//Queue of players waiting to be paired
        this.db = db; //Database object for storing player data
        numPlayersInQueue = 0;

    }

    private boolean isInRange(Player p1, Player p2) {return true;} //Compares elo scores. If either is not a HumanPlayer, return true
    private void pairUp() {
        if (playerQueue.size() < 2) return; //If there are not enough players in the queue, return
        while (playerQueue.size() >= 2) {
            Challenge curr = playerQueue.poll(); //Get the first player in the queue
            Player p1 = curr.first; //Get the first player
            Player p2 = curr.second; //Get the second player
            if (isInRange(p1, p2)) {
                db.startGame(p1, p2);
                numPlayersInQueue -= 2;
                return;
            } else {        
                playerQueue.add(p1);
                playerQueue.add(p2);
                break;
             }
         }
        
    } //This is where the actual pairing will take place. Will be called by boardAvailable, challenge, and addToQueue

    /**
     * Add a player to the challenge queue. Called by PageManager when a client requests matchmaking
     * @param p - the Player who requested the matchmaking
     * @return - false if the player was not added to the queue, true otherwise
     */
    public boolean addToQueue(Player p) {return false;}

    /**
     * Add a player v player challenge to the queue - after it has been accepted
     * @param p - the player who requested the challenge
     * @param c - the player who accepted the challenge
     * @return - false if the challengers were not added to the queue, true otherwise
     */
    public boolean challenge(Player p, Player c) {return false;}
    /**
     * Add a player v bot challenge to the queue
     * @param p - The player who requested the challenge
     * @param botI - the bot (True for bot I, false for bot II)
     * @return - false if the challengers were not added to the queue, true otherwise
     */
    public boolean challengeBot(Player p, boolean botI) {
        //return challenge(p, botI ? new BotI() : new BotII()); //just calls challenge with a bot
        return challenge(p, botI ? new BotI() : new BotII());
    }
    /**
     * Add a bot v bot challenge to the queue
     * @param botI - True for bot I, false for bot II
     * @param botII - True for bot I, false for bot II
     * @param spectator - the player who requested to watch
     * @return - false if the challengers were not added to the queue, true otherwise
     */
    public boolean botVBot(boolean botI, boolean botII, HumanPlayer spectator) {
        //return challenge(botI ? new BotI() : new BotII(), botI ? new BotI() : new BotII()); //just calls challenge with a bot
        return false;
    }

    /**
     * 
     * @param p - The player to remove (should be a HumanPlayer)
     * @return - True if the player was removed from the queue
     */
    public boolean removeFromQueue(Player p) {
        return false;
    }

    /**
     * Event method, to let PairUp know that a board is now available that previously was not.
     * Called by GameManager when a game is ended and the board is clear.
     * @return - false if there was an error, true otherwise
     */
    public boolean boardAvailable() {return false;}

    /**
     * Returns the number of players in the queue.
     * For Join Game page to display to users.
     * @return - number of players in the queue
     */
    public int getNumPlayersInQueue() {return numPlayersInQueue;}
}

//Private class for handling challenge queue
class Challenge {
    Player first;
    Player second;
    ArrayList<HumanPlayer> spectators;
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
