
// This is example code provided to CSE3310 Spring 2025
//
// You are free to use as is, or change any of the code provided

// Please comply with the licensing requirements for the
// open source packages being used.

// This code is based upon, and derived from the this repository
//            https:/thub.com/TooTallNate/Java-WebSocket/tree/master/src/main/example

// http server include is a GPL licensed package from
//            http://www.freeutils.net/source/jlhttp/

/*
 * Copyright (c) 2010-2020 Nathan Rajlich
 *
 *  Permission is hereby granted, free of charge, to any person
 *  obtaining a copy of this software and associated documentation
 *  files (the "Software"), to deal in the Software without
 *  restriction, including without limitation the rights to use,
 *  copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the
 *  Software is furnished to do so, subject to the following
 *  conditions:
 *
 *  The above copyright notice and this permission notice shall be
 *  included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 *  EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 *  OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 *  NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 *  HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 *  WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 *  FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 *  OTHER DEALINGS IN THE SOFTWARE.
 */

package uta.cse3310;

import uta.cse3310.PageManager.PageManager;
import uta.cse3310.PageManager.UserEventReply;
import uta.cse3310.PageManager.UserEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Collections;

import org.java_websocket.WebSocket;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import java.time.Instant;
import java.time.Duration;
import java.util.List;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.Hashtable;

public class App extends WebSocketServer {

  static Hashtable<WebSocket, Integer> con2id = new Hashtable<>();
  static Hashtable<Integer, WebSocket> id2con = new Hashtable<>();

  int clientId = 0;
  PageManager PM = new PageManager();
  public static PageManager pmInstance;
  class id {
    int clientId;
  }


  public App(int port) {
    super(new InetSocketAddress(port));
  }

  public App(InetSocketAddress address) {
    super(address);
  }

  public App(int port, Draft_6455 draft) {
    super(new InetSocketAddress(port), Collections.<Draft>singletonList(draft));
  }

  @Override
public void onOpen(WebSocket conn, ClientHandshake handshake) {
    System.out.println("A new connection has been opened");
    clientId = clientId + 1;
    System.out.println("The client id is " + clientId);

    // Save off the ID and connection pointer so they can be easily fetched
    con2id.put(conn, clientId);
    id2con.put(clientId, conn);

    // Add a dummy player for client 1 or 2 (for testing purposes only)
    // if (clientId == 1 || clientId == 2) {
    //     PM.addDummy(clientId);
    //     System.out.println("Added dummy player to activePlayers: dummy" + clientId);
    // }

    // Send the clientId as JSON back to the frontend
    id ID = new id();
    ID.clientId = clientId;
    Gson gson = new Gson();
    String jsonString = gson.toJson(ID);
    System.out.println("Sending " + jsonString);
    conn.send(jsonString);
}




@Override
public void onClose(WebSocket conn, int code, String reason, boolean remote) {
    System.out.println(conn + " has closed");

    Integer id = con2id.remove(conn);
    if (id == null) {
        System.out.println("No associated player found for this connection.");
        return;
    }

    id2con.remove(id);

    UserEventReply reply = PM.userLeave(id);
    if (reply == null) {
        System.out.println("No user event reply for player " + id);
    } else {
        for (Integer recipientId : reply.recipients) {
            WebSocket recipient = id2con.get(recipientId);
            if (recipient != null) {
                recipient.send(reply.replyObj.toString());
                System.out.println("Notified player " + recipientId + " that player " + id + " has left.");
            }
        }
    }

    System.out.println("Removed player " + id);
}


  
 

  

  public static void sendMessage(UserEventReply Reply)
  {
    

    for (Integer id : Reply.recipients) {
      WebSocket destination = id2con.get(id);

      destination.send(Reply.replyObj.toString());
      System.out.println("sending " + Reply.replyObj.toString() + " to " + id);
    }
  }
  
  
  @Override
  public void onMessage(WebSocket conn, String message) {

    // Bring in the data from the webpage
    // At this point, it is known it will be a UserEvent
    // In the future, that will have to be figured out.
    // Suggestion: look in the string for a specific name
    
    // where did this message come from?


    int Id = con2id.get(conn);
    // now, need to call a function to get this processed.
    // the function to be called needs to accept (for this example) a
    // UserEvent, and return a ReplyEvent

    //Log the raw incoming WebSocket message
    System.out.println("Incoming raw message: " + message);

    //Omar: trying new way to parse JSON to allow for clients to have their own JSON structure
    JsonObject jsonObj = JsonParser.parseString(message).getAsJsonObject();
    //checj if actiion exist in the Json
    if (!jsonObj.has("action")) {
    System.out.println("ERROR: 'action' field is missing in JSON: " + jsonObj);
    return;
}
    // String action = jsonObj.get("action").getAsString();
      String action = null;

  if (jsonObj.has("action")) {
      action = jsonObj.get("action").getAsString();
  } else {
      System.err.println("ERROR: 'action' field is missing in JSON: " + message);
      return;
  }

    //Omar: this is the main switch where we call our methods from PM depending on the action (every action is unique across all client-subsystems)
    UserEventReply Reply = null;

    switch(action)
    {
      case "login":
        Reply = PM.handleLogin(jsonObj, Id);
        break;
      case "new_user":
        Reply = PM.handleNewUser(jsonObj, Id);
        break;
      case "getActivePlayers":
        // each method from page manager returns a UserEventReply putting their JsonObject in replyObj and id of client in recipients
        Reply = PM.getActivePlayers(jsonObj, Id);
        break;
      case "joinQueue":        
        Reply = PM.joinQueue(jsonObj, Id);
        break;
      case "challengePlayer":      
        Reply = PM.challengePlayer(jsonObj, Id);
        break;
      case "challengeBot":        
        Reply = PM.challengeBot(jsonObj, Id);
        break;
      case "BotvsBot":        
        Reply = PM.BotvsBot(jsonObj, Id);
        break;        
      case "ViewMatch":        
        Reply = PM.ViewMatch(jsonObj, Id);
        break;  
      case "GameMove":        
        Reply = PM.GameMove(jsonObj, Id);
        break;
      case "backToHome":
        Reply = PM.backToHome(Id);
        break;
      case "summaryData":
        Reply = PM.retrieveLeaderboardJson(jsonObj, Id);
        break;
      default:
        System.out.println("Unknown action: " + action);
        break;
    }
    //


    // Send it to all that need it

    for (Integer id : Reply.recipients) {
      WebSocket destination = id2con.get(id);

      destination.send(Reply.replyObj.toString());
      System.out.println("sending " + Reply.replyObj.toString() + " to " + id);
    }

      // Send page transition and active players only on success
        if ((action.equals("login") || action.equals("new_user")) &&
            Reply.replyObj.has("msg") &&
            Reply.replyObj.get("msg").getAsString().contains("successfully")) {

            UserEventReply transition = PM.transitionPage(List.of(clientId), uta.cse3310.PageManager.GameState.JOIN_GAME);
            for (Integer id : transition.recipients) {
                WebSocket destination = id2con.get(id);
                destination.send(transition.replyObj.toString());
                System.out.println("Transitioning client " + id + " to join_game");
            }

            UserEventReply playerList = PM.getActivePlayers(new JsonObject(), Id);
            for (Integer id : playerList.recipients) {
                WebSocket destination = id2con.get(id);
                destination.send(playerList.replyObj.toString());
                System.out.println("Sending active players to " + id);
            }
        }
    }

  @Override
  public void onMessage(WebSocket conn, ByteBuffer message) {
    System.out.println(conn + ": " + message);
  }

  @Override
  public void onError(WebSocket conn, Exception ex) {
    ex.printStackTrace();
    if (conn != null) {
      // some errors like port binding failed may not be assignable to a specific
      // websocket
    }
  }

  @Override
  public void onStart() {
    setConnectionLostTimeout(0);
  }

  public static void main(String[] args) {

    String HttpPort = System.getenv("HTTP_PORT");
    int port = 9080;
    if (HttpPort != null) {
      port = Integer.valueOf(HttpPort);
    }

    // Set up the http server

    HttpServer H = new HttpServer(port, "./src/main/webapp/");
    H.start();
    System.out.println("http Server started on port: " + port);
    System.out.println("http://localhost:" + port + "/index.html");

    // create and start the websocket server

    port = 9180;
    String WSPort = System.getenv("WEBSOCKET_PORT");
    if (WSPort != null) {
      port = Integer.valueOf(WSPort);
    }

    App A = new App(port);
    A.setReuseAddr(true);
    A.start();
    System.out.println("websocket Server started on port: " + port);

    PageManager pm;
    pm = new PageManager();
    pmInstance = pm;
    System.out.println("Hello World!");
  }
}


