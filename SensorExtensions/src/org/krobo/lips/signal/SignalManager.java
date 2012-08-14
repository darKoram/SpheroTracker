package org.krobo.lips.signal;

import java.util.EnumSet;

import org.apache.commons.collections.buffer.CircularFifoBuffer;
import org.krobo.lips.core.HasSize;


public abstract class  SignalManager<E extends Enum<E> & HasSize, SensorDataObject> {

	
    protected EnumSet<E> mAvailableSensors;
	protected EnumSet<E> mActiveSensors;
	
	/*
	 * If sampling is done at a variable rate, the times belong in the SensorDataQueue, however,
	 * since it is not the same data type (int vs float) it needs to be stored apart from the Mat
	 * If the sampling is fixed rate, mTimes serves only to monitor that there are no gaps in the 
	 * signal due to IO or other resource dependent issues so it's good to have here.  We only want
	 * to send good continuous blocks of data to our pipeline.  This CircularFifoBuffer is from 
	 * Apache Commons
	 */
	protected CircularFifoBuffer<Long> mTimes;
	/*
	 * Oddly, the circular buffer has no method to access the most recent item added, so
	 * we will store it as we add it.
	 */
	protected Long mCurrentTime;

	protected abstract EnumSet<E> discoverAvailableSensors();
	
	/*
	 * update is responsible for updating both the sensorDataQueue and the cirularFifoBuffer if
	 * monitoring the timestamps is of interest. 
	 */
	protected abstract void update(SensorDataObject sensorDataObject, SensorDataQueue<E> sensorDataQueue);

	public abstract int getSampleRate();
	
	public abstract Long getLastTimeStamp();
	
	public abstract Long[] getTimes(); 
	
	
}
