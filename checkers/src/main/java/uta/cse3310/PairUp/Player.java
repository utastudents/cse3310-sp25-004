package uta.cse3310.PairUp;

public abstract class Player {
    public static enum STATUS {
        IN_GAME,
        IN_QUEUE,
        ONLINE
    };

    private static int nextId = 1;    
    protected int playerId;
    protected STATUS status = STATUS.ONLINE;

    /** Methodology
        - Game Manager will call these methods
        - The implementer will make a decision
        - The implementer will call a method in Game Manager
        - The implementer will return true (not necessarily before or after calling another method in GM)
     */

    public abstract boolean makeMove(/** BoardState */); //Returns false if the client could not be reached
    public abstract boolean updateBoard(/** BoardState */); //Returns false if the client could not be reached

    public static int nextId() {return nextId++;}

    public int getPlayerId() {return playerId;}
    public STATUS getStatus() {return status;}
}
