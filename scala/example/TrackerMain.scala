package example

import scala.collection.mutable.ArrayBuffer
import scala.math._
import util.control.Breaks._

object TrackerMain {

    val MORNING_SHARP_LENGTH: Int = 180;          
    val AFTERNOON_MAX_LENGTH: Int = 240;          
    val AFTERNOON_MIN_LENGTH: Int = 180;          
}


class TrackerMain  {

    var tracks: Int = 0;
    var talks: ArrayBuffer[Talk] = new ArrayBuffer[Talk]()
    
    def exec = {
        
        if (talks==null) println ("mar itt is null");
      
        //get and parse input
        this.readInput
        val sugg: Int = pre_evaluate_talk_total_length;
        if (sugg>0) {
            tracks = sugg;
        } else {
            System.exit(0);
        }

        val ws: Workshop = new Workshop (tracks, talks);
        val success: Boolean = ws.arrangeWorkshop;
        if (success) {
            ws.printWorkshop;
            System.exit(0);
        } else {
            println ("Workshop arrangement failed. Try with different input.");
            System.exit(0);
        }

    }
    
    
    def dump_talks = {
        println ("Reading back registered talks:\n");
        var no: Int = 1;
        for (t <- talks) {
            println (no+". " +t.title+" ("+t.duration+" mins)");
            no+=1
        }
    }
    
    def readInput = {
        var more: Boolean = true
        println ("Start adding raw data here. Mark end of input sequence with empty line.");
        while (more) {
          breakable {  
            
            val line = scala.io.StdIn.readLine().trim();
            
            if (line == null) break;
            //line = line.trim();
            if (line.equals ("")) {
                more = false
                break
            }

            val t:Talk = Talk.create (line);
            
            if (talks == null) println ("talksnull");
            
            if (t!=null) {
                talks += t;
            }
          }
        }

        dump_talks;
    }    
    
    def pre_evaluate_talk_total_length: Int = {
        var dur:Int=0;
        for (t<-talks) {
            dur+=t.duration;
        }
        println ("");
        println ("Evaluating input:");
        println ("-----------------\n");
        println ("Total added talk length: " + dur + " mins");


        println ("Morning presentation time: "+ Track.MORNING_TARGET_LENGTH + "mins (can finish before end with "+Track.MORNING_SHORTENING_ALLOWED + " mins)");
        println ("Afternoon presentation time: "+ Track.AFTERNOON_TARGET_LENGTH + "mins (can finish before end with "+Track.AFTERNOON_SHORTENING_ALLOWED+ " mins)");

        val workshopRunningTime: Int = (Track.MORNING_TARGET_LENGTH + Track.AFTERNOON_TARGET_LENGTH);
        println ("Max workshop presentation time to be filled: " + workshopRunningTime + " mins");
        
        val d: Double = dur.toDouble / workshopRunningTime.toDouble
        val tr_count: Int = scala.math.ceil(d).toInt
        
        println ("Minimum number of tracks needed to place all talks: " + tr_count);

        if ((Track.MORNING_TARGET_LENGTH - Track.MORNING_SHORTENING_ALLOWED) +
            (Track.AFTERNOON_TARGET_LENGTH - Track.AFTERNOON_SHORTENING_ALLOWED) > dur) {
                println ("ERROR: talks can't end before 4PM on either of the tracks - min talk length must be at least "+ (TrackerMain.MORNING_SHARP_LENGTH + TrackerMain.AFTERNOON_MIN_LENGTH)*tracks +" mins ");
                return -1;
        }

        //stone prevention
        val longerSessionLen: Int = if (Track.MORNING_TARGET_LENGTH > Track.AFTERNOON_TARGET_LENGTH) Track.MORNING_TARGET_LENGTH else Track.AFTERNOON_TARGET_LENGTH 
        
        for (t <- talks) {
            if (t.duration > longerSessionLen) {
                println ("ERROR: No talk can be longer than "+longerSessionLen+" mins (they could not be placed to either morning or afternoon session!) ");
                return -1;
            }
        }
        println ("All good.");
        return tr_count;
    }
    
}    
  
    
    
    