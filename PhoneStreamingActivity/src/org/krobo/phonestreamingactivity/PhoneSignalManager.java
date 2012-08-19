package org.krobo.phonestreamingactivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.lang.IllegalArgumentException;
import java.lang.reflect.Array;

import org.apache.commons.collections.*; 
//import org.apache.commons.collections.CollectionUtils;

import org.krobo.lips.*;
import org.krobo.lips.core.*;
import org.krobo.lips.signal.*;


import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.Sensor;
import android.util.Log;
import android.widget.TextView;

public class PhoneSignalManager extends SignalManager<PhoneSignalManager.PhoneSensor, SensorManager> {

	
	/*
	 * These are named to match the Android API, but here they are enums, there ints.
	 */
	public enum PhoneSensor implements HasSize { TYPE_ACCELEROMETER(3, Sensor.TYPE_ACCELEROMETER), 
												 TYPE_LINEAR_ACCELERATION(3, Sensor.TYPE_LINEAR_ACCELERATION),
												 TYPE_ROTATION_VECTOR(3, Sensor.TYPE_ROTATION_VECTOR),
												 TYPE_GYROSCOPE(3, Sensor.TYPE_GYROSCOPE),
												 TYPE_GRAVITY(3, Sensor.TYPE_GRAVITY),
												 TYPE_MAGNETIC_FIELD(3, Sensor.TYPE_MAGNETIC_FIELD),
												 TYPE_AMBIENT_TEMPERATURE(1, Sensor.TYPE_AMBIENT_TEMPERATURE),
												 TYPE_LIGHT(1, Sensor.TYPE_LIGHT),
												 TYPE_PRESSURE(1, Sensor.TYPE_PRESSURE),
												 TYPE_RELATIVE_HUMIDITY(1, Sensor.TYPE_RELATIVE_HUMIDITY),
												 TYPE_PROXIMITY(1, Sensor.TYPE_PROXIMITY);
												 
												 
		
		/*
		 * 
		 */
		private int mType;
		private int mSize;
		PhoneSensor(int size, int type) {
			mSize = size;
			mType = type;
		}
		public int getSize() {
			return mSize;
		}
		/*
		 * May want to encapsulate this.
		 * @return bitmask field/id for sensor
		 */
		public int getType() {
			return mType;
		}
	}
	/*
	 * Rate at which sensor events are sampled.  Still haven't found out how to set this.
	 * SensorEventListner is promising
	 */
	int					mRate = SensorManager.SENSOR_DELAY_FASTEST;
	/*
	 * Android API sensor manager
	 */
	private SensorManager 		mSensorManager;
	public List<Sensor> 		mAvailableSensorList;
	private  List<Sensor> 		mActiveSensorList;
	private String 				TAG = "PhoneSignalManager";

	
	public PhoneSignalManager(SensorManager sm, int rate) {
				mSensorManager = sm;
				setRate(rate);
        	}
	
	public PhoneSignalManager(SensorManager sensorManager) {
		mSensorManager = sensorManager;
	}

	public void setRate(int rateType) {
		if(		rateType == SensorManager.SENSOR_DELAY_FASTEST ||
				rateType == SensorManager.SENSOR_DELAY_GAME ||
				rateType == SensorManager.SENSOR_DELAY_NORMAL ||
				rateType == SensorManager.SENSOR_DELAY_UI)
			mRate = rateType;
		else
			throw new IllegalArgumentException("rate " + rateType + "must be a SensorManager constant");
		
		//TODO set the value in SensorManager or SensorEventListener
	}
	@Override
	protected EnumSet<PhoneSensor> discoverAvailableSensors()  {
		
		mAvailableSensorList = mSensorManager.getSensorList(Sensor.TYPE_ALL);
	    Log.i(TAG, "Available Phone Sensors: " + mAvailableSensorList.toString());
	    final EnumSet<PhoneSensor> allSensors = EnumSet.allOf(PhoneSensor.class);
	    // check if getType()s match for Sensor and Enum
	    // 
	    @SuppressWarnings({ "unchecked", "rawtypes" })
		EnumSet<PhoneSensor> availablePhoneSensorTypes = 
	    		(EnumSet<PhoneSensor>) CollectionUtils.collect(
	    				mAvailableSensorList, new Transformer() 
	    		{
	    			public Object transform(Object o) {
	    				for (PhoneSensor s : allSensors) {
	    					if(s.getType() == ((PhoneSensor) o).getType() )
	    						return s;
	    				}	 
	    				try {
							throw new Exception("Phone Sensor " + o + " discovered but not know to signal manager");
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	    				return null;
	    		}});
	    	return availablePhoneSensorTypes;
	    }

	@Override
	protected void update(	SensorManager sensorManager,
							SensorDataQueue<PhoneSensor> dataQueue) {
		for (IndexedSensor<PhoneSensor> s : (List<IndexedSensor<PhoneSensor>>) dataQueue.getIndexedSensorList()) {
			if (s.mSensor.mSize == 3) {
				//dataQueue.mCurrentRecordAry[s.mFirst]= sensorManager.getDefaultSensor(s.mSensor.mType).;
			}
		}
		
	}

	@Override
	public int getSampleRate() {
		// TODO Auto-generated method stub
		return mRate;
	}

	@Override
	public Long getLastTimeStamp() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long[] getTimes() {
		// TODO Auto-generated method stub
		return null;
	}

	public SensorManager getSensorManager() {
		// TODO Auto-generated method stub
		return mSensorManager;
	}

	public void updateSensor(SensorEvent event) {
	
	}

}
