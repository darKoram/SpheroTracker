import java.util.List;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import junit.framework.TestCase;
import org.krobo.phonestreamingactivity.PhoneStreamingActivity;

/**
 * 
 */

/**
 * @author kesten
 * We are accessing SensorS from a separate activity, so it's possible that we will have
 * threading issues since the API is not generally thread-safe.  Sensors could be being
 * written to in the StreamingActivity thread while we make a call to read from them.
 * The SensorS have a flag that should indicate if the data available is good.
 * 
 * Since these Unit tests are not created as Android activities, we will not be monitoring
 * the results with the phone device, rather the laptop will control display.  We need
 *
 */
public class TestPowerDraw extends TestCase {

	/*
	 * Getting and setting values for PhoneStramingActivity should be done in onResume()
	 * Not sure if i can run an Android apk within JUnit framework
	 * Might have to just test non-main classes
	 * Activities run on separate threads and don't know much bout that yet
	 */
	PhoneStreamingActivity mPhoneStreamingActivity = new PhoneStreamingActivity();
	SensorManager mSensorManager = mPhoneStreamingActivity.mSensorManager;
	private List<Sensor> mSensorList;
	
	/**
	 * @param name
	 * general stats and graphics visualization packages to display results.
	 */
	public TestPowerDraw(String name) {
		super(name);
		System.out.println("Got here 1");
	}

	/*
	 * Android Sensor s in SensorManager allows s.getPower()  // actually milliAmps
	 * 
	 */
	public void testPowerVsSampleRate() {
		/*
		 * Android offers three rates: SENSOR_DELAY_NORMAL, SENSOR_DELAY_GAME, SENSOR_DELAY_FASTEST
		 * Run each of the sensors for T seconds recording the power draw.
		 * Compare power draw per second (milliAmps/s)
		 */
		System.out.println("Got here 2");
		// registerListener(SensorEventListener listener, Sensor sensor, int rate, Handler handler)
	}
	
	public void testPowerVsMinDelay() {
		
		/*
		 *  Can only test minDelay for Android 9 or greater.
		 *  I don't see any way to set minDelay in the API
		 *  http://developer.android.com/reference/android/hardware/SensorManager.html
		 *  Record power draw for selected sensor for T seconds.
		 */
		
		// registerListener(SensorEventListener listener, Sensor sensor, int rate, Handler handler)
	}

	public void testPowerVsAccuracy() {
		
		/*
		 *  I don't see any way to set minDelay in the API
		 *  http://developer.android.com/reference/android/hardware/SensorManager.html
		 *  Perhaps this can't be set.
		 */
		
		// registerListener(SensorEventListener listener, Sensor sensor, int rate, Handler handler)
	}
	
	/*
	 * Assuming no coupling between power draw, add sensors one at a time and measure the jump in
	 * total power for each sensor at max settings
	 */
	public void testPowerVsDevice() {
		
		
		// registerListener(SensorEventListener listener, Sensor sensor, int rate, Handler handler)
	}


	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
	    //  Find out what sensors we have
	    System.out.println("Available sensors: " + mPhoneStreamingActivity.mAvailableSensorList.toString() );
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void displayStreamingSensors(List<Sensor> sensorList) {
		
	}

}
