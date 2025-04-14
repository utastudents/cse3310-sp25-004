// Initialize the game state
let currentPlayer = "red"; // Red starts first
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
// Initialize the game
function startGame() {
    updatePlayerTurn();
    addEventListeners();
}
// Start the game when the page loads
document.addEventListener("DOMContentLoaded", startGame);
