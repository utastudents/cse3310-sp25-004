package uta.cse3310.PageManager;


import uta.cse3310.GameManager.Game;
import uta.cse3310.GameState;
import uta.cse3310.PairUp.Player;
import uta.cse3310.PairUp.Player.STATUS;
import uta.cse3310.PageManager.PageManager;
import uta.cse3310.App;
import uta.cse3310.PageManager.UserEventReply;

public class HumanPlayer extends Player{

   
    private String username;
    private String password;
    private byte[] salt;
    private int wins;
    private int losses;
    private int gamesPlayed;

    

    //private socketId potentially here

    // constructors
    public HumanPlayer(String username, String password, byte[] salt)
    {
        this.playerId = nextId();
        this.username = username;
        this.password = password;
        this.salt = salt;
        this.wins = 0;
        this.losses = 0;
        this.ELO = 0;
        this.gamesPlayed = 0;
    }
    
    // recurring user
    public HumanPlayer(String username, String password, int playerId, STATUS status, int wins, int losses, int ELO, int gamesPlayed)
    {
        this.username = username;
        this.password = password;
        this.playerId = playerId;
        this.status = status;
        this.wins = wins;
        this.losses = losses;
        this.ELO = ELO;
        this.gamesPlayed = gamesPlayed;
    }   


    // These methods will be implemented soon

    @Override
    public boolean makeMove(GameState gs) //Returns false if the client could not be reached
    {
       // App.pmInstance.makeMove(playerId);
        
        return true;
    }

    @Override
    public boolean updateBoard(GameState gs) //Returns false if the client could not be reached
    {
        return false;
    }

    @Override
    public boolean startGame(Game g) {
        this.game = g;
        this.status = STATUS.IN_GAME;

        // Need game board to be sent over

        App.pmInstance.startGameNotifier(g, playerId);
        //call a static method in page manager to make UserEventReply then send it to App.java to send the info to the players
    

        return true; //TODO: let the client know the game has started
    }

    /*
     * we could have a method here that is used to add a spectator to the game and call a static method in 
     * page manager where it would create the UserEventReply which is then sent to another static method in App.java. 
     * Here the static method will send this to all spectators. Consider creating a new STATE : SPECTATOR
     */

    //getters
    public String getUsername(){
        return username;
    }

    public String getPassword() {
        return password;
    }

    public byte[] getSalt() {
        return salt;
    }

    public int getPlayerId(){
        return playerId;
    }

    public int getWins(){
        return wins;
    }

    public int getLosses(){
        return losses;
    }

    public int getGamesPlayed(){
        return gamesPlayed;
    }

    public STATUS getStatus(){
        return status;
    }

    public Game getGame(){
        return game;
    }


    // setters
    public void setWins(int wins){
        this.wins = wins;
    }

    public void setLosses(int losses){
        this.losses = losses;
    }

    public void setGamesPlayed(int gamesPlayed){
        this.gamesPlayed = gamesPlayed;
    }

    public void setStatus(STATUS status){
        this.status = status;
    }

}
