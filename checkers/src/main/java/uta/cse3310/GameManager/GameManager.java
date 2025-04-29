package uta.cse3310.GameManager;

import java.util.ArrayList;
import java.util.List;

import uta.cse3310.GamePlay.Checker;
import uta.cse3310.GamePlay.Color;
import uta.cse3310.GamePlay.Cord;
import uta.cse3310.GamePlay.GamePlay;
import uta.cse3310.GameTermination.GameTermination;
import uta.cse3310.PageManager.GameMove;
import uta.cse3310.PageManager.GameUpdate;
import uta.cse3310.PageManager.PageManager;
import uta.cse3310.PairUp.Player;
import uta.cse3310.PairUp.Player.STATUS;

public class GameManager {
    static final int MAX_GAMES = 10;
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

                games.set(i, newGame);
                newGame.setGameActive(true);

                p1.setStatus(STATUS.IN_GAME);
                p2.setStatus(STATUS.IN_GAME);

                p1.startGame(newGame);
                p2.startGame(newGame);

                // p1 goes first
                moveWrapper(newGame.getPlayer1(), newGame);

                return true;
            }
        }
        System.out.println("Failed to create a new game!");
        return false;
    }

    private void moveWrapper(Player p, Game g) {
        try {
            p.makeMove(g.getBoard());
        } catch (Exception e) {
            System.out.println(e);
            GameMove fallback = new GameMove(p.getPlayerId(), g.getGameID(), 
                g.getBoard().getBoard().getAllMoves().get(0));
            processMove(fallback, g.getBoard());
        }
    }

    // Removes game once GameTermination concludes game is over
    public void removeGame(Game g) {
        System.out.println("Game " + g.getGameID() + " has ended");

        g.getPlayer1().setStatus(STATUS.ONLINE);
        g.getPlayer2().setStatus(STATUS.ONLINE);

        g.getPlayer1().endGame(g.getBoard());
        g.getPlayer2().endGame(g.getBoard());

        games.set(g.getGameID(), null);

        PageManager.pu.boardAvailable();
    }

    // When a player disconnects
    public void removeGame(Game currentGame, Player p) {
        if (currentGame == null) {return;}
        GameTermination.forceEndGame(currentGame, p);

        currentGame.getPlayer1().setStatus(STATUS.ONLINE);
        currentGame.getPlayer2().setStatus(STATUS.ONLINE);

        currentGame.getPlayer1().endGame(currentGame.getBoard());
        currentGame.getPlayer2().endGame(currentGame.getBoard());

        games.set(currentGame.getGameID(), null);

        PageManager.pu.boardAvailable();
    }

    // When a draw is accepted
    public void drawGame(Game currentGame) {
        if (currentGame == null) {return;}
        GameTermination.forceEndGame(currentGame);

        currentGame.getPlayer1().setStatus(STATUS.ONLINE);
        currentGame.getPlayer2().setStatus(STATUS.ONLINE);

        currentGame.getPlayer1().endGame(currentGame.getBoard());
        currentGame.getPlayer2().endGame(currentGame.getBoard());

        games.set(currentGame.getGameID(), null);

        PageManager.pu.boardAvailable();
    }

    // Retrieves move by PageManager, passes to GamePlay to update, pass update back
    // to caller
    public GameUpdate processMove(GameMove move, GamePlay gamePlay) {
        int playerId = move.getClientId();
        Game g = games.get(move.getGameId());

        if (gamePlay.getTurn() == (g.getPlayer1().getPlayerId() == playerId)) {
            // Red's turn and black is trying to move or vice-versa
            g.printBasics();
            System.out.println("Wrong player (" + playerId + ") tried to move!");
            System.out.println("Expected move from player " + (gamePlay.getTurn() ? g.getPlayer2().getPlayerId() : g.getPlayer1().getPlayerId()));
            return new GameUpdate(false, "In Progress", "", false, false, "Playerid failed to move");
        }

        g.consecutiveAttempts++;

        Cord from = new Cord(move.getFromPosition_X(), move.getFromPosition_Y());
        Cord to = new Cord(move.getToPosition_X(), move.getToPosition_Y());

        System.out.println("Player " + playerId + " is moving from " + from + " to " + to);

        Checker piece = gamePlay.getBoard().checkerBoard[from.getY()][from.getX()];
        int result = 0;
        boolean valid = false;
        if (piece == null) {
            System.out.println("Attempted to move a piece that does not exist!");
            gamePlay.getBoard().printBoard();
        } else if (piece.getColor() == Color.RED && gamePlay.getTurn()) {
            //Black is trying to move red's piece
            g.printBasics();
            System.out.println("Black (" + playerId + ") tried to move red's piece!");
            gamePlay.getBoard().printBoard();
        } else if (piece.getColor() == Color.BLACK && !gamePlay.getTurn()) {
            //Red is trying to move black's piece
            g.printBasics();
            System.out.println("Red (" + playerId + ") tried to move black's piece!");
            gamePlay.getBoard().printBoard();
        } else {
            result = gamePlay.move(piece, to);
            valid = (result == 2);
        }

        // Sending all important information through movePath
        String movePath = "Playerid " + playerId + ":" + "(" + from.getX() + "," + from.getY() + ")" + " -> " + "("
                + to.getX() + "," + to.getY() + ")";


        // Let both players know that a move has been made & whose turn it is
        
        GamePlay board = g.getBoard();
        if (valid) {
            // Swap turns
            board.setTurn(!board.getTurn());
            if (GameTermination.gameResult(g)) {
                //Game is over
                removeGame(g);

                return null;
            }
            //GameTermination.endGame(g);
            g.consecutiveAttempts = 0;
        } else {
            // Send the actual game board back so they can compare
            System.out.println("Attempted invalid move. Actual board:");
            board.getBoard().printBoard();
            board.getBoard().printBoard(from, to);

            if (g.consecutiveAttempts > 10) {
                g.getBoard().getBoard().printAvailableMoves();
                throw new Error("Automatic move failed!");
            } else if (g.consecutiveAttempts > 5) {
                System.out.println("Too many failed attempts! Forcing automatic move.");
                GameMove forced = new GameMove(move, board.getBoard().getAllMoves().get(0));
                return processMove(forced, gamePlay);
            }
        }
        
        // Debug
        g.printBasics();

        if (board.getTurn()) {
            //Black's move
            g.getPlayer1().updateBoard(board);
            moveWrapper(g.getPlayer2(), g);
        } else {
            //Red's move
            moveWrapper(g.getPlayer1(), g);
            g.getPlayer2().updateBoard(board);
        }

        return new GameUpdate(valid, "In Progress", "", result == 2, (piece != null ? piece.isKing() : false), movePath);
    }
    /*
     * public void SendUpdatedBoard(Game game, int currentPlayerId) {
     * Player p1 = game.getPlayer1();
     * Player p2 = game.getPlayer2();
     * 
     * if (p1 != null) p1.updateBoard(null);
     * if (p2 != null) p2.updateBoard(null);
     * }
     */

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