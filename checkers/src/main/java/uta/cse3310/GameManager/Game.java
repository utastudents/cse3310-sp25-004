package uta.cse3310.GameManager;

import uta.cse3310.GamePlay.GamePlay;
import uta.cse3310.PairUp.Player;

public class Game{
   private Player player1;  // player 1 object
   private Player player2;  // player 2 object
   private int gameID;   // Unique game identifier
   private GamePlay board;
   // private boolean isGameActive;

	public Game(int gameID, Player player1){ // might need to handle challenge Q in here too 
      this.gameID = gameID;
      this.player1 = player1; // i think pair up needs to make a function to that returns player ID so we can call and store it for whatever we need 
   }

   /*public GamePlay getBoard(){        //decide how interacts with gameplay
      return board;
   }*/

   // Setting the second player after the game is created
   public void setPlayer2(Player player) {
      this.player2 = player;
   }

   public void setPlayer1(Player player) {
      this.player1 = player;
   }

   // Getting player 1 object
   public Player getPlayer1() {
      return player1;
   }

   // Getting player 2 object
   public Player getPlayer2() {
      return player2;
   }

   // Getting the unique game ID
   public int getGameID() {
      return gameID;
   }

   // initializing the board gameplay logic
   public void startGame(Game game) {
      board = new GamePlay(gameID);
   }
}

