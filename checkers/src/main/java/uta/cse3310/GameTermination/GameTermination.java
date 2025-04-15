package uta.cse3310.GameTermination;

import uta.cse3310.GameManager.Game;
import uta.cse3310.DB.DB;
import uta.cse3310.PageManager.HumanPlayer;

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

        public HumanPlayer[] saveResults(Game game){
                DB database = new DB();
                gameState state = new gameState();
                

                //retrieves playerId
                int player1Id = game.getPlayer1().getPlayerId();
                int player2Id = game.getPlayer2().getPlayerId();

                //Determine which player is winning
                int winnerID = state.checkForWinningPlayer(game.getBoard().getBoard(), game, game);
                
                //retrieve players current stats
                HumanPlayer player1 = database.getPlayerById(player1Id);
                HumanPlayer player2 = database.getPlayerById(player2Id);
                
                //if no games have ever been played start with 0
                int p1Wins = 0, p1Losses = 0, p1Games = 0;
                int p2Wins = 0, p2Losses = 0, p2Games = 0;
                
                //check incase players aren't detected
                if(player1 != null && player2 != null){
                        p1Wins = player1.getWins();
                        p1Losses = player1.getLosses();
                        p1Games = player1.getGamesPlayed();

                        p2Wins = player2.getWins();
                        p2Losses = player2.getLosses();
                        p2Games = player2.getGamesPlayed();
                }

                //Handles player draw
                if (winnerID == -1){
                        database.updatePlayerStats(player1Id, p1Wins, p1Losses, 0, p1Games + 1);
                        database.updatePlayerStats(player2Id, p2Wins, p2Losses, 0, p2Games + 1);
                //Handles Player 1 Win
                }else if(winnerID == player1Id){
                        database.updatePlayerStats(player1Id, p1Wins + 1, p1Losses, 0, p1Games + 1);
                        database.updatePlayerStats(player2Id, p2Wins, p1Losses + 1, 0, p2Games + 2);
                //Handles Player 2 Win
                }else if(winnerID == player2Id){
                        database.updatePlayerStats(player2Id, p2Wins + 1, p2Losses, 0, p2Games + 1);
                        database.updatePlayerStats(player1Id, p1Wins, p1Losses + 1, 0, p1Games + 1);
                }

                //return updated player stats
                HumanPlayer[] updatedStats = new HumanPlayer[2];
                updatedStats[0] = database.getPlayerById(player1Id);
                updatedStats[1] = database.getPlayerById(player2Id);
                
                return updatedStats;
        }

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





        

