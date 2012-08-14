package org.krobo.lips.core;


import java.util.List;

import org.opencv.core.*;

/*
 * This is a circular buffer style array where the indices used for adding data and performing SDFT are 
 * set within a window moving with respect to the fixed larger frame.  The purpose is to minimize the amount 
 * of copying required during the SDFT and to have a useful data image to be used in gesture recognition
 * when the mDataGpuBuffer is filled.
 */
public class DataQueue {

	private int  		mQueueLength = 0;   
	private int  		mRecordLength = 0; 
	private int 		mWindowLength = 0;
	protected int 		mWindowFront = 0;  
	protected int 		mWindowBack = 0; 

	public Mat 			mDataGpuBuffer;
	public Mat 			mDataWindow;
	
	// Header pointing to mDataGpuBuffer.col(mWindowFront-1)
	private MatOfDouble mCurrentRecordMat;
	
	/*
	 *  array of double of length mRecordLength used to set mCurrentRecordMat (and
	 *  therefore mDataGpuBuffer)
	 *  Usage: caller can fill current record from various sources 
	 *  Example: DataQueue A; A.mCurrentRecordAry[0] = 1;
	 */
	
	public double[]  	mCurrentRecordAry;

	// MatOfDouble.toList() convenience function does an extra copy 
	// from double[] of toArray() to a list.  Stick with toArray()
	// public List<Double> mCurrentRecordList;
	
	
	/*
	 * Constructors
	 * 
	 * Lazy mLengthOfQueue assignment for SensorDataQueue constructor.
	 * Need to assign this after we calculate length of mSensorList
	 */
	public DataQueue(int queueLength, int windowLength) {
		
		if( !(windowLength > 0 && windowLength*10 < queueLength) ) {
			throw new IllegalArgumentException("windowLength must be < 1/10th " +
					"queueLength. windowLength: " + windowLength + "queueLength: " + queueLength);
		}
		mQueueLength = queueLength;
		mWindowLength = windowLength;
		// mRecordLength and Matrix creation will take place in setRecordLength()

	}
	
	/*
	 * Constructor-like, lazy initialization of DataRecord called from subclass which needs to 
	 * calculate recordLength (from a sensor list for example).
	 */
	public void setRecordLength(int recordLength) {
		mRecordLength = recordLength;
		/*
		 * TODO check that garbage collection handles this correctly.  
		 * Do we need explicit release of mDataGpuBuffer and mCurrentRecordMat?
		 */
		mDataGpuBuffer = new Mat(mRecordLength, mQueueLength, CvType.CV_64FC1);
		/*
		 * mCurrentRecordMat is a 1xn reference into mDataGpuBuffer indexed by the moving window.
		 * It is private and should be defensive copied to export from DataQueueu.
		 */
		mCurrentRecordMat = new MatOfDouble(mDataGpuBuffer.col(0));
		mCurrentRecordAry = new double[mRecordLength];

		mWindowFront = mWindowLength;
		/*
		 * startrow – An inclusive 0-based start index of the row span.
		 * endrow – An exclusive 0-based ending index of the row span.
		 */
		mDataWindow = mDataGpuBuffer.rowRange(mWindowBack, mWindowFront);
	}

	/*
	 * @param recordLength length of data vector to be queued
	 * @param queueLength  number of data vectors in queue
	 * @param windowLength length of window for filtering
	 */
	public DataQueue(int recordLength, int queueLength, int windowLength) {
	
		if( !(windowLength > 0 && windowLength*10 < queueLength) ) {
			throw new IllegalArgumentException("windowLength must be < 1/10th " +
					"queueLength. windowLength: " + windowLength + "queueLength: " + queueLength);
		}
		mQueueLength = queueLength;
		mRecordLength = recordLength;
		mWindowLength = windowLength;
		
		mDataGpuBuffer = new Mat(mRecordLength, mQueueLength, CvType.CV_64FC1);
		/*
		 * startrow – An inclusive 0-based start index of the row span.
		 * endrow – An exclusive 0-based ending index of the row span.
		 */
		mWindowFront = windowLength;
		mDataWindow = mDataGpuBuffer.rowRange(mWindowBack, mWindowFront);
	}
	
	/*
	 * Default type for Sphero is double
	 * @param cvType should be CvType.64_FC1 or similar.  See opencv online documentation for options.
	 */
	public DataQueue(int recordLength, int queueLength, int windowLength, int cVtype)
	{
		if( !(windowLength > 0 && windowLength*10 < queueLength) ) {
				throw new IllegalArgumentException("windowLength must be < 1/10th " +
						"queueLength. windowLength: " + windowLength + "queueLength: " + queueLength);
		}
		// TODO also check values are greater than zero, less than max possible or change int to CvType.Size or Size
		mRecordLength = recordLength;
		mWindowLength = windowLength;
		mQueueLength = queueLength;
		
		mDataGpuBuffer = new Mat(mRecordLength, mQueueLength, cVtype);
		/*
		 * startrow – An inclusive 0-based start index of the row span.
		 * endrow – An exclusive 0-based ending index of the row span.
		 */
		mWindowFront = windowLength;
		mDataWindow = mDataGpuBuffer.rowRange(mWindowBack, mWindowFront);
	}
	/*
	 * TODO Want this parameterized.  Right now Eclipse complains.  
	 * Also, seems that 2.4.2 replaced at() with put() ?
	 * public <T> void setDataRecord (int colIndex, <T> value) {
	 * 	   mDataGpuBuffer.at<T>(mWindowFront, colIndex, value);
	 * }
	 * Call this after setting mCurrentRecordAry to add it to mDataGpuBuffer via mCurrentRecordMat
	 * Use mCurrentRecordAry as buffer to avoid mRecordLength calls to JNI in for loop with single column update
	 */
	public void addRecord (double[] data) {
		if(checkRecord(data)) {
			for (int i=0; i<mRecordLength; i++) {
				mCurrentRecordAry[i] = data[i];
				mCurrentRecordMat.put(0, 0, mCurrentRecordAry);
			}

		}
	}
	
	public void addRecord(List<Double> data) {
		if(checkRecord(data)) {
			for (int i=0; i<mRecordLength; i++) {
				mCurrentRecordAry[i] = data.get(i);
				mCurrentRecordMat.put(0, 0, mCurrentRecordAry);
			}
		}
	
	}
	public boolean checkRecord(double[] data) {
		if(data.length != mRecordLength) {
			throw new IllegalArgumentException("Data input must be of length " + mRecordLength);
		}
		else 
			return true;
	}

	public boolean checkRecord(List<Double> data) {
		if(data.size() != mRecordLength) {
			throw new IllegalArgumentException("Data input must be of length " + mRecordLength);
		}
		else 
			return true;
	}
	
	
	public void wrapWindowToStart() {
		// Copy mDataWindow to front of mDataGpuBuffer
		/*
		 * so that the destination matrix is reallocated if needed. 
		 * While m.copyTo(m); works flawlessly, the function does not handle the case of a partial overlap 
		 * between the source and the destination matrices.mDataGpuBuffer.copyTo()
		 * Should rename this method copyToSelf(int row, int col, double data) and put in OpenCV
		*/ 
		// TODO send mGpuDataBuffer to processing and logging
		// TODO wrap frequency domain and filter coefficients
		// TODOD "JNI calls are really expensive.  Use setTo instead of put." 
		// http://opencv.org/platforms/android/android-best-practices.html
		for (int i=mWindowBack; i<mWindowFront; i++) {
			for (int j=0; j<mRecordLength; j++) {
				mDataGpuBuffer.put(i-mRecordLength + mWindowLength, j, mDataGpuBuffer.get(i, j));
			}
		}
		mWindowBack = 0;
		mWindowFront = mWindowLength;
		mDataWindow = mDataGpuBuffer.rowRange(mWindowBack, mWindowFront);
		
	}
	
	/*
	 * 0(1) reference copy of current column of data
	 */
	public MatOfDouble getCurrentRecordMat() {
		mDataGpuBuffer.col(mWindowFront-1).copyTo(mCurrentRecordMat);
		return mCurrentRecordMat;
	}

	/*
	 * TODO Check that this is defensive
	 * @return an array of doubles of length mRecordLength defensively copied
	 */
	public double[] getCurrentRecordAry() {
		return mCurrentRecordMat.toArray();
	}
	
	public List<Double> getCurrentRecordList() {
		return mCurrentRecordMat.toList();
	}
	
	public int getRecordLength() {
		return mRecordLength;
	}
}


