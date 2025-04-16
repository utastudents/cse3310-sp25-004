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
        console.trace("No ACTION specified in sendMessage! Msg:");
        console.log(json);
        return;
    }
    console.log("Sending message " + json.action + " to server");
    console.log(JSON.stringify(json));
    connection.send(JSON.stringify(json));
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

    console.log(jsonMsg);

    let responseID = jsonMsg.responseID;

    console.log("Response ID: " + responseID);

    if (jsonMsg.clientId) {
        console.log("Connected with clientId:", jsonMsg.clientId);
        return;
    }
    
    
    if (!responseID) {
        //check namespace
        const nameKey = Object.keys(jsonMsg);
        if(nameKey.length > 0){
            const namespace = nameKey[0];
            const nestedJson = jsonMsg[namespace];

            if(nestedJson && nestedJson.responseID){
                responseID = nestedJson.responseID;

                console.log("Found nested namespace from: ", namespace);
                console.log("ResponseID: ", responseID);

                jsonMsg = nestedJson;
            } else{
                console.error("Received unexpected responseID! Got:", jsonMsg);
                return;
            }
        }
    }

    //convert data to JSON
    //Extracts the response identifier from the response, so we can determine whose code it belongs to
    //See ./INTERFACES/client-server-docs.md for details
    
    switch (responseID )
    {
    // Account Responses - Login Team
        case "login": {
            login(jsonMsg.msg);
            break;
        }
        case "loginSuccessful": {
            loginSuccess();
            sendMessage({action:"getActivePlayers"});
            break;
        }

        case "join_game":{
            console.log("Join Game Received: ", jsonMsg);
            updateJoinGameList(jsonMsg);
            break;
        }

        case "new_user": {
            newUser(jsonMsg.msg);
            break;
        }  
        // Game Responses

        // Summary Responses
        case "summaryData": {
            console.log("Received summaryTopTenData!");
            //ommits the responseID and only sends needed values to loadData
            loadData(jsonMsg.top10);
            break;
        }
        //Signals that a user has abrubtly left
        case "playerLeft": {
            console.log("Player left:", jsonMsg.username);
            break;
        }
        default:{
            console.log("Received unexpected responseID! Got: \n"+responseID);
        }
    }
}

/*document.getElementById("join_game").style.display = "none"; // set Login to visible but the rest hidden
document.getElementById("game_display").style.display = "none";
document.getElementById("new_account").style.display = "none";
document.getElementById("login").style.display = "block"; */

function handleJoinGame(data) { //function to update when join team
    console.log("Join team response received", data);

    document.getElementById("join_game").style.display = "block"; // set join game to visible and the rest to hidden
    document.getElementById("game_display").style.display = "none";
    document.getElementById("new_account").style.display = "none";
    document.getElementById("login").style.display = "none"; 
}

