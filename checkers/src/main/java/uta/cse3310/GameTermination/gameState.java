package uta.cse3310.GameTermination;

import uta.cse3310.GameManager.Game;
import uta.cse3310.GamePlay.Checker;
import uta.cse3310.GamePlay.Board;
import uta.cse3310.GamePlay.Cord;

public class gameState{


//function to see if player has won
public boolean hasPlayerWon(Board board, int playerID){
return !canPlayerMove(board, getOpponent(playerID));


}


//Board gameState = new Board();

//checks gameState after each move to see if a player is winning
public int checkForWinningPlayer(Board board, Game player1, Game player2){
    if (hasPlayerWon(board, player1.playerID)) {
        return player1.playerID;
    } 
    else if (hasPlayerWon(board, player2.playerID)) {
        return player2.playerID;
    }
    return -1;
}

//checks gameState for a draw
public boolean gameStateDraw(Board board, Game player1, Game player2) {
    return !canPlayerMove(board, player1.playerID) &&
!canPlayerMove(board, player2.playerID);
}

// Utility function for opponent playerID
private int getOpponent(int playerID) {
    return (playerID == 1) ? 2 : 1;
}


}
