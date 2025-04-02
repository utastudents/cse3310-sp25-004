Group 7 (Pair up)   
Interface description 

For interface we communicated to four groups:
1.Database
2.Game manager
3.Page manager 
4.join game

Database
It will retrieve elo score for players 
Method :

getPlayerInfo(int playerId) 
Logs of the players we match 


Game manager
Define an abstract class that can be extended by human player and bots .
Method:
Public boolean boardAvailable (String player1Id, String player2Id)
public abstract class Player {
    protected int playerId;
    public abstract int getId();
}
Page Manager :
Class for representing all players (humans and bots )
Methods :

Int player id 
Public class Human player ( int id )

Join game :

We mostly worked with join game groups, and both agree that games will be in an array and send players with their respective game id and agree to put on queue.
Methods:
Public boolean addToQueue (playerId)
Public boolean challenege(playerId1, playerID2)




