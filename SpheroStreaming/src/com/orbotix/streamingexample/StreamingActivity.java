package com.orbotix.streamingexample;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import orbotix.robot.app.StartupActivity;
import orbotix.robot.base.*;
import orbotix.robot.sensor.AccelerometerData;
import orbotix.robot.sensor.AttitudeData;
import orbotix.robot.sensor.DeviceSensorsData;

import java.util.List;

import com.orbotix.streamingexample.CoordinateView;

import org.krobo.lips.core.ColorBlobDetectionView;
import org.krobo.lips.pipeline.DataDisplayView;
import com.orbotix.streamingexample.SpheroSignalManager;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

public class StreamingActivity extends Activity
{
	
    /**
     * ID for starting the StartupActivity
     */
    private final static int sStartupActivity = 0;
    
	private static final String TAG = "SpheroStreamingActivity"; 

    /**
     * Robot from which we are streaming
     */
    private Robot mRobot = null;

    //The views that will show the streaming data
    private ImuView mImuView;
    private CoordinateView mAccelerometerFilteredView;
    private DataDisplayView mDataDisplayView;
    private ColorBlobDetectionView mColorBlobView;

    private SpheroSignalManager mSignalManager;
    
    /*
     * Use this to connect to OpenCV Manager already installed on phone or grabbed from GooglePlay on the fly
     * 
     */
	   private BaseLoaderCallback  mOpenCVCallBack = new BaseLoaderCallback(this) {
	    	@Override
	    	public void onManagerConnected(int status) {
	    		switch (status) {
					case LoaderCallbackInterface.SUCCESS:
					{
						Log.i(TAG, "OpenCV loaded successfully");
						// Create and set View
						mDataDisplayView = new DataDisplayView(mAppContext);
						setContentView(mDataDisplayView);
						// Check native OpenCV camera
						if( !mDataDisplayView.openCamera() ) {
							AlertDialog ad = new AlertDialog.Builder(mAppContext).create();
							ad.setCancelable(false); // This blocks the 'BACK' button
							ad.setMessage("Fatal error: can't open camera!");
							ad.setButton("OK", new DialogInterface.OnClickListener() {
							    public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
								finish();
							    }
							});
							ad.show(); 
						}
   
						}
					 break;
					default:
					{
						super.onManagerConnected(status);
					} break;
				}
	    	}
	    	public void openCamera() {
				
	    	}
		};

    /**
     * AsyncDataListener that will be assigned to the DeviceMessager, listen for streaming data, and then do the
     *
     */
    private DeviceMessenger.AsyncDataListener mDataListener = new DeviceMessenger.AsyncDataListener() {
        @Override
        public void onDataReceived(DeviceAsyncData data) {

            if(data instanceof DeviceSensorsAsyncData){

                //get the frames in the response
                List<DeviceSensorsData> data_list = ((DeviceSensorsAsyncData)data).getAsyncData();
                if(data_list != null){

                    //Iterate over each frame
                    for(DeviceSensorsData datum : data_list){
                    	/*mDataFilter.setSensorsData(datum);         	
                    	 *mDataConverter.getDeltaPosition().x;
                    	 *mDataConverter.getDeltaOrientation().x;
                    	 *mDataConverter.getDeltaTime(); // float in seconds
                    	 *mMotionState.update(List<Vector3D> convertedSensorsData);
                    	 *mMotionState.getePosition();
                    	 *mMotionState.getOrientation();
                    	 *mMotionState.getVelocity();
                    	 */
                        //Show attitude data
                        AttitudeData attitude = datum.getAttitudeData();
                        if(attitude != null){
                            mImuView.setPitch("" + attitude.getAttitudeSensor().pitch);
                            mImuView.setRoll("" + attitude.getAttitudeSensor().roll);
                            mImuView.setYaw("" + attitude.getAttitudeSensor().yaw);
                        }

                        //Show accelerometer data
                        AccelerometerData accel = datum.getAccelerometerData();
                        if(attitude != null){
                            mAccelerometerFilteredView.setX(""+accel.getFilteredAcceleration().x);
                            mAccelerometerFilteredView.setY("" + accel.getFilteredAcceleration().y);
                            mAccelerometerFilteredView.setZ("" + accel.getFilteredAcceleration().z);
                        }
                    }
                }
            }
        }
    };

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.main);

        //Get important views
        mImuView = (ImuView)findViewById(R.id.imu_values);
        mAccelerometerFilteredView = (CoordinateView)findViewById(R.id.accelerometer_filtered_coordinates);
 
        //Show the StartupActivity to connect to Sphero
        Log.i(TAG, "Trying to load OpenCV library");
        if (!OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_2, this, mOpenCVCallBack))
        {
          Log.e(TAG, "Cannot connect to OpenCV Manager");
          setContentView(R.layout.main);
        }

        startActivityForResult(new Intent(this, StartupActivity.class), sStartupActivity);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == sStartupActivity){

            if(resultCode == RESULT_OK){

                //Get the Robot from the StartupActivity
                String id = data.getStringExtra(StartupActivity.EXTRA_ROBOT_ID);
                mRobot = RobotProvider.getDefaultProvider().findRobot(id);

                startStreaming();

                StabilizationCommand.sendCommand(mRobot, false);
                FrontLEDOutputCommand.sendCommand(mRobot, 1f);
            }
        }
    }

    @Override
	protected void onPause() {
        Log.i(TAG, "onPause");
		super.onPause();
		if(mDataDisplayView != null)
			mDataDisplayView.releaseCamera();
	}
    
    protected void onResume() {
        super.onResume();
        
		if( (null != mDataDisplayView) && !mDataDisplayView.openCamera() ) {
			AlertDialog ad = new AlertDialog.Builder(this).create();
			ad.setCancelable(false); // This blocks the 'BACK' button
			ad.setMessage("Fatal error: can't open camera!");
			ad.setButton("OK", new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				finish();
			    }
			});
			ad.show();
		}
    }
    @Override
    protected void onStop() {
        super.onStop();

        if(mRobot != null){

    		// add this to turn off streaming, otherwise get reconnection problems
            if(mRobot != null){
                SetDataStreamingCommand.sendCommand(mRobot, 0, 0, 0, 0);
            }
            
            StabilizationCommand.sendCommand(mRobot, true);
            FrontLEDOutputCommand.sendCommand(mRobot, 0f);

            RobotProvider.getDefaultProvider().disconnectControlledRobots();
        }
    }

    private void startStreaming(){

        if(mRobot != null){

            //Set up a bitmask containing the sensor information we want to stream
            final int mask =
                    SetDataStreamingCommand.DATA_STREAMING_MASK_ACCELEROMETER_X_FILTERED |
                    SetDataStreamingCommand.DATA_STREAMING_MASK_ACCELEROMETER_Y_FILTERED |
                    SetDataStreamingCommand.DATA_STREAMING_MASK_ACCELEROMETER_Z_FILTERED |
                    SetDataStreamingCommand.DATA_STREAMING_MASK_IMU_PITCH_ANGLE_FILTERED |
                    SetDataStreamingCommand.DATA_STREAMING_MASK_IMU_ROLL_ANGLE_FILTERED |
                    SetDataStreamingCommand.DATA_STREAMING_MASK_IMU_YAW_ANGLE_FILTERED;

            //Specify a divisor. The frequency of responses that will be sent is 400hz divided by this divisor.
            final int divisor = 50;

            //Specify the number of frames that will be in each response. You can use a higher number to "save up" responses
            //and send them at once with a lower frequency, but more packets per response.
            final int packet_frames = 1;

            //Total number of responses before streaming ends. 0 is infinite.
            final int response_count = 0;

            //Send this command to Sphero to start streaming
            SetDataStreamingCommand.sendCommand(mRobot, divisor, packet_frames, mask, response_count);

            //Set the AsyncDataListener that will process each response.
            DeviceMessenger.getInstance().addAsyncDataListener(mRobot, mDataListener);
        }
    }
}
