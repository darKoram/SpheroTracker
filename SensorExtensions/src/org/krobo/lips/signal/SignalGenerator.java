package org.krobo.lips.signal;


import java.util.ArrayList;
import java.util.List;

/*
 * Simulates data generated by specified device and sensor list.
 * Device sensors may have typical noise profiles or constraints amongst axes.
 * User selects a device, a noise profile, 
 */
public class SignalGenerator {
	
	public enum MODE {FIXED_SAMPLE_RATE, VARIABLE_SAMPLE_RATE};
	
	private final MODE mMode;
	/*
	 * Sample rate in Hz
	 */
	private final int mSampleRate;
	/*
	 * For fixed frequency sampling t = mSampleCount/mSampleRate
	 */
	private int mSampleCount;
	
	/*
	 * Time in seconds
	 */
	private Double mTime = 0D;
	
	private List<Channel> mChannels;
	private List<Double> mCurrentSignal;
	
	public SignalGenerator(int hz, List<Channel> channels, MODE mode) {
		mSampleRate = hz;
		mCurrentSignal = new ArrayList<Double>(channels.size());
		mMode = mode;
		mSampleCount = 0;
		mTime = 0D;
		mChannels = channels;
	}

	/*
	 * If the signal receiver stops calling getSignal() we may still want internal time for the
	 * generator to keep flowing.  This will allow "re-synch" to external time.  
	 * If the SG is in variable sampling mode, call setTime before each getSignal()
	 */
	public void setTimeInSeconds(Double time) {
		mTime = time;
	}
	
	private void setTime() {
		mTime = (double) mSampleCount/mSampleRate;
	}
	
	public List<Double> getSignal() {
		if(mMode == MODE.FIXED_SAMPLE_RATE)
			setTime();
		mSampleCount++;
		Double sum=0.0D;
		mCurrentSignal.clear();
		for(Channel c : mChannels) {
			sum=0.0D;
			for (SignalFunctor s : c.getSignalFunctorList()) {
				sum = sum + s.execute(mTime);				
			}
			mCurrentSignal.add(sum);
		}
		// TODO defensive copy if necessary
		return mCurrentSignal;
	}
	
	@Override
	public String toString() {
		String str = "Sample Rate(hz): " + mSampleRate + ", Time(s): " + mTime +", Sample Count: " + mSampleCount +" ";
		return str;
	}
	
}
