package uta.cse3310.PageManager;

import uta.cse3310.PairUp;

public class HumanPlayer extends Player{

    private static int Next_Id = 1;    
    private String Username;
    private String Passsword;
    private int Wins;
    private int Losses;
    private int ELO;
    private int Games_Played;

    // constructors
    public HumanPlayer(String Username, String Passsword)
    {
        this.playerId = Next_Id;
        this.Username = Username;
        this.Passsword = Passsword;
        this.Wins = 0;
        this.Losses = 0;
        this.ELO = 0;
        this.Games_Played = 0;
    }
    
    public HumanPlayer(String Username, String Passsword, int playerId, STATUS status, int Wins, int Losses, int ELO, int Games_Played)
    {
        this.Username = Username;
        this.Passsword = Passsword;
        this.playerId = playerId;
        STATUS status = status;
        this.Wins = Wins;
        this.Losses = Losses;
        this.ELO = ELO;
        this.Games_Played = Games_Played;
    }   


    // These methods will be implemented soon

    @Override
    public boolean makeMove(/** BoardState */); //Returns false if the client could not be reached
    {

    }

    @Override
    public boolean updateBoard(/** BoardState */) //Returns false if the client could not be reached
    {

    }
}