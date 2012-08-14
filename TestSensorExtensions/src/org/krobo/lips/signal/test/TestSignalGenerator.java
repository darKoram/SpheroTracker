package org.krobo.lips.signal.test;

import java.util.Arrays;
import java.util.List;

import org.krobo.lips.signal.Channel;
import org.krobo.lips.signal.Linear;
import org.krobo.lips.signal.SignalFunctor;
import org.krobo.lips.signal.SignalGenerator;
import org.krobo.lips.signal.Sinusoid;
import org.krobo.lips.signal.Zero;

import junit.framework.TestCase;

public class TestSignalGenerator extends TestCase {
	
	protected SignalGenerator sg1;
	protected SignalGenerator sg2;

	public TestSignalGenerator(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
		// create a new function that implements SignalFunctor interface
		System.out.println("Setting up test");
		Linear line = new Linear(1.0D, 1.0D);
		// Sin(Amplitude, Frequency, Phase)
		Sinusoid sin = new Sinusoid(10.0D, 0.1D, 0.0D);
		Sinusoid sin2 = new Sinusoid(20.0D, 2.1D, 0.0D);
		Zero zero = new Zero();
		List<SignalFunctor> channel1Functors = Arrays.asList(line, sin, sin2);
		Channel channel1 = new Channel(channel1Functors);
		List<SignalFunctor> channel2Functors = Arrays.asList(sin, zero);
		Channel channel2 = new Channel(channel2Functors);

		List<Channel> channels = Arrays.asList(channel1, channel2);
		// Remember this if we try to templatize
		//List<Channel<?>> channels = Array.asList(channel1);
		// Create a signalGenerator with Channels
		int hz = 1;
		sg1 = new SignalGenerator(hz, channels, SignalGenerator.MODE.FIXED_SAMPLE_RATE);		
		sg2 = new SignalGenerator(hz, channels, SignalGenerator.MODE.VARIABLE_SAMPLE_RATE);
		
	}
	public void testSignalGenerator() {
		System.out.println("Test Signal Generator");
		// get fixed sample rate signal
		List<Double> fChannelSignals = null;	

		for (int i=0; i<6; i++) {
			fChannelSignals = sg1.getSignal();
		    System.out.println(sg1.toString() + fChannelSignals.toString());
		}
		fChannelSignals.clear();
		System.out.println("Test Variable Sample Rate Signal Generator");
		// get variable sample rate signal at time t in seconds
		for (int i=0; i<6; i++) {
			sg2.setTimeInSeconds((double)i*i);
			fChannelSignals = sg2.getSignal();
		    System.out.println(sg2.toString() + fChannelSignals.toString());
		}
		

	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

}
