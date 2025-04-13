package uta.cse3310.PageManager;

import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class UserEventReply 
{
    public JsonObject replyObj; //Json response data
    public ArrayList<Integer> recipients; // List of client IDs to receive the response

}


