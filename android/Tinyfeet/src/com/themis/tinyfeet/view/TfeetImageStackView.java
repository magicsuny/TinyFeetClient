package com.themis.tinyfeet.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import com.themis.tinyfeet.R;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class TfeetImageStackView extends RelativeLayout {
	private static final String LOG_TAG = "TfeetImageStackView";
	private static String TEMP_STORGE_PATH_DIR = "/temp/";
	private ImageView imageView;
	private ProgressBar progressBar;
	private Context context;
	private String TEMP_STORGE_PATH_FILE;
	private URL url;
	private Bitmap bitmap;

	public TfeetImageStackView(Context context) {
		super(context);
		this.context = context;
		init();

	}

	public TfeetImageStackView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		init();
	}

	public TfeetImageStackView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init();
	}

	public void init() {
		LayoutInflater.from(context).inflate(R.layout.tfeet_imagestack_view,
				this, true);
		imageView = (ImageView) findViewById(R.id.imageView);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
	}

	/**
	 * bind ImageUrl
	 * 
	 * @throws MalformedURLException
	 */
	public void bindUrl(String urlString) throws MalformedURLException,
			IOException {
		bindUrl(new URL(urlString));
	}

	/**
	 * bind ImageURL
	 * 
	 * @throws IOException
	 */
	public void bindUrl(URL url) throws IOException {
		Log.v(LOG_TAG, "bindURL...");
		this.url = url;
		String[] urlArr = url.toString().split("/");
		TEMP_STORGE_PATH_FILE = TEMP_STORGE_PATH_DIR
				+ urlArr[urlArr.length - 1];
		DownloadTask dTask = new DownloadTask();
		dTask.execute(url);
	}

	class DownloadTask extends AsyncTask<URL, Long, String> {
		@Override
		protected String doInBackground(URL... params) {
			// TODO get bitmap and set update process signal
			/* 取得连接 */
			HttpURLConnection conn;
			File tmpFile = null;
			try {
				conn = (HttpURLConnection) url.openConnection();
				conn.connect();
				/* 取得返回的InputStream */
				InputStream is = conn.getInputStream();
				long size = conn.getContentLength();
				Log.v(LOG_TAG, "文件大小：" + size);
				tmpFile = new File(Environment.getExternalStorageDirectory()
						+ TEMP_STORGE_PATH_FILE);
				boolean needDownload = true;
				// 存在，判断大小是否一致
				if (tmpFile.exists()) {
					FileInputStream fips = new FileInputStream(tmpFile);
					long tmpSize = fips.available();
					Log.v(LOG_TAG, "已存在文件大小：" + tmpSize);
					fips.close();
					// 一致，跳过下载
					if (tmpSize == size) {
						Log.v(LOG_TAG, "already downloaded");
						needDownload = false;
					} else {
						tmpFile.delete();
						Log.e(LOG_TAG, "deleted the same file");
					}
				}
				if (needDownload) {
					tmpFile.createNewFile();
					FileOutputStream fops = new FileOutputStream(tmpFile);
					int onceSize = 1024 * 4;
					byte[] buffer = new byte[onceSize];
					// 计算固定大小的缓冲区要读多少次
					int readNum = (int) Math.floor(size / onceSize);
					// 得到剩余的字节长度
					int leave = (int) (size - readNum * onceSize);
					int length = 0;
					int readed = 0;
					for (int i = readNum; i > 0; i--) {
						length = is.read(buffer);
						fops.write(buffer);
						fops.flush();
						readed += length;
						publishProgress((readed * 100) / size);
						Log.v(LOG_TAG, "readed");
					}
					buffer = new byte[leave];
					length = is.read(buffer);
					fops.write(buffer);
					fops.flush();
					readed += length;
					publishProgress((readed * 100) / size);
					Log.v(LOG_TAG, "readed");
					fops.close();
					is.close();
				} else {
					// 固定的显示一个过程
					publishProgress(80L);
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
			return tmpFile.toString();
		}
		
		@Override
		protected void onProgressUpdate(Long... values) {
			// TODO update ProgressBar
			progressBar.setProgress(values[0].intValue());
			super.onProgressUpdate(values);
		}

		@Override
		protected void onPostExecute(String tmpFileStr) {
			// TODO show bitmap
			super.onPostExecute(tmpFileStr);
		     //读取文件
			File tmpFile = new File(tmpFileStr);
		    try {
				bitmap = BitmapFactory.decodeStream(new FileInputStream(tmpFile));
				Log.v(LOG_TAG, "bitmapPath:"+tmpFileStr);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	      //更新显示
	      progressBar.setVisibility(View.GONE);
	      imageView.setVisibility(View.VISIBLE);
	      imageView.setImageBitmap(bitmap);
		}
	}
}
