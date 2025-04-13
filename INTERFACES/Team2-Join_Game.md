# Join Game Interface

- each logged in player will appeared in the lobby with status (Red for busy), Green for (Idle)
- Current player can view the lobby and request challenge to other player
- The lobby display each player name, Elo, status and two button (each button has a ID related to the player on the same row to indicate which player is being challenged)
- Current player will have his unique ID as well so we can send both Cur_playerId + challenged_playerId to PageManager so they can forward that to Pair up
- List will be updated in html for each new request from Page Manager (event such as, Game finished, Player Logged in)
- We will follow the Json format from Team 4 with some more modification:
{
“message-type”:‘pair-up’
“userID1” :
{
“ID” : 1001,
“username” : “user1”,
“elo” : 223,
“gamesWon” : 3
“gamesLost” : 0
‘Type’:‘Join Queue’ or ‘Req_userID2’ or ‘Req_bot1’ or ‘view_match_userID2’
},
}
