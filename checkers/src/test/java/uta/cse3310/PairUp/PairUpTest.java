package uta.cse3310.PairUp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import uta.cse3310.Bot.BotI.BotI;
import uta.cse3310.GameManager.GameManager;
import uta.cse3310.PageManager.HumanPlayer;
import uta.cse3310.PairUp.Player.STATUS;

public class PairUpTest {

	@Test
    void testAddToQueue() {
        GameManager gm = new GameManager();
        PairUp pu = new PairUp(gm);

        Player bot = new BotI();

        pu.addToQueue(bot); //Normally would add a HumanPlayer using this function but it shouldn't make a difference

        assertEquals(pu.getNumPlayersInQueue(), 1);
    }

    @Test
    void testRemoveFromQueue() {
        GameManager gm = new GameManager();
        PairUp pu = new PairUp(gm);

        Player human = new HumanPlayer("Test", "test", 1, STATUS.ONLINE, 0, 0, 1000, 0);
        Player human2 = new HumanPlayer("Test", "test", 1, STATUS.ONLINE, 0, 0, 1000, 0);

        pu.addToQueue(human); //Normally would add a HumanPlayer using this function but it shouldn't make a difference

        assertEquals(pu.getNumPlayersInQueue(), 1);

        pu.addToQueue(human2);

        assertEquals(pu.getNumPlayersInQueue(), 2);

        pu.removeFromQueue(human);

        assertEquals(pu.getNumPlayersInQueue(), 1);

        pu.removeFromQueue(human2);

        assertEquals(pu.getNumPlayersInQueue(), 0);
    }

    @Test
    void testChallenge() {
        GameManager gm = new GameManager();
        PairUp pu = new PairUp(gm);

        Player human = new HumanPlayer("Test", "test", 1, STATUS.ONLINE, 0, 0, 1000, 0);
        Player human2 = new HumanPlayer("Test", "test", 1, STATUS.ONLINE, 0, 0, 1000, 0);

        pu.challenge(human, human2);

        assertEquals(pu.getNumPlayersInQueue(), 2);
    }

    /* This test was to make sure the tests were running. They are!
    @Test
    void failure() {
        assertEquals(1, 0);
    }
    */
}