package uta.cse3310.GameTermination;

import java.util.HashMap;

import uta.cse3310.GameManager.Game;

public class gameResult{
    // HashMap to store  players scores. 
    private HashMap<String, Integer> playerScores = new HashMap<>();

    // Updates the scores of players at the end of the game. 
    public void updateGameResult(Game player1, Game player2){
        
        
    }

    // Generates and displays the leaderboard based on player scores. 
    public void generateLeaderboard(){
        List<String> topPlayers = new ArrayList<>();
        String url = "jdbc:sqlite:game.db";

        String sql = "SELECT username, wins FROM players ORDER BY wins DESC LIMIT 10";

        try (Connection conn = DriverManager.getConnection(url);
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {

        while (rs.next()) {
            String entry = rs.getString("username") + " - Wins: " + rs.getInt("wins");
            topPlayers.add(entry);
        }

    } catch (SQLException e) {
        System.out.println("Error retrieving leaderboard: " + e.getMessage());
    }

    // Proceed to send this list to the frontend
    
}


        
    }



