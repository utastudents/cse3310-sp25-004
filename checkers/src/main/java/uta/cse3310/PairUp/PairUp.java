package uta.cse3310.PairUp;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import uta.cse3310.Bot.BotI.BotI;
import uta.cse3310.Bot.BotII.BotII;

import uta.cse3310.DB.DB;
import uta.cse3310.GameManager.GameManager;
import uta.cse3310.PageManager.HumanPlayer;


public class PairUp {
    private LinkedList<Challenge> playerQueue;
    private DB db;
    private GameManager gm;
    private int numPlayersInQueue;

    /**
     * Create a new PairUp object. Should only be called ONCE, and only by Page Manager.
     * @param db
     */
    public PairUp(DB db, GameManager gm) {
        this.playerQueue = new LinkedList<>();//Queue of players waiting to be paired
        this.db = db; //Database object for storing player data
        this.gm = gm;
        numPlayersInQueue = 0;

    }

    private boolean isInRange(Player p1, Player p2) {return true;} //Compares elo scores. If either is not a HumanPlayer, return true

    private void pairUp() {
        if (playerQueue.size() == 0) return; //If there are no players in the queue, return
        //if (gm.numGamesAvailable() <= 0) {return;}

        //Try and match the first Challenge in the queue. If the first can't, try the second, etc.
        for (int c=0; c<playerQueue.size(); c++) {
            Challenge curr = playerQueue.get(c);
            if (curr.hasJustOne) {
                for (int i=c+1; i<playerQueue.size(); i++) {
                    Challenge temp = playerQueue.get(i);
                    if (temp.hasJustOne && isInRange(temp.first, curr.first)) {
                        //Make a match
                        playerQueue.remove(temp);
                        playerQueue.remove(curr);
                        numPlayersInQueue -= 2;
                        gm.createGame(curr.first, temp.first);
                        return;
                    }
                }
            } else {
                playerQueue.remove(curr);//Remove it from the queue, a match has been found
                numPlayersInQueue -= 2;
                gm.createGame(curr.first, curr.second);
                return;
            }
            //No match
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
        return challenge(botI ? new BotI() : new BotII(), botI ? new BotI() : new BotII()); //just calls challenge with a bot
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
