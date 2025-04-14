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

// Function to render the leaderboard
function renderLeaderboard() {
	// Leaderboard data
	let leaderboard = [];
	const table = document.getElementById("summary-table");
	const thead = table.getElementsByTagName("thead")[0];
	const tbody = table.getElementsByTagName("tbody")[0];

	// GET DATA!

    tbody.innerHTML = ""; // Clear the table body
    leaderboard.sort((a, b) => b.elo - a.elo); // Sort players in descending order based on Elo rating
    fillTable(leaderboard); // Fill the table with player data
}

function fillTable(leaderboard) {
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

function loadData( jsonData ) {
	// Loads data from JSON file/string
	for (const player of jsonData)
	{
		//Copy json object into JS class
		leaderboard.push( new Player(player["username"], player["elo"], player["gamesWon"], player["gamesLost"], player["ID"] ) );
	}
}

addEventListeners(); // Call the function to add event listeners, basically our main function


