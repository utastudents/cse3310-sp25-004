package uta.cse3310.PageManager;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.HashMap;
import java.util.Enumeration;
import java.util.List;



//import uta.cse3310.GameState;

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

    // Track user in which subsytem they are in.
    HashMap<Integer, GameState> clientStates = new HashMap<>();

    public PageManager() { 
        db = new DB();
        // pass over a pointer to the single database object in this system
        pu = new PairUp(db);
    }

    //gets top10 players are first 10 and 11th is the current player
    public UserEventReply retrieveLeaderboardJson(JsonObject inputJson, int id) {
        UserEventReply userEventReply = new UserEventReply();
    
        // Outer JSON with response ID
        JsonObject responseJson = new JsonObject();
        responseJson.addProperty("responseID", "summaryData");
    
        // Get the top10 players by elo from db
        HumanPlayer[] topPlayers = db.getTop10PlayersByElo();  
    
        int count = 1;
        for (HumanPlayer player : topPlayers) {
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
    
        // Add current player (even if not in top 10)
        HumanPlayer currentPlayer = db.getPlayerById(id);
        if (currentPlayer != null) {
            JsonObject playerData = new JsonObject();
            playerData.addProperty("ID", currentPlayer.getPlayerId());
            playerData.addProperty("Username", currentPlayer.getUsername());
            playerData.addProperty("elo", currentPlayer.getELO());
            playerData.addProperty("gamesWon", currentPlayer.getWins());
            playerData.addProperty("gamesLost", currentPlayer.getLosses());
    
            responseJson.add("currentUser", playerData);
        } else {
            responseJson.addProperty("ERROR", "no player found");
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
        responseJson.addProperty("playersInQueue", pu.getNumPlayersInQueue());
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
        int botId = jsonObj.get("botId").getAsInt();

        if(botId == 1)
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
        UserEventReply userEventReply= new UserEventReply();
        JsonObject responseJson = new JsonObject();

        // Extracting what i recieve
        boolean bot1 = jsonObj.get("bot1").getAsBoolean();
        boolean bot2 = jsonObj.get("bot2").getAsBoolean();

        // general identification of JSON
        responseJson.addProperty("responseID", "BotvsBot");

        // State whether the adding to queue was a success
        if (pu.botVBot(bot1, bot2, activePlayers.get(Id)))
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

    /*TODO
     * create static methods for spectating (maybe change the view match method to static and its parameters) and starting game.
     * Implement a way when a user leaves the website 
     */

    public UserEventReply ViewMatch(JsonObject jsonObj, int Id)
    {
        return null;
    }

    // handle login request from the frintend (expects a jsonObject w/ "username" "Password"
    public UserEventReply handleLogin(JsonObject jsonObj, int Id)
    {
        // 1) draw out the username and password from the json object
        String username = jsonObj.get("UserName").getAsString();
        String password = jsonObj.get("Password").getAsString();

        // 2)create a new userEventReply object to send back to the client
        UserEventReply reply = new UserEventReply();
        reply.recipients = new ArrayList<>();
        reply.recipients.add(Id);

        // 3) check if the username and password are correct by checking the database
        HumanPlayer player = db.getPlayer(username, password);

        // 4) create a new json object to send back to the client
        JsonObject status = new JsonObject();
        status.addProperty("responseID", "login");

        // 5) if the player is null, then the username and password are incorrect
        if (player == null) {
            status.addProperty("msg", "Invalid username or password.");
            //add the status to the reply object and return it
            reply.replyObj = status;
            return reply;
        }
        // 6) if the player is not null, then the username and password are correct
        status.addProperty("responseID", "loginSuccess"); // response for frontend
        status.addProperty("msg", "Login successful!");
        status.addProperty("playerID", player.getPlayerId());
        status.addProperty("redirect", "join_game");  // redirect to the join game page


        //transition to the home page
        reply.replyObj = status;
        transitionPage(List.of(Id), GameState.JOIN_GAME);
        // need to add
        //. public enum GameState {
        //   HOME,  // Add this constant
        // Include other game states as needed

        return reply;
    }
    // method handle new user registration from frontend 
    public UserEventReply handleNewUser(JsonObject jsonObj, int Id) {
        String username = jsonObj.get("UserName").getAsString();
        String password = jsonObj.get("Password").getAsString();
    
        UserEventReply reply = new UserEventReply();
        reply.recipients = new ArrayList<>();
        reply.recipients.add(Id);
    
        JsonObject status = new JsonObject();
        status.addProperty("responseID", "new_user");
    
        // SEND TO SQLITE DATABASE
        boolean success = db.addPlayer(username, password);
    
        if (success) {
            status.addProperty("msg", "Account created successfully!");
            status.addProperty("redirect", "join_game"); //redirect to the login page
        } else {
            status.addProperty("msg", "Username already exists.");
        }
    
        reply.replyObj = status;
        return reply;
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
   // Transition all given clients to a new game state and notify them
    private UserEventReply transitionPage(List<Integer> clientIds, GameState newState) {
        JsonObject response = new JsonObject();
        response.addProperty("action", "updateVisibility");
        response.addProperty("visible", newState.name().toLowerCase());

        UserEventReply reply = new UserEventReply();
        reply.replyObj = response;
        reply.recipients = new ArrayList<>(clientIds);

        for (Integer id : clientIds) {
            clientStates.put(id, newState);
        }

        return reply;
    }

    //Method to transition to join game page after user finishes reviewing summary of game
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

    //called frrom app.java to remove player from activePlayers and puQueue
    public void userLeave(int Id) {
        HumanPlayer player = activePlayers.get(Id);
        if (player != null) {
            activePlayers.remove(Id);
            //boolean removed = pu.removeFromQueue(player);

            // JsonObject msg = new JsonObject();
            // msg.addProperty("action", "playerLeft");
            // msg.addProperty("playerId", Id);
            // msg.addProperty("username", player.getUsername());

            //  // Create reply
            // UserEventReply reply = new UserEventReply();
            // reply.replyObj = msg;
            // reply.recipients = new ArrayList<>();

            //  // notify ALL other clients??????
            // for (Integer otherId : activePlayers.keySet()) {
            // reply.recipients.add(otherId);
            /////////////////////////////////////////
            //}

        } else {
            System.out.println("Player ID " + Id + " not found in activePlayers.");
        }
    }
    
    
    
   
}


