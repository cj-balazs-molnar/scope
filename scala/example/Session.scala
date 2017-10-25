package example
import java.util.Calendar
import scala.collection.mutable.ArrayBuffer


object Session {
    val MORNING: Int  = 1;
    val AFTERNOON: Int  = 2;
}

class Session (_t: Track, _w: Int, _tl: Int, _sa: Int) {
    
    /* properties */
    val startTime : Calendar = Calendar.getInstance()
    private val _talks = ArrayBuffer[Talk]()
    
    val belongs_to_track: Track  = _t;    
    private val _when: Int = _w
    private val _targetLength: Int = _tl
    private val _shortenAllowed: Int = _sa
    
    startTime.set (Calendar.HOUR_OF_DAY,0);
    startTime.set (Calendar.MINUTE,0);
    
    if (_when==Session.MORNING) startTime.add (Calendar.MINUTE, Workshop.MORNING_STARTTIME_MIN);
    if (_when==Session.AFTERNOON) startTime.add (Calendar.MINUTE, Workshop.AFTERNOON_STARTTIME_MIN);
   
   
    /* basic getters */
    def targetLength = _targetLength; 
    
    def shortenAllowed = _shortenAllowed; 
    
    /* operators, just for fun */    

    def -= (oldTalk: Talk): Unit = this._talks -= oldTalk;
    def += (ta: Talk): Unit = {
         if (ta==null) return;
         ta.unchain; //make sure talk is nowhere else
         _talks+=ta;
         ta._atSession = this;         
    }
    
    def talkAt (i: Int) = _talks(i);
    
    def actualLength(): Int = {
        var sum: Int = 0
        for (ta <- _talks) sum += ta.duration;
        return sum
    }
    
    def missingTimeToTarget : Int = {
        return _targetLength - actualLength;
    }

    def missingTimeToMinimum : Int = {
        return targetLength - shortenAllowed - actualLength;
    }
    
    //-2 -> too short, -1=short but OK, 0=OK precisely, 1=too long
    def evaluateTalkLength (): Int = {
        lazy val dur = actualLength;
        if (dur < targetLength - shortenAllowed) return -2;
        if (dur >= targetLength - shortenAllowed &&
            dur < targetLength)
                return -1;
        if (dur == targetLength) return 0;
        if (dur > targetLength) return 1;
        return 0;
    }
    
    def isSessionSatisfied : Boolean =  {
         if (missingTimeToTarget <= shortenAllowed) return true;
         else return false;
    }
    
    def removeRandomTalk = {
        val max: Int = _talks.size;
        val rand = new scala.util.Random();
        val n: Int = rand.nextInt(max);
        val ta: Talk = _talks(n);
        ta.unchain;
        ta.wasNeverMoved;
   }
   
   def printTalks = {
       for (ta <- _talks) {
           ta.printTalk;
       }
   }
   
   def finalizeStartTimes = {
       var offset: Int = 0; //min
       for (ta <- _talks) {
           ta.setStartTime (offset);
           offset+=ta.duration;
       }
    }
    
    def addPseudoTalks = {
            //push lunch and meetup
            if (_when==Session.MORNING)   _talks += new Talk ("Lunch", 0, this);
            if (_when==Session.AFTERNOON) _talks += new Talk ("Meet Your Colleagues Event", 0, this);
    }


}