package uta.cse3310.PageManager;

import uta.cse3310.PairUp.Player;
import uta.cse3310.PairUp.Player.STATUS;

public class HumanPlayer extends Player{

   
    private String username;
    private String password;
    private String salt;
    private int wins;
    private int losses;
    private int ELO;
    private int gamesPlayed;

    // constructors
    public HumanPlayer(String username, String password)
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
    public boolean makeMove(/** BoardState */) //Returns false if the client could not be reached
    {
        return false;
    }

    @Override
    public boolean updateBoard(/** BoardState */) //Returns false if the client could not be reached
    {
        return false;
    }


    //getters
    public String getUsername(){
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getSalt() {
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

    public int getELO(){
        return ELO;
    }

    public int getGamesPlayed(){
        return gamesPlayed;
    }

    public STATUS getStatus(){
        return status;
    }


    // setters
    public void setWins(int wins){
        this.wins = wins;
    }

    public void setLosses(int losses){
        this.losses = losses;
    }

    public void setELO(int ELO){
        this.ELO = ELO;
    }

    public void setGamesPlayed(int gamesPlayed){
        this.gamesPlayed = gamesPlayed;
    }

    public void setStatus(STATUS status){
        this.status = status;
    }

}
