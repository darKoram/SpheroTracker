package kesten.fragmentstestbed;

import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import orbotix.robot.app.*; 


public class FragmentsMainActivity extends FragmentActivity {

	private final static int STREAMING_ACTIVITY_RESULT=0;
	private final static int COLORBLOBDETECTION_ACTIVITY_RESULT=0;
	public final static int STARTUP_ACTIVITY_RESULT=0;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragments_main);
      
     // Set content view, etc.  
        //ViewServer.get(this).addWindow(this); 
        
      
        if(savedInstanceState == null) {
        	Intent intentStartupActivity = new Intent(this, StartupActivity.class);
			if(intentStartupActivity != null)
				startActivityForResult(intentStartupActivity, STARTUP_ACTIVITY_RESULT);

			// Intent intentBlob = new Intent(this, ColorBlobDetectionActivity.class);
			// startActivityForResult(intentBlob, COLORBLOBDETECTION_ACTIVITY_RESULT);
			
			// in case startupActivity changed the view, change it back.  StreamingActivity
			// can't be a direct child of both startupActivity's view and mainfragment's view
	        setContentView(R.layout.activity_fragments_main);

	        // get an instance of FragmentTransaction from your Activity
	        FragmentTransaction fragmentTransaction = 
	        		getSupportFragmentManager().beginTransaction();
	
	 		//add a fragment
	        StreamingActivity streamingActivity = new StreamingActivity();
	        if (streamingActivity != null) {
	        	fragmentTransaction.add(R.id.streamingactivity_view, streamingActivity);
	        	fragmentTransaction.commit();
	        }
        }
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	Log.i("FragmentsMainActivity", "onCreateOptionsMenu");
    	
        getMenuInflater().inflate(R.menu.activity_fragments_main, menu);
        return true;
    }

    @Override
    public void onDestroy() {  
        super.onDestroy();  
        //ViewServer.get(this).removeWindow(this);  
    } 
    
    public void onResume() {  
        super.onResume();  
        //ViewServer.get(this).setFocusedWindow(this);  
    }  
    
    
    
    
}
