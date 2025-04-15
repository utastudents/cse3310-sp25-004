package uta.cse3310.Bot.BotI;

import uta.cse3310.GameState;
import uta.cse3310.Bot.Bot;
import uta.cse3310.GameManager.Game;

public class BotI extends Bot {
    
    public void promoteToKing() {
        // TODO: Check if a piece reached the end and promote to king
    }

    
    public void defendPieces() {
        // TODO: Prioritize defending own pieces over offense
    }

    
    public void attackPieces() {
        // TODO: Look for chances to force opponent into bad moves
    }

    
    public void capturePieces() {
        // TODO: Capture opponent piece by jumping diagonally 
    }

    
    public void checkMultipleJumps() {
        // TODO: If multiple jumps are possible, do them
    }
    

    public void moveChecker() {
        // TODO: Move a non-king piece diagonally one square
    }
    
    public void moveKing() { 

        // TODO: Move a king piece diagonally one square in any direction 
    } 
    
    public boolean isMoveLegal() { 
     
        // TODO: Validate if a move is legal 
    return true; 
    } 
    
    public void waitForOpponent() { 
   
        // TODO: Wait for opponent to make a move before acting 
    } 
    
    public void adjustStrategy() { 
    // TODO: Change strategy based on early, mid, or late game 

    } 
    
    public void blockOpponent() { 

        // TODO: Try to block opponent from advancing or jumping 

    } 

    public void findOffensiveMove() { 

        // TODO: Decide if it's safe and smart to attack 

    } 

     public void allowMultipleKings() { 

        // TODO: Allow more than one king on board 

    } 

    public boolean isPieceCaptured(int pieceId) { 

        // TODO: Check if a piece has been captured 

    return false; 

    } 

    public void checkMultipleCaptures() { 

        // TODO: Detect and respond if multiple pieces were taken 

    } 

    public void protectBackLine() { 

        // TODO: Avoid moving back row pieces unless necessary 

    } 

    @Override
    public boolean makeMove(GameState gs) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'makeMove'");
    }


    @Override
    public boolean updateBoard(GameState gs) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateBoard'");
    }


    @Override
    public boolean startGame(Game g) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'startGame'");
    }   

} 
