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
        public void saveResults(HumanPlayer p1, HumanPlayer p2, String resultType){

                // retrieves p1 stats
                int p1Wins = p1.getWins();
                int p1Lossees = p1.getLosses();
                int p1ELO = p1.getELO();
                int p1Games = p1.getGamesPlayed();
                // retrieves p2 stats
                int p2Wins = p2.getWins();
                int p2Lossees = p2.getLosses();
                int p2ELO = p2.getELO();
                int p2Games = p2.getGamesPlayed();
                // changes stats based on result
                switch (resultType){
                        case "P1_WIN":
                                p1Wins++;
                                p1Games++;
                                p2Lossees++;
                                p2Games++;
                                break;
                        case "P2_WIN":
                                p2Wins++;
                                p2Games++;
                                p1Lossees++;
                                p1Games++;
                                break;
                        case "DRAW":
                                p1Games++;
                                p2Games++;
                                break;
                }
                // update p1 HumanPlayer object
                p1.setWins(p1Wins);
                p1.setLosses(p1Lossees);
                p1.setGamesPlayed(p1Games);
                // update p2 HumanPlayer object
                p2.setWins(p2Wins);
                p2.setLosses(p2Lossees);
                p2.setGamesPlayed(p2Games);

                //send p1 updated val to db
                database.updatePlayerStats(p1.getPlayerId(), p1Wins, p1Lossees, p1ELO, p1Games);
                //send p2 updated val to db
                database.updatePlayerStats(p2.getPlayerId(), p2Wins, p2Lossees, p2ELO, p2Games);

        }

}
        

