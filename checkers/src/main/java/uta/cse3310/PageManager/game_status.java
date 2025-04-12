package uta.cse3310.PageManager;

public class game_status {
    // this is just a made up example to demonstrate data flow
    Integer turn;
    Float score;
}
// this. class is used to manage the game state
// it is used to determine what page to display to the user
public enum GameState {
    LOGIN,          // login page
    JOIN_GAME,      // join game / home page
    GAME_DISPLAY,   // actual game display
    SUMMARY,        // summary after game ends
    HOME            // home page (newly added)
}