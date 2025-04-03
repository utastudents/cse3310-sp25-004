package uta.cse3310.PageManager;

public class GameUpdate {
    private boolean validMove;        
    private String gameStatus;         
    private String winner;            
    private boolean capture;          
    private boolean promotion;         
    private String capturedPosition;   

    //setters
    public GameUpdate(boolean validMove, String gameStatus, String winner, boolean capture, boolean promotion, String capturedPosition) {
        this.validMove = validMove;
        this.gameStatus = gameStatus;
        this.winner = winner;
        this.capture = capture;
        this.promotion = promotion;
        this.capturedPosition = capturedPosition;
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
}
