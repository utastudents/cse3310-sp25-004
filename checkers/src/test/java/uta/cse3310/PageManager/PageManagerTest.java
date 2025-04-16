package uta.cse3310.PageManager;

import uta.cse3310.DB.DB;
import uta.cse3310.PairUp.PairUp;
import uta.cse3310.PageManager.UserEvent;
import uta.cse3310.PageManager.UserEventReply;
import uta.cse3310.PageManager.HumanPlayer;
import uta.cse3310.PageManager.PageManager;
import uta.cse3310.PageManager.GameState;
import uta.cse3310.PairUp.Player;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.google.gson.JsonObject;
import com.google.gson.JsonArray;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

@ExtendWith(MockitoExtension.class)
public class PageManagerTest
{
    @Mock
    private PairUp pu;

    @InjectMocks
    private PageManager pm;

    @BeforeEach
    public void setup()
    {


        byte[] testSalt1 = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15 };
        byte[] testSalt2 = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15 };

        HumanPlayer human1 = new HumanPlayer("Alice", "Pass", testSalt1);
        human1.setELO(1450);
        human1.setWins(8);
        human1.setLosses(7);
        human1.setStatus(Player.STATUS.IN_GAME);

        HumanPlayer human2 = new HumanPlayer("Johnny", "Pass", testSalt2);
        human2.setELO(1300);
        human2.setWins(10);
        human2.setLosses(6);
        human2.setStatus(Player.STATUS.IN_QUEUE);

        pm.activePlayers.put(1, human1);
        pm.activePlayers.put(2, human2);

        pm.clientStates.put(1, GameState.GAME_DISPLAY);
        pm.clientStates.put(2, GameState.JOIN_GAME);


    }


    @Test
    public void getActivePlayersTest()
    {
        //getActivePlayers really doesnt use any of the JSON info that is given to it
        JsonObject temp = new JsonObject();

        UserEventReply reply = pm.getActivePlayers(temp, 1);

        JsonObject response = reply.replyObj;

        assertEquals("join_game", response.get("responseID").getAsString());
        assertEquals(1, response.get("MyClientID").getAsInt());

        JsonArray playersArray = response.get("activePlayers").getAsJsonArray();
        assertNotNull(playersArray);

        assertEquals(2, playersArray.size());

        boolean aliceHere = false;
        boolean johnnyHere = false;

        for (int i = 0; i < playersArray.size(); i++)
        {
            JsonObject playerData = playersArray.get(i).getAsJsonObject();
            if("Alice".equals(playerData.get("username").getAsString()))
            {
                aliceHere = true;
                assertEquals(1450, playerData.get("elo").getAsInt());
                assertEquals(8, playerData.get("gamesWon").getAsInt());
                assertEquals(7, playerData.get("gamesLost").getAsInt());
                assertEquals("IN_GAME", playerData.get("status").getAsString());
            }
            else if("Johnny".equals(playerData.get("username").getAsString()))
            {
                johnnyHere = true;
                assertEquals(1300, playerData.get("elo").getAsInt());
                assertEquals(10, playerData.get("gamesWon").getAsInt());
                assertEquals(6, playerData.get("gamesLost").getAsInt());
                assertEquals("IN_QUEUE", playerData.get("status").getAsString());
            }
        }

        assertTrue(johnnyHere, "Johnny not in player list");
        assertTrue(aliceHere, "Alice not in player list");

    }

    @Test
    public void joinQueueSuccessTest()
    {
        // Only using Alice for this test

        JsonObject temp = new JsonObject();
        temp.addProperty("playerClientId", 1);

        when(pu.addToQueue(pm.activePlayers.get(1))).thenReturn(true);

        UserEventReply reply = pm.joinQueue(temp, 1);

        JsonObject response = reply.replyObj;

        assertEquals("joinQueue", response.get("responseID").getAsString());
        assertEquals(1, response.get("MyClientID").getAsInt());
        assertTrue(response.get("inQueue").getAsBoolean(), "inQueue is false for some reason");

    }

    @Test
    public void joinQueueFailureTest()
    {
        // Only using Alice for this test

        JsonObject temp = new JsonObject();
        temp.addProperty("playerClientId", 1);

        when(pu.addToQueue(pm.activePlayers.get(1))).thenReturn(false);

        UserEventReply reply = pm.joinQueue(temp, 1);

        JsonObject response = reply.replyObj;

        assertEquals("joinQueue", response.get("responseID").getAsString());
        assertEquals(1, response.get("MyClientID").getAsInt());
        assertFalse(response.get("inQueue").getAsBoolean(), "inQueue is true for some reason");

    }

    @Test
    public void transitionPageTest()
    {
        // Only using Alice for this test

        ArrayList<Integer> recipients = new ArrayList();
        recipients.add(1);

        UserEventReply reply = pm.transitionPage(recipients, GameState.SUMMARY);

        JsonObject response = reply.replyObj;

        assertEquals("updateVisibility", response.get("responseID").getAsString());
        assertEquals("summary", response.get("visible").getAsString());
        assertTrue(pm.clientStates.get(1) == GameState.SUMMARY, "status is not in queue");

    }    

}
