package uta.cse3310.Bot.BotII;

import uta.cse3310.GameState;
import uta.cse3310.Bot.Bot;
import uta.cse3310.GameManager.Game;

public class BotII extends Bot {
    public void makeValidMove() {
        // TODO: Logic to choose and make a legal move
    }
    public void promoteToKing() {
        // TODO: Check if a piece reached the end and promote to king / Allow more than one king on board
    }
    public void defendPieces() {
        // TODO: Prioritize defending own pieces over offense
        // TODO: Try to block opponent from advancing or jumping
    }
    public void capturePiece() {
        // TODO: Capture opponent piece by jumping diagonally (Call method)
    }
    public void checkMultipleJumps() {
        // TODO: If multiple jumps are possible, do them
    }
    public void moveChecker() {
        // TODO: Move a non-king piece diagonally one square
    }
    public void moveKing() {
        // TODO: Move a king piece diagonally one square in any direction
    }
    public boolean isMoveLegal() {
        // TODO: Validate if a move is legal
        return true;
    }
    public void waitForOpponent() {
        // TODO: Wait for opponent to make a move before acting
    }
      public void adjustStrategy() {
        // When the opponent has 3 points more than us, adjustStrategy changes to more offensive
        // TODO: Change strategy based on early, mid, or late game
        // Early: Moving first row pieces?
        // Second: A King comes into play?
        // Late: A select # of pieces left on the board?
    }
    public void findOffensiveMove() {
        // TODO: Decide if it's safe and smart to attack
    }
     public boolean isPieceCaptured(int pieceId) {
        // TODO: Check if a piece has been captured
        return false;
    }
    public void protectBackLine() {
        // TODO: Avoid moving back row pieces unless necessary to stop other player from getting king pieces
    }

    @Override
    public boolean makeMove(GameState gs) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'makeMove'");
    }
    @Override
    public boolean updateBoard(GameState gs) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateBoard'");
    }
    @Override
    public boolean startGame(Game g) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'startGame'");
    }

}
