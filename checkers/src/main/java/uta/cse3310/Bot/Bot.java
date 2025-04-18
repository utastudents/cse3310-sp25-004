package uta.cse3310.Bot;

import uta.cse3310.PairUp.Player;


//Bot I and Bot II should both extend this class
public abstract class Bot extends Player {
    protected GamePlay gamePlay;
    protected Color botColor = Color.BLACK; // Default color for the bot

    public BotII(GamePlay gamePlay, Color botColor) {
        this.gamePlay = gamePlay; // Initialize the game play
        this.botColor = botColor; // Set the bot's color
    }

    public abstract void makeMove();
}
