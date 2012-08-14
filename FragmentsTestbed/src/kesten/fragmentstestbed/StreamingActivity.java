package kesten.fragmentstestbed;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import orbotix.robot.app.StartupActivity;
import orbotix.robot.base.*;
import orbotix.robot.sensor.AccelerometerData;
import orbotix.robot.sensor.AttitudeData;
import orbotix.robot.sensor.DeviceSensorsData;

import java.util.List;

//import com.orbotix.streamingexample.R;

public class StreamingActivity extends Fragment
{
    /**
     * ID for starting the StartupActivity
     */
	// Hold static in FragmentsMainActivity
    // private final static int sStartupActivity = 0;

    /**
     * Robot to from which we are streaming
     */
    private Robot mRobot = null;

    //The views that will show the streaming data
    private ImuView mImuView;
    private CoordinateView mAccelerometerFilteredView;

    /**
     * AsyncDataListener that will be assigned to the DeviceMessager, listen for streaming data, and then do the
     *
     */
    private DeviceMessenger.AsyncDataListener mDataListener; 
    
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
    	Log.i("StreamingActivity","onCreate");
        // second parameter is parent view to attach newly created view to
        //View view = getActivity().getView();
        //if(view !=null)
        //	onCreateView(getLayoutInflater(savedInstanceState), (ViewGroup) view.findViewById(R.layout.main), savedInstanceState);

        //setContentView(getView().findViewById(R.layout.main));
        //Get important views for AsyncDataListener later
       
        //Show the StartupActivity to connect to Sphero
        //Connection occurs in FragmentsMainActivity
        // getActivity().startActivityForResult(new Intent(this.getActivity(), StartupActivity.class), sStartupActivity);
    
        
    }
    
    @Override 
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	
    	Log.i("StreamingActivity","onCreateView");
    	
    	View view = inflater.inflate(R.layout.main, container, false);
        mImuView = (ImuView) getView().findViewById(R.id.imu_values);
        mAccelerometerFilteredView = (CoordinateView)getView().findViewById(R.id.accelerometer_filtered_coordinates);
        
        mDataListener = new DeviceMessenger.AsyncDataListener() {
            @Override
            public void onDataReceived(DeviceAsyncData data) {

                if(data instanceof DeviceSensorsAsyncData){

                    //get the frames in the response
                    List<DeviceSensorsData> data_list = ((DeviceSensorsAsyncData)data).getAsyncData();
                    if(data_list != null){

                        //Iterate over each frame
                        for(DeviceSensorsData datum : data_list){

                            //Show attitude data
                        	//KB if there are multiple entries in the list, does this show all
                        	// values sequentially, or just the last one in the list?
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
        return view;
    }

    // change from onActivityResult
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == FragmentsMainActivity.RESULT_OK){

            if(requestCode == FragmentsMainActivity.STARTUP_ACTIVITY_RESULT){

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
    public void onStop() {
        super.onStop();

        if(mRobot != null){

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
