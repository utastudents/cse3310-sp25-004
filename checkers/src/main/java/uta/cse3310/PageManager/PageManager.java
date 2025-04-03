package uta.cse3310.PageManager;

import java.util.ArrayList;

import uta.cse3310.DB.DB;
import uta.cse3310.PairUp.PairUp;
import uta.cse3310.PageManager.UserEvent;
import uta.cse3310.PageManager.UserEventReply;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class PageManager {
    DB db;
    PairUp pu;
    Integer turn = 0; // just here for a demo. note it is a global, effectively and
                      // is not unique per client (or game)

    public PageManager() {
        db = new DB();
        // pass over a pointer to the single database object in this system
        pu = new PairUp(db);
    }


    //EVAN SHUGART 1001817633 
    //Method to be called by summary to get JsonObject of the specified user.
    // public static JsonObject retrieveUserJson(int StudentID) {
    //         return new JsonObject(); 
    //     }
    
    //EVAN SHUGART 1001817633 
    //Method to be called by summary to get JsonObject of the top 10 users.
    // public static JsonObject retrieveTopTenJson() {
    //         return new JsonObject();
    //     }
    //EVAN SHUGART 1001817633 
    //Find top10 users and store them in a arrayList of Player Object Type
    // public static ArrayList<Player> findTopTen() {
    //         return new ArrayList<>(); 
    // 
    
    
    public UserEventReply getActivePlayers(JsonObject jsonObj, int Id)
    {
        return null;
    }

    public UserEventReply joinQueue(JsonObject jsonObj, int Id)
    {
        return null;
    }

    public UserEventReply challengePlayer(JsonObject jsonObj, int Id)
    {
        return null;
    }

    public UserEventReply challengeBot(JsonObject jsonObj, int Id)
    {
        return null;
    }

    public UserEventReply BotvsBot(JsonObject jsonObj, int Id)
    {
        return null;
    }

    public UserEventReply ViewMatch(JsonObject jsonObj, int Id)
    {
        return null;
    }






   
}
