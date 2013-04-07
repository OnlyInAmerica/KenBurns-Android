package net.openwatch.kenburns;

import java.util.Timer;
import java.util.TimerTask;

import android.os.Bundle;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class MainActivity extends Activity {
	
	ImageView image_1;
	ImageView image_2;
	
	Timer timer;
		
	Animation zoom;
		
	long animation_clock = 5000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.activity_main);
		
		image_1 = (ImageView) findViewById(R.id.image_1);
		image_2 = (ImageView) findViewById(R.id.image_2);
		
		zoom = AnimationUtils.loadAnimation(this, R.anim.zoom);
	}
	
	@Override
	public void onResume(){
		super.onResume();
		timer = new Timer(); 
		timer.scheduleAtFixedRate(new FadeTimerTask(), animation_clock, animation_clock);
	}
	
	@Override
	public void onPause(){
		super.onPause();
		timer.cancel();
		timer = null;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private void crossfade(){
		if(image_2.getVisibility() == View.VISIBLE){
    		_crossfade(image_1, image_2);
    	}
    	else{
    		_crossfade(image_2, image_1);
    	}
	}
	
	@SuppressLint("NewApi")
	private void _crossfade(View fadeIn, final View fadeOut) {

	    // Set the content view to 0% opacity but visible, so that it is visible
	    // (but fully transparent) during the animation.
		Log.i("FadeIn", "FadeOut alpha: " + String.valueOf(fadeOut.getAlpha()));
		fadeIn.setAlpha(0f);
		fadeIn.setVisibility(View.VISIBLE);

	    // Animate the content view to 100% opacity, and clear any animation
	    // listener set on the view.
		fadeIn.animate()
	            .alpha(1f)
	            .setDuration(1000)
	            .setListener(null).start();
		
		// Zoom content view
		
		fadeIn.animate()
				.scaleX((float) 1.10)
				.scaleY((float) 1.10)
				.setDuration(animation_clock - 50).start();
		 
	    // Animate the loading view to 0% opacity. After the animation ends,
	    // set its visibility to GONE as an optimization step (it won't
	    // participate in layout passes, etc.)
		Log.i("FadeOut", "FadeIn alpha: " + String.valueOf(fadeIn.getAlpha()));
		fadeOut.animate()
	            .alpha(0f)
	            .setDuration(1000)
	            .setListener(new AnimatorListenerAdapter() {
	                @Override
	                public void onAnimationEnd(Animator animation) {
	                	fadeOut.setVisibility(View.GONE);
	                	fadeOut.setScaleX((float)1.0);
	            		fadeOut.setScaleY((float)1.0);
	                }
	            }).start();
	}
	
	private class FadeTimerTask extends TimerTask{

        @Override
        public void run() {
            runOnUiThread(new Runnable() {              
                @Override
                public void run() {
                	crossfade();
                }
            });
        }       
    }

}
