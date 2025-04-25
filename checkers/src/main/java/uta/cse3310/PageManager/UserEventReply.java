package uta.cse3310.PageManager;

import java.util.ArrayList;
import com.google.gson.JsonObject;

public class UserEventReply 
{
    public JsonObject replyObj; //Json response data
    public ArrayList<Integer> recipients; // List of client IDs to receive the response

    public UserEventReply() {
        recipients = new ArrayList<>();
    }
    public UserEventReply(JsonObject obj) {
        replyObj = obj;
        recipients = new ArrayList<>();
    }
    public UserEventReply(JsonObject obj, int clientId) {
        replyObj = obj;
        recipients = new ArrayList<>();
        recipients.add(clientId);
    }
    public UserEventReply(JsonObject obj, ArrayList<Integer> list) {
        replyObj = obj;
        recipients = list;
    }
}


