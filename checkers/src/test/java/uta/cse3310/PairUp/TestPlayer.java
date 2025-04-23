package uta.cse3310.PairUp;

import uta.cse3310.GameState;
import uta.cse3310.GameManager.Game;
import uta.cse3310.PageManager.HumanPlayer;

public class TestPlayer extends HumanPlayer {
    public TestPlayer(int id) {
        super("Test " + id, "password", id, STATUS.ONLINE, 0, 0, 1000, 0);
    }
    public TestPlayer(int id, int elo) {
        super("Test " + id, "password", id, STATUS.ONLINE, 0, 0, elo, 0);
    }

    @Override
    public boolean startGame(Game g) {
        return true;
    }

    @Override
    public boolean makeMove(GameState gs) {
        return true;
    }

    @Override
    public boolean updateBoard(GameState gs) {
        return true;
    }

    @Override
    public boolean endGame(GameState gs) {
        return true;
    }
}