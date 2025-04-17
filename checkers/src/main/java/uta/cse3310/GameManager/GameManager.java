package uta.cse3310.GameManager;

import uta.cse3310.GamePlay.Checker;
import uta.cse3310.GamePlay.Cord;
import uta.cse3310.GamePlay.GamePlay;
import uta.cse3310.GameTermination.GameTermination;
import uta.cse3310.Bot.BotI.BotI;
import uta.cse3310.Bot.BotII.BotII;
import uta.cse3310.PairUp.PairUp;
import uta.cse3310.PairUp.Player;
import uta.cse3310.PageManager.GameUpdate;
import uta.cse3310.PageManager.GameMove;

import java.util.ArrayList;
import java.util.List;

public class GameManager {
    private static final int MAX_GAMES = 10;
    BotI b1;
    BotII b2;
    PairUp pu;
    GameTermination gt;
    private ArrayList<Game> games = new ArrayList<>(MAX_GAMES);

    public GameManager() {
        gt = new GameTermination();
        games = new ArrayList<>(MAX_GAMES);
        initializeGames();
    }

    // Used for GameTest since games is private
    public List<Game> getGames() {
        return games;
    }

    // Initialize first 10 games, gameID's are the game's index number in ArrayList
    public void initializeGames() {
        for (int i = 0; i < MAX_GAMES; i++) {
            Game game = new Game(i, null, null);
            game.setGameActive(false);
            games.add(game);
        }
    }

    // Track numOfGames for available game slots
    public int getNumOfAvailableGames() {
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
        System.out.println("Creating a new game");
        for (int i = 0; i < games.size(); i++) {
            Game game = games.get(i);
            if (game == null || !game.isGameActive()) {
                Game newGame = new Game(i, p1, p2); // Use index as game ID
                p1.startGame(newGame);
                p2.startGame(newGame);
                games.set(i, newGame);
                newGame.setGameActive(true);
                return true;
            }
        }
        System.out.println("Failed to create a new game!");
        return false;
    }

    // Removes game once GameTermination concludes game is over
    public void removeGame(Game currentGame) {
        Game gameToRemove = gt.endGame(currentGame);
        for (int i = 0; i < games.size(); i++) {
            Game g = games.get(i);
            if (g != null && g.getGameID() == gameToRemove.getGameID()) {
                games.set(i, null);
                pu.boardAvailable();
            }
        }
    }

    // Retrieves move by PageManager, passes to GamePlay to update, pass update back to caller
    public GameUpdate processMove(GameMove move, GamePlay gamePlay) {
        // Getter is misspelled in other class
        int playerId = move.getClietId();

        Cord from = new Cord(move.getFromPosition_X(), move.getFromPosition_Y());
        Cord to = new Cord(move.getToPosition_X(), move.getToPosition_Y());

        Checker piece = gamePlay.getBoard().checkerBoard[from.getY()][from.getX()];
        int result = gamePlay.move(piece, to);
        boolean valid = (result == 2);

        // Sending all important information through movePath
        String movePath = "Playerid " + playerId + ":" + "(" + from.getX() + "," + from.getY() + ")" + " -> " + "("
                + to.getX() + "," + to.getY() + ")";

        return new GameUpdate(valid, "In Progress", "", result == 2, piece.isKing(), movePath);
    }

}
