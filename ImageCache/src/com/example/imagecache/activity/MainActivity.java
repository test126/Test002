package com.example.imagecache.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.util.LruCache;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.android.volley.toolbox.Volley;
import com.example.imagecache.R;
import com.example.imagecache.bitmap.BitmapCache;
import com.example.imagecache.bitmap.MyImageLoader;

public class MainActivity extends ActionBarActivity {
	MyImageLoader mImageLoader;
	String url = "http://admin.schu.cc:80/attached/common/1421219933820.jpg";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ImageView imageView = (ImageView) findViewById(R.id.imageView);
		mImageLoader = new MyImageLoader(this);
		mImageLoader.getBitmapFromUrl(url, imageView, R.drawable.ic_launcher);
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasFocus);
		new Handler().postDelayed(new Runnable(){
			public void run() {
				ImageView imageView1 = (ImageView) findViewById(R.id.imageView1);
				mImageLoader.getBitmapFromUrl(url, imageView1, R.drawable.ic_launcher);
			}
			
		}, 3000);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
