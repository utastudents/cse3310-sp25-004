package uta.cse3310.PageManager;

import uta.cse3310.GamePlay.Move;

public class GameMove {
    private int clientId;
    private int fromPosition_X;
    private int fromPosition_Y;
    private int toPosition_X;
    private int toPosition_Y;
    private String pieceType;
    private String color;
    private int gameId;
  
    public GameMove( int clientId,int gameId, int fromPosition_X, int fromPosition_Y, int toPosition_X,int toPosition_Y, String color) {
        this.clientId = clientId; //Player ID, works for Bot or HP
        this.gameId = gameId;
        this.color = color;
        this.fromPosition_X = fromPosition_X;
        this.fromPosition_Y = fromPosition_Y;
        this.toPosition_X = toPosition_X;
        this.toPosition_Y = toPosition_Y;
    }
    public GameMove(GameMove g, Move m) {
        this.clientId = g.clientId;
        this.gameId = g.gameId;
        this.fromPosition_X = m.from.getX();
        this.fromPosition_Y = m.from.getY();
        this.toPosition_X = m.dest.getX();
        this.toPosition_Y = m.dest.getY();
    }
    public GameMove(int cId, int gId, Move m) {
        this.clientId = cId;
        this.gameId = gId;
        this.fromPosition_X = m.from.getX();
        this.fromPosition_Y = m.from.getY();
        this.toPosition_X = m.dest.getX();
        this.toPosition_Y = m.dest.getY();
    }
    //getters
    public int getClientId() { 
        return clientId; 
    }
    public int getFromPosition_X() {
         return fromPosition_X; 
    }
    public int getFromPosition_Y() {
        return fromPosition_Y; 
   }
    public int getToPosition_X() {
         return toPosition_X; 
    }
    public int getToPosition_Y() {
        return toPosition_Y; 
   }
    public String getPieceType() { 
        return pieceType; 
    }
    public String getColor() { 
        return color; 
    }
    public int getGameId() { 
        return gameId; 
    }
    //setters
    public void setClientId(int clientId) {
         this.clientId = clientId; 
    }
    public void setGameId(int gameId) {
        this.gameId = gameId; 
   }
    public void setColor(String color) {
    this.color = color; 
    }
    public void setFromPosition_X(int fromPosition) { 
        this.fromPosition_X = fromPosition;
    }
    public void setFromPosition_Y(int fromPosition) { 
        this.fromPosition_Y = fromPosition;
    }
    public void setToPosition_X(int toPosition) { 
        this.toPosition_X = toPosition; 
    }
    public void setToPosition_Y(int toPosition) { 
        this.toPosition_Y = toPosition; 
    }
    public void setPieceType(String pieceType) { 
        this.pieceType = pieceType; 
    }

}
