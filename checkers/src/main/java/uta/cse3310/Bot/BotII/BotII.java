package uta.cse3310.Bot.BotII;

import uta.cse3310.GameState;
import uta.cse3310.Bot.Bot;
import uta.cse3310.GamePlay.Board;
import uta.cse3310.GamePlay.Checker;
import uta.cse3310.GamePlay.Color;
import uta.cse3310.GamePlay.Cord;
import uta.cse3310.GamePlay.GamePlay;
import uta.cse3310.GameManager.Game;

import java.util.ArrayList;

public class BotII {

    private GamePlay gamePlay;
    private Color botColor; // Define whether Bot II is RED or BLACK

    public BotII(GamePlay gamePlay, Color botColor) {
        this.gamePlay = gamePlay;
        this.botColor = botColor;
    }

    // Method to make a defensive move
    public void makeDefensiveMove() {
        Board board = gamePlay.getBoard();  // Get the current board
        ArrayList<Checker> botCheckers = getBotCheckers(board); // Get the bot's pieces
        boolean moveMade = false;

        for (Checker checker : botCheckers) {
            if (!moveMade) {
                // Try to move each piece defensively
                moveMade = attemptDefensiveMove(checker, board);
            }
        }
    }

    // Get all the Bot II's checkers (either RED or BLACK)
    private ArrayList<Checker> getBotCheckers(Board board) {
        ArrayList<Checker> botCheckers = new ArrayList<>();

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Checker checker = board.checkSpace(new Cord(col, row));
                if (checker != null && checker.getColor() == botColor) {
                    botCheckers.add(checker);
                }
            }
        }
        return botCheckers;
    }

    // Attempt to make a defensive move for a given checker
    private boolean attemptDefensiveMove(Checker checker, Board board) {
        ArrayList<Cord> possibleMoves = new ArrayList<>();

        // Generate possible defensive moves based on the piece type (man or king)
        if (checker.isKing()) {
            possibleMoves = getPossibleKingMoves(checker, board);
        } else {
            possibleMoves = getPossibleManMoves(checker, board);
        }

        // Try each move and choose the safest one
        for (Cord dest : possibleMoves) {
            if (isMoveSafe(checker, dest, board)) {
                board.updatePosition(checker, dest);  // Execute the move
                return true;
            }
        }

        return false;
    }

    // Get possible moves for a king piece
    private ArrayList<Cord> getPossibleKingMoves(Checker checker, Board board) {
        ArrayList<Cord> possibleMoves = new ArrayList<>();
        Cord cord = checker.getCord();

        // Check both forward and backward diagonals
        for (int x = -1; x <= 1; x += 2) {
            for (int y = -1; y <= 1; y += 2) {
                Cord newCord = new Cord(cord.getX() + x, cord.getY() + y);
                if (board.moveForwardCheck(checker, newCord) || board.moveBackwardCheck(checker, newCord)) {
                    possibleMoves.add(newCord);
                }
            }
        }

        return possibleMoves;
    }

    // Get possible moves for a man piece
    private ArrayList<Cord> getPossibleManMoves(Checker checker, Board board) {
        ArrayList<Cord> possibleMoves = new ArrayList<>();
        Cord cord = checker.getCord();

        // If the piece is black, it moves forward (up the board)
        if (checker.getColor() == Color.BLACK) {
            Cord newCord = new Cord(cord.getX() + 1, cord.getY() - 1);
            if (board.moveForwardCheck(checker, newCord)) {
                possibleMoves.add(newCord);
            }
            newCord = new Cord(cord.getX() - 1, cord.getY() - 1);
            if (board.moveForwardCheck(checker, newCord)) {
                possibleMoves.add(newCord);
            }
        } 
        // If the piece is red, it moves backward (down the board)
        else if (checker.getColor() == Color.RED) {
            Cord newCord = new Cord(cord.getX() + 1, cord.getY() + 1);
            if (board.moveBackwardCheck(checker, newCord)) {
                possibleMoves.add(newCord);
            }
            newCord = new Cord(cord.getX() - 1, cord.getY() + 1);
            if (board.moveBackwardCheck(checker, newCord)) {
                possibleMoves.add(newCord);
            }
        }

        return possibleMoves;
    }

    // Check if the move is safe (the piece is not exposed to being captured)
    private boolean isMoveSafe(Checker checker, Cord dest, Board board) {
        // Check if the move does not leave the piece vulnerable to the opponent's jumps
        ArrayList<Cord> possibleJumps = new ArrayList<>();

        if (checker.getColor() == Color.BLACK) {
            possibleJumps = board.getPossibleForwardJump(checker);
        } else if (checker.getColor() == Color.RED) {
            possibleJumps = board.getPossibleBackwardJump(checker);
        }

        // If no opponent can jump this position, it's safe
        for (Cord jump : possibleJumps) {
            if (jump.equals(dest)) {
                return false; // The destination is vulnerable
            }
        }

        return true; // The move is safe
    }


    

    /*@Override
    public boolean makeMove(GameState gs) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'makeMove'");
    }*/
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
