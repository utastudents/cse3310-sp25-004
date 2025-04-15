package uta.cse3310.GameTermination;

import uta.cse3310.GameManager.Game;
import uta.cse3310.GamePlay.Board;
import uta.cse3310.GamePlay.Checker;

public class gameState{


//function to see if a player can still make legal moves
public boolean canPlayerMove(Board board, int playerID){

    //assume player has moves
    //replace logic to check board for valid moves
    for (int y = 0; y < 8; y++) {
        for (int x = 0; x < 8; x++) {
            Checker piece = board.checkerBoard[y][x];
    
            if (piece != null && piece.getPlayerID() == playerID) {
                // Check for possible moves or jumps
                if (!board.getPossibleForwardJump(piece).isEmpty() ||
                    !board.getPossibleBackwardJump(piece).isEmpty()){
                        return true;
                    }
                //checks non-jump diagonal moves
                int dir = (piece.getColor().toString().equals("BLACK")) ? 1 : -1;
                //checks forward left    
                if(isValidNormalMove(board, x - 1, y + dir)) return true;
                //checks forward right        
                if(isValidNormalMove(board, x + 1, y + dir)) return true;
                //check king piece
                if(piece.isKing()){
                    //checks backward left
                    if(isValidNormalMove(board, x - 1, y - dir)) return true;
                    //checks backward right
                    if(isValidNormalMove(board, x + 1, y - dir)) return true;
                }
            }    
        }        
    }
    
    return false;
}

private boolean isValidNormalMove(Board board, int x, int y){
    return x >= 0 && x < 8 && y >= 0 && y < 8 && board.checkerBoard[y][x] == null;
}

//function to see if player has won
public boolean hasPlayerWon(Board board, int playerID){
return !canPlayerMove(board, getOpponent(playerID));


}


//Board gameState = new Board();

//checks gameState after each move to see if a player is winning
public int checkForWinningPlayer(Board board, Game game){
    //put single game in parameter due to parameters in game.java (Group 21)

    //added by Game.java to fix compiling issue
    int player1ID = game.getPlayer1().getPlayerId();
    int player2ID = game.getPlayer2().getPlayerId();

    if (hasPlayerWon(board, player1ID)) {
        return player1ID;
    } 
    else if (hasPlayerWon(board, player2ID)) {
        return player2ID;
    }
    else {
        return -1;
    }
}

//checks gameState for a draw
public boolean gameStateDraw(Board board, Game game) {


    //added by game.java to fix compiling issue
    int player1ID = game.getPlayer1().getPlayerId();
    int player2ID = game.getPlayer2().getPlayerId();

    return !canPlayerMove(board, player1ID) &&
    !canPlayerMove(board, player2ID);
}

// Utility function for opponent playerID
private int getOpponent(int playerID) {
    return (playerID == 1) ? 2 : 1;
}


}
