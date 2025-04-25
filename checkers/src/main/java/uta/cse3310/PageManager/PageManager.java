package uta.cse3310.PageManager;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import uta.cse3310.App;
import uta.cse3310.Bot.Bot;
import uta.cse3310.DB.DB;
import uta.cse3310.GameManager.Game;
import uta.cse3310.GameManager.GameManager;
import uta.cse3310.PairUp.PairUp;
import uta.cse3310.PairUp.Player;
import uta.cse3310.PairUp.Player.STATUS;
import uta.cse3310.GamePlay.*;
public class PageManager {
    DB db;
    PairUp pu;
    Integer turn = 0; // just here for a demo. note it is a global, effectively and
                      // is not unique per client (or game)

    
    // List to track active players in the subsystem
    public Hashtable<Integer, HumanPlayer> activePlayers = new Hashtable<>();
    Gson gson = new Gson();
    public static GameManager Gm;
    public Hashtable<Integer, Integer> userIDToClientID = new Hashtable<>();
    public Hashtable<Integer,GamePlay> getGamePlay = new Hashtable<>();

    // Track user in which subsytem they are in.
    public HashMap<Integer, GameState> clientStates = new HashMap<>();

    public PageManager() { 
        PageManager.Gm = new GameManager(); // Instance variable for Bots to reference

        db = new DB();
        // pass over a pointer to the single database object in this system
        pu = new PairUp(Gm);
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
    
    
    public JsonObject activePlayerList() {
        JsonObject responseJson = new JsonObject();

        // general identification of JSON
        responseJson.addProperty("responseID", "active_players");
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

        return responseJson;
    }

    public UserEventReply sendActivePlayersToAll() {
        UserEventReply userEventReply= new UserEventReply();
        userEventReply.recipients = new ArrayList<>();

        Enumeration<Integer> e = activePlayers.keys();
        while (e.hasMoreElements())
        {
            int key = e.nextElement();
            userEventReply.recipients.add(key);
        }

        userEventReply.replyObj = activePlayerList();

        return userEventReply;
    }
    
    
    public UserEventReply getActivePlayers(JsonObject jsonObj, int Id)
    {
        UserEventReply userEventReply= new UserEventReply();
        userEventReply.recipients = new ArrayList<>();

        userEventReply.recipients.add(Id);

        userEventReply.replyObj = activePlayerList();

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

    // used whenever a person updates their status (send to all online players)
    public UserEventReply statusUpdate(Player.STATUS status, int Id)
    {
        JsonObject responseJson = new JsonObject();
        UserEventReply userEventReply=  new UserEventReply();

        responseJson.addProperty("responseID", "statusUpdate");
        responseJson.addProperty("playerID", Id);
        responseJson.addProperty("statusChange", status.toString());

        userEventReply.replyObj = responseJson;

        userEventReply.recipients = new ArrayList<>();

        //send to every player that is online
        Enumeration<Integer> e = activePlayers.keys();
        while (e.hasMoreElements())
        {   
            int key = e.nextElement();
            userEventReply.recipients.add(key);
        }

        return userEventReply;
        
        // {
        //     "responseID": "playerID",
        //     "MyClientID": 123,
        //     "statusChange": "IN_QUEUE"
        // }
    } 
    
    // just a wrapper for ease of use
    private void changePlayerStatus(Player.STATUS status, int Id)
    {
        activePlayers.get(Id).setStatus(status);
        App.sendMessage(statusUpdate(status, Id));  
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
                changePlayerStatus(Player.STATUS.IN_QUEUE, playerClientId);
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
        int opponentClientId = jsonObj.get("opponentClientID").getAsInt();

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
                    changePlayerStatus(Player.STATUS.IN_QUEUE, playerClientId);
                    changePlayerStatus(Player.STATUS.IN_QUEUE, opponentClientId);
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
                changePlayerStatus(Player.STATUS.IN_QUEUE, Id);
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
                changePlayerStatus(Player.STATUS.IN_QUEUE, Id);
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
        System.out.println("Getting client id from pid " + UserId);
        int clientId = userIDToClientID.get(UserId);

        UserEventReply userEventReply= new UserEventReply();
        JsonObject responseJson = new JsonObject();

        userEventReply.replyObj = responseJson;

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

        // change status of the player that called this method (the other player will also start the game and change their status)
        changePlayerStatus(Player.STATUS.IN_GAME, clientId);

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

        System.out.println("Mapped " + player.getPlayerId() + " to client id " + Id);

        // Now that the player is an active player, let the other clients know
        App.sendMessage(sendActivePlayersToAll());

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
            if (db.getPlayerByUsername(username) != null) {
                status.addProperty("msg", "Username already exists");
                reply.replyObj = status;
                return reply;
            }
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

        // Now that the player is an active player, let the other clients know
        App.sendMessage(sendActivePlayersToAll());

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
    public void sendUpdate(int UserId, GameUpdate update){
        int userId = UserId;
        GameUpdate updates = update;
        String board[][] = To2DstringArray(updates.getUpdatedBoard());
        updates.setboardState(board);
        int clientId = userIDToClientID.get(userId);
        JsonObject json = JsonParser.parseString(gson.toJson(updates)).getAsJsonObject();
        json.addProperty("responseID", "GameUpdate");
        UserEventReply reply = new UserEventReply();
        reply.replyObj = json;
        reply.recipients.add(clientId);
        App.sendMessage(reply);
    
    }
    public void sendBoard(int UserId, GamePlay gs){
        int userId = UserId;
        int clientId = userIDToClientID.get(userId);
        JsonObject json = new JsonObject();
        String board[][] = To2DstringArray(gs.getBoard().checkerBoard);
        json.add("boardState", gson.toJsonTree(board));
        json.addProperty("responseID", "UpdateBoard");
        UserEventReply reply = new UserEventReply();
        reply.replyObj = json;
        reply.recipients.add(clientId);
        App.sendMessage(reply);
    
    }
    public String[][] To2DstringArray (Checker[][] board){
     
        String[][] Sboard = new String[8][8];
        for(int i = 0; i < 8; i++){
            for(int j = 0; i < 8; j++){
                Checker c = board[i][j];
                if(c == null){
                    Sboard[i][j]="empty";
                }
                else{
                    String type = c.isKing()?"king" : "pawn";
                    String color = c.getColor().toString().toLowerCase();
                    Sboard[i][j] = color + "_" + type;
                }

            }

        }

        return Sboard;
    }
    public UserEventReply GameMove(JsonObject jsonObj, int Id)
    {
        // {"action":"GameMove","color":"red","fromPosition":"R2-C6","toPosition":"R3-C5"}
        UserEventReply reply = new UserEventReply();
        JsonObject json = new JsonObject();
        reply.replyObj = json;
        reply.recipients = new ArrayList<>();
        reply.recipients.add(Id);

        HumanPlayer hp = activePlayers.get(Id);
        if (hp == null) {
            //Player is not signed in!
            json.addProperty("responseID", "invalidMove");
            return reply;
        }

        // Get game from the player object
        Game g = hp.getGame();

        if (g == null) {
            //Player is not in a game!
            json.addProperty("responseID", "invalidMove");
            return reply;
        }

        GamePlay gamePlay = g.getBoard();
        Cord from = PageManager.codeToCord(jsonObj.get("fromPosition").getAsString());
        Cord to = PageManager.codeToCord(jsonObj.get("toPosition").getAsString());
        GameMove gameMove = new GameMove(Id, g.getGameID(), from.getX(), from.getY(), to.getX(), to.getY(), jsonObj.get("color").getAsString());
       
        gameMove.setClientId(Id);
        GameUpdate update = Gm.processMove(gameMove, gamePlay);

        json.addProperty("valid", update.isValidMove());
        json.addProperty("responseID", "validMove");

        return reply;
    }

    private static Cord codeToCord(String code) {
        //"R2-C6" as in Row 2 Col 6
        
        int y = Integer.parseInt(code.substring(1,2)); // Row is Y
        int x = Integer.parseInt(code.substring(4,5)); // Col is X
        Cord c = new Cord(x, y);
        return c;
    }

     public void EndGameNotifier(int UserId, GamePlay gs){
        
        UserEventReply reply = new UserEventReply();
        JsonObject json = new JsonObject();
        int clientId = userIDToClientID.get(UserId);
        reply.recipients.add(clientId);

        //turning the board to 2d string array
        String board[][] = To2DstringArray(gs.getBoard().checkerBoard);

        //getting the 2d string board as a jsonobj
        json.add("boardState", gson.toJsonTree(board));
        json.addProperty("responseID", "EndGame");
        
        changePlayerStatus(STATUS.ONLINE, clientId);
       
        reply.replyObj = json;
        App.sendMessage(reply);
        App.sendMessage(transitionPage(reply.recipients, GameState.SUMMARY));
        
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
            msg.addProperty("ID", userIDToClientID.get(Id));
    
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
        response.addProperty("responseID", "updateVisibility");
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
        return transitionPage(List.of(clientId), GameState.JOIN_GAME);
    }

    // Method to check if transition possible
    public boolean canTransition(GameState from, GameState to) {
        switch(from) {
            case LOGIN: return to == GameState.JOIN_GAME;
            case JOIN_GAME: return to == GameState.GAME_DISPLAY;
            case GAME_DISPLAY: return to == GameState.SUMMARY;
            case SUMMARY: return to == GameState.JOIN_GAME;
            default: return false;
        }
    }

    // Method to get the current state of user
    public GameState getCurrentState(int clientId) {
        return clientStates.getOrDefault(clientId, GameState.LOGIN);
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
