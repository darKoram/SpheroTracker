package org.krobo.lips.core;



/*
 * This is an attempt to unify management of various Devices (Sphero, Arduino, Phone..) and their respective Sensors (Accelerometer, Gyroscope..)
 * Unfortunately, the need to have a unified enum to pass to SensorDataQueue breaks other OOP principles (having to modify this file rather than 
 * inherit or implement an interface.  However, enum is restricted in that it can't be extended so I see no way
 * to pass a single List<enum> to SensorDataQueue without merging what should be to distinct enum groups.
 * I'll keep looking for a cleaner way to do this, but for now...  
 */
public class DeviceSensorType {
	
	// Add new devices here
	/*
	 * List of devices know to API
	 */
	public enum Device { PHONE, SPHERO, ARDUINO };  
	
	/*
	 * List of Sensors, prefix is redundant, but each device can only support specific Sensors
	 * so we would need a Map of allowed pairs so we don't construct bad (Device, Sensor) pairs.
	 */
	public enum Sensor { ACCELEROMETER, 
						 ATTITUDE, 
						 GYROSCOPE, 
						 RIGHT_BACKEMF, 
						 LEFT_BACKEMF,
						 THERMOMETER}; 

}
