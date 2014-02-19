package com.qplaytech.screenshare;

import java.io.IOException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.Window;
import android.widget.Toast;

public class Player extends Activity implements Callback {
	private SurfaceView surfaceView;
	private SurfaceHolder surfaceHolder;
	private MediaPlayer mediaPlayer;
	private ProgressDialog buffering;
	private Uri uri;
	private String path;
@Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	
	checkOS();
	
	setContentView(R.layout.activity_player);
	
	surfaceView=(SurfaceView)findViewById(R.id.surfaceView);
	getWindow().setFormat(PixelFormat.UNKNOWN);
	surfaceHolder = surfaceView.getHolder();
	surfaceHolder.addCallback(this);
	surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

//	setupMediaPlayer();
	mediaPlayer = new MediaPlayer();
	mediaPlayer.setScreenOnWhilePlaying(true);

	// Get the path from an activity
	Bundle bundle = getIntent().getExtras();
	if (bundle.getString("liveStreamingLink") != null) {
		path = bundle.getString("liveStreamingLink");
		uri = Uri.parse(path);
	}
	Toast.makeText(getApplicationContext(),uri.toString(), 3000).show();
	// Create a progressbar
	buffering = new ProgressDialog(Player.this);
	// Set progressbar title
	buffering.setTitle("Screen Share");
	// Set progressbar message
//	buffering.setMessage("Buffering...");
	buffering.setIndeterminate(false);
//	buffering.setCancelable(false);
	// Show progressbar
	buffering.show();
}

private void setupMediaPlayer() {
	mediaPlayer = new MediaPlayer();
	mediaPlayer.setScreenOnWhilePlaying(true);
}

private void checkOS() {
	if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
//	    getActionBar().hide();
	}
}

@Override
public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
	// TODO Auto-generated method stub
	
}

@Override
public void surfaceCreated(SurfaceHolder arg0) {
	mediaPlayer.reset();
	mediaPlayer.setDisplay(surfaceHolder);
	try {
		mediaPlayer.setDataSource(getApplicationContext(), uri);
		mediaPlayer.prepare();
	} catch (IllegalArgumentException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (SecurityException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IllegalStateException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	mediaPlayer.setOnPreparedListener(new OnPreparedListener() {
		
		@Override
		public void onPrepared(MediaPlayer mp) {
			buffering.dismiss();
			mediaPlayer.start();
//			setTotalTimeLabel();
//			updateProgressBar();				
		}
	});
}	


@Override
public void surfaceDestroyed(SurfaceHolder arg0) {
	// TODO Auto-generated method stub
	
}

@Override
public void onBackPressed() {
	// TODO Auto-generated method stub
	super.onBackPressed();
	finish();
}

}
