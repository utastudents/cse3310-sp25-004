package uta.cse3310.GameManager;

import uta.cse3310.GamePlay.GamePlay;
import uta.cse3310.GamePlay.Board;
import uta.cse3310.GameTermination.GameTermination;
import uta.cse3310.Bot.BotI.BotI;
import uta.cse3310.Bot.BotII.BotII;
import uta.cse3310.PairUp.PairUp;
import uta.cse3310.PageManager.PageManager;

public class GameCommunication {

    // Receiving result from gameplay then sending to page manager
    static int receiveResult(int location) {
        // using to store result of piece

        // Checking if valid location
        if (location < 0) {
            return location;
        } else {
            // if not valid location send error
            return -1;
        }
    }

    static void playerInformation(String id, String username, String nextID) {
        // Using to store player information that is recieved from page manager, then
        // sending to gameplay

    }

}
