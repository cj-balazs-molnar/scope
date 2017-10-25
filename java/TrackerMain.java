import java.io.*;
import java.util.*;


public class TrackerMain {

    public static final int MORNING_SHARP_LENGTH = 180;
    public static final int AFTERNOON_MAX_LENGTH = 240;
    public static final int AFTERNOON_MIN_LENGTH = 180;

    public int tracks = 0;


    public Vector<Talk> talks;


    public static void main(String [] args) {
        TrackerMain tr = new TrackerMain();
        tr.exec();
    }

    public TrackerMain () {
        talks = new java.util.Vector<Talk>();

    }

    public void exec () {
      try {
        //get and parse input
        this.readInput();
        int sugg = pre_evaluate_talk_total_length ();
        if (sugg>0) {
            tracks = sugg;
        } else {
            System.exit(1);
        }

        Workshop ws = new Workshop (tracks, talks);
        boolean success = ws.arrangeWorkshop();
        if (success) {
            ws.printWorkshop();
            System.exit(1);
        } else {
            System.out.println ("Workshop arrangement failed. Try with different input.");
            System.exit(0);
        }
      } catch (Exception e) {
        //more user friendly error handling if we have time
        e.printStackTrace();
      }
    }

    public void dump_talks () {
        System.out.println ("Reading back registered talks:\n");
        for (int i=0; i<talks.size(); i++) {
            Talk t=talks.get(i);
            System.out.println ((t.no+1)+". " +t.getTitle()+" ("+t.getDuration()+" mins)");
        }
    }


    public void readInput () {
        BufferedReader _reader = _reader = new BufferedReader(new InputStreamReader(System.in));
        String line = "";
        System.out.println ("Start adding raw data here. Mark end of input sequence with empty line.");
        while (true) {
            try {
                line = _reader.readLine();
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (line == null) break;
            line = line.trim();
            if (line.equals ("")) break;

            Talk t=Talk.create (line);
            if (t==null) continue;
            t.no=talks.size();
            if (t==null) continue;
            talks.add (t);
        }

        dump_talks();
    }
    
    
    public int pre_evaluate_talk_total_length () {
        int dur=0;
        for (int i=0; i<talks.size(); i++) {
            Talk t = talks.get(i);
            dur+=t.getDuration();
        }
        System.out.println ("");
        System.out.println ("Evaluating input:");
        System.out.println ("-----------------\n");
        System.out.println ("Total added talk length: " + dur + " mins");


        System.out.println ("Morning presentation time: "+ Track.MORNING_TARGET_LENGTH + "mins (can finish before end with "+Track.MORNING_SHORTENING_ALLOWED + " mins)");
        System.out.println ("Afternoon presentation time: "+ Track.AFTERNOON_TARGET_LENGTH + "mins (can finish before end with "+Track.AFTERNOON_SHORTENING_ALLOWED+ " mins)");

        int workshopRunningTime = (Track.MORNING_TARGET_LENGTH + Track.AFTERNOON_TARGET_LENGTH);
        System.out.println ("Max workshop presentation time to be filled: " + workshopRunningTime + " mins");

        int tr_count = (int)Math.ceil((float)((float)dur / (float)workshopRunningTime));
        System.out.println ("Minimum number of tracks needed to place all talks: " + tr_count);

        if ((Track.MORNING_TARGET_LENGTH - Track.MORNING_SHORTENING_ALLOWED) +
            (Track.AFTERNOON_TARGET_LENGTH - Track.AFTERNOON_SHORTENING_ALLOWED) > dur) {
                System.out.println ("ERROR: talks can't end before 4PM on either of the tracks - min talk length must be at least "+ (MORNING_SHARP_LENGTH + AFTERNOON_MIN_LENGTH)*tracks +" mins ");
                return -1;
        }

        //stone prevention
        int longerSessionLen = Track.MORNING_TARGET_LENGTH > Track.AFTERNOON_TARGET_LENGTH ? Track.MORNING_TARGET_LENGTH : Track.AFTERNOON_TARGET_LENGTH;
        for (int i=0; i<talks.size(); i++) {
            Talk t = talks.get(i);
            if (t.getDuration() > longerSessionLen) {
                System.out.println ("ERROR: No talk can be longer than "+longerSessionLen+" mins (they could not be placed to either morning or afternoon session!) ");
                return -1;
            }
        }
        System.out.println ("All good.");
        return tr_count;
    }





}



