package example

/*
Represents one track in the workshop. Contains a morning session and an afternoon session
*/

object Track {
    val MORNING_TARGET_LENGTH: Int = 180;
    val AFTERNOON_TARGET_LENGTH: Int = 240;

    val MORNING_SHORTENING_ALLOWED: Int = 0;
    val AFTERNOON_SHORTENING_ALLOWED: Int = 60;
}

class Track (_id: Int) {

    val morning_session: Session = new Session (this, Session.MORNING, Track.MORNING_TARGET_LENGTH, Track.MORNING_SHORTENING_ALLOWED);
    val afternoon_session: Session = new Session (this, Session.AFTERNOON, Track.AFTERNOON_TARGET_LENGTH, Track.AFTERNOON_SHORTENING_ALLOWED);

    val id = _id;
    
    
    def printTrack () {
        println ("Track No. "+(id+1));
        println ("-----------");
 
        morning_session.printTalks;
        
        afternoon_session.printTalks;
                
        println ("");
    }


}