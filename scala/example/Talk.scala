package example
import java.util.Calendar
import scala.util.matching.Regex


object Talk {
    val MAX_MOVING_ALLOWED : Int = 5;
    
    def create (raw: String): Talk = {
       
        //for a lighning, no need to parse, we know it's 5 mins long
        if (raw.indexOf ("lightning") != -1) {
            return new Talk (raw, 5, null);
        }
        
        //build regex to parse a raw line
        val patt = """(.+) (\d+)min.*""".r;
        val ret = patt.findFirstMatchIn (raw);
        
        ret match {
            case Some(m) => {
                return new Talk (m.group(1), m.group(2).toInt, null);
            }
            case None => {
                return null;
            }
        }        
  
    return null;       
    }
        
}

class Talk (val t: String, 
            val d: Int,
            val se: Session   
            ) {
    
    val _title : String = t
    val _duration : Int = d
    
    var startTime : Calendar = Calendar.getInstance();
    var _atSession : Session = se;
    
    private var _movingCount =0;
    
    //lightweight getters as methods
    def duration = _duration;
    def title = _title;
    def atSession = _atSession;
    
    def wasMoved = _movingCount += 1;
    def movedTooManyTimes : Boolean = { _movingCount >= Talk.MAX_MOVING_ALLOWED }        
    def wasNeverMoved = { _movingCount = 0; }
    
    //def getMovingCount = _movingCount;
    
    
    def unchain = {
        if (_atSession!=null) {
            _atSession-=this; 
            _atSession=null;
        }
    }
    
    def setSession_= (s: Session) = {
        _atSession = s;
        _atSession += this;
    }
    
    def setStartTime (offset_min: Int) = {                
        startTime = atSession.startTime.clone().asInstanceOf[Calendar]
        startTime.add (Calendar.MINUTE, offset_min)
    }
    
    def getStartTimeOffsetMins : Int = {
        val mins : Long = (startTime.getTimeInMillis () / Workshop.MILLIS_PER_MIN);        
        return mins.toInt;
    }
    
    def printTalk  = {
      var ret : String = "";
      val hr : Int = startTime.get(Calendar.HOUR_OF_DAY);
      val min : Int = startTime.get(Calendar.MINUTE);

      if (startTime != null) ret+= "%02d:%02d".format (hr, min);
      ret += " "+title;
      if (duration>0) ret+=" (" +duration+" mins)";
      System.out.println (ret);

    }

}