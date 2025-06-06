package uta.cse3310.PairUp;

import uta.cse3310.GameManager.Game;
import uta.cse3310.GamePlay.GamePlay;

public abstract class Player {
    public static enum STATUS {
        IN_GAME,
        IN_QUEUE,
        ONLINE
    };

    private static int nextId = 1;    
    protected int playerId;
    protected STATUS status = STATUS.ONLINE;
    protected Game game;
    protected int ELO;

    /** Methodology
        - Game Manager will call these methods
        - The implementer will make a decision
        - The implementer will call a method in Game Manager
        - The implementer will return true (not necessarily before or after calling another method in GM)
     */

    public abstract boolean makeMove(GamePlay gs); //Returns false if the client could not be reached
                                                    //will uncomment soon
    public abstract boolean updateBoard(GamePlay gs); //Returns false if the client could not be reached
    public abstract boolean endGame(GamePlay gs); //Let the player know that the game has ended. Include the GS to show what the last move was

    //If this method is overridden, be sure to do super.startGame(g) or to store g in game yourself
    public boolean startGame(Game g) {
        game = g;
        return true;
    }

    public static int nextId() {return nextId++;}

    public int getPlayerId() {return playerId;}
    public STATUS getStatus() {return status;}
    public int getELO(){return ELO;}

    public void setELO(int ELO){this.ELO = ELO;}
    public void setStatus(STATUS s) {this.status = s;}

    public boolean equals(Player p) {
        return this.playerId == p.playerId;
    }
}
