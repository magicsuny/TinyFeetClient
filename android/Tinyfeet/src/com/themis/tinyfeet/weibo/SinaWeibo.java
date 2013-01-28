package com.themis.tinyfeet.weibo;

import android.content.Context;
import android.util.Log;

import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.Weibo;
import com.weibo.sdk.android.WeiboAuthListener;

public class SinaWeibo {
	private Weibo mWeibo;
	private static final String CONSUMER_KEY = "2878395190";
	private static final String REDIRECT_URL = "http://tinyfeet.us/callback";
	public static Oauth2AccessToken accessToken;
	public static final String TAG = "sinasdk";

	public SinaWeibo() {
		mWeibo = Weibo.getInstance(CONSUMER_KEY, REDIRECT_URL);
	}

	public void authorize(Context context, WeiboAuthListener listener) {
		mWeibo.authorize(context, listener);
		Log.i(TAG, "新浪微博认证中...");
	}

}
