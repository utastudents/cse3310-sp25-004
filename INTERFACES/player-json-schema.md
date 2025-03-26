## Documentation
- This is the schema for how json player data will be passed between `PageMgr` and other client-side methods
- Each toplevel key (player key) is a player UUID
	- should match the `ID` field to allow lookups later
- Each player-key's value is a json object with the following fields: 
	- **ID** (type?): Users UUID 
	- **username** (string): users display name 
	- **elo** (int?): quanitfied player skill, used to determine leader board 
	- **gamesWon** (int): number of games the player has won 
	- **gamesLost** (int): number of games the player has lost
	- **type** (string?): A string representing the users current state
		- ie: "join_queue" , "play_bot", "offline" etc...
## Schema
```
{
    "userID1" :
    {
      "ID" : 1001,
      "username" : "user1",
      "elo" : 223,
      "gamesWon" : 3,
      "gamesLost" : 0,
	  "type" : "join_queue"
    },
    "userID2" :
    {
      "ID" : 1002,
      "username" : "user2",
      "elo" : 72,
      "gamesWon" : 0,
      "gamesLost" : 3,
	  "type": "play_bot"
    }
}
```
## Interfaces
- A json object will be passed from `PageMgr` to `Summary` via method `retrieveUserJson` and `retrieveTopTenJson`