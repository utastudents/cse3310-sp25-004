package uta.cse3310.GameManager;

import uta.cse3310.GamePlay.GamePlay;
import uta.cse3310.PairUp.Player;

public class Game{
   private Player player1;  // player 1 object
   private Player player2;  // player 2 object
   private int gameID;   // Unique game identifier
   private GamePlay board;
   public boolean isAvailable;
   public int consecutiveAttempts = 0;

	public Game(int gameID, Player player1, Player player2){ // might need to handle challenge Q in here too 
      this.gameID = gameID; // gameID is index number in ArrayList
      this.player1 = player1;
      this.player2 = player2;
      this.isAvailable = true;
      this.board = new GamePlay(gameID);
   }

   // Getting available game boolean value, true via constructor
   public boolean isGameActive(){
      return isAvailable;
   }

   // Setting game available or not
   public void setGameActive(boolean status){
      this.isAvailable = status;
   }


   public GamePlay getBoard(){        //decide how interacts with gameplay
      return board;
   }

   // Setting the second player after the game is created
   public void setPlayer2(Player player) {
      this.player2 = player;
   }

   // Setting the first player after the game is created
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

   public void printBasics() {
      System.out.println("GAME " + gameID);
      System.out.println("Player 1: " + player1.getPlayerId());
      System.out.println("Player 2: " + player2.getPlayerId());
      System.out.println("Turn: " + (board.turn ? "black" : "red"));
   }
}