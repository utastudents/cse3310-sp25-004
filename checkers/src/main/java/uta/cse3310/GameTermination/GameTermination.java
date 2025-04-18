package uta.cse3310.GameTermination;

import uta.cse3310.GameManager.Game;
import uta.cse3310.DB.DB;
import uta.cse3310.PageManager.HumanPlayer;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GameTermination {
//<<<<<<< HEAD
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
//>>>>>>> 53d9da39b39717d17063f1874bfb036c82d15d50
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


                //retrieves playerId
                int player1Id = game.getPlayer1().getPlayerId();
                int player2Id = game.getPlayer2().getPlayerId();

                //Determine which player is winning
                int winnerID = state.checkForWinningPlayer(game.getBoard().getBoard(), game);

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
         // Generates and displays the leaderboard based on player scores. 
         public void generateLeaderboard(int clientId) {
        // Query the database for top 10 players.
        List<String> topPlayers = new ArrayList<>();
        String url = "jdbc:sqlite:game.db";  // Ensure this path is correct for your system

        String sql = "SELECT username, wins FROM players ORDER BY wins DESC LIMIT 10";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                // For simplicity, just create a string representation.
                String entry = rs.getString("username") + " - Wins: " + rs.getInt("wins");
                topPlayers.add(entry);
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving leaderboard: " + e.getMessage());
        }

        // Delegate the sending of the leaderboard data to the PageManager.
        // Note: You could either create a new PageManager instance here or use an existing one if available.
       // PageManager pm = new PageManager();
       // pm.sendLeaderboard(topPlayers, clientId);
    }
}






