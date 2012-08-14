package org.krobo.lips.pipeline;


/*
 * For now, DataFilter will handle two distinct roles.  In the Data Pipeline context it applies the appropriate filter
 * to the raw data and stores FFT and IFFT info.  In the Device context (Robot or Phone) it selects the parameters
 * that determine the data coming from the device.  On the Robot, this means setting the bit fields of a mask.
 * On the Phone, this means registering listeners with SensorManager.  Conceptually, the second context might be
 * better placed in SensorManager and AsyncDataListner as a SensorManagerFilterConfig helper class, but since
 * AsyncDataManager is closed source and changes to the filter in the Device data context is tightly coupled to 
 * the DataFilter (data rate, gap management, range, resolution), especially if we want to do adaptive sampling. 
 * 
 */

/*Depending on how good the robot's signal processing chip is, this may be very basic, or complex
 * If the sphero DSP is already good, just set the sample rate and set the low pass filter values.
 * Taking a sample of one out of every n is cheapest, but may most important events like collisions.
 * Decimating values (averaging every n) will be better, but watch out for noise.
 * Finally, sophisticated signal processing could be done here if necessary.
 * 
 * DataFilter can set filtering properties in the Robot API or do filtering after it has been streamed to a device.
 * 
 * See http://docs.gosphero.com/ios/robot_kit/interface_r_k_accelerometer_filter.html for example (deprecated?)
 * Ideally, this would be a python dictionary with key, value pairs.  We will do for now with an enum of data names.
 *
 * The design of this class is made difficult by the fact that DeviceSensorsData has only high level accessor functions
 * to the data we want.  To do serious signal processing, we'd like a simple data format with a header which is what sphero 
 * streams, but not what DeviceSensorsData exposes.  Robot has SensorsFilter, but it's closed source and undocumented
 * so we don't know if what id does is sufficient (there are methods to set low and high pass filter cutoffs).  
 * It would be nice to have access to the raw data and a SensorsFilter interface for custom filtering purposes.
 * 
 * Ideally, the data structure would be a circularly linked list of doubles with a fast look  up table for indices
 * per sensor type.  And the pipeline classes would not hold copies to the data, they would just take in the 
 * pipeline efficient data structure as mutable input like openCV does with MAT matrices.
 */ 

 /* One possible use of the filter is as follows.  The data queue can be thought of as an n x m matrix where m
  * are the time-series of samples of n degrees of freedom streaming from the sensors (not all independent).
  * We will assume that OpenCV will give much better results for acceleration -> velocity -> position, than
  * the sphero accelerometer can.  However, we may be able to "tune" the filter for the accelerometer data 
  * during calibration against OpenCV data.  It may be that accelerometer x,y,z need simple scale factors to
  * fit the OpenCV data.  It's possible that the x,y,z data needs to be rotated slightly for a good fit.  
  * Hopefully, an affine transformation will be able to give good results.  Hopefully, the scale factors will
  * not depend on the magnitude of the acceleration (linearlity).  If a good filter can be found, then 
  * we would no longer need openCV to supply the motionState information, we could get it with a lot less demand on 
  * the devices compute resources than OpenCV requires.
  * 
  * The Math:
  * 
  *    Filter      DataQueue     OpenCV         When Filter is known, then correct sphero data
  * _ _ _ n _ _ _  _ _ m _ _     _ _ m _ _                   _1_      __
  * |            | |        |   |         |                 |Raw|    |  |
  * |            | |        |   |         |                 |   |    |  |  
  * n            | n        | = n         |      Filter x   n   | =  |  | Corrected (Filtered) sphero data
  * |            | |        |   |         |                 |   |    |  |
  * |_          _| |_      _|   |_       _|                 |_ _|    |__|
  * 
  * Another possibility is to extend the existing collision detection or low/high-pass filtering of data.
  */

 /**
  * <p> Class that offers signal processing to the data extraction pipeline.  This class handles sensor deltas.
  * 
  * The data processing pipeline proceeds as follows (each passing on to the next):
  * AsyncDataListener takes the most basic raw data from the robot's sensors.
  * DataFilter sets sample rate, decimation and optionally does digital signal processing
  * DataConverter outputs physically relevant deltas.  
  * MotionState will integrate deltas to get position, orientation, velocity and other state info.  
  * 
  * If we wish to integrate with Bullet later, DataFilter may need to output fixed time-step values,
  * usually 30 or 60 frames per second as this makes physics simulation more stable and reproducible.
  * 
  * @author kesten
  *
  */

public class DataFilter {
	


	/*
	 * 
	 */
	 
	public DataFilter() {

	}
	
	

	
}

