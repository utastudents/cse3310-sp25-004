package uta.cse3310.PageManager;

import java.util.ArrayList;

import uta.cse3310.DB.DB;
import uta.cse3310.PairUp.PairUp;
import uta.cse3310.PageManager.UserEvent;
import uta.cse3310.PageManager.UserEventReply;
import uta.cse3310.PageManager.HumanPlayer;


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



   
    public static JsonObject retrieveUserJson(int StudentID) 
    {
             return null; 
    }
    
    public static JsonObject retrieveTopTenJson() 
    {
           return null;
    }
    
    public static ArrayList<HumanPlayer> findTopTen() 
    {
         return null; 
    }
    
    
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

    public UserEventReply handleNewUser(JsonObject jsonObj, int Id) {
        // Extract input fields from frontend
    
        // Create reply object
    
        // Check if user already exists in database
        return reply;
        }
    
    
        
    
        public UserEventReply handleLogin(JsonObject jsonObj, int Id) {
        // Extract login info from JSON
    
        // Create reply object
        // Send the reply only to the requesting client
    
        // Check if user exists in db.getPasswordForUser (username)
    
        // Validate credentials
        return reply;
        }


   
}
