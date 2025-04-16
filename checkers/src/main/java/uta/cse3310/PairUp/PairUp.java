package uta.cse3310.PairUp;

import java.util.ArrayList;
import java.util.LinkedList;
import uta.cse3310.Bot.BotI.BotI;
import uta.cse3310.Bot.BotII.BotII;

import uta.cse3310.GameManager.GameManager;
import uta.cse3310.PageManager.HumanPlayer;


public class PairUp {
    private LinkedList<Challenge> playerQueue;
    private GameManager gm;
    private int numPlayersInQueue;

    private static final int MAX_ELO_DISPARITY = 300;

    /**
     * Create a new PairUp object. Should only be called ONCE, and only by Page Manager.
     * @param gm
     */
    public PairUp(GameManager gm) {
        this.playerQueue = new LinkedList<>();//Queue of players waiting to be paired
        this.gm = gm;
        numPlayersInQueue = 0;

    }

    //Compares elo scores. If either is not a HumanPlayer, return true
    private boolean isInRange(Player p1, Player p2) {
        // If either player is not a HumanPlayer, return true
        if (!(p1 instanceof HumanPlayer) || !(p2 instanceof HumanPlayer)) {
            return true;
        }

        int elo1 = p1.getELO();
        int elo2 = p2.getELO();

        int diff = Math.abs(elo1 - elo2);

        // If elo difference is within 300, return true
        return diff <= MAX_ELO_DISPARITY;
    }

    //This is where the actual pairing will take place. Will be called by boardAvailable, challenge, and addToQueue
    private void pairUp() {
        if (playerQueue.size() <= 1) return; //If there are no players in the queue, return
        if (gm.getNumOfAvailableGames() <= 0) return; // If there aren't any open boards, return

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

    } 

    /**
     * Add a player to the challenge queue. Called by PageManager when a client requests matchmaking
     * @param p - the Player who requested the matchmaking
     * @return - false if the player was not added to the queue, true otherwise
     */
    public boolean addToQueue(Player p) {
      
        //If there are no games available, add the player to the queue
        Challenge challenge = new Challenge(p);
        numPlayersInQueue++; //Increment the number of players in the queue
        playerQueue.add(challenge);
        pairUp();
        return true;
    }

    /**
     * Add a player v player challenge to the queue - after it has been accepted
     * @param p - the player who requested the challenge
     * @param c - the player who accepted the challenge
     * @return - false if the challengers were not added to the queue, true otherwise
     */
    public boolean challenge(Player p, Player c) {
        return challenge(p, c, null);
    }

    /**
     * Add a player v player challenge to the queue - after it has been accepted
     * @param p - the player who requested the challenge
     * @param c - the player who accepted the challenge
     * @param spectator - can be null. HumanPlayer who will be the spectator
     * @return - false if the challengers were not added to the queue or a game, true otherwise
     */
    public boolean challenge(Player p, Player c, HumanPlayer spectator) {
        if (gm.getNumOfAvailableGames() > 0 && gm.createGame(p, c)) {
            //Queue was bypassed
            return true;
        }
        //Add players to the queue
        Challenge challenge = new Challenge(p, c, spectator);
        numPlayersInQueue += 2;
        playerQueue.add(challenge);
        pairUp();
        return true;
    }

    /**
     * Add a player v bot challenge to the queue
     * @param p - The player who requested the challenge
     * @param botI - the bot (True for bot I, false for bot II)
     * @return - false if the challengers were not added to the queue, true otherwise
     */
    public boolean challengeBot(Player p, boolean botI) {
        return challenge(p, botI ? new BotI() : new BotII(), null);//just calls challenge with a bot
    }

    /**
     * Add a bot v bot challenge to the queue
     * @param botI - True for bot I, false for bot II
     * @param botII - True for bot I, false for bot II
     * @param spectator - the player who requested to watch
     * @return - false if the challengers were not added to the queue, true otherwise
     */
    public boolean botVBot(boolean botI, boolean botII, HumanPlayer spectator) {
        Player b1 = botI ? new BotI() : new BotII();
        Player b2 = botII ? new BotI() : new BotII();

        return challenge(b1, b2, spectator);
    }
     
    /**
     * 
     * @param p - The player to remove (should be a HumanPlayer)
     * @return - True if the player was removed from the queue
     */
    public boolean removeFromQueue(Player p) {
        if (p instanceof HumanPlayer) {
            //Find player p in the queue
            for (int c=0; c<playerQueue.size(); c++) {
                Challenge challenge = playerQueue.get(c);
                if (challenge.first.equals(p) || challenge.second.equals(p)) {
                    //Found it
                    if (challenge.hasJustOne) {
                        numPlayersInQueue -= 1;
                    } else {
                        numPlayersInQueue -= 2;
                    }
                    playerQueue.remove(c);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Event method, to let PairUp know that a board is now available that previously was not.
     * Called by GameManager when a game is ended and the board is clear.
     * @return - false if there was an error, true otherwise
     */
    public boolean boardAvailable() {
        try {
            pairUp(); // Attempt to make a new match
            return true;
        } catch (Exception e) { //excpetion thrown when match could not be made by pairUp()
            return false;
        }
    }
    
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
        spectators = new ArrayList<>();
    }
    public Challenge(Player p, Player c) {
        first = p;
        second = c;
        hasJustOne = false;
        spectators = new ArrayList<>();
    }
    public Challenge(Player p, Player c, HumanPlayer spectator) {
        first = p;
        second = c;
        hasJustOne = false;
        spectators = new ArrayList<>();
        spectators.add(spectator);
    }
}
