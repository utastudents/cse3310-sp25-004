package uta.cse3310.GameManager;

import uta.cse3310.GamePlay.GamePlay;
import uta.cse3310.GameTermination.GameTermination;
import uta.cse3310.Bot.BotI.BotI;
import uta.cse3310.Bot.BotII.BotII;
import uta.cse3310.PairUp.PairUp;
import java.util.ArrayList;

public class GameManager {
    GamePlay gp;
    GameTermination gt;
    BotI b1;
    BotII b2;
    PairUp pu;
    private ArrayList<Games> games = new ArrayList<>();
    private int maxGames = 10; // essentially a global variable we can check later on

    public GameManager() {
        gp = new GamePlay();
        gt = new GameTermination();
        b1 = new BotI();
        b2 = new BotII();
        pu = new PairUp();
        
        games = new ArrayList<>(maxGames);
    }
    
    public void initializeGames(){
   	
   }
    
    public boolean addToGame{
    	for(int i = 0; i < maxGames; i++){
    		
    	}
    
    	return false;
    }

}
