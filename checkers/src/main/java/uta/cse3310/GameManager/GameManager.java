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

    public GameManager() {
        // gp = new GamePlay();
        gt = new GameTermination();

        games = new ArrayList<>(MAX_GAMES);
    }

    // Initialize first 10 games, gameID's are the game's index number in ArrayList
    public void initializeGames() {
        for (int i = 0; i < MAX_GAMES; i++) {
            games.add(new Game(i, null, null)); // null since unassigned players
        }
    }

    // Track numOfGames for available game slots
    public int getNumOfGames() {
        int availableGames = 0;
        for (Game game : games) {
            if (game == null || !game.isGameActive()) {
                availableGames++;
            }
        }
        return availableGames;
    }

    // Create a new game from Pair Up
    public boolean createGame(Player p1, Player p2) {
        for (int i = 0; i < games.size(); i++) {
            Game game = games.get(i);
            if (game == null || !game.isGameActive()) { // Found an open slot
                Game newGame = new Game(i, p1, p2); // Use index as game ID
                p1.startGame(newGame);
                p2.startGame(newGame);
                games.set(i, newGame);
                System.out.println("Game created at index: " + i);
                game.setGameActive(false);
                return true;
            }
        }
        System.out.println("No available game slots.");
        return false;
        // return boardAvailable(p1, p2, spectator); (?)
    }

    // Removes game once GameTermination concludes game is over
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

    public GameUpdate processMove(GameMove move) {
        // call GamePlay Board method to validate move ? and return GameUpdate object
        // with new position and player ID
        boolean valid = true;
        String status = "In Progress";
        String winner = "";
        boolean capture = false;
        boolean promotion = false;

        String movePath = move.getFromPosition() + " -> " + move.getToPosition();

        return new GameUpdate(valid, status, winner, capture, promotion, movePath);

    }

}
