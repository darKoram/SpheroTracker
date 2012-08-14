package com.orbotix.streamingexample;

import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.buffer.CircularFifoBuffer;

import org.krobo.lips.core.HasSize;
import org.krobo.lips.signal.IndexedSensor;
import org.krobo.lips.signal.SensorDataQueue;
import org.krobo.lips.signal.SignalManager;

import android.util.Log;

import orbotix.robot.base.SetDataStreamingCommand;
import orbotix.robot.sensor.DeviceSensorsData;

public class SpheroSignalManager extends SignalManager<SpheroSignalManager.SpheroSensor,
														DeviceSensorsData> {

	
	public enum SpheroSensor implements HasSize { 	ACCELEROMETER(3), 
										   			ATTITUDE(3), 
									   				GYRO(3), 
									   				LEFT_BACKEMF(1), 
									   				RIGHT_BACKEMF(1);
		private int mSize;
		SpheroSensor(int size) {
			mSize = size;
		}
		public int getSize() {
			return mSize;
		}
	}
	/* Immutables */
	
	/**
	 * the mask determines what data the robot will send us
	 */
	private final long mMask;

	/**
	 *  frequency in hz of robot data sampling	
	 */
	private final int mBaseFrequency = 400; 
	/**
	 *  decimation factor 
	 */
	private final int mDivisor;
	
    /*For communication with Robot: Specify the number of frames that will be in each response. 
	*You can use a higher number to "save up" responses and send them at once with a lower frequency, 
	* but more packets per response.
	*/
	final int mPacketFrames;
	
    //Total number of responses before streaming ends. 0 is infinite.
    final int mResponseCount;

	
	/*
	 * Constructor
	 * @param mask 				the mask determines what data the robot will send us
	 * @param divisor 			Sphero can issue data at 400 hz.  Divisor reduces this to the 
	 * 							return of getDataFrequency()
	 * @param packetFrames  	You can use a higher number to "save up" responses and send them at 
	 * 							once with a lower frequency, but more packets per response.
	 * @param responseCount 	Total number of responses before streaming ends. 0 is infinite.
	 * @param activeSensorList	EnumSet<SpheroSensor> which will stream data 
	 * 
	 */
	public SpheroSignalManager(	int divisor, 
								int packetFrames,
								int responseCount,
								EnumSet<SpheroSensor> activeSensorList ) {
		
		mDivisor = divisor;
		mPacketFrames = packetFrames;
		mResponseCount = responseCount;
		mMask = addSensorsToMask(activeSensorList);
		mActiveSensors = activeSensorList;
		mAvailableSensors = discoverAvailableSensors();
	}
	
	/** 
	 * For bitmasking newbies, x |= y is same as x = x | y
	 * @return mask to pick data stream from robot
	 *
	 * Constants file: Sphero-Android-SDK/api_doc/RobotLibrary Doc/constant-values.html
	 * already checked in addSensor that sensor is not in mSensorList (or mask) already.
	*/
	private long addSensorsToMask(EnumSet<SpheroSensor> sensorList) {
		long mask = 0;
		for (SpheroSensor s: sensorList) {
			switch(s) {
				case ACCELEROMETER:
					mask |= SetDataStreamingCommand.DATA_STREAMING_MASK_ACCELEROMETER_X_FILTERED;
					mask |= SetDataStreamingCommand.DATA_STREAMING_MASK_ACCELEROMETER_Y_FILTERED;
					mask |= SetDataStreamingCommand.DATA_STREAMING_MASK_ACCELEROMETER_Z_FILTERED;
				case ATTITUDE:
					mask |= SetDataStreamingCommand.DATA_STREAMING_MASK_IMU_PITCH_ANGLE_FILTERED;
					mask |= SetDataStreamingCommand.DATA_STREAMING_MASK_IMU_ROLL_ANGLE_FILTERED;
					mask |= SetDataStreamingCommand.DATA_STREAMING_MASK_IMU_YAW_ANGLE_FILTERED;
				case GYRO:
					mask |= SetDataStreamingCommand.DATA_STREAMING_MASK_GYRO_X_FILTERED;
					mask |= SetDataStreamingCommand.DATA_STREAMING_MASK_GYRO_Y_FILTERED;
					mask |= SetDataStreamingCommand.DATA_STREAMING_MASK_GYRO_Z_FILTERED;
				case LEFT_BACKEMF:
					mask |= SetDataStreamingCommand.DATA_STREAMING_MASK_LEFT_MOTOR_BACK_EMF_FILTERED;
				case RIGHT_BACKEMF:
					mask |= SetDataStreamingCommand.DATA_STREAMING_MASK_RIGHT_MOTOR_BACK_EMF_FILTERED;
					default:
						throw new AssertionError("Unknown coin: " + s);

				}
		}
		return mask;
	}
	
	/*
	 * For a generic device, this might be more interesting
	 * @Override
	 */
	protected EnumSet<SpheroSensor> discoverAvailableSensors() {
		return EnumSet.of(	SpheroSensor.ACCELEROMETER,
							SpheroSensor.ATTITUDE,
							SpheroSensor.GYRO,
							SpheroSensor.LEFT_BACKEMF,
							SpheroSensor.RIGHT_BACKEMF);
	}
	
	/*
	 * Converts Sphero format DeviceSensorsData to DataQueue
	 *@param dataRecord DeviceSensorsData is a Sphero data container with sensor data
	 *@param dataQueue  dataQueue receives new data record and places it in queue (openCV CircularMat)
	 *@Override
	 */
	public void update(	DeviceSensorsData dataRecord, 
						SensorDataQueue<SpheroSignalManager.SpheroSensor> dataQueue) {
		if (dataRecord == null)
			return;
		// these new's will be deleted when removed from the mDataQueue
		for (IndexedSensor<SpheroSensor> s : (List<IndexedSensor<SpheroSensor>>) dataQueue.getIndexedSensorList()) {
			switch (s.mSensor) {
			case ACCELEROMETER:
				dataQueue.mCurrentRecordAry[s.mFirst]= dataRecord.getAccelerometerData().getFilteredAcceleration().x;
				dataQueue.mCurrentRecordAry[s.mFirst+1] = dataRecord.getAccelerometerData().getFilteredAcceleration().y;
				dataQueue.mCurrentRecordAry[s.mFirst+2] = dataRecord.getAccelerometerData().getFilteredAcceleration().z;				
			case ATTITUDE:
				dataQueue.mCurrentRecordAry[s.mFirst] = dataRecord.getAttitudeData().getAttitudeSensor().yaw;
				dataQueue.mCurrentRecordAry[s.mFirst+1] = dataRecord.getAttitudeData().getAttitudeSensor().pitch;
				dataQueue.mCurrentRecordAry[s.mFirst+2] = dataRecord.getAttitudeData().getAttitudeSensor().roll;				
			case GYRO:
				dataQueue.mCurrentRecordAry[s.mFirst] = dataRecord.getGyroData().getRotationRateFiltered().x;
				dataQueue.mCurrentRecordAry[s.mFirst+1] = dataRecord.getGyroData().getRotationRateFiltered().y;
				dataQueue.mCurrentRecordAry[s.mFirst+2] = dataRecord.getGyroData().getRotationRateFiltered().z;				
			case LEFT_BACKEMF:
				dataQueue.mCurrentRecordAry[s.mFirst] = dataRecord.getBackEMFData().getEMFFiltered().leftMotorValue;				
			case RIGHT_BACKEMF:
				dataQueue.mCurrentRecordAry[s.mFirst] = dataRecord.getBackEMFData().getEMFFiltered().leftMotorValue;				
			default:
				break;
			}		
		}
		
		// add the time-stamp.  Strange, the Robot API only has getTimeStamp for AccelerometerData
		// so if we don't stream accelerometer data, what happens?  Where else can we get timestamp?
		// Posted on forum.  This is an error.  Will be added to SensorData in future SDK's
		if (dataQueue.getIndexedSensorList().contains(SpheroSensor.ACCELEROMETER)) {

			mCurrentTime = dataRecord.getAccelerometerData().getTimeStamp();
			mTimes.add(mCurrentTime);

		}
					else
			Log.d("SPHERO_TRACKER", "SpheroSignalManager: AsyncDataManager getAccelerometerData() failed.  No timestamp.");
	}  
	/* Getters and Setters: these values are exposed so that a test program can tune the crucial values */
	
	/*
	 * @return sample rate if fixed, or average for framebuffer if variable
	 */
	public int getSampleRate() {
		//TODO check what actually happens with divisor.  Probably a rounded int for data rate.  Close enough for now.
		return mBaseFrequency/mDivisor;
	}
	
	/**
	 * Defensive copying http://www.javapractices.com/topic/TopicAction.do?Id=15
	 * @returns number of milliseconds (since sphero start streaming command?) 
	 **/
	public Long getLastTimeStamp() {
		return mCurrentTime;
	}
	
	/*
	 * Get the Nyquist frequency (2x highest frequency in signal) from the DataFilter.  
	 * If sample rate < Nyquist frequency, need to increase sample rate. 
	 * Nominal because we are educated-guessing this for our use-case
	 * @return Nyquist Frequency we have estimated for filtering
	 */
	public float getNominalNyquistFreuency() {
		// TODO complete this stub
		return 1.0f;
	}

	/*
	 * Set the Nyquist Frequency for filtering
	 */
	public void setNominalNyquistFrequency() {
		
	}
	
	/*
	 * TODO complete stub
	 * Returns actual measured frequency from DataFilter.  May be able to reduce the 
	 * NominalNyquistFrequency if our bandwidth (2*highest freq component) is significantly below it.
	 * @return the best estimate for nyquist freq as determined by data pipeline
	 */
	public float getMeasuredNyquistFrequency() {
		return 1.0f;
	}
	/*
	 * Since mTimes is a circular buffer, it may not be as simple as toArray(). should check this.
	 * @return an array with the first element being the least recently added time.
	 */
	public Long[] getTimes() {
		Long[] times = new Long[mTimes.size()];
		Iterator<Long> iter = mTimes.iterator();
		int i=0;
		while(iter.hasNext()) {
			times[i] = iter.next();
			i++;
		}
		return times;
	}


}
