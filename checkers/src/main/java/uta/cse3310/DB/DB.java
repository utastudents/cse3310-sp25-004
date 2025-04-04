
/* William Sarratt 1001858341 */
package uta.cse3310.DB;

import java.util.LinkedList; 
import uta.cse3310.PageManager.HumanPlayer;
        
public class DB 
{
        private LinkedList<HumanPlayer> players;
        private int nextPlayerId = 1;
         
        public DB() 
        {
                players = new LinkedList<>();
        }
        /* Nandisha Maharjan */
        /* Insert a new player to the DB */
        public void addPlayer(String username, String password) 
        {
                if (getPlayerByUsername(username) != null) 
                {
                        return; // Username already exists, do nothing
                }
                HumanPlayer newPlayer = new HumanPlayer(username, password, nextPlayerId++);
                players.add(newPlayer);
        }
                
        /* Get player by playerId */
        public HumanPlayer getPlayerById(int playerId) 
        {
                for (HumanPlayer player : players) 
                {
                        if (player.getPlayerId() == playerId) 
                        {
                                return player;
                        }
                }
                return null;  /* Player not found */
        }
                
        /* Get player by username */
        public HumanPlayer getPlayerByUsername(String username) 
        {
                for (HumanPlayer player : players) 
                {
                        if (player.getUsername().equals(username)) 
                        {
                                return player;
                        }
                }
                return null;  /* Player not found */
        }
        /* Natalie Tran */
        /* verifying  password */
        public boolean verifyPassword(String username, String password) 
        {
                HumanPlayer player = getPlayerByUsername(username);
                return player != null && checkPassword(password, player.getPassword());
        }

        /* Get total games played across all players */
        public int getTotalGamesPlayed() 
        {
                int totalGames = 0;
                for (HumanPlayer player : players) 
                {
                        totalGames += player.getGamesPlayed();
                }
                return totalGames;
        }

        /* Update player's status */
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
                                return true;  // Player updated successfully
                        }
                }
                return false;  // Player not found  
        }
        
}
