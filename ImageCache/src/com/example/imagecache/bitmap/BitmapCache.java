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
 * @category ͼƬ���棬�ڴ滺��ͱ��ش洢
 * 
 */
public class BitmapCache implements ImageCache {

	private LruCache<String, Bitmap> mMemoryCache;// �ڴ滺��
	private String externalCacheDir;// SD������Ŀ¼

	public BitmapCache(String externalCacheDir) {
		this.externalCacheDir = externalCacheDir;
		initLruCache();
	}

	/**
	 * �����ڴ滺��
	 */
	private void initLruCache() {
		// LruCacheͨ�����캯�����뻺��ֵ����KBΪ��λ��
		Runtime runtime = Runtime.getRuntime();
		int maxMemory = (int) runtime.maxMemory();
		// ʹ���������ڴ�ֵ��1/8��Ϊ����Ĵ�С��
		int cacheSize = maxMemory / 1024 / 8;
		mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
			@SuppressLint("NewApi")
			@Override
			protected int sizeOf(String key, Bitmap bitmap) {
				// ��д�˷���������ÿ��ͼƬ�Ĵ�С��Ĭ�Ϸ���ͼƬ������
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
	 * �Ա��ػ���Ĳ���
	 */
	private Bitmap getBitmapFromSD(String url) {
		// SD��������
		if (externalCacheDir == null) {
			return null;
		}
		// ��ȡ�ļ���
		int begin = url.lastIndexOf("/");
		String bitmapName = url.substring(begin + 1);

		File cacheDir = new File(externalCacheDir);
		File[] cacheFiles = cacheDir.listFiles();
		if (cacheFiles == null) {
			return null;
		}

		// ���ұ����ļ�
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
	 * �����ļ������ػ���
	 */
	private void putBitmapToSD(String url, Bitmap bitmap) {

		// SD��������
		if (externalCacheDir == null) {
			return;
		}

		// ��ȡ�ļ���
		int begin = url.lastIndexOf("/");
		String bitmapName = url.substring(begin + 1);

		String fileName = externalCacheDir + "/" + bitmapName;
		File bitmapFile = new File(fileName);

		// ����
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
