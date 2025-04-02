package uta.cse3310.GamePlay;
//import uta.cse3310.GameTermination.GameTermination;

//Main Class holding everything
public class GamePlay {
    private int GameID;
    private Board board;
    //private GameTermination termination;

    public GamePlay(int id) {
        this.GameID = id;
        //Initialize class with GameID and create starting board
    }

    public int move(Checker piece) {
        int result = 0;
        //Call Board's move action based on type then forcefully check for jumps
        //Send Game Termination end board
        return result;
    }

    public Board getBoard() {
        //Return Board
        return board;
    }
}
