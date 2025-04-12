
package uta.cse3310.DB;

import java.util.LinkedList;
import uta.cse3310.DB.PasswordManager;
import uta.cse3310.PageManager.HumanPlayer;


public class DB 
{
    /*declaring conn variable with Connection type, Connection will let us SELECT, INSERT, and UPDATE the database*/
    //private Connection conn;
    private LinkedList<HumanPlayer> players;

    public DB() 
    {
        players = new LinkedList<>();
    }
    	/*
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:players.db");
            System.out.println("Connected to SQLite database.");
            initializeDatabase();
        } catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
        }
    }
    /*
    
    /*This block will create the "playters.db" if it doesnt already exist.
     * 
     * Table will look the following way:
     * id: | username: | password: | salt: | wins=0 | losses=0 | elo=1000 | games_played=0
     *  
     *  */
    /*
    private void initializeDatabase() {
        String sql = "CREATE TABLE IF NOT EXISTS players (" +
                     "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                     "username TEXT UNIQUE NOT NULL," +
                     "password TEXT NOT NULL," +
                     "salt TEXT NOT NULL," +
                     "wins INTEGER DEFAULT 0," +   
                     "losses INTEGER DEFAULT 0," + 
                     "elo INTEGER DEFAULT 1000," + //Unsure how we are gonig to do elo, starting at 1000 for now
                     "games_played INTEGER DEFAULT 0)";
        
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println("Failed to create table: " + e.getMessage());
        }
    }
    */
    /* if a username is new it will add new player */

    // testing if we need to return a boolean or void
    public boolean addPlayer(String username, String password) 
    {
        if (getPlayerByUsername(username) != null) 
        {
            return false; /*if username exists it will return */    //Temporary, please fix when you can
        }
        
        /* 
        String salt = PasswordManager.generateSalt();
        String hashedPassword = PasswordManager.hashPassword(password, salt);
        

        HumanPlayer newPlayer = new HumanPlayer(username, hashedPassword, salt);
        players.add(newPlayer);
        return true; // successfully added
        */
        return false; //Temporary, please fix when you can
    }

    /* gets a player using playerId, and return if the player is found */
    public HumanPlayer getPlayerById(int playerId) 
    {
        for (HumanPlayer player : players) 
        {
            if (player.getPlayerId() == playerId) 
            {
                return player;
            }
        }
        return null;  /* null if Player not found */
    }

    /* gets player using username and it will return if it found  */
    public HumanPlayer getPlayerByUsername(String username) 
    {
        for (HumanPlayer player : players) 
        {
            if (player.getUsername().equals(username)) 
            {
                return player;
            }
        }
        return null;  /* null when Player not found */
    }

    /* this method will verify password but havenot implemented yet */
    public boolean verifyPassword(String username, String password) 
    {
        HumanPlayer player = getPlayerByUsername(username);
        if (player == null) 
        {
            return false; 
        }
        //return PasswordManager.verifyPassword(password, player.getPassword(), player.getSalt());
        return false; /* this will be implemented later */
        
    }

    /* This will calculate the total number of game played by adding the total games played by players*/
    public int getTotalGamesPlayed() 
    {
        int Total_Games = 0;
        for (HumanPlayer player : players) 
        {
            Total_Games += player.getGamesPlayed();
        }
        return Total_Games;
    }
        /* This methods is called from addPlayer and will create a text document storing what is being stored in the DB per person. 
     * 
     * gabriel wynne
     * Function savePlayerToFile(player):
	    	Create a file named "player_<playerId>.txt"
	
	    	Write the following to the file, each on a new line:
		        - player's username
		        - player's hashed password
		        - player's salt
		        - player's number of wins followed by " wins"
		        - player's number of losses followed by " losses"
		        - player's ELO followed by " ELO"
		        - player's total games played followed by " games played"
	
	    	Close the file
     * -gw*/
    
    /* This method will update Players status their ID, wins, losses, Elo and gamesPlayed */
    public boolean updatePlayerStats(int playerId, int wins, int losses, int ELO, int gamesPlayed) 
    {
        for (HumanPlayer player : players) 
        {
            if (player.getPlayerId() == playerId) 
            {
                player.setWins(wins);
                player.setLosses(losses);
                player.setELO(ELO);
                player.setGamesPlayed(gamesPlayed);
                return true;  /* if Status are updated with no any issues */
            }
        }
        return false;
    }
    /* This method will returns top 10 players ordered with their ELO */	
    public HumanPlayer[] getTop10PlayersByElo()
    {
	return new HumanPlayer[0];
    }

    // TODO this method will return the player using username and password
    public HumanPlayer getPlayer(String username, String password)
    {
        // string sql = "SELECT * FROM players WHERE username = ? AND password = ?";
        return null;  /* null when Player not found */
    }

}
