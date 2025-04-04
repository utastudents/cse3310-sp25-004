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
