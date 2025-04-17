package uta.cse3310.PageManager;
import uta.cse3310.GamePlay.Checker;

public class GameUpdate {
    private boolean validMove;        
    private String gameStatus;         
    private String winner;            
    private boolean capture;          
    private boolean promotion;         
    private String capturedPosition; 
    private transient Checker[][] updatedBoard;  
    private String[][] boardState;

    //setters                                                                                                                         will uncomment after confirming with the gamemanager and gamedisplay
    public GameUpdate(boolean validMove, String gameStatus, String winner, boolean capture, boolean promotion, String capturedPosition/* ,Checker[][] updatedBoard*/) {
        this.validMove = validMove;
        this.gameStatus = gameStatus;
        this.winner = winner;
        this.capture = capture;
        this.promotion = promotion;
        this.capturedPosition = capturedPosition;
        //this.updatedBoard = updatedBoard;
    }

    // Getters 
    public boolean isValidMove() { 
        return validMove; 
    }
    public String getGameStatus() { 
        return gameStatus; 
    }
    public String getWinner() { 
        return winner; 
    }
    public boolean isCapture() { 
        return capture; 
    }
    public boolean isPromotion() { 
        return promotion; 
    }
    public String getCapturedPosition() { 
        return capturedPosition; 
    }
    public Checker[][] getUpdatedBoard(){
        return updatedBoard;

    }
    public void setboardState(String[][] board){
        this.boardState = board;
        
    }
}
