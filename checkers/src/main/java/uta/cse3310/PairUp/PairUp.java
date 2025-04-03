package uta.cse3310.PairUp;

<<<<<<< HEAD
import java.util.Queue;
import java.util.ArrayList;
import java.util.LinkedList;
import uta.cse3310.DB.DB;
import uta.cse3310.Bot.BotI.BotI;
import uta.cse3310.Bot.BotII.BotII;
import uta.cse3310.PageManager.HumanPlayer;

public class PairUp {
    private Queue<Challenge> playerQueue;
    private DB db;
<<<<<<< HEAD

    public PairUp(DB db) {
        this.playerQueue = new LinkedList<>(); //Queue of players waiting to be paired
        this.db = db; //Database object for storing player data
=======
    private int numPlayersInQueue;
    public PairUp(DB db) {
        this.playerQueue = new LinkedList<>();
        this.db = db;
        numPlayersInQueue = 0;
>>>>>>> 9e2eea4ea65a5c95c4c7a3a90ac3e305b20ceb74
=======
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

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

>>>>>>> 54ad28769fd10d579bfe9c7d55da29b9c5e7fd66
    }

    private boolean isInRange(Player p1, Player p2) {return true;} //Compares elo scores. If either is not a HumanPlayer, return true
    private void pairUp() {} //This is where the actual pairing will take place. Will be called by boardAvailable, challenge, and addToQueue

<<<<<<< HEAD
    public boolean addToQueue(Player p) {return false;} //Adds a player to the Queue
    public boolean challenge(Player p, Player c) {return false;} //Makes 2 players challenge eachother (or player & bot, or 2 bots)
=======
    /**
     * Add a player to the challenge queue. Called by PageManager when a client requests matchmaking
     * @param p - the Player who requested the matchmaking
     * @return
     */
    public boolean addToQueue(Player p) {return false;}

    /**
     * Called by PageManager when a user requests a challenge, but it has not been accepted yet.
     * The request should be forwarded to the second user.
     * @param p - The player who requested the challenge
     * @param c - The player who is being challenged.
     * @return
     */
    public boolean requestChallenge(Player p, Player c) {return false;}

    /**
     * Add a player v player challenge to the queue - after it has been accepted
     * @param p - the player who requested the challenge
     * @param c - the player who accepted the challenge
     * @return
     */
    public boolean challenge(Player p, Player c) {return false;}
    /**
     * Add a player v bot challenge to the queue
     * @param p - The player who requested the challenge
     * @param botI - the bot (True for bot I, false for bot II)
     * @return
     */
>>>>>>> 54ad28769fd10d579bfe9c7d55da29b9c5e7fd66
    public boolean challengeBot(Player p, boolean botI) {
        //return challenge(p, botI ? new BotI() : new BotII()); //just calls challenge with a bot
        return false;
    }
<<<<<<< HEAD
=======
    /**
     * Add a bot v bot challenge to the queue
     * @param botI - True for bot I, false for bot II
     * @param botII - True for bot I, false for bot II
     * @param spectator - the player who requested to watch
     * @return
     */
>>>>>>> 54ad28769fd10d579bfe9c7d55da29b9c5e7fd66
    public boolean botVBot(boolean botI, boolean botII, HumanPlayer spectator) {
        //return challenge(botI ? new BotI() : new BotII(), botI ? new BotI() : new BotII()); //just calls challenge with a bot
        return false;
    }

<<<<<<< HEAD
    public boolean boardAvailable() {return false;} //Called by GameManager when a board becomes available (in case all were previously taken)

=======
    /**
     * Event method, to let PairUp know that a board is now available that previously was not.
     * Called by GameManager when a game is ended and the board is clear.
     * @return
     */
    public boolean boardAvailable() {return false;}

    /**
     * Returns the number of players in the queue.
     * For Join Game page to display to users.
     * @return
     */
>>>>>>> 54ad28769fd10d579bfe9c7d55da29b9c5e7fd66
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
