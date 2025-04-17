package uta.cse3310.Bot.BotI; 


import uta.cse3310.GameState; 
import uta.cse3310.Bot.Bot;
import uta.cse3310.Bot.BotII.BotII.Move;
import uta.cse3310.GameManager.Game; 
import uta.cse3310.GamePlay.Board; 
import uta.cse3310.GamePlay.Checker; 
import uta.cse3310.GamePlay.Color;
import uta.cse3310.GamePlay.Cord; 
import uta.cse3310.GamePlay.GamePlay; 
import uta.cse3310.PairUp.Player; 
import java.util.ArrayList; 
import java.util.Random; 
import java.util.List; 

public class BotI extends Bot {                                      
    @Override
    public boolean makeMove(GameState gs){
        return false;
    }


    @Override 
    public boolean updateBoard(GameState gs) {
        return false;
    } 

    @Override 
    public boolean endGame(GameState gs) {return false;} 
    /* 
    public BotI() {} 

    public BotI(Game game, Color color){}


    protected ArrayList<Checker> getAvailableCheckers() {} 

    private ArrayList<Move> getAllJumpMoves(ArrayList<Checker> checkers){} 

    private ArrayList<Move> getAllMoves(ArrayList<Checker> checkers){} 

    private ArrayList<Cord> getPossibleMoves(Checker piece){} 

    private Move selectBestJumpMove(ArrayList<Move> jumpMoves) {return jumpMoves;}
 
    private Move selectBestMove(ArrayList<Move> moves) {}
 
    private boolean wouldBecomeKing(Move move) {}
    */

}
