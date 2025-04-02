package uta.cse3310.GameManager;

// import uta.cse3310.GamePlay.GamePlay;
import uta.cse3310.GameTermination.GameTermination;
import uta.cse3310.Bot.BotI.BotI;
import uta.cse3310.Bot.BotII.BotII;
import uta.cse3310.PairUp.PairUp;
import java.util.ArrayList;


public class GameManager {
    // GamePlay gp;
    private static final int MAX_GAMES = 10;// essentially a global variable we can check later on
    BotI b1;
    BotII b2;
    private ArrayList<Game> games = new ArrayList<>(MAX_GAMES);
    private ArrayList<Integer> numOfGames = new ArrayList<>();


   
    public GameManager() {
        // gp = new GamePlay();
        gt = new GameTermination();
        b1 = new BotI();
        b2 = new BotII();
        
        games = new ArrayList<>(MAX_GAMES);
   }
    // Initialize first 10 games
   public void initializeGames() {	// most likely a for loop over ArrayList and adding games with Game Play board and players
    for (int i = 0; i < MAX_GAMES; i++) {
        games.add(new Game());
        numOfGames.add(i); // marking these as available
    }
}
  //Track numOfGames[] ArrayList for available game slots
public ArrayList<Integer> getNumOfGames() {
    ArrayList<Integer> availableSlots = new ArrayList<>();
    for (int i = 0; i < games.size(); i++) {
        if (games.get(i) == null) {
            availableSlots.add(i);
        }
    }
    return availableSlots;
}

   

    // Create a new game from Pair Up
   public boolean createGame(Player one, Player two, ArrayList<Player> spectator){
    return boardAvailable(one,two,spectator);
   	// if checkAvailableGames returns true, call pu.boardAvailable(); to start a new game
    return false;
   }
   
   public void removeGame(){
   	// not sure if needed (?) have to check with GameTermination if they're removing game fully or we do
   }
   //Check for null in ArrayList Games

   public boolean checkAvailableGames(){
    for (Game game : games) {
        if (game == null) {
            return true;
        }
    }
    
   	// code implementing later to by checking if elements in ArrayList is null then set return value to true, else false
   	return false;
   }
   
}
