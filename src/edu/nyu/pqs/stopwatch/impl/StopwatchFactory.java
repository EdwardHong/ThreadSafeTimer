package edu.nyu.pqs.stopwatch.impl;

import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import edu.nyu.pqs.stopwatch.api.IStopwatch;

/**
 * The StopwatchFactory is a thread-safe factory class for IStopwatch objects.
 * It maintains references to all created IStopwatch objects and provides a
 * convenient method for getting a list of those objects.
 *@author Ssangwook Hong
 *@date 03/01/16
 */
public class StopwatchFactory {

//effective java p.273
  private static final ConcurrentMap<String, IStopwatch> stopWatches = new ConcurrentHashMap<String, IStopwatch>();
  private static Object lock = new Object();

	/**
	 * Creates and returns a new IStopwatch object
	 * @param id The identifier of the new object
	 * @return The new IStopwatch object
	 * @throws IllegalArgumentException if <code>id</code> is empty, null, or already
   *     taken.
	 */
	public static IStopwatch getStopwatch(String id) {
    if (id=="")
      throw new IllegalArgumentException("ID should not be empty! Cannot get stopwatch!");
    else if (id==null)
      throw new IllegalArgumentException("ID should not be null! Cannot get stopwatch!");
	  synchronized(lock) {
	    if (stopWatches.containsKey(id))
	      throw new IllegalArgumentException("A stopwatch with the same ID already exists!");
	    else {
	      IStopwatch stopwatch= Stopwatch.getStopwatch(id);
	      stopWatches.putIfAbsent(id,stopwatch);
	      return stopwatch;
	    }
	  }
	}

	/**
	 * Returns a list of all created stopwatches
	 * @return a List of al creates IStopwatch objects.  Returns an empty
	 * list if no IStopwatches have been created.
	 */
	public static List<IStopwatch> getStopwatches() {
		return new ArrayList<IStopwatch>(stopWatches.values());
	}
}
