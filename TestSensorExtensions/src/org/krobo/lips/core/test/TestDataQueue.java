package org.krobo.lips.core.test;

import java.util.Arrays;
import java.util.List;

import org.krobo.lips.core.DataQueue;
import org.krobo.lips.signal.SignalFunctor;
import org.krobo.lips.signal.SignalGenerator;
import org.krobo.lips.signal.Sinusoid;
import org.krobo.lips.signal.Zero;
import org.krobo.lips.signal.Channel;
import org.krobo.lips.signal.test.TestSignalGenerator;

public class TestDataQueue extends TestSignalGenerator {

	int mRecordLength1 = 2;
	int mRecordLength2 = 3;
	int mWindowLength1 = 1;
	int mWindowLength2 = 5;
	int mQueueLength1 = 11;
	int mQueueLength2 = 70;
	
	DataQueue dq1;
	DataQueue dq2;
	
	Zero zero;
	Sinusoid sin1;
	Sinusoid sin2;
	Sinusoid sin3;
	Sinusoid sin4;

	List<SignalFunctor> sf1;
	List<SignalFunctor> sf2;
	List<SignalFunctor> sf3;
	
	Channel ch1;
	Channel ch2;
	Channel ch3;
	
	public TestDataQueue(String name) {
		super(name);
		// Direct initialization
		//dq1 = new DataQueue(mRecordLength1, mQueueLength1, mWindowLength1);
		// Lazy initialization used by 
		dq2 = new DataQueue(mQueueLength2, mWindowLength2);
		

		sin1 = new Sinusoid(10D, 10D, 0D);
		sin2 = new Sinusoid(20D, 20D, 0D);
		sin3 = new Sinusoid(30D, 30D, 0D);
		
		sf1 = Arrays.asList(sin1, zero);
		sf2 = Arrays.asList(sin2, zero);
		sf3 = Arrays.asList(sin3, zero);

		List<Channel> channels1;
		List<Channel> channels2;

		ch1 = new Channel(sf1);
		ch2 = new Channel(sf2);
		ch3 = new Channel(sf3);
		
		channels1 = Arrays.asList(ch1, ch2);
		channels2 = Arrays.asList(ch1, ch2, ch3);
		// Inherited from SignalGenerator Test
		int hz = 3;
		sg1 = new SignalGenerator(hz, channels1, SignalGenerator.MODE.FIXED_SAMPLE_RATE);
		sg2 = new SignalGenerator(hz, channels2, SignalGenerator.MODE.FIXED_SAMPLE_RATE);

	}

	protected void setUp() throws Exception {
		//super.setUp();
		// Super sets up signal generator
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testSetRecordLength() {
		dq2.setRecordLength(mRecordLength2);		
	}

	public void testAddRecord() {
		List<Double> sig1 = sg1.getSignal();
		dq1.addRecord(sig1);
		List<Double> sig2 = sg2.getSignal();
		dq2.addRecord(sig2);
	
		System.out.println("sig1: ch1  ch2   	ch3 " + sig1.toString());
		System.out.println("dq1: ch1 	ch2 	ch3 " + dq1.getCurrentRecordList().toString());

		System.out.println("sig2: ch1   ch2   	ch3 " + sig1.toString());
		System.out.println("dq2:  ch1 	ch2 	ch3 " + dq2.getCurrentRecordList().toString());

	}


	public void testGetCurrentRecordMat() {
		
	}

	public void testGetCurrentRecordAry() {

	}

}
