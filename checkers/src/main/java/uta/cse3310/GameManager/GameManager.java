package uta.cse3310.GameManager;

// import uta.cse3310.GamePlay.GamePlay;
import uta.cse3310.GameTermination.GameTermination;
import uta.cse3310.Bot.BotI.BotI;
import uta.cse3310.Bot.BotII.BotII;
import uta.cse3310.GameManager.Game;
import java.util.ArrayList;

public class GameManager {
    // GamePlay gp;
    GameTermination gt;
    BotI b1;
    BotII b2;
    private ArrayList<Game> games = new ArrayList<>();
    private static final int maxGames = 10; // essentially a global variable we can check later on
   
    public GameManager() {
        // gp = new GamePlay();
        gt = new GameTermination();
        b1 = new BotI();
        b2 = new BotII();
        
        games = new ArrayList<>(maxGames);
   }
   
   public void initializeGames(){
   	// most likely a for loop over ArrayList and adding games with Game Play board and players
   }
   
   public void createGame(){
   	// if checkAvailableGames returns true, call pu.boardAvailable(); to start a new game
   }
   
   public void removeGame(){
   	// not sure if needed (?) have to check with GameTermination if they're removing game fully or we do
   }
   
   public boolean checkAvailableGames(){
   	// code implementing later to by checking if elements in ArrayList is null then set return value to true, else false
   	return false;
   }
   
}
