// Initialize the game state
let currentPlayer = "red"; // Red starts first
let selectedPieceId = null;
const board = document.getElementById("game-board");
const initialRedPieceRows = [0, 1, 2];
const initialBlackPieceRows = [5, 6, 7];
// Update the player turn display
function updatePlayerTurn() {
    const playerDisplay = document.getElementById("players");
    playerDisplay.textContent = currentPlayer === "red" ? "Player 1's Turn (Red)" : "Player 2's Turn (Black)";
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
    document.querySelectorAll("td").forEach(cell => {
        cell.addEventListener("click", () => {
            // For now, just switch the player on any cell click
            switchPlayer();
        });
    });
}
//Game Board Setup
function createBoard() {
    for (let row = 0; row < 8; row++) {
        const tr = document.createElement("tr");

        for (let col = 0; col < 8; col++) {
            const td = document.createElement("td");
            const isPlayable = (row + col) % 2 === 0;

            if (isPlayable) {
                td.classList.add("emptySquare");

                const isRed = initialRedPieceRows.includes(row);
                const isBlack = initialBlackPieceRows.includes(row);

                if (isRed) {
                    td.classList.add("red-piece");
                    td.id = `red-piece-r${row}-c${col}`;
                } else if (isBlack) {
                    td.classList.add("black-piece");
                    td.id = `black-piece-r${row}-c${col}`;
                } else {
                    td.id = `square-r${row}-c${col}`;
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
         (currentPlayer === "black" && clickedElement.classList.contains("black-piece")))) {

        selectedPieceId = clickedId;
    }

    //Moving to an empty square
    else if (selectedPieceId &&
        clickedElement.classList.contains("emptySquare") &&
        !clickedElement.classList.contains("red-piece") &&
        !clickedElement.classList.contains("black-piece")) {

        const fromId = selectedPieceId;
        const toId = clickedId;

        movePiece(fromId, toId);
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
        switchPlayer();
    }

    //Invalid or cancel selection
    else {
        selectedPieceId = null;
    }
}

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

function getRowColFromId(id) {
    const match = id.match(/r(\d+)-c(\d+)/i);
    return {
        row: match ? (parseInt(match[1])+1) : -1,
        col: match ? (parseInt(match[2])+1) : -1
    };
}

//Sending moves to pageManager
function sendGameMove(fromId, toId) {
    const fromMatch = fromId.match(/r(\d+)-c(\d+)/i);
    const toMatch = toId.match(/r(\d+)-c(\d+)/i);

    if (!fromMatch || !toMatch) return;

    const fromPos = `R${fromMatch[1]}-C${fromMatch[2]}`;
    const toPos = `R${toMatch[1]}-C${toMatch[2]}`;
    const pieceType = currentPlayer; // "red" or "black"
    const clientId = currentPlayer === "red" ? 1 : 2;

    const movePayload = {
        clientId,
        fromPosition: fromPos,
        toPosition: toPos,
        pieceType
    };
    
    fetch("/game/move", {/*THIS IS SUBJECT TO CHANGE!!! IM NOT SURE HOW TO DO A WEB SOCKEEEEET*/
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(movePayload)
    });

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
