// Initialize the game state
let currentPlayer = "red"; // Red starts first
let selectedPieceId = null;
let board = document.getElementById("game-board");
const initialRedPieceRows = [0, 1, 2];
const initialBlackPieceRows = [5, 6, 7];

let redName;
let blackName;

let me = "red"; // default

function startGameInitialize(json) {
    me = json.you;
    console.log("You are " + me);
    redName = json.player1.isBot ? 'Bot' : json.player1.username;
    blackName = json.player2.isBot ? 'Bot' : json.player2.username;
    currentPlayer = "red";
    displayPlayerTurn();
}

function displayPlayerTurn() {
    const playerDisplay = document.getElementById("players");
    if (currentPlayer == me) {
        playerDisplay.textContent = currentPlayer === "red"
            ? `Your Turn (Red)` : `Your Turn (Black)`;
    } else {
        playerDisplay.textContent = currentPlayer === "red"
            ? `${redName}'s Turn (Red)` : `${blackName}'s Turn (Black)`;
    }
}

function yourTurn() {
    //Called by requestMove
    currentPlayer = me;
    displayPlayerTurn();
}

function opponentTurn() {
    currentPlayer = me === "red" ? "black" : "red";
    displayPlayerTurn();
}

function invertBoard(boardState) {
    const newBoardState = [];
    for (let i=7; i>=0; i--) {
        let row = [];
        for (let j=7; j>=0; j--) {
            row.push(boardState[i][j]);
        }
        newBoardState.push(row);
    }
    return newBoardState;
}

// Update the player turn display
function updatePlayerTurn() {
    const playerDisplay = document.getElementById("players");
    playerDisplay.textContent = currentPlayer === "red"
     ? `${redName}'s Turn (Red)` : `${blackName}'s Turn (Black)`;
}
// Switch the current player
function switchPlayer() {
    currentPlayer = currentPlayer === "red" ? "black" : "red";
    updatePlayerTurn();
}
//add log entry to the live box
function addLog(message, type='info') {
    const logBox = document.getElementById("live-box");
    const log = document.createElement("p");

    log.textContent = message;
    log.className= `log-entry ${type}`;
    logBox.appendChild(log);

    //keep recent logs only
    if(logBox.children.length > 8) {
        logBox.removeChild(logBox.children[0]);

    }

    //for debugging
    console.log(`[${type}] ${message}`);
}

//clear example logs, start clean
function clearExampleLogs() {
    document.getElementById("live-box").innerHTML = " ";
}

// handle move results from the backend and add them to the log
function handleMoveResult(moveData)  {
    if(moveData.valid) {
        addLog(`${moveData.player} moved from ${moveData.from} to ${moveData.to}`, "valid");

        if(moveData.capture) {
            addLog(`${moveData.player} captured a piece at ${moveData.capturePosition}`, "capture");
        }
    }
    else {
        addLog(`Invalid move by ${moveData.player}: ${moveData.reason}`, "invalid");
    }

}

// Add event listeners to the board
function addEventListeners() {
    /* This code has been replaced by java-side move handling. Thank you for your service o7
    document.querySelectorAll("td").forEach(cell => {
        cell.addEventListener("click", () => {
            // For now, just switch the player on any cell click
            switchPlayer();
        });
    });
    */
}
//Game Board Setup
function createBoard() {
    console.log("Creating board");
    if (!board) {board = document.getElementById("game-board");} //Script may have loaded in before page
    for (let row = 0; row < 8; row++) {
        const tr = document.createElement("tr");
        tr.style.height = "50px";
        for (let col = 0; col < 8; col++) {
            const td = document.createElement("td");
            const isPlayable = (row + col) % 2 === 1;
            td.style.height = "50px";
            td.id = `square-r${row}-c${col}`;

            if (isPlayable) {
                td.classList.add("emptySquare");

                const isRed = initialRedPieceRows.includes(row);
                const isBlack = initialBlackPieceRows.includes(row);

                if (isRed) {
                    td.classList.add("red-piece");
                } else if (isBlack) {
                    td.classList.add("black-piece");
                }

                td.addEventListener("click", () => handleClick(td.id));
            }

            tr.appendChild(td);
        }
        board.appendChild(tr);
    }
}

//Move Handling 
function handleClick(clickedId) {
    const clickedElement = document.getElementById(clickedId);

    //Selecting a piece
    if (!selectedPieceId &&
        ((currentPlayer === "red" && clickedElement.classList.contains("red-piece")) ||
         (currentPlayer === "black" && clickedElement.classList.contains("black-piece")) ||
         (currentPlayer === "red" && clickedElement.classList.contains("red-king")) ||
         (currentPlayer === "black" && clickedElement.classList.contains("black-king")))) {

        selectedPieceId = clickedId;
    }

    //Moving to an empty square
    else if (selectedPieceId &&
        clickedElement.classList.contains("emptySquare") &&
        !clickedElement.classList.contains("red-piece") &&
        !clickedElement.classList.contains("black-piece") &&
        !clickedElement.classList.contains("red-king") &&
        !clickedElement.classList.contains("black-king")) {

        const fromId = selectedPieceId;
        const toId = clickedId;

        // movePiece(fromId, toId); // Don't actually move the piece :) java-side will take care of that
        selectedPieceId = null;

        const fromCoords = getRowColFromId(fromId);
        const toCoords = getRowColFromId(toId);

        const moveData = {
            valid: true,
            player: currentPlayer === "red" ? "Player 1" : "Player 2",
            from: `R${fromCoords.row}-C${fromCoords.col}`,
            to: `R${toCoords.row}-C${toCoords.col}`,
            capture: false
        };

        handleMoveResult(moveData);
        sendGameMove(fromId, toId);
        //switchPlayer(); // Java will handle this
    }

    //Invalid or cancel selection
    else {
        selectedPieceId = null;
    }
}

/* No longer used
function movePiece(fromId, toId) {
    const fromElement = document.getElementById(fromId);
    const toElement = document.getElementById(toId);
    if (!fromElement || !toElement) {
        addLog("Invalid move: Could not find elements", "error");
        return;
    }

    const isRed = fromElement.classList.contains("red-piece");
    const isBlack = fromElement.classList.contains("black-piece");

    if (isRed) toElement.classList.add("red-piece");
    if (isBlack) toElement.classList.add("black-piece");

    toElement.classList.add("emptySquare");
    fromElement.classList.remove("red-piece", "black-piece");
    toElement.id = fromElement.id;
    fromElement.id = `square-r${fromElement.parentNode.rowIndex}-c${fromElement.cellIndex}`;
}
*/

function getRowColFromId(id) {
    const match = id.match(/r(\d+)-c(\d+)/i);
    const rc = {
        row: match ? (parseInt(match[1])+1) : -1,
        col: match ? (parseInt(match[2])+1) : -1
    };
    return rc;
}

//Sending moves to pageManager
function sendGameMove(fromId, toId) {
    const fromMatch = fromId.match(/r(\d+)-c(\d+)/i);
    const toMatch = toId.match(/r(\d+)-c(\d+)/i);

    if (!fromMatch || !toMatch) return;

    if (me == "red") {
        //Invert
        fromMatch[1] = 7 - fromMatch[1];
        fromMatch[2] = 7 - fromMatch[2];
        toMatch[1] = 7 - toMatch[1];
        toMatch[2] = 7 - toMatch[2];
    }

    const fromPos = `R${fromMatch[1]}-C${fromMatch[2]}`;
    const toPos = `R${toMatch[1]}-C${toMatch[2]}`;
    const pieceType = currentPlayer; // "red" or "black"
    const clientId = currentPlayer === "red" ? 1 : 2;

    const movePayload = {
        action: "GameMove",
        color: currentPlayer,
        fromPosition: fromPos,
        toPosition: toPos,
        //pieceType
    };
    
    sendMessage(movePayload);

    console.log("Sent GameMove:", movePayload);
}
// Initialize the game
function startGame() {
    clearExampleLogs();
    createBoard();
    updatePlayerTurn();
    addEventListeners();
}
// Start the game when the page loads
document.addEventListener("DOMContentLoaded", startGame);



let popupAction = null; // 'quit', 'draw', etc.

// Show the confirmation popup
function showPopup(actionType) {
    popupAction = actionType;

    const modal = document.getElementById("popup-modal");
    const message = document.getElementById("popup-message");
    const controls = document.getElementById("game-controls"); // Optional button container

    if (!modal || !message) return;

    // Set dynamic message
    switch (actionType) {
        case 'quit':
            message.textContent = "Are you sure you want to quit?";
            break;
        case 'draw':
            message.textContent = "Are you sure you want to request a draw?";
            break;
        default:
            message.textContent = "Are you sure?";
    }

    // Show modal
    modal.style.display = "flex";

    // Optional: dim and disable the board
    document.querySelectorAll("td").forEach(cell => {
        cell.style.pointerEvents = "none";
        cell.style.opacity = "0.6";
    });

    // Optional: disable Quit/Draw buttons if wrapped in a div
    if (controls) {
        controls.style.pointerEvents = "none";
        controls.style.opacity = "0.5";
    }
}

// Hide the confirmation popup
function hidePopup() {
    const modal = document.getElementById("popup-modal");
    const controls = document.getElementById("game-controls");

    if (!modal) return;

    // Hide popup
    modal.style.display = "none";
    popupAction = null;

    // Restore board
    document.querySelectorAll("td").forEach(cell => {
        cell.style.pointerEvents = "auto";
        cell.style.opacity = "1";
    });

    // Re-enable buttons
    if (controls) {
        controls.style.pointerEvents = "auto";
        controls.style.opacity = "1";
    }
}

// Run when page is loaded
document.addEventListener("DOMContentLoaded", function () {
    const cancelBtn = document.getElementById("popup-cancel");
    const confirmBtn = document.getElementById("popup-confirm");

    // 🪝 Hook up Quit/Draw buttons to popup
    const quitBtn = document.getElementById("quit");
    const drawBtn = document.getElementById("draw-request");

    if (quitBtn) quitBtn.onclick = () => showPopup("quit");
    if (drawBtn) drawBtn.onclick = () => showPopup("draw");

    if (cancelBtn) cancelBtn.onclick = hidePopup;

    if (confirmBtn) {
        confirmBtn.onclick = function () {
            if (popupAction === 'quit') {
                quitGame();
            } else if (popupAction === 'draw') {
                sendDrawRequest();
            }
            hidePopup();
        };
    }
});


// Example placeholder functions:
function quitGame() {
    alert("Game will now end. (Placeholder for quit logic)");
    // Optionally: redirect, send WS message, etc.
}

function sendDrawRequest() {
    alert("Draw request sent. (Placeholder for draw logic)");
    // Optionally: send WebSocket message, notify opponent, etc.
}


