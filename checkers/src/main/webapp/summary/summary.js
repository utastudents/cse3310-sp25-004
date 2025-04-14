// Initialize leaderboard array with actual data
let leaderboard = [];

const table = document.getElementById("summary-table");
const thead = table.getElementsByTagName("thead")[0];
const tbody = table.getElementsByTagName("tbody")[0];

// Player class definition
class Player {
    constructor(name, elo, gamesWon, gamesLost, ID) {
        this.name = name;
        this.elo = elo;
        this.gamesWon = gamesWon;
        this.gamesLost = gamesLost;
		this.ID = ID;
    }

    // Calculate total games played
    getTotalGames() {
        return this.gamesWon + this.gamesLost;
    }
}

function addEventListeners() {
    // Event listener for page load
    document.addEventListener("DOMContentLoaded", function () {
        renderLeaderboard(); // Initial render of leaderboard
    });
}

// Temporary test data
// Loads data from JSON file/string
function loadData( jsonData ) {
	for (const player of jsonData)
	{
		//Copy json object into JS class
		leaderboard.push( new Player(player["username"], player["elo"], player["gamesWon"], player["gamesLost"], player["ID"] ) );
	}
	renderLeaderboard();

}

// Function to render the leaderboard
function renderLeaderboard() {

    clearTable(); // Clear existing table data
    sortLeaderboard(); // Sort players by Elo before rendering
    fillTable(); // Fill the table with player data
}

// Function to sort leaderboard by Elo (descending order)
function sortLeaderboard() {
    leaderboard.sort((a, b) => b.elo - a.elo); // Sort in descending order based on Elo rating
}

function clearTable() {
    tbody.innerHTML = ""; // Clear the table body
}

function fillTable() {
    leaderboard.forEach((player, index) => {
        const row = document.createElement("tr");
        row.innerHTML = `
        <td>${index + 1}</td>
        <td>${player.name}</td>
        <td>${player.elo}</td>
        <td>${player.gamesWon}</td>
        <td>${player.gamesLost}</td>
        <td>${player.getTotalGames()}</td>
        `;
        tbody.appendChild(row);
    });
}

loadData(); // Temporary, should be called only when a request is made to render the leaderboard
addEventListeners(); // Call the function to add event listeners, basically our main function


