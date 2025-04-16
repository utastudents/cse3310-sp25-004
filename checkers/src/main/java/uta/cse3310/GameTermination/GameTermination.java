package uta.cse3310.GameTermination;

import uta.cse3310.GameManager.Game;
import uta.cse3310.DB.DB;
import uta.cse3310.PageManager.HumanPlayer;

public class GameTermination {
        // Tells game play game is over.
        public Game endGame() {
                Game game = new Game(0, null, null); // temp code for compiling purposes
                return game;
        // New endGame functionality; Compiles but need to pass board of current game instead of a new board. 
                // Also need to tell GameManager that a parameter was added to this method. 
        /*public Game endGame(Game currentGame) {
                gameState state = new gameState();
                int winnerID = -1; // -1 for draw

                Board myBoard = new Board(); 
                // Check if player 1 has won
                if (state.hasPlayerWon(myBoard, currentGame.getPlayer1().getPlayerId())) {
                        winnerID = currentGame.getPlayer1().getPlayerId(); // lowercase 'd'
                        currentGame.setGameActive(false);
                        saveResults(currentGame, winnerID);
                        return currentGame;
                }

                // Check if player 2 has won
                if (state.hasPlayerWon(myBoard, currentGame.getPlayer2().getPlayerId())) {
                        winnerID = currentGame.getPlayer2().getPlayerId(); // lowercase 'd'
                        currentGame.setGameActive(false);
                        saveResults(currentGame, winnerID);
                        return currentGame;
                }

                // Check for draw
                if (state.gameStateDraw(myBoard, currentGame)) {
                        winnerID = -1;
                        currentGame.setGameActive(false);
                        saveResults(currentGame, winnerID);
                        return currentGame;
                }

                return null;
        }*/
        }
/*
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
                int p1Wins = 0, p1Losses = 0, p1Games = 0, p1Elo = 1000;
                int p2Wins = 0, p2Losses = 0, p2Games = 0, p2Elo = 1000;

                //check incase players aren't detected
                if(player1 != null && player2 != null){
                        p1Wins = player1.getWins();
                        p1Losses = player1.getLosses();
                        p1Games = player1.getGamesPlayed();
                        p1Elo = player1.getELO();

                        p2Wins = player2.getWins();
                        p2Losses = player2.getLosses();
                        p2Games = player2.getGamesPlayed();
                        p2Elo = player2.getELO();
                }

                int updatedElo1 = p1Elo;
                int updatedElo2 = p2Elo;

                //Handles player draw
                if (winnerID == -1){
                        database.updatePlayerStats(player1Id, p1Wins, p1Losses, p1Elo, p1Games + 1);
                        database.updatePlayerStats(player2Id, p2Wins, p2Losses, p2Elo, p2Games + 1);
                //Handles Player 1 Win
                }else if(winnerID == player1Id){
                        updatedElo1 = (int)(p1Elo + 32 * (1 - (1.0/ (1.0 + Math.pow(10, (p2Elo - p1Elo) / 400.0)))));
                        updatedElo2 = (int)(p2Elo + 32 * (1 - (1.0/ (1.0 + Math.pow(10, (p1Elo - p2Elo) / 400.0)))));

                        database.updatePlayerStats(player1Id, p1Wins + 1, p1Losses, updatedElo1, p1Games + 1);
                        database.updatePlayerStats(player2Id, p2Wins, p2Losses + 1, updatedElo2, p2Games + 2);
                //Handles Player 2 Win
                }else if(winnerID == player2Id){
                        updatedElo2 = (int)(p2Elo + 32 * (1 - (1.0/ (1.0 + Math.pow(10, (p1Elo - p2Elo) / 400.0)))));
                        updatedElo1 = (int)(p1Elo + 32 * (1 - (1.0/ (1.0 + Math.pow(10, (p2Elo - p1Elo) / 400.0)))));

                        database.updatePlayerStats(player2Id, p2Wins + 1, p2Losses, updatedElo2, p2Games + 1);
                        database.updatePlayerStats(player1Id, p1Wins, p1Losses + 1, updatedElo1, p1Games + 1);
                }

                //return updated player stats
                HumanPlayer[] updatedStats = new HumanPlayer[2];
                updatedStats[0] = database.getPlayerById(player1Id);
                updatedStats[1] = database.getPlayerById(player2Id);

                return updatedStats;
        }
*/
        }







