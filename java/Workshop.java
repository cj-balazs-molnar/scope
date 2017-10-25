import java.util.*;

public class Workshop {

    public Vector<Track> tracks;
    public Vector<Session> sessions;
    private Vector<Talk> talklist;


    public static final int MORNING_STARTTIME_MIN = 9*60; //9AM
    public static final int AFTERNOON_STARTTIME_MIN = 13*60; //9AM
    public static final int MILLIS_PER_MIN = 1000*60;

    public static final int MAX_MESSUP = 100; //empirical value


    public Workshop (int trackCount, Vector<Talk> talks) {
        tracks = new Vector<Track>();
        sessions = new Vector<Session>();

        talklist = talks;
        for (int i=0;i<trackCount;i++) {
           Track tr = new Track(i);
           tracks.add(tr);
           sessions.add (tr.morning_session);
           sessions.add (tr.afternoon_session);
        }
    }


    public void printWorkshop () {
        System.out.println ("");
        System.out.println ("********************************");
        System.out.println ("Workshop arrangement suggestion:");
        System.out.println ("********************************");
        System.out.println ("");
        for (int t=0;t<tracks.size();t++) {
            Track tr = tracks.get(t);
            tr.printTrack();
        }

    }


    public boolean arrangeWorkshop () {

      int messup_count=0;
      while (true) {
         int alloc_success_count=0;

         if (isAllSessionSatisfied ()) {
             finalizeStartTimes ();
             return true;
         }
         for (int s=0;s<sessions.size();s++) {

              Session se = sessions.get(s);
              int ta_id;

              //morning
              int status = se.evaluateTalkLength();
              if (status==-2 || status==-1) { //more space left

                 ta_id = getBestFitTalkIdFromFree (se);
                 if (ta_id != -1) {
                      Talk ta = talklist.get(ta_id);
                      se.insertTalk (ta);
                      alloc_success_count++;
                      continue;
                 }

                 ta_id = getBestFitTalkIdFromTaken (se);
                 if (ta_id != -1) {
                      Talk ta = talklist.get(ta_id);
                      ta.unchain(); //steal from current session
                      se.insertTalk (ta);
                      ta.movingCount++;
                      alloc_success_count++;
                      continue;
                 }

                 //could not move anything
                 se.removeRandomTalk ();

         //        System.exit(1);


              }
         }

         //System.exit(1);
         if (alloc_success_count==0) {
            if (messup_count >= Workshop.MAX_MESSUP) return false;

            for (int s=0;s<sessions.size();s++) {
                Session se = sessions.get(s);
                if (se.isSessionSatisfied() && se.shorten_allowed==0) continue;
                se.removeRandomTalk ();
                messup_count++;
            }
        }
      }
   }


   public int suggestTalkToRemove (Session se) {
       return -1;
   }



    public int getBestFitTalkIdFromFree (Session se) {
        int hole = se.getMissingTimeToTarget();
        int bestFitId = -1;
        int bestFitSizeSoFar = -1;
        for (int i=0; i<talklist.size(); i++) {
            Talk ta = talklist.get(i);
            if (ta.atSession!=null) continue;
            if (ta.getDuration() > hole) continue;
            if (bestFitSizeSoFar ==-1 ||
                ta.getDuration() > bestFitSizeSoFar ) {
                    bestFitId = i;
                    bestFitSizeSoFar = ta.getDuration();
                }
        }
        return bestFitId;
    }

    public int getBestFitTalkIdFromTaken (Session se) {
        int hole = se.getMissingTimeToTarget();
        int bestFitId = -1;
        int bestFitSizeSoFar = -1;
        for (int i=0; i<talklist.size(); i++) {
            Talk ta = talklist.get(i);
            if (ta.atSession==null) continue;
            if (ta.atSession == se) continue;
            if (ta.movingCount>=Talk.MAX_MOVING_ALLOWED) continue;
            if (ta.getDuration() > hole) continue;
            if (bestFitSizeSoFar ==-1 ||
                ta.getDuration() > bestFitSizeSoFar ) {
                    bestFitId = i;
                    bestFitSizeSoFar = ta.getDuration();
                }
        }
        return bestFitId;
    }




    public boolean isAllSessionSatisfied () {
        for (int s=0;s<sessions.size();s++) {
            Session se = sessions.get(s);
            if (!se.isSessionSatisfied ()) return false;
        }
        return true;
    }

    public void finalizeStartTimes () {
        for (int s=0;s<sessions.size();s++) {
            Session se = sessions.get(s);

            //push lunch and meetup
            if (se.when==Session.MORNING)   se.talks.add (new Talk ("Lunch", 0, se));
            if (se.when==Session.AFTERNOON) se.talks.add (new Talk ("Meet Your Colleagues Event", 0, se));


            int offset = 0; //min
            for (int t=0;t<se.talks.size();t++) {
                Talk ta = se.talks.get(t);
                ta.setStartTime (offset);
                offset+=ta.getDuration();
            }
        }
    }

    public void dumpTracks () {
        for (int i=0;i<tracks.size();i++) {
            Track tr = tracks.get(i);
            System.out.println ("Track "+i);

            System.out.print (" - M: ");
            int mdur = 0;
            for (int t=0;t<tr.morning_session.talks.size();t++) {
                Talk ta = tr.morning_session.talks.get(t);
                System.out.print (ta.no+" ("+ta.getDuration()+"min) ");
                mdur += ta.getDuration();
            }
            System.out.println (" Total: "+mdur);
            System.out.print (" - A: ");
            int adur = 0;
            for (int t=0;t<tr.afternoon_session.talks.size();t++) {
                Talk ta = tr.afternoon_session.talks.get(t);
                System.out.print (ta.no+" ("+ta.getDuration()+"min) ");
                adur += ta.getDuration();
            }
            System.out.println (" Total: "+adur);
        }
        System.out.print ("Unhandled: ");
        int udur = 0;
        for (int t=0;t<this.talklist.size();t++) {
                Talk ta = this.talklist.get(t);
                if (ta.atSession!=null) continue;
                System.out.print (ta.no+" ("+ta.getDuration()+"min) ");
                udur += ta.getDuration();
        }
         System.out.println (" Total: "+udur);

        System.out.println ("");
    }


}