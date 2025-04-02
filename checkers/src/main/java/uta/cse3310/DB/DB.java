
/* William Sarratt 1001858341 */
package uta.cse3310.DB;

import java.util.LinkedList; 
import uta.cse3310.PageManager.HumanPlayer;
        
public class DB {
    private LinkedList<HumanPlayer> players;
    private int nextPlayerId = 1;
         
    public DB() {
        players = new LinkedList<>();
    }
  
