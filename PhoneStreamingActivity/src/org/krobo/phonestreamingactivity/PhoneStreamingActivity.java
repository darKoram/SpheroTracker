package org.krobo.phonestreamingactivity;

import java.util.List;

import org.krobo.lips.core.ColorBlobDetectionView;
import org.krobo.lips.utils.StreamingStats;

import org.krobo.phonestreamingactivity.PhoneSignalManager.PhoneSensor;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;


import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.Menu;
import android.view.Window;
import android.widget.TextView;

public class PhoneStreamingActivity extends Activity implements SensorEventListener {
    
	public PhoneSignalManager mSignalManager;
    private Sensor mAccelerometer;
    private Sensor mGyroscope;
	private static final String TAG = "PhoneStreaming";
	private ColorBlobDetectionView mColorBlobView;
	private StreamingStats mStreamingStats;
	private double mTime;
	private boolean mFirstFlag;

    

    public PhoneStreamingActivity() {
    }

	   private BaseLoaderCallback  mOpenCVCallBack = new BaseLoaderCallback(this) {
	    	@Override
	    	public void onManagerConnected(int status) {
	    		switch (status) {
					case LoaderCallbackInterface.SUCCESS:
					{
						Log.i(TAG, "OpenCV loaded successfully");
						// Create and set View
						//mColorBlobView = new ColorBlobDetectionView(mAppContext);
						//setContentView(mColorBlobView);
						setContentView(R.layout.phone_streaming_rel);
						mColorBlobView = (ColorBlobDetectionView) findViewById(R.id.cvsurface);
						// Check native OpenCV camera
						if( !mColorBlobView.openCamera() ) {
							AlertDialog ad = new AlertDialog.Builder(mAppContext).create();
							ad.setCancelable(false); // This blocks the 'BACK' button
							ad.setMessage("Fatal error: can't open camera!");
							ad.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
							    public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
								finish();
							    }
							});
							ad.show();
						}
					} break;
					default:
					{
						super.onManagerConnected(status);
					} break;
				}
	    	}
		};

	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        //setContentView(R.layout.activity_phone_streaming);
        	mTime = 0;
        	mFirstFlag = true;

	        Log.i(TAG, "Trying to load OpenCV library");
	        if (!OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_2, this, mOpenCVCallBack))
	        {
	        	Log.e(TAG, "Cannot connect to OpenCV Manager");
	        }
	        
			SensorManager sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
	        if(sensorManager == null) {
	        	Log.i("Phone Streaming ", "Unable to get Sensor Manager");
	        	throw new ExceptionInInitializerError() ; 
	        }
	        mSignalManager = new PhoneSignalManager(sensorManager);
		    mAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		    mStreamingStats = new StreamingStats();
		    
		    mGyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
		    if (mGyroscope==null)
		    {
		    	Log.i(TAG, "No Gyroscope sensor");
		    }

	        requestWindowFeature(Window.FEATURE_NO_TITLE);        
	    }
	    
	    protected void onPause() {
	        super.onPause();
	        mSignalManager.getSensorManager().unregisterListener(this);
	    	if (null != mColorBlobView)
				mColorBlobView.releaseCamera();
		}

	    protected void onResume() {
	        super.onResume();
	        Log.i(TAG, "On Resume");
	        mSignalManager.getSensorManager().registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
	        
			if( (null != mColorBlobView) && !mColorBlobView.openCamera() ) {
				AlertDialog ad = new AlertDialog.Builder(this).create();
				ad.setCancelable(false); // This blocks the 'BACK' button
				ad.setMessage("Fatal error: can't open camera!");
				ad.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
				    public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					finish();
				    }
				});
				ad.show();
			}
	    }
			
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
    	super.onConfigurationChanged(newConfig);
    	setContentView(R.layout.phone_streaming_rel);
    }
    
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void onSensorChanged(SensorEvent event) {
    	
    	String str = "";
    	TextView tv = null;
    	
    	mSignalManager.updateSensor(event);
    	
    	double timestamp = event.timestamp;
    	// convert from nanoseconds to microseconds
    	timestamp /= 1000000;

    	
    	if (mFirstFlag) {
    		mFirstFlag = false;
        	mTime = timestamp;
    	}
    	else {    		
	    	double timeStep = timestamp - mTime;
	    	mTime = timestamp;
	    	Log.i(TAG, "timeStep: " + timeStep);
	    	mStreamingStats.updateMeanAndVariance(timeStep);
	
	    	if ( mColorBlobView != null)  {
				str = "time: " + mStreamingStats.mSum;
				tv = (TextView) findViewById(R.id.timestamp_label);
				mColorBlobView.setTextView(tv, str);
				str = "mean time step: " + mStreamingStats.mMean;
				tv = (TextView) findViewById(R.id.meanTimeStep_label);
				mColorBlobView.setTextView(tv, str);
				str = "variance of time steps: " + mStreamingStats.mVariance;
				tv = (TextView) findViewById(R.id.varianceTimeStep_label);
				mColorBlobView.setTextView(tv, str);
			}
	    }
    	
    	Float x = event.values[0];
    	Float y = event.values[1];
    	Float z = event.values[2];

    	
    	// TODO avoid findViewByID by caching these values locally since we're streaming
    	if(event.sensor.getType() == PhoneSensor.TYPE_ACCELEROMETER.getType() ) {
    		
    		if ( mColorBlobView != null)  {
    		str = "x: " + x.toString();
    		tv = (TextView) findViewById(R.id.x_label);
    		mColorBlobView.setTextView(tv, str);
    		str = "y: " + y.toString();
    		tv = (TextView) findViewById(R.id.y_label);
    		mColorBlobView.setTextView(tv, str);
    		str = "z: " + z.toString();
    		tv = (TextView) findViewById(R.id.z_label);
    		mColorBlobView.setTextView(tv, str);
    		}
    		
    	}	

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_phone_streaming, menu);
        return true;
    }
    
    public void onStop() {
    	super.onStop();
    }

}
