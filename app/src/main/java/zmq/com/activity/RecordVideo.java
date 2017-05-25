
package zmq.com.activity;

/**
 * @author zmq
 *
 */
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;





import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ToggleButton;

import zmq.com.database.MyDatabaseAdapter;
import zmq.com.database.MySharedPreference;
import zmq.com.gac.R;
import zmq.com.utility.Utility;

public class RecordVideo extends Activity implements SurfaceHolder.Callback{
	
	public static final int MEDIA_TYPE_IMAGE = 1; 
	public static final int MEDIA_TYPE_VIDEO = 2;	 
	public static final int MEDIA_TYPE_AUDIO = 3;
	
    private Button start, stop;
    MediaRecorder mMediaRecorder;
    Camera mCamera;
    SurfaceHolder mHolder;
    SurfaceView surfaceView;
    ToggleButton toggleButton;
//  String VIDEO_PATH_NAME = getOutputFilePath(MEDIA_TYPE_VIDEO).toString();//Environment.getExternalStorageDirectory().getPath() +"/YOUR_VIDEO.mp4";
	private boolean mInitSuccesful;
	
//;   public  String patientID="NA";

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.record);
		surfaceView = (SurfaceView) findViewById(R.id.surfaceView1);
		start = (Button) findViewById(R.id.button1);
		mHolder = surfaceView.getHolder();
		mHolder.addCallback(this);
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);


        System.out.println("ID on onCreate" + Utility.patientID);
	}
    
    
    public void startClick(View view) {
		// TODO Auto-generated method stub
    	Toast.makeText(this, "startClick", Toast.LENGTH_SHORT).show();

    	start.setBackgroundResource(R.drawable.stop);
    	mMediaRecorder.start();
    	start.setClickable(false);    	
    	try {
            Thread.sleep(10 * 1000); // This will recode for 10 seconds, if you don't want then just remove it.
            setResult(Activity.RESULT_OK);
            
    	} catch (Exception e) {
            e.printStackTrace();
        }
    	
    	Intent intent = new Intent();
		intent.putExtra("VideoRecorder", "recorded");
        intent.putExtra("patientId", getIntent().getStringExtra("patientId"));

        Utility.isFileSaved=true;
        setResult(2, intent);
//		finish();
//    	mMediaRecorder.stop();
    //	showMyDialog();
        finish();
	}
    
    


	/* Init the MediaRecorder, the order the methods are called is vital to
     * its correct functioning */
    @SuppressLint("NewApi")
	private void initRecorder(Surface surface) throws IOException {
        // It is very important to unlock the camera before doing setCamera
        // or it will results in a black preview
    	int numberOfCameras = 0;
    	
        if(mCamera == null) {
        	numberOfCameras = Camera.getNumberOfCameras();
        	if(numberOfCameras == 2){
        		mCamera = Camera.open(1);
        	}else{
        		mCamera = Camera.open(0);
        	}
        	
            Camera.Parameters p = mCamera.getParameters();
            if (Integer.parseInt(Build.VERSION.SDK) >= 8)
                setDisplayOrientation(mCamera, 90);
            else
            {
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
                {
                    p.set("orientation", "portrait");
                    p.set("rotation", 90);
                }
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
                {
                    p.set("orientation", "landscape");
                    p.set("rotation", 90);
                }
            }   
            
            
         //   mCamera.setDisplayOrientation(90);
            mCamera.unlock();
        }

        if(mMediaRecorder == null)  mMediaRecorder = new MediaRecorder();
        mMediaRecorder.setPreviewDisplay(surface);
        mMediaRecorder.setCamera(mCamera);

        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.DEFAULT);
       //       mMediaRecorder.setOutputFormat(8);
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
//        mMediaRecorder.setVideoEncodingBitRate(512 * 1000);
//        mMediaRecorder.setVideoFrameRate(30);
//        mMediaRecorder.setVideoSize(640, 480);
        mMediaRecorder.setOutputFile(getOutputFilePath(MEDIA_TYPE_VIDEO).toString());
        
        CameraInfo info = new CameraInfo();
        Camera.getCameraInfo(0, info);
        
        if(numberOfCameras == 2){
        	
        	mMediaRecorder.setOrientationHint(270);

        }else{
        	
        	if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            	mMediaRecorder.setOrientationHint(90);
            }
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            	mMediaRecorder.setOrientationHint(180);
            }
        }
 //       mMediaRecorder.setOrientationHint(90);
        
        try {
            mMediaRecorder.prepare();
        } catch (IllegalStateException e) {
            // This is thrown if the previous calls are not called with the 
            // proper order
            e.printStackTrace();
        }

        mInitSuccesful = true;



    }
    
    
//    public void stopClick(View view) {
//		// TODO Auto-generated method stub
//    	Toast.makeText(this, "stopClick", Toast.LENGTH_SHORT).show();
//    	videoCapture.stopCapturingVideo();
//        setResult(Activity.RESULT_OK);
//        finish();
//    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            if(!mInitSuccesful)
                initRecorder(mHolder.getSurface());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        shutdown();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

    private void shutdown() {
        // Release MediaRecorder and especially the Camera as it's a shared
        // object that can be used by other applications
//        updateTable();
        mMediaRecorder.reset();
        mMediaRecorder.release();
        mCamera.release();

        // once the objects have been released they can't be reused
        mMediaRecorder = null;
        mCamera = null;


    }
    
    public  File getOutputFilePath(int type) {
		// TODO Auto-generated method stub

    	File mediaStorageDir = new File(Environment.getExternalStorageDirectory()+"/GAC","Compliance");
    	
    	// Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }
        
     // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd").format(new Date());
        File  mediaFile;


        
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
            "IMG_"+ timeStamp + ".jpg");
        } else if(type == MEDIA_TYPE_VIDEO) {
//            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
//            "VID_"+ timeStamp + ".mp4");
        	
        	mediaFile = new File(mediaStorageDir , Utility.patientID+"_"+Utility.Server_Date + "_TBC" + ".mp4" );

//            File file = new File(Environment.getExternalStorageDirectory().getPath()+"/GAC","Compliance");
//            final File[] allFiles = file.listFiles();



        } 
        else if(type == MEDIA_TYPE_AUDIO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
            "AUD_"+ timeStamp + ".mp3");
        }else {
            return null;
        }

		return mediaFile;
	}
    
    protected void setDisplayOrientation(Camera camera, int angle){
        Method downPolymorphic;
        try
        {
            downPolymorphic = camera.getClass().getMethod("setDisplayOrientation", new Class[] { int.class });
            if (downPolymorphic != null)
                downPolymorphic.invoke(camera, new Object[] { angle });
        }
        catch (Exception e1)
        {
        }
    }
    
    @Override
    protected void onUserLeaveHint() {
    	// TODO Auto-generated method stub
    	setResult(Activity.RESULT_CANCELED);
 //   	Toast.makeText(this, "onUserLeaveHintASIF", Toast.LENGTH_SHORT).show();
    	finish();
    	super.onUserLeaveHint();
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	// TODO Auto-generated method stub
    	return false;
    	
    	
    }

}


