package org.krobo.lips.signal;



import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import org.krobo.lips.core.DataQueue;
import org.krobo.lips.core.*;

/*
 * SensorDataQueue adds indexedSensors which acts as the header to the dataRecord.
 * Also allows access to data based on sensor name.
 * Fancy template extension allows us to take in an EnumSet of any enum type which
 * implements getSize() of the HasSize interface.
 * <E extends Enum<E> & HasSize> should be read as "E extendes Enum and implements HasSize
 */
public  class SensorDataQueue<E extends Enum<E> & HasSize> extends DataQueue {

	
	private final List<IndexedSensor<E>> mSensorList;
	
	/**
	 * Constructor
	 * 
	 * @param maxRecordsInQueue
	 * @param sensorList
	 * usage:
	 * {@code  
	 * List<DataFilter> sensorList; 
	 * sensorList.add(Sensor.ACCELEROMETER);
	 * }
	 * Sensor { ACCELEROMETER, ATTITUDE, GYRO, RIGHT_BACKEMF, LEFT_BACKEMF }
	 */
	public  SensorDataQueue(int queueLength,  
							int windowLength,
							EnumSet<E> sensorList) {
		super(queueLength, windowLength);
		super.setRecordLength( calculateRecordLength(sensorList) );
		mSensorList = addSensors(sensorList);
	}
	

	/*
	 * Keep it general so others can define new Devices and Sensor enum types
	 * Add a simple enumerated sensor name and create a List<IndexedSensor>
	 * Enum must implement HasSize to set the correct number of fields in the data record.
	 * e.g. mySensor.ACCELEROMETER.getSize() -> 3 while mySensor.TEMPERATURE.getSize() -> 1
	 */
	private List<IndexedSensor<E>> addSensors(EnumSet<E> sensorList) {
		
		List<IndexedSensor<E>> indexedList = new ArrayList<IndexedSensor<E>>(sensorList.size());

		int lastIndex = 0;
		for(E s : sensorList) {
			if (indexedList.size() > 0)
				lastIndex = ( indexedList.get(indexedList.size()-1) ).mLast;
			IndexedSensor<E> newSensor = new IndexedSensor<E>();
			newSensor.mFirst = lastIndex+1;
			newSensor.mLast = lastIndex+s.getSize();
			indexedList.add(newSensor);		
		}
		return indexedList;
	}

	/**
	 * Ideally, we would get Sphero's data from the AsyncData stream buffer and keep it in serialized form.
	 * For now, we build the serialized array using the API's accessor methods.  
	 */
	
	public int calculateRecordLength(EnumSet<E> sensorList) {
		int sum = 0;
		for(E s : sensorList) {
			sum += s.getSize();
		}
		return sum;
	}

	/*
	 * Shallow (defensive) copy of mSensorList is returned
	 * Probably want to get rid of these for encapsulation
	 */
	public List<IndexedSensor<E>> getIndexedSensorList() {
		List<IndexedSensor<E>> sensorList = new ArrayList<IndexedSensor<E>>(mSensorList.size());
		sensorList.addAll(mSensorList);
		return sensorList;
	}
	
	public IndexedSensor<E> getIndexedSensor(E sensor) {
		for (IndexedSensor<E> e : mSensorList) {
			if(sensor == e.mSensor)
				return e;
		}
		throw new IllegalArgumentException("Sensor " + sensor + " not found");
	}
	
	/*@param sensor from EnumSet PhoneSensor or SpheroSensor or other user defined EnumSet of sensors
	 *@return double[3] if mSize = 3, double[1] if mSize = 1, so for the latter must use 
	 * getRawSensorData(sensor)[0] to get a double
	 *@throws IllegalArgumentException if sensor is not found
	 *
	 * TODO allow return type to be selected as appropriate CvType
	 */
	public double[] getRawSensorData(E sensor) {
		IndexedSensor<E> indexedSensor = getIndexedSensor(sensor);
		double[] data = new double[indexedSensor.mLast - indexedSensor.mFirst + 1];
		for (int i=indexedSensor.mFirst; i<indexedSensor.mLast+1; i++)
		{
			data[i-indexedSensor.mFirst] = mDataGpuBuffer.get(mWindowFront-1, i)[0];
		}
		return data;
	}
}
