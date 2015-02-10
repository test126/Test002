package com.example.imagecache.bitmap;
import java.io.File;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.android.volley.toolbox.Volley;
import android.content.Context;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.ImageView;

public class MyImageLoader {

	private Context context = null;
	private int width, height;
	private ImageLoader imageLoader;
	public MyImageLoader(Context context) {
		this.context = context;
		initWidthHeight();
		RequestQueue mQueue = Volley.newRequestQueue(context);
		BitmapCache bitmapCache = new BitmapCache(getSDCacheDir());
		imageLoader = new ImageLoader(mQueue, bitmapCache);
	}

	/**
	 * ��ȡ��Ļ��ߣ�ʹ����ͼƬ�����ڸóߴ�
	 */
	private void initWidthHeight() {
		WindowManager manager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		manager.getDefaultDisplay().getMetrics(dm);
		width = dm.widthPixels;
		height = dm.heightPixels;
	}

	/**
	 * 
	 * @return ��ȡ�ⲿ�洢������Ŀ¼
	 */
	private String getSDCacheDir() {
		String externalCacheDir = null;
		String sdState = Environment.getExternalStorageState();
		if (sdState.equals(Environment.MEDIA_MOUNTED)) {
			File cacheDir = context.getExternalCacheDir();
			externalCacheDir = cacheDir.getAbsolutePath();
		}
		return externalCacheDir;
	}

	public void getBitmapFromUrl(String url, ImageView imgView, int defId) {
		ImageListener imageListener = ImageLoader.getImageListener(imgView,
				defId, defId);
		imageLoader.get(url, imageListener, width, height);
	}

}
