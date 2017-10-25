import java.util.*;

/*
Represents one session in a track. Either morning or afternoon. Variable lenght and possible shortening time
*/

public class Session {

    public static final int MORNING = 1;
    public static final int AFTERNOON = 2;

    public final Calendar startTime;


    public final int when; //1-Morning 2-Afternoon
    public Vector<Talk> talks;
    public int target_length;
    public int shorten_allowed;
    public Track belongs_to_track;

    public Session (Track t, int w, int tl, int sa) {
        when=w;
        target_length = tl;
        shorten_allowed = sa;
        belongs_to_track = t;

        talks = new Vector<Talk>();

        startTime = Calendar.getInstance();
        startTime.set (Calendar.HOUR_OF_DAY,0);
        startTime.set (Calendar.MINUTE,0);

        if (when==MORNING) startTime.add (Calendar.MINUTE, Workshop.MORNING_STARTTIME_MIN);
        if (when==AFTERNOON)startTime.add (Calendar.MINUTE, Workshop.AFTERNOON_STARTTIME_MIN);

       // System.out.printf("%d-%02d-%02d %02d:%02d:%02d.%03d", year, month, day, hour, minute, second, millis);
    }




    public boolean insertTalk (Talk ta) {
         if (ta==null) return false;

         //test if fits - protected
         int dur = getActualLength()+ta.getDuration();
         if (dur > target_length) return false;

         ta.unchain(); //make sure talk is nowhere else

         talks.add (ta);
         ta.atSession = this;

         return true;
    }


    
    public int actualLength {
        int dur=0;
        for (int i=0; i<talks.size(); i++) {
           Talk t=talks.get(i);
           dur+=t.getDuration();
        }
        return dur;
    }


    public int missingTimeToTarget () {
        return target_length - getActualLength();
    }


    public int missingTimeToMinimum () {
        return target_length - shorten_allowed - getActualLength();
    }


    //-2 -> too short, -1=short but OK, 0=OK precisely, 1=too long
    public int evaluateTalkLength () {
        int dur = getActualLength();
        if (dur < target_length - shorten_allowed) return -2;
        if (dur >= target_length - shorten_allowed &&
            dur < target_length)
                return -1;
        if (dur == target_length) return 0;
        if (dur > target_length) return 1;
        return 0;
    }

    public boolean isSessionSatisfied () {
         if (getMissingTimeToTarget() <= shorten_allowed) return true;
         else return false;
    }

    public void removeRandomTalk () {
        int max = talks.size();
        Random rand = new Random();
        int n = rand.nextInt(max);
        Talk ta = talks.get(n);
        ta.unchain();
        ta.movingCount=0;
   }
}