
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
                HumanPlayer newPlayer = new HumanPlayer(username, password);
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
        //Get player by password
        public HumanPlayer getPlayerByPassword(String password) {
                for (HumanPlayer player : players) {
                    if (player.getPassword().equals(password)) {
                        return player;
                    }
                }
                return null;  // Player not found
        }
}
