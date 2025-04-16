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
    const table = document.getElementById("summary-table");
    const thead = table.getElementsByTagName("thead")[0];
    const tbody = table.getElementsByTagName("tbody")[0];
	const dataRequest = 
	{
		action : "summaryData"
	};
	//sends 
	sendMessage(dataRequest);

    tbody.innerHTML = ""; // Clear the table body
    leaderboard.sort((a, b) => b.elo - a.elo); // Sort players in descending order based on Elo rating
    fillTable(leaderboard); // Fill the table with player data
}

function fillTable(leaderboard) {
    const tbody = document.getElementById("summary-table").getElementsByTagName("tbody")[0];

    leaderboard.forEach((player, index) => {
        const row = document.createElement("tr");

        // Show medals on the top three ranks
        let rankDisplay;
        switch (index) {
            case 0:
                rankDisplay = "\u{1F947}"; // gold medal
                break;
            case 1:
                rankDisplay = "\u{1F948}"; // silver medal
                break;
            case 2:
                rankDisplay = "\u{1F949}"; // bronze medal
                break;
            default:
                rankDisplay = index + 1;
        }

        row.innerHTML = `
        <td>${rankDisplay}</td>
        <td>${player.name}</td>
        <td>${player.elo}</td>
        <td>${player.gamesWon}</td>
        <td>${player.gamesLost}</td>
        <td>${player.getTotalGames()}</td>
        `;
        tbody.appendChild(row);
    });
}

// Loads data from JSON file/string
function loadData(jsonData) {
    console.log("Loading data! " + JSON.stringify(jsonData));
    leaderboard = []; // Reset
    let leaderboardIndex = 0;
    for (const player of jsonData) {
        //Copy json object into JS class
        //{"ID":7,"Username":"test","elo":0,"gamesWon":0,"gamesLost":0}
        leaderboard[leaderboardIndex] = new Player(
            player["Username"],
            player["elo"],
            player["gamesWon"],
            player["gamesLost"],
            player["ID"]
        );
        leaderboardIndex++;
    }
    fillTable(leaderboard);
}

// NEW: This function sends a request to the server for leaderboard data
function initSummaryWebSocket() {
    if (!socket || socket.readyState !== WebSocket.OPEN) {
        console.warn("WebSocket is not open. Cannot request leaderboard data.");
        return;
    }

    // Remove any previous duplicate listener
    socket.removeEventListener("message", handleSummaryMessage);
    socket.addEventListener("message", handleSummaryMessage);

    // Send a request to get leaderboard
    socket.send(JSON.stringify({
        type: "summaryRequestTopTen"
    }));
}

// NEW: Handle WebSocket messages related to leaderboard
function handleSummaryMessage(event) {
    const data = JSON.parse(event.data);

    if (data.summaryTopTenData) {
        console.log("Received summaryTopTenData!");
        loadData(data.summaryTopTenData); // Load new data into leaderboard
        renderLeaderboard(); // Render updated leaderboard
    }
}

// Initialize leaderboard array with actual data
let leaderboard = [];

// addEventListeners(); // Call the function to add event listeners, basically our main function
