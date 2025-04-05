package uta.cse3310.GameTermination;

import uta.cse3310.GameManager.Game;
import uta.cse3310.GamePlay.Board;

public class gameState{

//function to see if player can still make legal moves; will use same function as gamePlay.
public boolean canPlayerMove(Board gameState, int playerId){

//assume player has moves
//replace logic to check board for valid moves

return true;
}

//function to see if player has won
public boolean hasPlayerWon(Board gameState, int playerID){
return false;
}


Board gameState = new Board();

//checks gameState after each move to see if a player is winning
public int checkForWinningPlayer(Board gameState, Game player1, Game player2){
return -1;
}

//checks gameState for a draw
public boolean gameStateDraw(){
return false;
}



}

