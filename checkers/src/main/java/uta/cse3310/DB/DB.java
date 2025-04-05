
package uta.cse3310.DB;

import java.util.LinkedList;
import uta.cse3310.DB.PasswordManager;
import uta.cse3310.PageManager.HumanPlayer;


public class DB 
{
    private LinkedList<HumanPlayer> players;

    public DB() 
    {
        players = new LinkedList<>();
    }

    /* if a username is new it will add new player */
    public void addPlayer(String username, String password) 
    {
        if (getPlayerByUsername(username) != null) 
        {
            return; /*if username exists it will return */
        }
        String salt = PasswordManager.generateSalt();
        String hashedPassword = PasswordManager.hashPassword(password, salt);

        HumanPlayer newPlayer = new HumanPlayer(username, hashedPassword, salt);
        players.add(newPlayer);
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
        return PasswordManager.verifyPassword(password, player.getPassword(), player.getSalt());
        
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
        return false;  
    }
}
