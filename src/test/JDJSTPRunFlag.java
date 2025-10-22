package test;

public class JDJSTPRunFlag {

  boolean running = false; 
  synchronized public void setRunning() {
    running = true; 
  }
  synchronized public void setDone() {
    running = false; 
  }
  synchronized public boolean isRunning() {
    return running; 
  }

}
