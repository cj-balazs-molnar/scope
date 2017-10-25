import java.util.*;

/*
Represents one track in the workshop. Contains a morning session and an afternoon session
*/

public class Track {

    public Session morning_session;
    public Session afternoon_session;

    public final int id;

    public static final int MORNING_TARGET_LENGTH = 180;
    public static final int AFTERNOON_TARGET_LENGTH = 240;

    public static final int MORNING_SHORTENING_ALLOWED = 0;
    public static final int AFTERNOON_SHORTENING_ALLOWED = 60;

    public Track (int _id) {
        morning_session = new Session (this, Session.MORNING, MORNING_TARGET_LENGTH, MORNING_SHORTENING_ALLOWED);
        afternoon_session = new Session (this, Session.AFTERNOON, AFTERNOON_TARGET_LENGTH, AFTERNOON_SHORTENING_ALLOWED);
        id = _id;
    }


    public void printTrack {
        System.out.println ("Track No. "+(id+1));
        System.out.println ("-----------");

        for (int t=0;t<morning_session.talks.size();t++) {
            Talk ta = morning_session.talks.get(t);
            ta.printTalk();
        }

        for (int t=0;t<afternoon_session.talks.size();t++) {
            Talk ta = afternoon_session.talks.get(t);
            ta.printTalk();
        }
        System.out.println ("");
    }



}