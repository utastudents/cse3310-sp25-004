package uta.cse3310.Bot;

import uta.cse3310.PairUp.Player;

//Bot I and Bot II should both extend this class
public abstract class Bot extends Player {

    /* This code is not meant to be uncommented. It is meant as a reference
    private GameMove botMethodThatMakesAMoveHere(GamePlay gp) {
        Cord from = new Cord(0,0); //Should use actual game logic here to determine which checker and where to move it
        Cord to = new Cord(1,1);
        return new GameMove(this.playerId, this.game.getGameID(), from.getX(), from.getY(), to.getX(), to.getY(), "black");
    }
    */     

    /* This code is not here to be uncommented. It is meant as a reference
    @Override
    public boolean makeMove(GamePlay gp) {

        GameMove move = botMethodThatMakesAMoveHere(gp);
       
        PageManager.Gm.processMove(move, gp);
        return true;
    }
    */
}
