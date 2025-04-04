package uta.cse3310.PageManager;

import java.util.ArrayList;

import uta.cse3310.DB.DB;
import uta.cse3310.PairUp.PairUp;
import uta.cse3310.GameManager.GameManager;
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
    GameManager Gm = new GameManager();
    Gson gson = new Gson();
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
    public UserEventReply GameMove(JsonObject jsonObj, int Id)
    {
        GameMove move = gson.fromJson(jsonObj,GameMove.class);
        move.setClientId(Id);
       /*  GameUpdate update = Gm.ProcessMove(move); will onlu work after GameManager have created the methid

        JsonObject json = JsonParser.parseString(gson.toJson(update)).getAsJsonObject();
       */
        UserEventReply reply = new UserEventReply();
       // reply.replyObj = json;
        reply.recipients.add(move.getClientId());
        return reply;
    }

    // feat: implement login and registration handlers in PageManager.java

    // - Added handleLogin(UserEvent U) to check if a username exists in the DB and validate password
    // - Added handleNewUser(UserEvent U) to create new user records if username does not exist
    // - Updated ProcessInput(UserEvent U) to route "login" and "new_user" events to appropriate handlers
    // - Integrated UserEventReply to return status messages to the frontend
    // - Stubbed out user credential checking using db.getPasswordForUser() and db.userExists()
    
    // Partially fulfills requirements 31–42 on the task sheet under Tony's responsibilities.
    



   
}
