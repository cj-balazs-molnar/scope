package example


class TrackerTest {

   val talk1_title: String = "Better way of reading books";
   val talk1_len: Int = 30;
   val talk2_title: String = "Blabla";
   val talk2_len: Int = 30;

   var caseCounter: Int = 0;



   def testThings() = {

      val tr_m: Track = new Track (0);

      val se: Session = new Session (tr_m, Session.MORNING, 70, 20);
      val testTalk1: Talk = new Talk (talk1_title, talk1_len, se);
      val testTalk2: Talk = new Talk (talk2_title, talk2_len, se);

      assertEquals(talk1_title,testTalk1.title);
      assertEquals(talk1_len,  testTalk1.duration);

      //1
      se += testTalk1;
      assertEquals(30, se.actualLength);
      assertEquals(40, se.missingTimeToTarget);
      assertEquals(false, se.isSessionSatisfied);
      assertEquals(20, se.missingTimeToMinimum);
      assertEquals(-2, se.evaluateTalkLength());
      
      

      //2
      se += testTalk2;

      assertEquals(60, se.actualLength);
      assertEquals(10, se.missingTimeToTarget);
      assertEquals(true, se.isSessionSatisfied);
      assertEquals(-1, se.evaluateTalkLength());

      //3rd attempt
      se += testTalk2;

      se.removeRandomTalk;
      assertEquals(30, se.actualLength);


   }

   def assertEquals (s1: String, s2: String) {
      caseCounter+=1;
      print (caseCounter+ " ->");
      if (s1.equals(s2)) println ("OK");
      else {
        println ("NOK");
      }
   }
   def assertEquals (s1: Int, s2: Int) {
      caseCounter+=1;
      print (caseCounter+" ->");
      if (s1 ==s2) println ("OK");
      else {
        println ("NOK");
      }
   }

   def assertEquals (s1: Boolean, s2: Boolean) {
      caseCounter+=1;
      print (caseCounter+" ->");
      if (s1 == s2) println ("OK");
      else {
        println ("NOK");
      }
   }

}