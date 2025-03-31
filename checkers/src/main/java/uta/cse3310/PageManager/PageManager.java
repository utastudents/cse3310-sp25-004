package uta.cse3310.PageManager;

import uta.cse3310.DB.DB;
import uta.cse3310.PairUp.PairUp;

public class PageManager {
    DB db;
    PairUp pu;

    public String input(String S) {
        return "XXXX";

    }

    public PageManager() {
        db = new DB();
        // pass over a pointer to the single database object in this system
        pu = new PairUp(db);
    }

}
