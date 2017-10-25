import java.util.regex.*;
import java.util.*;

class Talk {

    //natural
    private String title;
    private int duration;
    public int no;

   // public Track atTrack;
    public Session atSession;

    private Calendar startTime;


    public int movingCount=0; //hanyszor lett ellopva
    public static final int MAX_MOVING_ALLOWED = 5;


    public String getTitle () {
        return title;
    }

    public int getDuration () {
        return duration;
    }


    public static Talk create (String raw) {

        //for a lighning, no need to parse, we know it's 5 mins long
        if (raw.indexOf ("lightning") != -1) {
            return new Talk (raw, 5);
        }

        //build regex to parse a raw line
        String regex = "^(.+) (\\d+)min.*$";
        Pattern r = Pattern.compile (regex);
        Matcher m = r.matcher (raw);

        if (m.find()) {
              return new Talk (m.group(1), Integer.parseInt(m.group(2)));
        } else {
            System.out.println ("ERROR parsing Talk from input line '"+raw+"'. Include mins or lightning keyword (=5mins)");
            return null;
        }
    }

    public void unchain () {
        if (atSession==null) return;
        atSession.talks.remove(this);
        atSession=null;
    }


    public Talk (String t, int d) {
        title = t;
        duration = d;
    }

    public Talk (String t, int d, Session s) {
        title = t;
        duration = d;
        atSession = s;
    }

    public void setStartTime (int offset_min) {
        startTime = (Calendar)atSession.startTime.clone();
        startTime.add (Calendar.MINUTE, offset_min);
    }

    public Calendar getStartTime () {
        return startTime;
    }

    public int getStartTimeOffsetMins () {
        return (int)(startTime.getTimeInMillis () / Workshop.MILLIS_PER_MIN);
    }

    public void printTalk () {
      String ret = "";
      int hr = startTime.get(Calendar.HOUR_OF_DAY);
      int min = startTime.get(Calendar.MINUTE);

      if (startTime != null) ret+= String.format("%02d:%02d", hr, min);
      ret += " "+getTitle();
      if (getDuration()>0) ret+=" (" +getDuration()+" mins)";
      System.out.println (ret);

    }

}