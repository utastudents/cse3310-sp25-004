package uta.cse3310.PageManager;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.HashMap;
import java.util.Enumeration;
import java.util.List;

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

    Hashtable<Integer, Integer> userIDToClientID = new Hashtable<>();

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

             
             HumanPlayer player = db.getPlayerById(Id);

            //outer json object will have the responseID
            JsonObject responseJson = new JsonObject(); 
             responseJson.addProperty("responseID", "summaryUserJson");

            //inner json object will have the player info
            if(player != null){
             JsonObject playerData = new JsonObject();
             playerData.addProperty("ID", player.getPlayerId());
             playerData.addProperty("Username", player.getUsername());
             playerData.addProperty("elo", player.getELO());
             playerData.addProperty("gamesWon", player.getWins());
             playerData.addProperty("gamesLost", player.getLosses());

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
    
    public UserEventReply retrieveTopTenJson(JsonObject inputJson, int id) {
        UserEventReply userEventReply = new UserEventReply();
    
        // Outer Json will have response ID
        JsonObject responseJson = new JsonObject();
        responseJson.addProperty("responseID", "summaryTopTenData");
    
        // somehow need to get all players from db 
        List<HumanPlayer> allPlayers = new ArrayList<>(); //need to get access to all players
    
        // Sort by ELO descending
        allPlayers.sort((p1, p2) -> Integer.compare(p2.getELO(), p1.getELO()));
    
        // Add top 10 players to responseJson using userID1, userID2 etc
        int count = 1;
        for (HumanPlayer player : allPlayers) {
            if (count > 10) break;
    
            JsonObject playerData = new JsonObject();
            playerData.addProperty("ID", player.getPlayerId());
            playerData.addProperty("Username", player.getUsername());
            playerData.addProperty("elo", player.getELO());
            playerData.addProperty("gamesWon", player.getWins());
            playerData.addProperty("gamesLost", player.getLosses());
    
            String userKey = "userID" + count;
            responseJson.add(userKey, playerData);
    
            count++;
        }
    
        userEventReply.replyObj = responseJson;
        userEventReply.recipients = new ArrayList<>();
        userEventReply.recipients.add(id);
        return userEventReply;
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
            playerData.addProperty("gamesWon", player.getWins());
            playerData.addProperty("gamesLost", player.getLosses());
            playerData.addProperty("state", player.getStatus().toString());


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

        JsonObject responseJson = new JsonObject();
        UserEventReply userEventReply=  new UserEventReply();

        // Extracting what i recieve
        int playerClientId = jsonObj.get("playerClientId").getAsInt();

    //      general identification of JSON
        responseJson.addProperty("responseID", "joinQueue");
        responseJson.addProperty("WhoAmI", Id);

        // State whether the adding to queue was a success
        if (pu.addToQueue(activePlayers.get(playerClientId)))
        {
            responseJson.addProperty("inQueue", true);
        }
        else
        {
            responseJson.addProperty("inQueue", false);
        }

        userEventReply.replyObj = responseJson;

        userEventReply.recipients = new ArrayList<>();
        userEventReply.recipients.add(Id);

        return userEventReply;

     }

    public UserEventReply challengePlayer(JsonObject jsonObj, int Id)
    {
        UserEventReply userEventReply= new UserEventReply();
        JsonObject responseJson = new JsonObject();

        // Extracting what i recieve
        int opponentClientId = jsonObj.get("opponentClientId").getAsInt();

        // general identification of JSON
        responseJson.addProperty("responseID", "challengePlayer");

        // Send the players info to the opponent
        responseJson.addProperty("playerClientId", Id);
        HumanPlayer player = activePlayers.get(Id);
        responseJson.addProperty("Username", player.getUsername());
        responseJson.addProperty("elo", player.getELO());
        responseJson.addProperty("gamesWon", player.getWins());
        responseJson.addProperty("gamesLost", player.getLosses());

        userEventReply.replyObj = responseJson;

        userEventReply.recipients = new ArrayList<>();
        userEventReply.recipients.add(opponentClientId);

        return userEventReply;
    }

    public UserEventReply challengePlayerReply(JsonObject jsonObj, int Id)
    {
        UserEventReply userEventReply= new UserEventReply();
        JsonObject responseJson = new JsonObject();

        // Extracting what i recieve
        int opponentClientId = jsonObj.get("opponentClientId").getAsInt();
        int playerClientId = jsonObj.get("playerClientId").getAsInt();
        boolean accepted = jsonObj.get("accepted").getAsBoolean();

        // general identification of JSON
        responseJson.addProperty("responseID", "challengePlayerReply");

        // if challenge is accepted or not
        if(accepted)
        {

            // State whether the adding to queue was a success
            if (pu.challenge(activePlayers.get(playerClientId), activePlayers.get(opponentClientId)))
            {
                responseJson.addProperty("inQueue", true);
            }
            else
            {
                responseJson.addProperty("inQueue", false);
            }
    
            userEventReply.recipients = new ArrayList<>();
            userEventReply.recipients.add(opponentClientId);
            userEventReply.recipients.add(playerClientId);
        }
        else
        {
            responseJson.addProperty("accepted", false);
            userEventReply.recipients = new ArrayList<>();
            userEventReply.recipients.add(playerClientId);
        }


        userEventReply.replyObj = responseJson;


        return userEventReply;
    }

    public UserEventReply challengeBot(JsonObject jsonObj, int Id)
    {
        UserEventReply userEventReply= new UserEventReply();
        JsonObject responseJson = new JsonObject();
        boolean bot1 = false;


        // Extracting what i recieve
        int BotId = jsonObj.get("BotId").getAsInt();

        if(BotId == 1)
        {
            bot1 = true;
        }


        // general identification of JSON
        responseJson.addProperty("responseID", "challengeBot");

        // State whether the adding to queue was a success
        if (pu.challengeBot(activePlayers.get(Id), bot1))
        {
            responseJson.addProperty("inQueue", true);
        }
        else
        {
            responseJson.addProperty("inQueue", false);
        }

        userEventReply.replyObj = responseJson;

        userEventReply.recipients = new ArrayList<>();
        userEventReply.recipients.add(Id);

        return userEventReply;

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
        
       /*
        public UserEventReply GameMove(JsonObject jsonObj, int Id) {
        GameMove move = gson.fromJson(jsonObj, GameMove.class);
        move.setClientId(Id);
        GameUpdate update = Gm.ProcessMove(move);
        UserEventReply reply = new UserEventReply();

        JsonObject json = JsonParser.parseString(gson.toJson(update)).getAsJsonObject();
        reply.replyObj = json;
        reply.recipients.add(move.getClientId());
        return reply;
            */
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
