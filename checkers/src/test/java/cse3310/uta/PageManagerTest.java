package cse3310.uta;

import uta.cse3310.DB.DB;
import uta.cse3310.PairUp.PairUp;
import uta.cse3310.PageManager.UserEvent;
import uta.cse3310.PageManager.UserEventReply;
import uta.cse3310.PageManager.HumanPlayer;
import uta.cse3310.PairUp.Player;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.jupiter.api.BeforeEach;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonArray;

import org.junit.Test;


public class PageManagerTest
{
    private PageManager pm;

    @BeforeEach
    public void setup()
    {
        pm = new PageManager();

        HumanPlayer human1 = new HumanPlayer("Alice", "Pass", "Salt");
        human1.setELO(1450);
        human1.setWins(8);
        human1.setLosses(7);
        human1.setStatus(Player.STATUS.IN_GAME);

        HumanPlayer human2 = new HumanPlayer("Johnny", "Pass", "Salt");
        human2.setELO(1300);
        human2.setWins(10);
        human2.setLosses(6);
        human2.setStatus(Player.STATUS.IN_QUEUE);

        pm.activePlayers.put(1, human1);
        pm.activePlayers.put(2, human2);


    }


    @Test
    public void getActivePlayersTest()
    {
        //getActivePlayers really doesnt use any of the JSON info that is given to it
        JsonObject temp = new JsonObject();

        UserEventReply reply = pm.getActivePlayers(temp, 1);

        JsonObject response = reply.replyObj;

        assertEquals("getActivePlayers", response.get("responseID").getAsString());
        assertEquals(1, response.get("MyClientID").getAsInt());

        JsonArray playersArray = response.get("activePlayers");
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

    
}