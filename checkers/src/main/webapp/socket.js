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
    let jsonMsg = null;
    if (typeof json === "string") {
        jsonMsg = JSON.parse(json);
    } else {
        jsonMsg = json;
    }

    if (!jsonMsg.action) {
        console.trace("No ACTION specified in sendMessage!");
        return;
    }
    
    console.log("Sending message " + jsonMsg.action + " to server");

    let toSend = JSON.stringify(jsonMsg);

    console.log(toSend);
    connection.send(toSend);
}


connection.onopen = function (evt) {
    console.log("open");
}

connection.onclose = function (evt) {
    console.log("close");
}

var globalClientID = null;

connection.onmessage = function (msg) {
    let jsonMsg = JSON.parse(msg.data);
    // console.log("Message from server: ", jsonMsg);
    let responseID = jsonMsg.responseID;

    if (jsonMsg.clientId) {
        globalClientID = jsonMsg.clientId;
        console.log("Connected with clientId:", globalClientID);
        swapToPage("login"); //added this to init display for login for every client connection
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
        // Page Manager
        case "updateVisibility": {
            swapToPage(jsonMsg.visible);
            break;
        }

        // Account Responses - Login Team
        case "login": {
            login(jsonMsg.msg);
            break;
        }
        case "loginSuccessful": {
            loginSuccess();
            sendMessage({action:"getActivePlayers"});
            swapToPage("join_game");
            break;
        }

        case "active_players": {
            console.log("Active Players list Received: ", jsonMsg);
            updateJoinGameList(jsonMsg);
            break;
        }
        case "challengePlayer": {
            // console.log("Player challenge request accepted", jsonMsg);
            sendMessage({action:"challengePlayerReply", opponentClientId:jsonMsg.playerClientId, playerClientId:globalClientID, accepted:true});
            break;
        }
        case "challengePlayerReply":{
            // console.log("Player vs player ",jsonMsg);
            break;
        }

        case "new_user": {
            newUser(jsonMsg.msg);
            break;
        }  

        // Join Game Responses
        case "challengeBot": {
            botChallenged(jsonMsg.inQueue);
            break;
        }

        case "statusUpdate": {
            updateStatus(jsonMsg.playerID, jsonMsg.statusChange);
            break;
        }

        // Game Responses
        case "getActivePlayers": {
            updateJoinGameList(jsonMsg);
            break;
        }
        case "startGame": {
            //Start the actual game!
            //{"responseID":"startGame","gameType":"pvb","player1":{"isBot":false,"playerClientId":1,"username":"test","elo":0,"gamesWon":0,"gamesLost":0,"status":"IN_GAME"},"player2":{"isBot":true}}
            console.log("Starting the game!", JSON.stringify(jsonMsg));
            break;
        }
        case "invalidMove": {
            alert("Invalid Move!");
            break;
        }
        case "validMove": {
            console.log("Valid move");
            console.log(jsonMsg.valid);
            break;
        }
        case "UpdateBoard": {
            console.log("Board update! e.g. Bot Move");
            console.log(jsonMsg);
            displayBoard(jsonMsg);
            break;
        }
        case "requestMove": {
            console.log("Your move!");
            console.log(jsonMsg);
        }

        // Summary Responses
        case "summaryData": {
            console.log("Received summary data for leaderboard!");
            //ommits the responseID and only sends needed values to loadData
            loadData(jsonMsg.top10);
            break;
        }
        //Signals that a user has abrubtly left
        case "playerLeft": {
            console.log("Player left:", jsonMsg.username);
            break;
        }
		case "gameWon": {
			//Behaves identically to summaryData, but toggles an HTML banner telling the player that they've won the game
			//Expects the same response data, just a different responseID
			showWinStatus();
			loadData(jsonMsg.top10);
			break;
		}
		case "gameLost": {
			//Behaves identically to summaryData, but toggles an HTML banner telling the player that they've lost the game
			//Expects the same response data, just a different responseID
			showLoseStatus();
			loadData(jsonMsg.top10);
			break;
		}
        default:{
            console.log("Received unexpected responseID! Got: \n"+responseID);
            console.log(jsonMsg);
        }
    }
}

/**
 * This file is for all of the websocket logic
 * All of the websocket logic should be in this file
 * Nothing that is not websocket logic should be in this file
 * - NO document.getElementById
 * - NO game logic, game code, etc.
 */

/*START: This part will be for the challenged button timer, this is so I can come back to it */
function startChallenge(button){
    button.classList.add("challenged");
    let time = 10;
    const origText = button.innerText;
    button.disabled = true;

    //to show count down//
    const count = setInterval(() => {
        button.innerText = 'Waiting... ${timeLeft}s';
        time--;

        if(time < 0){
            clearInterval(count);
            button.classList.remove("challenged");
            button.classList.add("fail");
            button.innerText = "Challenge Expired";
        }
    }, 1000);

    setTimeout(() => {
        clearInterval(count);
        const accepted = Math.random() > 0.5;
        button.classList.remove("challenged");

        if(accepted){
            button.classList.add("success");
            button.innerText = "Challenge Accepted!";
        }else{
            button.classList.add("fail");
            button.innerText = "Challenge Declined";
        }
    }, Math.floor(Math.random() * 10000))
}

/*END of timer code*/ 

function handleJoinGame(data) { //function to update when join team
    console.log("Join team response received", data);
    document.getElementById("join_game").style.display = "block"; // set join game to visible and the rest to hidden
    document.getElementById("game_display").style.display = "none";
    document.getElementById("new_account").style.display = "none";
    document.getElementById("login").style.display = "none"; 
}
