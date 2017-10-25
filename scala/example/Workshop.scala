package example

import scala.collection.mutable.ArrayBuffer
import util.control.Breaks._

object Workshop {
    val MILLIS_PER_MIN : Int = 1000 * 60;
    val MORNING_STARTTIME_MIN: Int = 9*60
    val AFTERNOON_STARTTIME_MIN: Int = 13*60;     
    val MAX_MESSUP: Int = 100; //empirical value
}


class Workshop (_trackCount: Int, talks: ArrayBuffer[Talk]) {
        
    val tracks: ArrayBuffer[Track] = new ArrayBuffer[Track]();
    val sessions: ArrayBuffer[Session] = new ArrayBuffer [Session]();
    private val _talkList: ArrayBuffer[Talk] = talks;
    
    var i: Int=0;
    while (i<_trackCount) {
        val tr: Track = new Track(i);
        tracks += tr;
        sessions += tr.morning_session;
        sessions += tr.afternoon_session;       
        i+=1;
    }    
    
    
    
    def printWorkshop () = {
        println ("");
        println ("********************************");
        println ("Workshop arrangement suggestion:");
        println ("********************************");
        println ("");
        
        for (ta <- tracks) {
            ta.printTrack();
        }

    }
    
    
  def arrangeWorkshop: Boolean = {

      var messup_count: Int =0;
      
      while (true) {
        
         var alloc_success_count:Int=0;

         if (isAllSessionSatisfied) {
             finalizeStartTimes;
             return true;
         }
         
         for (se <- sessions) {
           breakable {                             
              var ta_id: Int = 0;
              val status: Int = se.evaluateTalkLength;
              if (status== -2 || status== -1) { //more space left

                 ta_id = getBestFitTalkIdFromFree (se);
                 if (ta_id != -1) {
                      val ta: Talk = _talkList(ta_id);
                      se += ta;                           
                      alloc_success_count+=1
                      break //continue
                 } else {

                     ta_id = getBestFitTalkIdFromTaken (se);
                     if (ta_id != -1) {
                          val ta: Talk = _talkList(ta_id);
                          ta.unchain; //steal from current session
                          se += ta;
                          ta.wasMoved
                          alloc_success_count+=1
                          break //continue
                     } else {
                        
                     //could not move anything
                     se.removeRandomTalk;
                    }
                 }
              }            
           }
           
        }
         //System.exit(1);
         if (alloc_success_count==0) {
            if (messup_count >= Workshop.MAX_MESSUP) return false;

            for (se <- sessions) {
                if (!se.isSessionSatisfied && se.shortenAllowed!=0) {
                    se.removeRandomTalk;
                    messup_count+=1
                }
            }
        }
      }
      return true;
   } 
   
 
   
   def getBestFitTalkIdFromFree (se: Session): Int = {
        val hole: Int = se.missingTimeToTarget;
        var bestFitId: Int = -1;
        var bestFitSizeSoFar: Int = -1;
        
        var i: Int = 0;
        for (ta <- talks) {
            
          breakable {  
            if (ta.atSession!=null) break; 
            if (ta.duration > hole) break;
            if (bestFitSizeSoFar == -1 ||
                ta.duration > bestFitSizeSoFar ) {
                    bestFitId = i;
                    bestFitSizeSoFar = ta.duration;                    
                }
           }
           i+=1 
        }        
        return bestFitId;
    } 
    
    
    def getBestFitTalkIdFromTaken (se: Session): Int = {
        val hole: Int = se.missingTimeToTarget;
        var bestFitId: Int = -1;
        var bestFitSizeSoFar: Int = -1;
        
        var i:Int = 0;
        for (ta <- talks) {        
          breakable {
            if (ta.atSession==null) break;
            if (ta.atSession == se) break;
            if (ta.movedTooManyTimes) break;
            if (ta.duration > hole) break;
            if (bestFitSizeSoFar == -1 ||
                ta.duration > bestFitSizeSoFar ) {
                    bestFitId = i;
                    bestFitSizeSoFar = ta.duration;
                }
            }
            i+=1;
        }
        return bestFitId;
    }

    def isAllSessionSatisfied : Boolean = {
        for (se <- sessions) {
            if (!se.isSessionSatisfied) return false;
        }
        return true;
    }

    def finalizeStartTimes  = {
        for (se <- sessions) {
            se.addPseudoTalks; // lunch and meetup 
            se.finalizeStartTimes;
        }
    }
}
