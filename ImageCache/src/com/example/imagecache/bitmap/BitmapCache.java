package com.example.imagecache.bitmap;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.util.LruCache;
import com.android.volley.toolbox.ImageLoader.ImageCache;

/**
 * 
 * @author ching yuan
 * @category 图片缓存，内存缓存和本地存储
 * 
 */
public class BitmapCache implements ImageCache {

	private LruCache<String, Bitmap> mMemoryCache;// 内存缓存
	private String externalCacheDir;// SD卡缓存目录

	public BitmapCache(String externalCacheDir) {
		this.externalCacheDir = externalCacheDir;
		initLruCache();
	}

	/**
	 * 设置内存缓存
	 */
	private void initLruCache() {
		// LruCache通过构造函数传入缓存值，以KB为单位。
		Runtime runtime = Runtime.getRuntime();
		int maxMemory = (int) runtime.maxMemory();
		// 使用最大可用内存值的1/8作为缓存的大小。
		int cacheSize = maxMemory / 1024 / 8;
		mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
			@SuppressLint("NewApi")
			@Override
			protected int sizeOf(String key, Bitmap bitmap) {
				// 重写此方法来衡量每张图片的大小，默认返回图片数量。
				int size = bitmap.getByteCount();
				return size / 1024;
			}
		};
	}

	@Override
	public Bitmap getBitmap(String url) {
		Bitmap mBitmap = mMemoryCache.get(url);
		if (mBitmap == null) {
			return getBitmapFromSD(url);
		} else {
			return mMemoryCache.get(url);
		}
	}

	@Override
	public void putBitmap(String url, Bitmap bitmap) {
		mMemoryCache.put(url, bitmap);
		putBitmapToSD(url, bitmap);
	}

	/**
	 * 对本地缓存的查找
	 */
	private Bitmap getBitmapFromSD(String url) {
		// SD卡不可用
		if (externalCacheDir == null) {
			return null;
		}
		// 截取文件名
		int begin = url.lastIndexOf("/");
		String bitmapName = url.substring(begin + 1);

		File cacheDir = new File(externalCacheDir);
		File[] cacheFiles = cacheDir.listFiles();
		if (cacheFiles == null) {
			return null;
		}

		// 查找本地文件
		for (int i = 0; i < cacheFiles.length; i++) {
			String fileName = cacheFiles[i].getName();
			if (bitmapName.equals(fileName)) {
				String fileUrl = externalCacheDir + bitmapName;
				return BitmapFactory.decodeFile(fileUrl);
			}
		}
		return null;

	}

	/**
	 * 保存文件到本地缓存
	 */
	private void putBitmapToSD(String url, Bitmap bitmap) {

		// SD卡不可用
		if (externalCacheDir == null) {
			return;
		}

		// 截取文件名
		int begin = url.lastIndexOf("/");
		String bitmapName = url.substring(begin + 1);

		String fileName = externalCacheDir + "/" + bitmapName;
		File bitmapFile = new File(fileName);

		// 保存
		try {
			if (!bitmapFile.exists()) {
				bitmapFile.createNewFile();
			}
			FileOutputStream fos = new FileOutputStream(bitmapFile);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
