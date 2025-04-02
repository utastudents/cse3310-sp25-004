# Gameplay Interfaces (Team 21)

The Game Play system talks to these systems:
- Game Manager 
- Game Termination
- Game Display (indirectly)
- Database (indirectly)

The Game Play system is a class that has a board of the game and receives 2 positions indicating a checker move
## Game Manager
- The Game Manager creates an instance of the Game Play class and provides an ID on creation for the class to store. Upon creation, the class makes an starting board and stores the ID to pass along to other systems. 
- When the Manager wants to make a move for a piece, it should call a method on the Game Play class that will check the move and return an updated board if the move was valid.
- The Manager can check the status of the game by calling another Game Play method that returns the current game state

## Game Termination
- When the Game Play class is instanced, the class will also create an instance of the Game Termination class, passing along the Game ID to it
- After every valid move is made, an updated game board shall be sent to the Game Termination through one of their methods
- When the Game Play method for the game status is called, the Game Play class shall make a method call to the Termination class to get the status to then pass to the Manager

## Game Display
- The Display shows the pieces for the user to move which the Game Manager sends the move to the Game Play system to check for a valid move
- The Game Play system returns an updated board for the Manager to pass to the Display to show
## Database
- The Manager makes a Game ID for the Game Play class to hold to then pass it to the Game Termination for it to use with Database records
