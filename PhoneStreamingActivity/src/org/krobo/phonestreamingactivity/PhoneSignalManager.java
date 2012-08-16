package org.krobo.phonestreamingactivity;

import java.util.EnumSet;
import java.util.List;

import org.krobo.lips.*;
import org.krobo.lips.core.*;
import org.krobo.lips.signal.*;



import android.hardware.Sensor;
import android.hardware.SensorManager;

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
	 */
	int					mRate;
	SensorManager 		mSensorManager;
	
	public PhoneSignalManager(int rate, SensorManager sensorManager) {
		if(		rate == SensorManager.SENSOR_DELAY_FASTEST ||
				rate == SensorManager.SENSOR_DELAY_GAME ||
				rate == SensorManager.SENSOR_DELAY_NORMAL ||
				rate == SensorManager.SENSOR_DELAY_UI)
			mRate = rate;
	}
	@Override
	protected EnumSet<PhoneSensor> discoverAvailableSensors() {
		// TODO Auto-generated method stub
		return null;
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

}
