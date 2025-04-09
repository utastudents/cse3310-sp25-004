package uta.cse3310.PageManager;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.HashMap;
import java.util.Enumeration;

import uta.cse3310.GameState;

import uta.cse3310.DB.DB;
import uta.cse3310.PairUp.PairUp;
import uta.cse3310.PageManager.UserEvent;
import uta.cse3310.PageManager.UserEventReply;
import uta.cse3310.PageManager.HumanPlayer;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonArray;

public class PageManager {
    DB db;
    PairUp pu;
    Integer turn = 0; // just here for a demo. note it is a global, effectively and
                      // is not unique per client (or game)


    // List to track active players in the subsystem
    Hashtable<Integer, HumanPlayer> activePlayers = new Hashtable<>();

    // 
    HashMap<Integer, GameState> clientStates = new HashMap<>();

    public PageManager() {
        db = new DB();
        // pass over a pointer to the single database object in this system
        pu = new PairUp(db);
    }

    //what retrieveUserJson could look like
    public UserEventReply retrieveUserJson(JsonObject jsonObject, int Id) 
    {
             UserEventReply userEventReply= new UserEventReply();

             //dummy player but eventually will get the desired player from db of players using int Id
             HumanPlayer player = new HumanPlayer("temp", "temp", "temp");

            //outer json object will have the responseID
            JsonObject responseJson = new JsonObject(); 
             responseJson.addProperty("responseID", "summaryUserJson");

            //inner json object will have the player info
            if(player != null){
             JsonObject playerData = new JsonObject();
             playerData.addProperty("ID", player.getPlayerId());
             playerData.addProperty("Username", player.getUsername());
             playerData.addProperty("elo", player.getELO());
             playerData.addProperty("gamesWon", player.getLosses());
             playerData.addProperty("gamesLost", player.getWins());

             responseJson.add("USER", playerData);

            }
            //error if there's no valid user
            else {
                responseJson.addProperty("ERROR", "no player found");
            }

            userEventReply.replyObj = responseJson;

            userEventReply.recipients = new ArrayList<>();
            userEventReply.recipients.add(Id);
            
            return userEventReply;

    }
    
    public UserEventReply retrieveTopTenJson(JsonObject jsonObj) 
    {
           return null;
    }
    
    
    public UserEventReply getActivePlayers(JsonObject jsonObj, int Id)
    {
        UserEventReply userEventReply= new UserEventReply();
        JsonObject responseJson = new JsonObject();

        // general identification of JSON
        responseJson.addProperty("responseID", "getActivePlayers");
        responseJson.addProperty("WhoAmI", Id);
        Enumeration<Integer> e = activePlayers.keys();

        // to have an array of active players
        JsonArray playersArray = new JsonArray();

        // loop through active players and add info onto the players array
        while (e.hasMoreElements())
        {
            JsonObject playerData = new JsonObject();
            int key = e.nextElement();

            HumanPlayer player = activePlayers.get(key);
            playerData.addProperty("ClientID", key);
            playerData.addProperty("Username", player.getUsername());
            playerData.addProperty("elo", player.getELO());
            playerData.addProperty("gamesWon", player.getLosses());
            playerData.addProperty("gamesLost", player.getWins());


            playersArray.add(playerData);
        }


        // add the array to the JSON and add recipients
        responseJson.add("activePlayers", playersArray);

        userEventReply.replyObj = responseJson;

        userEventReply.recipients = new ArrayList<>();
        userEventReply.recipients.add(Id);

        return userEventReply;

        /* JSON STRUCTURE
    {
  "responseID": "getActivePlayers",
  "WhoAmI": 123,
  "activePlayers": [
        {
        "ClientID": 1,
        "Username": "player1",
        "elo": 1500,
        "gamesWon": 10,
        "gamesLost": 5
        },
        {
        "ID": 2,
        "Username": "player2",
        "elo": 1450,
        "gamesWon": 8,
        "gamesLost": 7
        }
        ]
    } */
    }

    public UserEventReply joinQueue(JsonObject jsonObj, int Id)
    {
        UserEventReply userEventReply= new UserEventReply();
        JsonObject responseJson = new JsonObject();

        Int PlayerClientId = jsonObj.get("PlayerClientId").getAsInt();

        // general identification of JSON
        responseJson.addProperty("responseID", "joinQueue");
        responseJson.addProperty("WhoAmI", Id);

        if (pu.addToQueue(activePlayers.get(PlayerClientId)).getPlayerId())
        {
            responseJson.addProperty("matchFound", true);
        }
        else
        {
            responseJson.addProperty("matchFound", false);
        }

        userEventReply.replyObj = responseJson;

        userEventReply.recipients = new ArrayList<>();
        userEventReply.recipients.add(Id);

        return userEventReply;

        // No where near complete

    }

    public UserEventReply challengePlayer(JsonObject jsonObj, int Id)
    {
        return null;
    }

    public UserEventReply challengePlayerReply(JsonObject jsonObj, int Id)
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

    // handle login request from the frintend (expects a jsonObject w/ "username" "Password"
    public UserEventReply handleLogin(JsonObject jsonObj, int Id)
    {
        return null;
    }
    // method handle new user registration from frontend 
    public UserEventReply handleNewUser(JsonObject jsonObj, int Id)
    {
        return null;
    }

    public UserEventReply GameMove(JsonObject jsonObj, int Id)
    {
        return null;
    }    


    // Method to transition between pages
    private UserEventReply transitionPage(int clientId, GameState newState) {
        return null;
    }

    // Method to transition to join game page after user finishes reviewing summary of game
    public UserEventReply backToHome(int clientId) {
        return null;
    }

    // Method to check if transition possible
    public boolean canTransition(GameState from, GameState to) {
        return false;
    }

    // Method to get the current state of user
    public GameState getCurrentState(int clientId) {
        return null;
    }

    // Method to reset client states for the new game
    public void resetClient(int clientId) {
        clientStates.remove(clientId);
    }

    


   
}
