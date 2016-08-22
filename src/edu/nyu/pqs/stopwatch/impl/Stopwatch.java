package edu.nyu.pqs.stopwatch.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import edu.nyu.pqs.stopwatch.api.IStopwatch;

/**
 * This is a thread-safe stop watch class that provides basic functionalities of a stopwatch.
 * Stopwatch can be started, stopped, resetted and measure lap times. 
 * These are implemented in a thread-safe manner so that different threads can function properly on one object.
 * @author Ssangwook Hong
 * @date 03/01/16
 *
 */
public class Stopwatch implements IStopwatch {
  private final List<Long> lapTimes;
  private final String id;
  private boolean isRunning;
  private long startTime;

  /**
   * private constructor that can only be called by static getStopwatch method.
   * @param id
   */
  private Stopwatch(String id) {
    this.id=id;
    isRunning = false;
    lapTimes = Collections.synchronizedList(new ArrayList<Long>());
  }
  /**
   * Static factory to allow a private constructor for an immutable class (effective java p.78)
   * @param id id of the stopwatch
   * @return Stopwatch object 
   * @throws IllegalArgumentException when ID is either empty or null. 
   */
  public static Stopwatch getStopwatch(String id) throws IllegalArgumentException{
    if (id=="")
      throw new IllegalArgumentException("ID is empty!");
    else if (id==null)
      throw new IllegalArgumentException("ID cannot be null!");
    return new Stopwatch(id);
  }
  
  /**
   * Retrieves the id of the stop watch.
   * @return id of the stopwatch
   */
  @Override
  public String getId() {
    return id;
  }
  
  /**
   * Starts the timewatch and set its status to running.
   * @throws IllegalStateException when stopwatch is already running.
   */
  @Override
  public void start() throws IllegalStateException {
    synchronized (this) {
      if (isRunning)
        throw new IllegalStateException("The stopwatch with id:" +id+"is already running!");
      startTime = System.currentTimeMillis();
      isRunning=true;
    }
  }
  
  /**
   * Records the lap time leaped since the last lap time.
   * @throws IllegalStateException when the stopwatch is not running.
   */
  @Override
  public void lap() throws IllegalStateException {
    long time;
    synchronized(this) {
      if (!isRunning)
        throw new IllegalStateException("The stopwatch with id:" +id+"is not running.");
      long endTime = System.nanoTime();
      time = endTime - startTime;
      startTime = endTime;
      lapTimes.add(time);
    }
  }
  /**
   * Stops the stopwatch and adds the final time to the list.
   * @throws IllegalStateException when the stopwatch is not running.
   */
  @Override
  public void stop() throws IllegalStateException {
    synchronized(this) {
      if (!isRunning)
        throw new IllegalStateException("The stopwatch with id:" +id+"is not running.");
      isRunning=false;
      lapTimes.add(System.nanoTime()-startTime); 
    }  
  }
  
  /**
   * Resets the stopwatch by stopping it if it is running and clearing all the lap times.
   */
  @Override
  public void reset() {
    synchronized(this) {
      if (isRunning) {
        isRunning=false;
      }
      lapTimes.clear();
    }
  }
  
  /**
   * Retrieves the lap time of the stopwatch.
   * @return List<Long> which consists of lap times measured by the stopwatch.
   */
  @Override
  public List<Long> getLapTimes() {
    synchronized (this) {
      ArrayList<Long> lapTimesCopy = new ArrayList<Long>(lapTimes);
      return lapTimesCopy;
    }
  }
  
  /**
   * Tests whether the two objects have the same reference, then check the instance,
   * and see if the fields of an object match with one another. (effective java p.42)
   * @param o an object that is being compared to.
   * @return boolean whether 
   */
  @Override
  public boolean equals(Object o) {
    if (o==this)
      return true;
    if (!(o instanceof Stopwatch))
      return false;
    
    Stopwatch anotherWatch= (Stopwatch) o;
    if (anotherWatch.getId().equals(this.getId())) 
      return true;
    else
      return false;
  }
  
  /**
   * Start with a constant which is multiplied by a constant and add an hashcode of the id to generate
   * a hashcode. (Effective Java p.47)
   * @return a hashcode that is shared by the same objects but vary across different objects.
   */
  @Override
  public int hashCode() {
    int result = 17;
    int c = id.hashCode();
    result = 31 * result + c;
    return result;
  }
  
  /**
   * 
   * @return output string 
   * Stopwatch id #___
   * x miliseconds
   * y miliseconds
   * z miliseconds
   * ...
   */
  public String toString() {
    String output="Stopwatch id #"+this.id+"\n";
    for (Long lapTime:lapTimes) {
      output += lapTime+" miliseconds"+"\n"; 
    }
    return output;
  } 
}
  
 

