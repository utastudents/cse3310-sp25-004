package uta.cse3310.PageManager;

public class GameMove {
    private int clientId;
    private String fromPosition;
    private String toPosition;
    private String pieceType;

  
    public GameMove(int clientId, String fromPosition, String toPosition, String pieceType) {
        this.clientId = clientId;
        this.fromPosition = fromPosition;
        this.toPosition = toPosition;
        this.pieceType = pieceType;
    }
    //getters
    public int getClientId() { 
        return clientId; 
    }
    public String getFromPosition() {
         return fromPosition; 
    }
    public String getToPosition() {
         return toPosition; 
    }
    public String getPieceType() { 
        return pieceType; 
    }
    //setters
    public void setClientId(int clientId) {
         this.clientId = clientId; 
    }
    public void setFromPosition(String fromPosition) { 
        this.fromPosition = fromPosition;
    }
    public void setToPosition(String toPosition) { 
        this.toPosition = toPosition; 
    }
    public void setPieceType(String pieceType) { 
        this.pieceType = pieceType; 
    }

}
