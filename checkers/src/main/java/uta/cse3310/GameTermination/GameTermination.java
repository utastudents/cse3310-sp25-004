package uta.cse3310.GameTermination;

import uta.cse3310.GameManager.Game;
import uta.cse3310.App;
import uta.cse3310.DB.DB;
import uta.cse3310.PageManager.HumanPlayer;
import uta.cse3310.PageManager.PageManager;
import uta.cse3310.PageManager.UserEventReply;
import uta.cse3310.PairUp.Player;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;

public class GameTermination {
        // Tells game play game is over. 
/*
        public void checkGameEnd(GameState gameState) {
                // Logic to determine if the game has ended
                if (gameHasEnded) {
                    GameResult gameResult = new GameResult();
                    gameResult.generateLeaderboard();
                }
            } */
        // Tells game play game is over.

        public static void forceEndGame(Game g, Player p) {
            // Player p lost
            g.getBoard().setWinner(g.getOther(p).getPlayerId());
            PageManager.db.recordMatchResult(g.getOther(p).getPlayerId(), p.getPlayerId());
        }

        public static void forceEndGame(Game g) {
            // Game was a draw
            g.getBoard().setWinner(0);
        }

        public static boolean isGameOver(Game g) {
            if (g.getBoard().getBoard().getAllMoves().size() == 0) {
                return true;
            }
            return false;
        }

        public static void handleGameOver(Game g) {
            // Assumes game is over
            //If it is black's turn (true), red wins. Otherwise, black wins
            int winner = g.getBoard().getTurn() ? g.getPlayer1().getPlayerId() : g.getPlayer2().getPlayerId();

            g.getBoard().setWinner(winner);

            int loser = g.getOther(winner);

            PageManager.db.recordMatchResult(winner, loser);

            // GameManager will call player.endGame
        }

        public static boolean gameResult(Game g) {
            if (isGameOver(g)) {
                handleGameOver(g);
                return true;
            }
            return false;
        }

        // New endGame functionality. Takes into accound player turns.   
        public static Game endGame(Game currentGame) {
                gameState state = new gameState();
                int winnerID = -1; // -1 for draw
                Player player1 = currentGame.getPlayer1();
                Player player2 = currentGame.getPlayer2();

                // Check whose turn it is and if they have no legal moves
                boolean isPlayer1Turn = !currentGame.getBoard().getBoard().turn; // false for red's turn (player 1)
                boolean isPlayer2Turn = currentGame.getBoard().getBoard().turn;  // true for black's turn (player 2)
                
                // If it's player 1's turn and they can't move, player 2 wins
                if (isPlayer1Turn && !state.canPlayerMove(currentGame.getBoard().getBoard(), currentGame.getPlayer1().getPlayerId())) {
                    winnerID = currentGame.getPlayer2().getPlayerId();
                    currentGame.setGameActive(false);
                    return currentGame;
                }
                
                // If it's player 2's turn and they can't move, player 1 wins
                if (isPlayer2Turn && !state.canPlayerMove(currentGame.getBoard().getBoard(), currentGame.getPlayer2().getPlayerId())) {
                    winnerID = currentGame.getPlayer1().getPlayerId();
                    currentGame.setGameActive(false);
                    return currentGame;
                }

                // Check if player 1 has won
                if (state.hasPlayerWon(currentGame.getBoard().getBoard(), currentGame.getPlayer1().getPlayerId())) {
                        winnerID = currentGame.getPlayer1().getPlayerId();
                        currentGame.setGameActive(false);
                        return currentGame;
                }

                // Check if player 2 has won
                if (state.hasPlayerWon(currentGame.getBoard().getBoard(), currentGame.getPlayer2().getPlayerId())) {
                        winnerID = currentGame.getPlayer2().getPlayerId(); 
                        currentGame.setGameActive(false);
                        return currentGame;
                }

                // Check for draw - only if it's neither player's turn and both have no legal moves
                if (!isPlayer1Turn && !isPlayer2Turn && 
                    !state.canPlayerMove(currentGame.getBoard().getBoard(), currentGame.getPlayer1().getPlayerId()) &&
                    !state.canPlayerMove(currentGame.getBoard().getBoard(), currentGame.getPlayer2().getPlayerId())) {
                        winnerID = -1;
                        currentGame.setGameActive(false);
                        return currentGame;
                }

                // Checks if game ended before it was over. 
                if(!(currentGame.isAvailable))
                {
                        winnerID = -2; // Indicates game ended early
                        return currentGame; 
                }

                return null;
        }

        public HumanPlayer[] saveResults(Game game) {
                DB database = new DB();
                gameState state = new gameState();
            
                // Ensure both players are Humans
                if (!(game.getPlayer1() instanceof HumanPlayer) || !(game.getPlayer2() instanceof HumanPlayer)) {
                    System.out.println("BOT DETECTED");
                    return new HumanPlayer[]{null, null};
                }
            
                // Cast players
                HumanPlayer gamePlayer1 = (HumanPlayer) game.getPlayer1();
                HumanPlayer gamePlayer2 = (HumanPlayer) game.getPlayer2();
            
                // Retrieve usernames
                String username1 = gamePlayer1.getUsername();
                String username2 = gamePlayer2.getUsername();
            
                // Retrieve players from DB
                HumanPlayer player1 = database.getPlayerByUsername(username1);
                HumanPlayer player2 = database.getPlayerByUsername(username2);
            
                // Safety check
                if (player1 == null || player2 == null) {
                    System.out.println("Player not detected");
                    return new HumanPlayer[]{null, null};
                }
            
                // Determine winner
                int winnerID = state.checkForWinningPlayer(game.getBoard().getBoard(), game);
            
                // Current stats
                int p1Id = player1.getPlayerId();
                int p2Id = player2.getPlayerId();
            
                int p1Wins = player1.getWins();
                int p1Losses = player1.getLosses();
                int p1Games = player1.getGamesPlayed();
                int p1Elo = player1.getELO();
            
                int p2Wins = player2.getWins();
                int p2Losses = player2.getLosses();
                int p2Games = player2.getGamesPlayed();
                int p2Elo = player2.getELO();
            
                int updatedElo1 = p1Elo;
                int updatedElo2 = p2Elo;
            
                // Handle outcome
                if (winnerID == -1) {
                    // Draw
                    database.updatePlayerStats(p1Id, p1Wins, p1Losses, p1Elo, p1Games + 1);
                    database.updatePlayerStats(p2Id, p2Wins, p2Losses, p2Elo, p2Games + 1);
                } else if (winnerID == p1Id) {
                    // Player 1 wins
                    updatedElo1 = calculateElo(p1Elo, p2Elo, true);
                    updatedElo2 = calculateElo(p2Elo, p1Elo, false);
            
                    database.updatePlayerStats(p1Id, p1Wins + 1, p1Losses, updatedElo1, p1Games + 1);
                    database.updatePlayerStats(p2Id, p2Wins, p2Losses + 1, updatedElo2, p2Games + 1);
                } else if (winnerID == p2Id) {
                    // Player 2 wins
                    updatedElo2 = calculateElo(p2Elo, p1Elo, true);
                    updatedElo1 = calculateElo(p1Elo, p2Elo, false);
            
                    database.updatePlayerStats(p2Id, p2Wins + 1, p2Losses, updatedElo2, p2Games + 1);
                    database.updatePlayerStats(p1Id, p1Wins, p1Losses + 1, updatedElo1, p1Games + 1);
                }
            
                
            
                // Return updated stats
                HumanPlayer[] updatedStats = new HumanPlayer[2];
                updatedStats[0] = database.getPlayerByUsername(username1);
                updatedStats[1] = database.getPlayerByUsername(username2);

                // Send updated leaderboard to both players
                int outcome1;
                int outcome2;
                if (winnerID == -1) {
                    outcome1 = 0;
                    outcome2 = 0;
                } else if (winnerID == p1Id) {
                    outcome1 = 1;
                    outcome2 = -1;
                } else {
                    outcome1 = -1;
                    outcome2 = 1;
                }
                App.sendMessage(App.pmInstance.retrieveLeaderboardJson(new JsonObject(), p1Id, outcome1));
                App.sendMessage(App.pmInstance.retrieveLeaderboardJson(new JsonObject(), p2Id, outcome2));
            
                return updatedStats;
            }
            
        
        //moved here so Elo can be calculated easily
        private int calculateElo(int playerElo, int opponentElo, boolean isWinner) {
                double expectedScore = 1.0 / (1.0 + Math.pow(10, (opponentElo - playerElo) / 400.0));
                return (int)(playerElo + 32 * ((isWinner ? 1 : 0) - expectedScore));

        }

}






