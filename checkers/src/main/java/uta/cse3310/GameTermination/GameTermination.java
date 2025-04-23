package uta.cse3310.GameTermination;

import uta.cse3310.GameManager.Game;
import uta.cse3310.App;
import uta.cse3310.DB.DB;
import uta.cse3310.PageManager.HumanPlayer;
import uta.cse3310.PageManager.PageManager;
import uta.cse3310.PageManager.UserEventReply;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;

public class GameTermination {
        // Tells game play game is over. 
/*
        public void checkGameEnd(GameState gameState) {
                // Logic to determine if the game has ended
                if (gameHasEnded) {
                    GameResult gameResult = new GameResult();
                    gameResult.generateLeaderboard();
                }
            } */
        // Tells game play game is over.

        // New endGame functionality.  
        public Game endGame(Game currentGame) {
                gameState state = new gameState();
                int winnerID = -1; // -1 for draw

                // Check if player 1 has won
                if (state.hasPlayerWon(currentGame.getBoard().getBoard(), currentGame.getPlayer1().getPlayerId())) {
                        winnerID = currentGame.getPlayer1().getPlayerId();
                        currentGame.setGameActive(false);
                        //saveResults(currentGame, winnerID);
                        return currentGame;
                }

                // Check if player 2 has won
                if (state.hasPlayerWon(currentGame.getBoard().getBoard(), currentGame.getPlayer2().getPlayerId())) {
                        winnerID = currentGame.getPlayer2().getPlayerId(); 
                        currentGame.setGameActive(false);
                        //saveResults(currentGame, winnerID);
                        return currentGame;
                }

                // Check for draw
                if (state.gameStateDraw(currentGame.getBoard().getBoard(), currentGame)) {
                        winnerID = -1;
                        currentGame.setGameActive(false);
                        //saveResults(currentGame, winnerID);
                        return currentGame;
                }

                // Checks if game ended before it was over. 
                if(!(currentGame.isAvailable))
                {
                        winnerID = -2; // Indicates game ended early
                        return currentGame; 
                }

                return null;
        }

        public HumanPlayer[] saveResults(Game game){
                DB database = new DB();
                gameState state = new gameState();

                //ensure both platers are Humans(not Bots)
                if(!(game.getPlayer1() instanceof HumanPlayer) || !(game.getPlayer2() instanceof HumanPlayer )){
                        System.out.println("BOT DETECTED");
                        return new HumanPlayer[] {null , null};
                }

                //cast players
                HumanPlayer gamePlayer1 = (HumanPlayer) game.getPlayer1();
                HumanPlayer gamePlayer2 = (HumanPlayer) game.getPlayer2();

                //retrieves username
                String username1 = gamePlayer1.getUsername();
                String username2 = gamePlayer2.getUsername();

                //retrieve players current stats
                HumanPlayer player1 = database.getPlayerByUsername(username1);
                HumanPlayer player2 = database.getPlayerByUsername(username2);

                //safety check to avoid game corruption
                if(player1 == null || player2 == null){
                        System.out.println("Player not detected");
                        return new HumanPlayer[]{null, null};
                }

                //Determine who won game
                int winnerID = state.checkForWinningPlayer(game.getBoard().getBoard(), game);
                
                //extract current player stats
                int p1Id = player1.getPlayerId();
                int p2Id = player2.getPlayerId();

                int  p1Wins = player1.getWins();
                int  p1Losses = player1.getLosses();
                int  p1Games = player1.getGamesPlayed();
                int  p1Elo = player1.getELO();

                int  p2Wins = player2.getWins();
                int  p2Losses = player2.getLosses();
                int  p2Games = player2.getGamesPlayed();
                int  p2Elo = player2.getELO();
                
                int updatedElo1 = p1Elo;
                int updatedElo2 = p2Elo;

                //Handles player draw
                if (winnerID == -1){
                        database.updatePlayerStats(p1Id, p1Wins, p1Losses, p1Elo, p1Games + 1);
                        database.updatePlayerStats(p2Id, p2Wins, p2Losses, p2Elo, p2Games + 1);
                //Handles Player 1 Win
                }else if(winnerID == p1Id){
                        updatedElo1 = calculateElo(p1Elo, p2Elo, true);
                        updatedElo2 = calculateElo(p2Elo, p1Elo, false);

                        database.updatePlayerStats(p1Id, p1Wins + 1, p1Losses, updatedElo1, p1Games + 1);
                        database.updatePlayerStats(p2Id, p2Wins, p2Losses + 1, updatedElo2, p2Games + 1);
                //Handles Player 2 Win
                }else if(winnerID == p2Id){
                        updatedElo2 = calculateElo(p2Elo, p1Elo, true);
                        updatedElo1 = calculateElo(p1Elo, p2Elo, false);

                        database.updatePlayerStats(p2Id, p2Wins + 1, p2Losses, updatedElo2, p2Games + 1);
                        database.updatePlayerStats(p1Id, p1Wins, p1Losses + 1, updatedElo1, p1Games + 1);
                }

                //return updated player stats
                HumanPlayer[] updatedStats = new HumanPlayer[2];
                updatedStats[0] = database.getPlayerByUsername(username1);
                updatedStats[1] = database.getPlayerByUsername(username2);

                //send updated leaderboard to both players before returning
                App.sendMessage(App.pmInstance.retrieveLeaderboardJson(new JsonObject(), player1.getPlayerId()));
                App.sendMessage(App.pmInstance.retrieveLeaderboardJson(new JsonObject(), player2.getPlayerId()));

                return updatedStats;
        }
        
        //moved here so Elo can be calculated easily
        private int calculateElo(int playerElo, int opponentElo, boolean isWinner) {
                double expectedScore = 1.0 / (1.0 + Math.pow(10, (opponentElo - playerElo) / 400.0));
                return (int)(playerElo + 32 * ((isWinner ? 1 : 0) - expectedScore));

        }

}






