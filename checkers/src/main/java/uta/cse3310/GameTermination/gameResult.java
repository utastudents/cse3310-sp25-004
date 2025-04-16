package uta.cse3310.GameTermination;

import java.util.HashMap;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
//import uta.cse3310.PageManager.PageManager;


import uta.cse3310.GameManager.Game;

public class gameResult{
    // HashMap to store  players scores. 
    private HashMap<String, Integer> playerScores = new HashMap<>();

    // Updates the scores of players at the end of the game. 
    public void updateGameResult(Game player1, Game player2){
        
        
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