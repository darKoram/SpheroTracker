package org.krobo.lips.pipeline;

import java.text.DecimalFormat;
import java.util.EnumSet;

import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;

import org.krobo.lips.signal.*;
import org.krobo.lips.core.*;
import org.krobo.lips.pipeline.*;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;
import android.content.Context;

public class DataDisplayView extends SampleCvViewBase {

	private static final String TAG = "SpheroStreamingActivity";
	
	Paint			mPaint;
	SurfaceHolder 	mHolder;
	Mat 			mRgba;
	
	public DataDisplayView(Context context) {
		super(context);
		init(context);
        Log.i(TAG, "Constructing new DataDisplayView" + this.getClass());
	}
	
	public void init(Context context) {
		mPaint = new Paint();
		mPaint.setColor(Color.BLUE);
		mPaint.setTextSize(50);
        mHolder = getHolder();
        mHolder.addCallback(this);
        //mRgba = new Mat();
        
        Log.i(TAG, "Instantiated new " + this.getClass());
	}
	

    public <E extends Enum<E> & HasSize > void drawData( EnumSet<E> displayedSensors, 
    													SensorDataQueue<E> sensorData) {
    	Canvas canvas = mHolder.lockCanvas();
        if (canvas != null) {
	    	float offsetx = 0; 
	    	float offsety = 0;
	    	for (E s : displayedSensors) {
	    		IndexedSensor<E> sensor = sensorData.getIndexedSensor(s);
	            DecimalFormat twoPlaces = new DecimalFormat("0.00");
	            String str = "";
	            canvas.drawText(sensor.mSensor.name(), 
	    						20 + offsetx, 
	    						10 + 50 + offsety, 
	    						mPaint);
	    		offsety += 50;		
	    		for(int i=sensor.mFirst; i<=sensor.mLast; i++) {
	                str = twoPlaces.format(sensorData.getCurrentRecordAry()[i]);
	                canvas.drawText(str,  offsetx,  offsety,  mPaint);
	                offsety += 50;
	    		}
	    			
	    	}
        }
        mHolder.unlockCanvasAndPost(canvas);
        Log.i(TAG, "Finished drawing diplay canvas ");
    }

	@Override
	protected Bitmap processFrame(VideoCapture capture) {
		capture.retrieve(mRgba, Highgui.CV_CAP_ANDROID_COLOR_FRAME_RGBA);		
        Bitmap bmp = Bitmap.createBitmap(mRgba.cols(), mRgba.rows(), Bitmap.Config.ARGB_8888);
        if(bmp==null ) {
        	Log.i(TAG, "Couldn't create bitmap");
        }
        return bmp;
	}
	
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        synchronized (this) {
            // initialize Mat before usage
            mRgba = new Mat();
        }
        
        super.surfaceCreated(holder);
    }
	
    /*
     * @see com.orbotix.streamingexample.SampleCvViewBase#run()
     * overrides run() from SampleCvViewBase which calls process frame.
     * note on synchronized(): If an object is visible to more than one thread, all reads or 
     *  writes to that object's variables are done through synchronized methods. 
     *  (An important exception: final fields, which cannot be modified after 
     *  the object is constructed, can be safely read through non-synchronized 
     *  methods, once the object is constructed)
     */
    @Override
    public void run() {
        super.run();

        synchronized (this) {
            // Explicitly deallocate Mats
            if (mRgba != null)
                mRgba.release();

            mRgba = null;
        }
    }

}