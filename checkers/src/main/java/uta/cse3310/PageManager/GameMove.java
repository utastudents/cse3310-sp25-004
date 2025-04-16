package uta.cse3310.PageManager;

public class GameMove {
    private int clientId;
    private int fromPosition_X;
    private int fromPosition_Y;
    private int toPosition_X;
    private int toPosition_Y;
    private String pieceType;

  
    public GameMove(int clientId, int fromPosition_X, int fromPosition_Y, int toPosition_X,int toPosition_Y, String pieceType) {
        this.clientId = clientId;
        this.fromPosition_X = fromPosition_X;
        this.fromPosition_Y = fromPosition_Y;
        this.toPosition_X = toPosition_X;
        this.toPosition_Y = toPosition_Y;
        this.pieceType = pieceType;
    }
    //getters
    public int getClietId() { 
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
    //setters
    public void setClientId(int clientId) {
         this.clientId = clientId; 
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
