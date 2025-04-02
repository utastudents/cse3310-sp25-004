GAME MANAGER (TEAM 8)

The Game Manager will communicate with these subsystems:
	- Page Manager
	- Pair Up
	- Bot I and Bot II
	- Game Play
	

PAGE MANAGER/ GAME PLAY
    - Page Manager will send an object consisting of the Player's ID, Checker Position, and next move
    - We invoke the Game Play method and pass the object from Page Manager
    - Once result is recieved from Game Play method, we pass the object back to Page Manager so Game Display can update the display board


PAIR UP
    - After recieving Bot I and Bot II's class, we will send that to Pair Up, in order for Pair Up to create PvP game
    - Pair Up will most likely just import the class into their system

BOT I AND BOT II
    - We will recieve a class from Bot I and Bot II to pass to pair up

