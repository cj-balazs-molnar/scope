//import org.junit.Test;
//import org.junit.Assert.assertEquals;

/* Poor man's unit test */

public class TrackerTest {

   String talk1_title = "Better way of reading books";
   int talk1_len = 30;
   String talk2_title = "Blabla";
   int talk2_len = 30;

   int caseCounter = 0;


    public static void main(String [] args) {
        System.out.println ("WorkshopTracker unit test");
        TrackerTest tt = new TrackerTest();
        tt.testThings();
    }

   public void testThings() {
      Talk testTalk1 = new Talk (talk1_title, talk1_len);
      Talk testTalk2 = new Talk (talk2_title, talk2_len);

      Track tr_m = new Track (0);

      Session se = new Session (tr_m, Session.MORNING, 70, 20);

      assertEquals(talk1_title,testTalk1.getTitle());
      assertEquals(talk1_len,  testTalk1.getDuration());

      //1
      assertEquals(true, se.insertTalk (testTalk1));
      assertEquals(30, se.getActualLength());
      assertEquals(40, se.getMissingTimeToTarget());
      assertEquals(false, se.isSessionSatisfied());
      assertEquals(20, se.getMissingTimeToMinimum());
      assertEquals(-2, se.evaluateTalkLength());

      //2
      assertEquals(true, se.insertTalk (testTalk2));

      assertEquals(60, se.getActualLength());
      assertEquals(10, se.getMissingTimeToTarget());
      assertEquals(true, se.isSessionSatisfied());
      assertEquals(-1, se.evaluateTalkLength());

      //3rd attempt
      assertEquals(false, se.insertTalk (testTalk2));

      se.removeRandomTalk ();
      assertEquals(30, se.getActualLength());


   }

   private void assertEquals (String s1, String s2) {
      caseCounter++;
      System.out.print (caseCounter+ " ->");
      if (s1.equals(s2)) System.out.println ("OK");
      else {
        System.out.println ("NOK");
      }
   }
   private void assertEquals (int s1, int s2) {
      caseCounter++;
      System.out.print (caseCounter+" ->");
      if (s1 ==s2) System.out.println ("OK");
      else {
        System.out.println ("NOK");
      }
   }

   private void assertEquals (boolean s1, boolean s2) {
      caseCounter++;
      System.out.print (caseCounter+" ->");
      if (s1 ==s2) System.out.println ("OK");
      else {
        System.out.println ("NOK");
      }
   }

}