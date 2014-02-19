package com.qplaytech.screenshare;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;

public class SplashScreen extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		checkOS();
		setContentView(R.layout.activity_splash_screen);

/*		// Setting the activity fullscreen without actionbar
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		ActionBar actionBar = getActionBar();
		actionBar.hide();
*/
		Thread thrd=new Thread(){
			public void run(){
				try {
					sleep(2000);
					Intent next=new Intent(getApplicationContext(),Login.class);
					startActivity(next);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				finally{
					finish();
				}
				
			}
		};
		thrd.start();

		
	}

	private void checkOS() {
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
			getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
//		    getActionBar().hide();
		}
	}

}
