package uta.cse3310.GameTermination;

import uta.cse3310.GameManager.Game;
import uta.cse3310.DB.DB;


public class GameTermination {
        // Tells game play game is over. 
        public Game endGame() {
                Game game = new Game(0, null, null); // temp code for compiling purposes 
                return game;
                // endGame method basic functionality; need to fix errors before committing
                /*
                        // Tells game play game is over. 
                        public Game endGame(Game currentGame) 
                        {
                                gameState state = new gameState(); 
                                gameResult result = new gameResult(); 

                                // Check if player 1 is winning player.  
                                if (state.hasPlayerWon(currentGame.Board, currentGame.player1))
                                {
                                        currentGame.playerID = currentGame.player1; 
                                        saveResults(currentGame); 
                                        return currentGame; 
                                }

                                // Check if player 2 is winning player. 
                                if (state.hasPlayerWon(currentGame.Board, currentGame.player2))
                                {
                                        currentGame.playerID = currentGame.player2; 
                                        saveResults(currentGame); 
                                        return currentGame; 
                                }

                                // Check if draw 
                                if (state.gameStateDraw(currentGame.Board, currentGame.player2))
                                {
                                        currentGame.playerID = -1; // -1 denotes a draw
                                        saveResults(currentGame); 
                                        return currentGame; 
                                }

                                // Game still ongoing
                                return null; 
                          }
                */
        }

        // updates each player stats based on match results sends to db
// public void saveResults(Game player1, Game player2){



        }

        //    public GameResult evaluate(GameState gameState) {

        // boolean p1HasPieces = gameState.hasPieces("P1");

        // boolean p2HasPieces = gameState.hasPieces("P2");



        // boolean p1CanMove = gameState.hasValidMoves("P1");

        // boolean p2CanMove = gameState.hasValidMoves("P2");



        /**i

         * f (!p1HasPieces || !p1CanMove) {

            if (!p2HasPieces || !p2CanMove) {

                return GameResult.DRAW;

            }

            return GameResult.PLAYER_TWO_WINS;

        }



        if (!p2HasPieces || !p2CanMove) {

            return GameResult.PLAYER_ONE_WINS;

        }



        return GameResult.IN_PROGRESS;

         }

        } */



}
}
        

