package org.krobo.lips.pipeline;

import org.krobo.hips.kinematicbody.sptMotionState;

import org.opencv.core.*;
import org.opencv.video.Video;

/*
 * This class is designed to give the best approximation for physical quantities based on the 
 * data measured by the device's sensors.  In particular, the accelerometer sensor samples the true acceleration
 * with a sampling rate and some error.  The Data converter will take acceleration data and return the best
 * approximation for velocity and position.  
 * 
 * This could be accomplished using black-box Runge-Kutta techniques or using a method that models the motor
 * ramp up times factoring in how long it takes for an applied signal to achieve the acceleration/heading/speed
 * demanded.
 * 
 * The output can be calibrated against known grid-points in the 3D world, or openCV data which should allow
 * the tuning of parameters for the desired effects.
 * 
 * The ultimate goal is a better driving experience.  In the end, we need DataFilter and DataConverter to supply
 * the best possible information to determine the mapping between joystick commands and commands to the motor
 * issued by the KestenController.
 * 
 * DataConverter's job is to take an imperfect time-series of signal samples and map it to the real 3D position
 * velocity (motionState) values as accurately as possible.  It will be useful if it can compete with openCV's
 * ability to extract motionstate from video frames, since the sensor data is much lower bandwidth and less
 * APU intensive than openCV.
 */

public class DataConverter {
	
	public MatOfFloat getPosition() {
		MatOfFloat r = null;
		return r;
	}
	
	public MatOfFloat getVelocity() {
		MatOfFloat v = null;
		return v;
	}
	
	public MatOfFloat getAcceleration() {
		MatOfFloat a = null;
		return a;
	}
	
	public MatOfFloat getJerk() {
		MatOfFloat j = null;
		return j;
	}
	
	
	/*
	 * An way to arrive at the TNB expressions is to take the first three derivatives 
	 * of the curve r′(t), r′′(t), r′′′(t), and to apply the Gram-Schmidt process. 
	 * The resulting ordered orthonormal basis is precisely the TNB frame. 
	 * 
	 * If the curvature is always zero then the curve will be a straight line. 
	 * Here the vectors N, B and the torsion are not well defined.
	 * If the torsion is always zero then the curve will lie in a plane. 
	 * A circle of radius r has zero torsion and curvature equal to 1/r.
	 * A helix has constant curvature and constant torsion.
	 */
	public MatOfFloat getTangentVector() {
		MatOfFloat T = null;
		return T;
	}
	
	public MatOfFloat getNormalVector() {
		MatOfFloat N = null;
		return N;
	}
	
	public MatOfFloat getBinoramlVector() {
		MatOfFloat B = null;
		return B;
	}
	
	public int getTime() {
		int t = 1;
		return t;
	}
	
	/* MotionState is position, momentum and more complex motion attributes */
	//private MotionState				mMotionState;
	/* Camera can be mobile device, or other camera.  Model includes distortions calculated in calib3d */
	private sptMotionState				mMotionState;
	private Mat							mMotionHistoryImage;
	private Mat							mSilhouette;
	private Mat							mMask;
	
	/* logest time between frame captures */
	private float						mMaxDuration;
	/* read mTimeStamp_1 as mTimeStamp minus 1 (from previous capture) */
	private float						mTimeStamp_2;
	private float						mTimeStamp_1;
	private float						mTimeStamp;
	

	// private Robot.AccelerometerData		mRobotData;
	
	// Guess: these are the time deltas:  mDelta1 = 
	private float						mDelta1;
	private float						mDelta2;
	
	/* how good is the match to a sphere.  Sphero could be hidden or on a white surface or poorly lit 
	 * range is [0,1], 0 = no confidence, 1 = almost surely
	 */
	private float						mConfidenceFactor;
	
	/* do some filtering to eliminate noise */
	private void				FilterRawImage() {
		
	}
	
	/* fit a sphere to the image data */
	private void				ExtrapolateImage() {
		
	}
	
	private void				DetectObject() {
		
	}
	
	private void				UpdateMotionState() {
		Video.updateMotionHistory(mSilhouette, mMotionHistoryImage, mTimeStamp, mMaxDuration);
		Video.calcMotionGradient(mMotionHistoryImage, mMask, mMotionState.sptGetOrientation(), mDelta1, mDelta2);
	}
	
	public void					sptUpdate() {
		
		
	}
	
	public sptMotionState	sptGetMotionState() {
		return mMotionState;
		
	}

}
