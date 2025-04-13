## Description
- A json object will be passed from `PageMgr` to `Summary` via method `TBD`
- It will contain sub objects containing fields: ID, username, elo, gamesWon, and gamesLost
- each object should be index by the user ID
## Schema
```
{
    "userID1" :
    {
      "ID" : 1001,
      "username" : "user1",
      "elo" : 223,
      "gamesWon" : 3,
      "gamesLost" : 0
    },
    "userID2" :
    {
      "ID" : 1002,
      "username" : "user2",
      "elo" : 72,
      "gamesWon" : 0,
      "gamesLost" : 3
    }
}
```