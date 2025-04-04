package uta.cse3310.GameTermination;

import java.util.HashMap;
import uta.cse3310.GameManager.GameCommunication;
import uta.cse3310.GamePlay.Board;
import uta.cse3310.GameManager.Game;

public class gameState{
    //todo: add function to see if player can still make legal moves; will use same function as gamePlay.
     //todo: add function to see if player has won


    Board gameState = new Board();

    //Checks gameState after each move to see if a player is winning
    public int checkForWinningPlayer(Board gameState, Game player1, Game player2){
        return -1;
    }

    //checks gameState if the game is draw. 
    public boolean gameStateDraw(){
        return false;
    }



    
    
}
