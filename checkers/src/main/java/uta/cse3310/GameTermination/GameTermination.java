package uta.cse3310.GameTermination;

import uta.cse3310.GameManager.Game;
import uta.cse3310.DB.DB;


public class GameTermination {
        // Tells game play game is over. 
        public Game endGame() {
                Game game = new Game(0, 1); // temp code for compiling purposes 
                return game;
        }

        //saves the results of the end of the match and put into database for leaderboard
        public void saveResults(Game player1, Game player2){

        }
        //    public GameResult evaluate(GameState gameState) {
        // boolean p1HasPieces = gameState.hasPieces("P1");
        // boolean p2HasPieces = gameState.hasPieces("P2");


}
        

