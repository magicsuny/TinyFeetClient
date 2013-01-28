package com.themis.tinyfeet;

import java.util.Stack;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.util.Log;

public class TinyFeetApplication extends Application implements
		OnSharedPreferenceChangeListener {
	private final static String TAG = TinyFeetApplication.class.getSimpleName();
	private Stack<Activity> activityStack;
	private static TinyFeetApplication instance;

	public static TinyFeetApplication getInstance() {
		if (instance == null) {
			instance = new TinyFeetApplication();
		}
		return instance;
	}

	/**
	 * 将启动的activity压入栈中
	 * 
	 * @param activity
	 */
	public void addActivity(Activity activity) {
		if (activityStack == null) {
			activityStack = new Stack<Activity>();
		}
		activityStack.add(activity);
	}

	/**
	 * 获取当前activity
	 * 
	 * @return
	 */
	public Activity getCurrentActivity() {
		return activityStack.lastElement();
	}

	/**
	 * 结束当前activity
	 */
	public void finishCurrentActivity() {
		Activity currentActivity = activityStack.lastElement();
		if (currentActivity != null) {
			currentActivity.finish();
			currentActivity.overridePendingTransition(0, 0);
			currentActivity = null;
		}
	}

	/**
	 * 结束所有的activity
	 */
	public void finishAllActivity() {
		for (int i = 0; i < activityStack.size(); i++) {
			if (activityStack.get(i) != null) {
				activityStack.get(i).finish();
			}
		}
		activityStack.clear();
	}

	/**
	 * 退出应用
	 * @param context
	 */
	public void appExit(Context context) {
		try {
			finishAllActivity();
			ActivityManager activityMgr = (ActivityManager) context
					.getSystemService(Context.ACTIVITY_SERVICE);
			activityMgr.restartPackage(context.getPackageName());
			System.exit(0);
		} catch (Exception e) {
		}
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		Log.i(TAG, "内容变更");
	}

	@Override
	public void onCreate() {
		Log.i(TAG, "启动");
		super.onCreate();
	}

	@Override
	public void onTerminate() {
		Log.i(TAG, "终止");
		super.onTerminate();
	}

}
