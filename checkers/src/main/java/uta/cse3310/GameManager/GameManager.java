package uta.cse3310.GameManager;

import java.util.ArrayList;
import java.util.List;

import uta.cse3310.Bot.BotI.BotI;
import uta.cse3310.Bot.BotII.BotII;
import uta.cse3310.GamePlay.Checker;
import uta.cse3310.GamePlay.Cord;
import uta.cse3310.GamePlay.GamePlay;
import uta.cse3310.GameTermination.GameTermination;
import uta.cse3310.PageManager.GameMove;
import uta.cse3310.PageManager.GameUpdate;
import uta.cse3310.PageManager.PageManager;
import uta.cse3310.PairUp.PairUp;
import uta.cse3310.PairUp.Player;

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
        int playerId = move.getClientId();

        Cord from = new Cord(move.getFromPosition_X(), move.getFromPosition_Y());
        Cord to = new Cord(move.getToPosition_X(), move.getToPosition_Y());

        System.out.println("Player " + playerId + " is moving from " + from + " to " + to);

        Checker piece = gamePlay.getBoard().checkerBoard[from.getY()][from.getX()];
        if (piece == null) {
            System.out.println("Attempted to move a piece that does not exist!");
            System.out.println(gamePlay.getBoard());
        }
        int result = gamePlay.move(piece, to);
        boolean valid = (result == 2);

        // Sending all important information through movePath
        String movePath = "Playerid " + playerId + ":" + "(" + from.getX() + "," + from.getY() + ")" + " -> " + "("
                + to.getX() + "," + to.getY() + ")";


        // Let both players know that a move has been made & whose turn it is
        Game g = games.get(move.getGameId());
        GamePlay board = g.getBoard();
        if (valid) {
            
            if (g.getPlayer1().getPlayerId() == playerId) {
                //This is player 1. Player 2's move
                g.getPlayer1().updateBoard(board);
                g.getPlayer2().makeMove(board);
            } else {
                //Player 1's move
                g.getPlayer1().makeMove(board);
                g.getPlayer2().updateBoard(board);
            }   
        } else {
            // Send the actual game board back so they can compare
            g.getPlayer2().updateBoard(board);
            g.getPlayer2().updateBoard(board);
            System.out.println("Attempted invalid move. Actual board:");
            board.getBoard().printBoard();
            board.getBoard().printBoard(from, to);
        }

        return new GameUpdate(valid, "In Progress", "", result == 2, (piece != null ? piece.isKing() : false), movePath);
    }

    //Converting the board from Checker[][] to String[][] for display
    public String[][] To2DstringArray(Checker[][] board) {
        String[][] result = new String[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Checker c = board[i][j];
                if (c == null) {
                    result[i][j] = "empty";
                } else {
                    String type = c.isKing() ? "king" : "pawn";
                    String color = c.getColor().toString().toLowerCase();
                    result[i][j] = color + "_" + type;
                }
            }
        }
        return result;
    }
    
    // Handling move logic and sending updated board state to both players involved in the game
    public void sendUpdateBoard(GameMove move, GamePlay gamePlay, PageManager pageManager) {
        int playerId = move.getClientId(); //recording a move
        
        //Getting coordinates for the move
        Cord from = new Cord(move.getFromPosition_X(), move.getFromPosition_Y());
        Cord to = new Cord(move.getToPosition_X(), move.getToPosition_Y());
        
        //Getting the checkers peice being moved
        Checker piece = gamePlay.getBoard().checkerBoard[from.getY()][from.getX()];

        // Attempting to move the piece, it will return 2 if successful
        int result = gamePlay.move(piece, to);
    
        if (result == 2) {
            boolean isCapture = true;
            boolean isKing = piece.isKing();
    
            String movePath = "Playerid " + playerId + ": (" + from.getX() + "," + from.getY() + ") -> (" +
                    to.getX() + "," + to.getY() + ")";
    
            GameUpdate update = new GameUpdate(true, "In Progress", "", isCapture, isKing, movePath);
            update.setboardState(To2DstringArray(gamePlay.getBoard().checkerBoard));
    
            // Sending to both players of the game the player is in
            for (Game game : games) {
                if (game != null && game.isGameActive()) {
                    Player p1 = game.getPlayer1();
                    Player p2 = game.getPlayer2();
                    
                     // Matching player to game
                    if (p1 != null && p1.getPlayerId() == playerId) {
                        pageManager.sendUpdate(p1.getPlayerId(), update);
                        if (p2 != null) {
                            pageManager.sendUpdate(p2.getPlayerId(), update);
                        }
                        break;
                    } else if (p2 != null && p2.getPlayerId() == playerId) {
                        pageManager.sendUpdate(p2.getPlayerId(), update);
                        if (p1 != null) {
                            pageManager.sendUpdate(p1.getPlayerId(), update);
                        }
                        break;
                    }
                }
            }
        }
    }
}   