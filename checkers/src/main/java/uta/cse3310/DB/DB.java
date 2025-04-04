
package uta.cse3310.DB;

import java.util.LinkedList; 
import uta.cse3310.PageManager.HumanPlayer;
import uta.cse3310.DB.PasswordManager;

public class DB {
    /* List to store player objects */
    private LinkedList<HumanPlayer> players;

    /* Constructor: Initializes the players list */
    public DB() {
        players = new LinkedList<>();
    }

    /* Adds a new player to the database. Ensures that usernames are unique before adding. */
    public void addPlayer(String username, String password) {
        if (getPlayerByUsername(username) != null) {
            return; // Username already exists, do nothing
        }
        String salt = PasswordManager.generateSalt();
        String hashedPassword = PasswordManager.hashPassword(password, salt);

        HumanPlayer newPlayer = new HumanPlayer(username, hashedPassword, salt);
        players.add(newPlayer);
    }

    /* Retrieves a player by their unique player ID. Returns the player if found, otherwise null. */
    public HumanPlayer getPlayerById(int playerId) {
        for (HumanPlayer player : players) {
            if (player.getPlayerId() == playerId) {
                return player;
            }
        }
        return null;  /* Player not found */
    }

    /* Retrieves a player by their username. Returns the player if found, otherwise null. */
    public HumanPlayer getPlayerByUsername(String username) {
        for (HumanPlayer player : players) {
            if (player.getUsername().equals(username)) {
                return player;
            }
        }
        return null;  /* Player not found */
    }

    /* Verifies the player's password. Currently not implemented. */
    public boolean verifyPassword(String username, String password) {
        HumanPlayer player = getPlayerByUsername(username);
        if (player == null) {
            return false; // User not found
        }
        return PasswordManager.verifyPassword(password, player.getPassword(), player.getSalt());
        
    }

    /* Calculates the total number of games played across all players. */
    public int getTotalGamesPlayed() {
        int totalGames = 0;
        for (HumanPlayer player : players) {
            totalGames += player.getGamesPlayed();
        }
        return totalGames;
    }

    /* Updates a player's statistics, including wins, losses, ELO, and total games played. 
       Returns true if the update was successful, otherwise false. */
    public boolean updatePlayerStats(int playerId, int wins, int losses, int ELO, int gamesPlayed) {
        for (HumanPlayer player : players) {
            if (player.getPlayerId() == playerId) {
                player.setWins(wins);
                player.setLosses(losses);
                player.setELO(ELO);
                player.setGamesPlayed(gamesPlayed);
                return true;  // Player updated successfully
            }
        }
        return false;  // Player not found  
    }
}
