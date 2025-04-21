
package uta.cse3310.DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import uta.cse3310.PageManager.HumanPlayer;
import uta.cse3310.PairUp.Player.STATUS;


public class DB 
{
    /*declaring conn variable with Connection type, Connection will let us SELECT, INSERT, and UPDATE the database*/
    private Connection conn;

    public DB() 
    {
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:players.db");
            System.out.println("Connected to SQLite database.");
            initializeDatabase();
        } catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
        }
    }
    
    
    /*This block will create the "playters.db" if it doesnt already exist.
     * 
     * Table will look the following way:
     * id: | username: | password: | salt: | wins=0 | losses=0 | elo=1000 | games_played=0
     *  
     *  */
  
    private void initializeDatabase() 
    {
        String sql = "CREATE TABLE IF NOT EXISTS players (" +
                     "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                     "username TEXT UNIQUE NOT NULL," +
                     "password TEXT NOT NULL," +
                     "salt TEXT NOT NULL," +
                     "wins INTEGER DEFAULT 0," +   
                     "losses INTEGER DEFAULT 0," + 
                     "elo INTEGER DEFAULT 1000," + 
                     "games_played INTEGER DEFAULT 0)";
        
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println("Failed to create table: " + e.getMessage());
        }
    }
    /* if a username is new it will add new player */
    public boolean addPlayer(String username, String password) 
    {
        if (getPlayerByUsername(username) != null) 
        {
            System.out.println("User " + username + " already exists!");
            return false; /*if username exists it will return */
        }
        
        try {
            String sql = "INSERT INTO players (username, password, salt) VALUES (?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                byte[] salt = PasswordManager.generateSalt();
                String hashedPassword = PasswordManager.hashPassword(password, salt);

                pstmt.setString(1, username);
                pstmt.setString(2, hashedPassword);
                pstmt.setBytes(3, salt);
                pstmt.executeUpdate();

                System.out.println("User " + username + " inserted into DB succesfully");

                return true;
            }
        } catch (SQLException e) {
            System.out.println("Failed to add player: " + e.getMessage());
        }
        return false; // if something fails
    }
    
    /* gets player using username and it will return if it found  */
    public HumanPlayer getPlayerByUsername(String username) 
    {
        try {
            String sql = "SELECT * FROM players WHERE username = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, username);
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    String password = rs.getString("password"); //Password here being, of course, the hash
                    int id = rs.getInt("id");
                    int wins = rs.getInt("wins");
                    int losses = rs.getInt("losses");
                    int elo = rs.getInt("elo");
                    int games_played = rs.getInt("games_played");
                
                    return new HumanPlayer(username, password, id, STATUS.ONLINE, wins, losses, elo, games_played);
                    //return new HumanPlayer(rs.getString("username"), rs.getString("password"), rs.getBytes("salt"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching player by username: " + e.getMessage());
        }
        return null;
    }

    /* verifies player login & returns if valid */
    public HumanPlayer getPlayer(String username, String password)
     {
         String sql = "SELECT * FROM players WHERE username = ?"; //Hi, we removed "AND password = ?" so that we could simulate creating and logging in to accounts.
         try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String storedHash = rs.getString("password");
                byte[] salt = rs.getBytes("salt");
                int id = rs.getInt("id");
                int wins = rs.getInt("wins");
                int losses = rs.getInt("losses");
                int elo = rs.getInt("elo");
                int games_played = rs.getInt("games_played");
                
                //checks password
                if (PasswordManager.verifyPassword(password, storedHash, salt)) {
                    return new HumanPlayer(username, password, id, STATUS.ONLINE, wins, losses, elo, games_played);
                    //return new HumanPlayer(rs.getString("username"), storedHash, salt);
                }
            }
         } catch (SQLException e) {
            System.out.println("Error fetching player: " + e.getMessage());
         }
         
         return null;  /* null when Player not found */
     }
 

    /* This will calculate the total number of game played by adding the total games played by players*/
    public int getTotalGamesPlayed() 
    {
        int totalGames = 0;
        try {
            String sql = "SELECT SUM(games_played) AS total FROM players";
            try (Statement stmt = conn.createStatement()) {
                ResultSet rs = stmt.executeQuery(sql);
                if (rs.next()) {
                    totalGames = rs.getInt("total");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error calculating total games played: " + e.getMessage());
        }
        return totalGames;
    }
    
    /* This method will update Players status their ID, wins, losses, Elo and gamesPlayed */
    public boolean updatePlayerStats(int playerId, int wins, int losses, int ELO, int gamesPlayed)
    {
        try {
            String sql = "UPDATE players SET wins = ?, losses = ?, elo = ?, games_played = ? WHERE id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) 
            {
                pstmt.setInt(1, wins);
                pstmt.setInt(2, losses);
                pstmt.setInt(3, ELO);
                pstmt.setInt(4, gamesPlayed);
                pstmt.setInt(5, playerId);
                int rowsUpdated = pstmt.executeUpdate();
                return rowsUpdated > 0; // Return true if the update was successful
            }
        }
        catch (SQLException e) 
        {
            System.out.println("Error updating player stats: " + e.getMessage());
            return false;
        }
    }
    
    // This method returns the top 10 players ordered by their ELO
    public HumanPlayer[] getTop10PlayersByElo()
    {
        HumanPlayer[] topPlayers = new HumanPlayer[10];
        try {
            String sql = "SELECT * FROM players ORDER BY elo DESC LIMIT 10";
            try (Statement stmt = conn.createStatement()) {
                ResultSet rs = stmt.executeQuery(sql);
                int index = 0;
                while (rs.next() && index < 10) {
                    String username = rs.getString("username");
                    String password = rs.getString("password"); //Password here being, of course, the hash
                    int id = rs.getInt("id");
                    int wins = rs.getInt("wins");
                    int losses = rs.getInt("losses");
                    int elo = rs.getInt("elo");
                    int games_played = rs.getInt("games_played");
                
                    topPlayers[index++] = new HumanPlayer(username, password, id, STATUS.ONLINE, wins, losses, elo, games_played);
                }
            }
        }
        catch (SQLException e) 
        {
            System.out.println("Error fetching top players: " + e.getMessage());
        }
    
        return topPlayers;
    }
    public void recordMatchResult(int winnerId, int loserId) {
        try {
            String query = "SELECT * FROM players WHERE id = ?";
    
            //fetch winners data
            PreparedStatement stmt1 = conn.prepareStatement(query);
            stmt1.setInt(1, winnerId);
            ResultSet rs1 = stmt1.executeQuery();
            if (!rs1.next()) return; 
    
            //fetch losers data
            PreparedStatement stmt2 = conn.prepareStatement(query);
            stmt2.setInt(1, loserId);
            ResultSet rs2 = stmt2.executeQuery();
            if (!rs2.next()) return;  
    
            //get the info for both of the players
            int winnerElo = rs1.getInt("elo");
            int loserElo = rs2.getInt("elo");
            int winnerWins = rs1.getInt("wins") + 1;  // Winner's wins incremented by 1
            int loserLosses = rs2.getInt("losses") + 1;  // Loser's losses incremented by 1
            int winnerGames = rs1.getInt("games_played") + 1;  // Winner's total games incremented by 1
            int loserGames = rs2.getInt("games_played") + 1;  // Loser's total games incremented by 1

            int k = 32; //Elo constant (can be adjusted)

            int newLoserElo =  (int) (loserElo  + k * (0 - (1.0 / (1.0 + Math.pow(10, (winnerElo - loserElo ) / 400.0)))));
            int newWinnerElo = (int) (winnerElo + k * (1 - (1.0 / (1.0 + Math.pow(10, (loserElo  - winnerElo) / 400.0)))));
    
            updatePlayerStats(winnerId, winnerWins, rs1.getInt("losses"), newWinnerElo, winnerGames);
    
            updatePlayerStats(loserId, rs2.getInt("wins"), loserLosses, newLoserElo, loserGames);
    
        } catch (SQLException e) {
            System.out.println("Error recording match result: " + e.getMessage());
        }
    }

}
