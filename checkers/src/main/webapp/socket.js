/**
 * This file is for all of the websocket logic
 * All of the websocket logic should be in this file
 * Nothing that is not websocket logic should be in this file
 * - NO document.getElementById
 * - NO game logic, game code, etc.
 */

var connection = null;

var serverUrl;
serverUrl = "ws://" + window.location.hostname + ":" + (parseInt(location.port) + 100);
connection = new WebSocket(serverUrl);


// Messenger - This is how ALL outgoing messages will be sent to the server. ALL OF THEM. 
function sendMessage(json = {}) {
    if (!json.action) {
        console.trace("No ACTION specified in sendMessage!");
        return;
    }
    console.log("Sending message " + json.action + " to server");
    console.log(JSON.stringify(json));
    connection.send(json);
}


connection.onopen = function (evt) {
    console.log("open");
}

connection.onclose = function (evt) {
    console.log("close");
}


connection.onmessage = function (evt) {
    let msg = evt.data; //extract data from websocket response
    console.log("Message received: " + msg);
    let jsonMsg = JSON.parse(msg); 


    if (jsonMsg.clientId) {
        console.log("Connected with clientId:", jsonMsg.clientId);
        return;
    }
    
    if (!jsonMsg.responseID) {
        console.error("Received unexpected responseID! Got:", jsonMsg);
        return;
    }
    //convert data to JSON
    //Extracts the response identifier from the response, so we can determine whose code it belongs to
    //See ./INTERFACES/client-server-docs.md for details
    let responseID = Object.keys(jsonMsg)[0];
    switch (responseID )
    {
        // Account Responses - Login Team
        case "login": {
            login(jsonMsg.msg);
            break;
        }
        case "loginSuccess": {
            loginSuccess();
            break;
        }
        case "new_user": {
            newUser(jsonMsg.msg);
            break;
        }
        
        
        // Game Responses

        // Summary Responses
        case "summaryTopTenData": {
            console.log("Received summaryTopTenData!");
            //ommits the responseID and only sends needed values to loadData
            loadData(jsonMsg[responseID]); 
            break;
        }
        default:{
            console.log("Received unexpected responseID! Got: \n"+responseID);
        }
    }
}

document.getElementById("join_game").style.display = "none"; // set Login to visible but the rest hidden
document.getElementById("game_display").style.display = "none";
document.getElementById("new_account").style.display = "none";
document.getElementById("login").style.display = "block"; 

// Function to update page display based on server instructions
function updatePageDisplay(displaySettings) {
  // Handle display_new_account
  if (displaySettings.hasOwnProperty('display_new_account')) {
    document.getElementById("new_account").style.display = 
      displaySettings.display_new_account ? "block" : "none";
  }
  
  // Handle display_join_game
  if (displaySettings.hasOwnProperty('display_join_game')) {
    document.getElementById("join_game").style.display = 
      displaySettings.display_join_game ? "block" : "none";
  }
  
  // Handle display_game_display
  if (displaySettings.hasOwnProperty('display_game_display')) {
    document.getElementById("game_display").style.display = 
      displaySettings.display_game_display ? "block" : "none";
  }
  
  // Handle display_login
  if (displaySettings.hasOwnProperty('display_login')) {
    document.getElementById("login").style.display = 
      displaySettings.display_login ? "block" : "none";
  }
  
  // Handle display_summary
  if (displaySettings.hasOwnProperty('display_summary')) {
    document.getElementById("summary-leaderboard").style.display = 
      displaySettings.display_summary ? "block" : "none";
    document.getElementById("summary-button-display").style.display = 
      displaySettings.display_summary ? "none" : "inline-block";
  }
}

// Update existing login functions to handle display state
function loginSuccess() {
  // Update the display state
  updatePageDisplay({
    display_login: false,
    display_new_account: false,
    display_join_game: true
  });
  
  // Alert the user
  alert(jsonMsg.msg || "Login successful!");
}
