package uta.cse3310.GameManager;

import uta.cse3310.GamePlay.GamePlay;
import uta.cse3310.GameTermination.GameTermination;
import uta.cse3310.Bot.BotI.BotI;
import uta.cse3310.Bot.BotII.BotII;
import uta.cse3310.PairUp.PairUp;
import uta.cse3310.PairUp.Player;
import uta.cse3310.PageManager.GameUpdate;
import uta.cse3310.PageManager.GameMove;

import java.util.ArrayList;

public class GameManager {
    // GamePlay gp;
    private static final int MAX_GAMES = 10;
    BotI b1;
    BotII b2;
    GameTermination gt;
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
    public void initializeGames() {
        for (int i = 0; i < MAX_GAMES; i++) {
            games.add(new Game(0, -1));
            numOfGames.add(i); // marking these as available
        }
    }

    // Track numOfGames[] ArrayList for available game slots
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
    public boolean createGame(Player p1, Player p2) {
        ArrayList<Integer> availableSlots = getNumOfGames();
        if (!availableSlots.isEmpty()) {
            int index = availableSlots.get(0);
            Game newGame = new Game(index, 1); // Game ID = index, state = 1 (example)
            /*
             * newGame.setPlayer1(p1);
             * newGame.setPlayer2(p2);
             */
            games.set(index, newGame);
            System.out.println("Game created at index: " + index);
            return true;
        }
        System.out.println("No available game slots.");
        return false;
        // return boardAvailable(p1, p2, spectator); (?)
    }

    public void removeGame() {
        Game gameToRemove = gt.endGame();
        for (int i = 0; i < games.size(); i++) {
            Game g = games.get(i);
            if (g != null && g.getGameID() == gameToRemove.getGameID()) {
                games.set(i, null);
            }
        }
        // if GameTermination sends a Game object that should end, check Game ID and
        // remove here
    }

    // public GameUpdate processMove(GameMove move) {
    // call GamePlay Board method to validate move ? and return GameUpdate object
    // with new position and player ID

    // Making new gameUpdate object
    // GameUpdate newStats = new GameUpdate(true," ", " ", false, true, " " );

    // return;

    // }

}
