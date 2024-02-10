package test.misc;


public class TimeoutThread extends Thread {

TimeoutThreadCallback callback;
int sleepSeconds;
Object[] args;
boolean aborted = false; 
boolean callbackFired = false; 
String threadName = null; 
  public TimeoutThread(TimeoutThreadCallback callback, int sleepSeconds, Object[] args) {
        this.callback =callback;
    this.sleepSeconds = sleepSeconds;
      this.args = args; 
      threadName = "TimeoutThread-"+callback.getClass().getName()+"-"+sleepSeconds;
      setName(threadName);

  }
  
  public void run() { 
      try { 
       long endMillis = System.currentTimeMillis() + sleepSeconds * 1000; 
      synchronized (this) {
        while ((!aborted) && System.currentTimeMillis() < endMillis) {
          wait(333);
        }
      }

      if (!aborted) {
          callback.doCallback(args);; 
          callbackFired = true; 
      }
      
       
    } catch (Exception e) {
      synchronized (System.out) {
        System.out.println("Exception caught by thread " + threadName);
        e.printStackTrace(System.out); 
      }
    }

  }
  
  public void abort() { 
    synchronized(this) { 
      aborted = true; 
      notify(); 
    }
  }

  public boolean getCallbackFired() { 
    return callbackFired; 
    
  }
}
