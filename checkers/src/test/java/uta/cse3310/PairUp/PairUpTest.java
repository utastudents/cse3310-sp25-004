package uta.cse3310.PairUp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import uta.cse3310.Bot.BotI.BotI;
import uta.cse3310.GameManager.GameManager;

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

        Player human = new TestPlayer(1);
        Player human2 = new TestPlayer(2);

        pu.addToQueue(human); //Normally would add a HumanPlayer using this function but it shouldn't make a difference

        assertEquals(pu.getNumPlayersInQueue(), 1);

        pu.removeFromQueue(human);

        assertEquals(pu.getNumPlayersInQueue(), 0); 

        pu.addToQueue(human2);

        assertEquals(pu.getNumPlayersInQueue(), 1);

        pu.removeFromQueue(human2);

        assertEquals(pu.getNumPlayersInQueue(), 0);
    }

    @Test
    void testChallenge() {
        GameManager gm = new GameManager();
        PairUp pu = new PairUp(gm);

        Player human = new TestPlayer(0);
        Player human2 = new TestPlayer(1);

        pu.challenge(human, human2);

        assertEquals(pu.getNumPlayersInQueue(), 0); //Challenge should now be in GM
        assertEquals(gm.getNumOfAvailableGames(), 9);
    }
    @Test
    void isInRange() {
        GameManager gm = new GameManager();
        PairUp pu = new PairUp(gm);

        Player human = new TestPlayer(0, 1000);
        Player human2 = new TestPlayer(1, 1300);
        Player human3 = new TestPlayer(2, 1600);

        assertEquals(pu.isInRange(human, human2), true);
        assertEquals(pu.isInRange(human, human3), false);
    }
    /* This test was to make sure the tests were running. They are!
    @Test
    void failure() {
        assertEquals(1, 0);
    }
    */
}