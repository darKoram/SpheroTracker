package com.orbotix.streamingexampletests;

import java.util.EnumSet;

import org.krobo.lips.pipeline.DataFilter;
import org.krobo.lips.signal.SensorDataQueue;
import com.orbotix.streamingexample.SpheroSignalManager;
import com.orbotix.streamingexample.SpheroSignalManager.SpheroSensor;

import junit.framework.TestCase;

public class DataFilterTest extends TestCase {

	EnumSet<SpheroSensor> 			tSensors;
	SensorDataQueue<SpheroSensor> 	tSensorDataQueue;
	int 							tDivisor;
	int								tPacketFrames;
	int 							tResponseCount;
	int 							tWindowLength;
	int 							tQueueLength;
	
	public DataFilterTest(String name) {
		super(name);
	}
	
	public void testAddSensor() throws Exception {
		tSensors = EnumSet.of(SpheroSensor.ATTITUDE);
		tSensors.add(SpheroSensor.ACCELEROMETER);
		if (tSensors.size() != 2) {
			fail("Initializing sensor list failed");
		}
		
		System.out.println(tSensors);

		
	}
	
	public void testAddRecord() throws Exception {

		double[] record= new double[tSensorDataQueue.getRecordLength()];
		for (int i=0; i<tSensorDataQueue.getRecordLength(); i++)
			record[i] = i;
		
		tSensorDataQueue.addRecord(record);
		record = tSensorDataQueue.getCurrentRecordAry();
		System.out.println(record.toString());
		
		// Configure SignalManager
		SpheroSignalManager signalManager = new SpheroSignalManager(	tDivisor,
																		tPacketFrames,
																		tResponseCount,
																		tSensors);
		signalManager.getLastTimeStamp();
		
		
		DataFilter df = new DataFilter();
		
		
	}

	protected void setUp() throws Exception {
		super.setUp();
		tDivisor = 10;
		tPacketFrames = 1;
		tResponseCount = 0;
		tQueueLength = 64;
		tWindowLength = 6;
		
		// configure DataQueue.  queueLength must be > 10 times windowLength  
		tSensorDataQueue = new SensorDataQueue<SpheroSensor>(tQueueLength, tWindowLength, tSensors);

	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

}
