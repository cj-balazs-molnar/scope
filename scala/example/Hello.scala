package example




object Hello {
  
  def main(args: Array[String]) = {
     def tt: TrackerTest = new TrackerTest();
     tt.testThings();
     
     def tm: TrackerMain = new TrackerMain ();
     tm.exec;
     
  }
  
}


