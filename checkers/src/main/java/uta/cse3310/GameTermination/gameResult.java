package uta.cse3310.GameTermination;

import java.util.HashMap;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
// these should not be here.
// code in this part of the system should not deal with json objects.
//import org.json.JSONObject;
//import org.json.JSONArray;


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
    //     please send this via page manager.
    //     you do not know where they need to go.
    //
    //     please also pay attention in class where these items are discussed
    //JSONObject msg = new JSONObject();
    //msg.put("type", "leaderboard");
    //msg.put("data", new JSONArray(topPlayers));

    // Send the JSON message to frontend (placeholder method)
    // Replace these with actual player/client socket methods if available
    //WebSocketManager.sendToAllClients(msg.toString());

}


        
    }



