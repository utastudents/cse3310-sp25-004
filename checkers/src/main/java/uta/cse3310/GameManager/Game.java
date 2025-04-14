package uta.cse3310.GameManager;

import uta.cse3310.PairUp.PairUp;
import uta.cse3310.PairUp.Player;
import uta.cse3310.GamePlay.GamePlay;

public class Game{
   public int player1;  // ID for player 1
   public int player2;  // ID for player 2
   private int gameID;   // Unique game identifier
   public int playerID;
   // private GamePlay board;     *decide later how to use when gameplay is more fleshed out 
   // private boolean isGameActive;
   // public int gameID


	public Game(int gameID, int playerId){ // might need to handle challenge Q in here too 
      this.gameID = gameID;
      this.player1 = playerId; // i think pair up needs to make a function to that returns player ID so we can call and store it for whatever we need 
   }

   /*public GamePlay getBoard(){        //decide how interacts with gameplay
      return board;
   }*/

   // Setting the second player after the game is created
   public void setPlayer2(int playerId) {
      this.player2 = playerId;
   }

   public void setPlayer1(int playerId) {
      this.player1 = playerId;
   }

   // Getting player 1 ID
   public int getPlayer1() {
      return player1;
   }

   // Getting player 2 ID
   public int getPlayer2() {
      return player2;
   }

   // Getting the unique game ID
   public int getGameID() {
      return gameID;
   }

   // initializing the board gameplay logic
   public void startGame() {
      // Eventually it initializes and starts the board game logic once GamePlay is fully implemented
   }
}
