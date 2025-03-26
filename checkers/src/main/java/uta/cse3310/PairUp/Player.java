package uta.cse3310.PairUp;

public abstract class Player {
    static enum STATUS {
        IN_GAME,
        IN_QUEUE,
        ONLINE
    };
    
    int playerId;
    STATUS status = STATUS.ONLINE;

    public abstract boolean makeMove(/** BoardState */); //Returns false if the client could not be reached
    public abstract boolean updateBoard(/** BoardState */); //Returns false if the client could not be reached
}
