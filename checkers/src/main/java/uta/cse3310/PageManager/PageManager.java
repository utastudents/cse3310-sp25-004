package uta.cse3310.PageManager;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.HashMap;
import java.util.Enumeration;
import java.util.List;
import uta.cse3310.App;


//import uta.cse3310.GameState;

import uta.cse3310.DB.DB;
import uta.cse3310.GameManager.GameManager;
import uta.cse3310.GameManager.Game;
import uta.cse3310.PageManager.GameMove;
import uta.cse3310.PairUp.PairUp;
import uta.cse3310.PageManager.UserEvent;
import uta.cse3310.PageManager.UserEventReply;
import uta.cse3310.PageManager.HumanPlayer;
import uta.cse3310.PairUp.Player;
import uta.cse3310.GamePlay.GamePlay;
import uta.cse3310.GamePlay.Cord;
import uta.cse3310.GamePlay.Checker;
import uta.cse3310.GamePlay.Board;
import uta.cse3310.GamePlay.Color;
import uta.cse3310.Bot.Bot;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonArray;

public class PageManager {
    GameManager gm;
    DB db;
    PairUp pu;
    Integer turn = 0; // just here for a demo. note it is a global, effectively and
                      // is not unique per client (or game)

    
    // List to track active players in the subsystem
    public Hashtable<Integer, HumanPlayer> activePlayers = new Hashtable<>();
    Gson gson = new Gson();
    GameManager Gm = new GameManager();
    public Hashtable<Integer, Integer> userIDToClientID = new Hashtable<>();

    // Track user in which subsytem they are in.
    public HashMap<Integer, GameState> clientStates = new HashMap<>();

    public PageManager() { 
        gm = new GameManager();
        db = new DB();
        // pass over a pointer to the single database object in this system
        pu = new PairUp(gm);
    }

    //gets top10 playersfirst , 11th is the current player
    public UserEventReply retrieveLeaderboardJson(JsonObject jsonObj, int id) {
        UserEventReply userEventReply = new UserEventReply();

    
        // Outer JSON with response ID
        JsonObject responseJson = new JsonObject();
        responseJson.addProperty("responseID", "summaryData");
    
        // Get the top10 players by elo from db
        HumanPlayer[] topPlayers = db.getTop10PlayersByElo();

        JsonArray top10 = new JsonArray(10);
    
        //int count = 1;
        for (HumanPlayer player : topPlayers) {
            if (player == null) {continue;}
            JsonObject playerData = new JsonObject();
            playerData.addProperty("ID", player.getPlayerId());
            playerData.addProperty("Username", player.getUsername());
            playerData.addProperty("elo", player.getELO());
            playerData.addProperty("gamesWon", player.getWins());
            playerData.addProperty("gamesLost", player.getLosses());

            top10.add(playerData);
    
            //String userKey = "userID" + count;
            //responseJson.add(userKey, playerData);
            //count++;
        }
        responseJson.add("top10", top10);
    
        // Add current player (even if not in top 10)
        HumanPlayer currentPlayer = activePlayers.get(id);
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
        responseJson.addProperty("responseID", "join_game");
        responseJson.addProperty("MyClientID", Id);
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
            playerData.addProperty("username", player.getUsername());
            playerData.addProperty("elo", player.getELO());
            playerData.addProperty("gamesWon", player.getWins());
            playerData.addProperty("gamesLost", player.getLosses());
            playerData.addProperty("status", player.getStatus().toString());


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
  "responseID": "join_game",
  "MyClientID": 123,
  "playersInQueue": 3,
  "activePlayers": [
        {
        "ClientID": 1,
        "username": "player1",
        "elo": 1500,
        "gamesWon": 10,
        "gamesLost": 5
        "status": "ONLINE"
        },
        {
        "ClientID": 2,
        "username": "player2",
        "elo": 1450,
        "gamesWon": 8,
        "gamesLost": 7
        "status": "IN_GAME"
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
        responseJson.addProperty("MyClientID", Id);

        // State whether the adding to queue was a success
        if (pu.addToQueue(activePlayers.get(playerClientId)))
        {
            responseJson.addProperty("inQueue", true);

            if(!(activePlayers.get(playerClientId).getStatus() == Player.STATUS.IN_GAME))
            {
                activePlayers.get(playerClientId).setStatus(Player.STATUS.IN_QUEUE);
            }
        }
        else
        {
            responseJson.addProperty("inQueue", false);
        }

        userEventReply.replyObj = responseJson;

        userEventReply.recipients = new ArrayList<>();
        userEventReply.recipients.add(Id);

        return userEventReply;

        //Successful
        // {
        //     "responseID": "joinQueue",
        //     "MyClientID": 123,
        //     "inQueue": true
        // }

        // Unsuccessful
        // {
        //     "responseID": "joinQueue",
        //     "MyClientID": 123,
        //     "inQueue": false
        // }
        

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
        responseJson.addProperty("username", player.getUsername());
        responseJson.addProperty("elo", player.getELO());
        responseJson.addProperty("gamesWon", player.getWins());
        responseJson.addProperty("gamesLost", player.getLosses());
        responseJson.addProperty("status", player.getStatus().toString());

        userEventReply.replyObj = responseJson;

        userEventReply.recipients = new ArrayList<>();
        userEventReply.recipients.add(opponentClientId);

        return userEventReply;


        // {
        //     "ClientID": 1,
        //     "username": "player1",
        //     "elo": 1500,
        //     "gamesWon": 10,
        //     "gamesLost": 5,
        //     "status": "ONLINE"
        // }
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
                if(!(activePlayers.get(playerClientId).getStatus() == Player.STATUS.IN_GAME))
                {
                    activePlayers.get(playerClientId).setStatus(Player.STATUS.IN_QUEUE);
                    activePlayers.get(opponentClientId).setStatus(Player.STATUS.IN_QUEUE);
                }
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

        //Challenge Accepted and queue success
        // {
        //     "responseID": "challengePlayerReply",
        //     "inQueue": true
        // }

        //Challenge Accepted and queue unsuccessful
        // {
        //     "responseID": "challengePlayerReply",
        //     "inQueue": false
        // }

        //Challenge unsuccessful
        // {
        //     "responseID": "challengePlayerReply",
        //     "accepted": false
        // }
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
            if(!(activePlayers.get(Id).getStatus() == Player.STATUS.IN_GAME))
            {
                activePlayers.get(Id).setStatus(Player.STATUS.IN_QUEUE);
            }
        }
        else
        {
            responseJson.addProperty("inQueue", false);
        }

        userEventReply.replyObj = responseJson;

        userEventReply.recipients = new ArrayList<>();
        userEventReply.recipients.add(Id);

        return userEventReply;

        //Successful
        // {
        //     "responseID": "challengeBot",
        //     "inQueue": true
        // }

        //Unsuccessful
        // {
        //     "responseID": "challengeBot",
        //     "inQueue": false
        // }

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
            if(!(activePlayers.get(Id).getStatus() == Player.STATUS.IN_GAME))
            {
                activePlayers.get(Id).setStatus(Player.STATUS.IN_QUEUE);
            }
        }
        else
        {
            responseJson.addProperty("inQueue", false);
        }

        userEventReply.replyObj = responseJson;

        userEventReply.recipients = new ArrayList<>();
        userEventReply.recipients.add(Id);

        return userEventReply;

        //Successful
        // {
        //     "responseID": "BotvsBot",
        //     "inQueue": true
        // }

        //Unsuccessful
        // {
        //     "responseID": "BotvsBot",
        //     "inQueue": false
        // }
    }

    /*TODO
     * create static methods for spectating (maybe change the view match method to static and its parameters) and starting game.
     * Implement a way when a user leaves the website 
     */

    public void startGameNotifier(Game g, int UserId)
    {
        int clientId = userIDToClientID.get(UserId);

        UserEventReply userEventReply= new UserEventReply();
        JsonObject responseJson = new JsonObject();

        boolean player1IsBot = false;
        boolean player2IsBot = false;

        if (g.getPlayer1() instanceof Bot)
        {
            player1IsBot = true;
        }

        if (g.getPlayer2() instanceof Bot)
        {
            player2IsBot = true;
        }

        // general identification of JSON
        responseJson.addProperty("responseID", "startGame");

        if (player1IsBot && player2IsBot)
        {
            responseJson.addProperty("gameType", "bvb");
        }
        else if (player1IsBot || player2IsBot)
        {
            responseJson.addProperty("gameType", "pvb");
        }
        else
        {
            responseJson.addProperty("gameType", "pvp");
        }

        // Player 1 info
        JsonObject player1 = new JsonObject();
        if (player1IsBot)
        {
            player1.addProperty("isBot", true);
        }
        else
        {
            HumanPlayer humanPlayer1 = (HumanPlayer) g.getPlayer1();
            player1.addProperty("isBot", false);
            player1.addProperty("playerClientId", userIDToClientID.get(humanPlayer1.getPlayerId()));
            player1.addProperty("username", humanPlayer1.getUsername());
            player1.addProperty("elo", humanPlayer1.getELO());
            player1.addProperty("gamesWon", humanPlayer1.getWins());
            player1.addProperty("gamesLost", humanPlayer1.getLosses());
            player1.addProperty("status", humanPlayer1.getStatus().toString());
        }
        responseJson.add("player1", player1);

        // Player 2 info
        JsonObject player2 = new JsonObject();
        if (player2IsBot)
        {
            player2.addProperty("isBot", true);
        }
        else
        {
            HumanPlayer humanPlayer2 = (HumanPlayer) g.getPlayer2();
            player2.addProperty("isBot", false);
            player2.addProperty("playerClientId", userIDToClientID.get(humanPlayer2.getPlayerId()));
            player2.addProperty("username", humanPlayer2.getUsername());
            player2.addProperty("elo", humanPlayer2.getELO());
            player2.addProperty("gamesWon", humanPlayer2.getWins());
            player2.addProperty("gamesLost", humanPlayer2.getLosses());
            player2.addProperty("status", humanPlayer2.getStatus().toString());
        }
        responseJson.add("player2", player2);

        //According to game display, they will have the game setup

        // GamePlay board = g.getBoard();

        // // responseJson.add("board", boardToJson(board));


        userEventReply.recipients = new ArrayList<>();
        userEventReply.recipients.add(clientId);

        //transition
        App.sendMessage(transitionPage(userEventReply.recipients, GameState.GAME_DISPLAY));

        //send info to new onmessage
        App.sendMessage(userEventReply);

    }
    


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
        // 3) create a new json object to send back to the client
        JsonObject status = new JsonObject();

        // 4) check if the username and password are correct by checking the database
        HumanPlayer player = db.getPlayer(username, password);


        // 5) if the player is null, then the username and password are incorrect
        if (player == null) {
            status.addProperty("responseID", "login");
            status.addProperty("msg", "Invalid username or password.");
            reply.replyObj = status;
            return reply;
        }

        // ADDING PLAYER TO ACTIVE PLAYER MAP
        activePlayers.put(Id,player);
        userIDToClientID.put(player.getPlayerId(),Id);
        player.setStatus(HumanPlayer.STATUS.ONLINE);

        // 6) if the player is not null, then the username and password are correct
        status.addProperty("responseID", "loginSuccessful");
        status.addProperty("msg", "Login successful!");
        status.addProperty("playerID", player.getPlayerId());

        //transition to the home page

        reply.replyObj = status;


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
        System.out.println("addPlayer result: " + success);

        if(!success){
             status.addProperty("msg", "failed to create player !");
             reply.replyObj = status;
             return reply;
        }
        // fetching new added user form DB
        HumanPlayer player = db.getPlayer(username,password);
        // check if player was retrieved successfully
            if (player == null) {
                System.err.println("ERROR: Failed to retrieve newly added user from DB for username=" + username);
                status.addProperty("msg", "User creation succeeded, but failed to retrieve user from database.");
                reply.replyObj = status;
                return reply;
            }
        //     // Add to active player list and mark as online
        activePlayers.put(Id, player);
        userIDToClientID.put(player.getPlayerId(), Id);
        player.setStatus(Player.STATUS.ONLINE);

        // Respond to frontend
        status.addProperty("msg", "Account created successfully!");
        status.addProperty("playerID", player.getPlayerId());

        reply.replyObj = status;
        return reply;
    }
    public void makeMove (int UserID){
        int userId = UserID;
        int clientId = userIDToClientID.get(userId);
        JsonObject obj = new JsonObject ();
        obj.addProperty("action", "requestMove");
        UserEventReply reply = new UserEventReply();
        reply.recipients.add(clientId);
        App.sendMessage(reply);
    }

    public UserEventReply GameMove(JsonObject jsonObj, int Id)
    {
        
       
        
        GameMove move = gson.fromJson(jsonObj, GameMove.class);
        move.setClientId(Id);
        //GameUpdate update = Gm.processMove(move);
        UserEventReply reply = new UserEventReply();
// none of this stuff compiles....
//
        //JsonObject json = JsonParser.parseString(gson.toJson(update)).getAsJsonObject();
        //reply.replyObj = json;
        //reply.recipients.add(move.getClientId());
        return reply;
            
        
    }

     
     //removes player who left from queue, active players hashmap, and notifies clients.
     //Called from app.java OnCLose();
     //because users are only put on active list when they log in, no message will be generated for users who did not log in and left
     public UserEventReply userLeave(int Id) {
        HumanPlayer player = activePlayers.get(Id);
        if (player != null) {
            // Remove from all the maps and stuff I know of
            activePlayers.remove(Id);
            pu.removeFromQueue(player);
            userIDToClientID.remove(Id);
            clientStates.remove(Id);
            
            Game g = player.getGame(); //get game object from that player

            if(g!= null){ //if the player is in a game
            g.setGameActive(false); //signal the game must end due to player leaving.
            }
    
            // message
            JsonObject msg = new JsonObject();
            msg.addProperty("responseID", "playerLeft");
            msg.addProperty("username", player.getUsername());
            msg.addProperty("ID", player.getPlayerId());
    
            UserEventReply reply = new UserEventReply();
            reply.replyObj = msg;
            reply.recipients = new ArrayList<>();
    
            for (Integer otherId : activePlayers.keySet()) {
                reply.recipients.add(otherId);
            }
    
            return reply;
        } else {
            return null;
        }
    }

    


    // Method to transition between pages
   // Transition all given clients to a new game state and notify them
    public UserEventReply transitionPage(List<Integer> clientIds, GameState newState) {
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

    //test method for handling case a user suddenly
    public void addDummy(int clientId) {
        // Create a dummy HumanPlayer instance with predefined values
        HumanPlayer dummyPlayer = new HumanPlayer("dummy", "123", clientId, Player.STATUS.ONLINE, 0, 0, 0, 0);
    
        // Add the dummy player to the active player list
        activePlayers.put(clientId, dummyPlayer);
        userIDToClientID.put(dummyPlayer.getPlayerId(), clientId);
    
        // Optionally log or send a message confirming the player was added
        System.out.println("Added dummy player to activePlayers: " + dummyPlayer.getUsername());
    }
    
 
   
}
