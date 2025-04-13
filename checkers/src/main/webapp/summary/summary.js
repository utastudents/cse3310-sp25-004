// Initialize leaderboard array with actual data
let leaderboard = [];

const table = document.getElementById("summary-table");
const thead = table.getElementsByTagName("thead")[0];
const tbody = table.getElementsByTagName("tbody")[0];

function addEventListeners() {
    // Event listener for page load
    document.addEventListener("DOMContentLoaded", function () {
        renderLeaderboard(); // Initial render of leaderboard
    });
}

// Temporary test data
// Loads data from JSON file/string
function loadData( jsonData ) {
    // leaderboard = [
    //     new Player("Player 1", 1500, 10, 5),
    //     new Player("Player 2", 1800, 15, 3),
    //     new Player("Player 3", 1700, 12, 6),
    //     new Player("Player 4", 1600, 11, 7),
    //     new Player("Player 5", 1400, 8, 9),
    //     new Player("Player 6", 1900, 20, 2),
    //     new Player("Player 7", 1750, 14, 4),
    //     new Player("Player 8", 1300, 6, 10),
    //     new Player("Player 9", 1200, 4, 12),
    //     new Player("Player 10", 1100, 2, 15)
    // ];
	leaderboard = [];
	let leaderboardIndex = 0;
	for (const player of jsonData)
	{
		//Copy json object into JS class
		leaderboard[leaderboardIndex] = new Player(player["username"], player["elo"], player["gamesWon"], player["gamesLost"], player["ID"] );
	}

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

loadData(); // Temporary, should be called only when a request is made to render the leaderboard
addEventListeners(); // Call the function to add event listeners, basically our main function