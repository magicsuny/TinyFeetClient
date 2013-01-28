package com.themis.tinyfeet;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;

import com.themis.tinyfeet.weibo.AccessTokenKeeper;
import com.themis.tinyfeet.weibo.SinaWeibo;
import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.WeiboAuthListener;
import com.weibo.sdk.android.WeiboDialogError;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.api.AccountAPI;
import com.weibo.sdk.android.net.RequestListener;

public class LoginActivity extends Activity {
	public static Oauth2AccessToken accessToken ;
	public ImageButton loginBtn;
	private static SinaWeibo sinaWeibo;
	public static final String TAG = "LoginActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
		//将该Activity压入全局管理栈中
		TinyFeetApplication.getInstance().addActivity(this);
        sinaWeibo = new SinaWeibo();
        loginBtn = (ImageButton)findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				sinaWeibo.authorize(LoginActivity.this, new AuthDialogListener());
			}
		});
    }

    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case R.id.itemExit:
			TinyFeetApplication.getInstance().appExit(this);
		}
		return super.onOptionsItemSelected(item);
	}




	class AuthDialogListener implements WeiboAuthListener {
		@Override
		public void onComplete(Bundle values) {
			String token = values.getString("access_token");
			String expires_in = values.getString("expires_in");
			accessToken = new Oauth2AccessToken(token, expires_in);
			if (accessToken.isSessionValid()) {
				AccountAPI accountApi = new AccountAPI(accessToken);
				accountApi.getUid(new RequestListener(){

					@Override
					public void onComplete(String arg0) {
						try {
							JSONTokener jsonParser = new JSONTokener(arg0);
							JSONObject person = (JSONObject) jsonParser.nextValue();
							String uid = person.get("uid").toString();
							SharedPreferences.Editor sharedata = getSharedPreferences("data", 0).edit();  
							sharedata.putString(Constants.SP_CURRENT_UID,uid);  
							sharedata.commit();
							Log.i(TAG, "登录用户的UID是："+uid);
							//如果获取信息成功则跳转
							Intent intent=new Intent();
							intent.setClass(LoginActivity.this, DiscoveryActivity.class);
							startActivity(intent);
							TinyFeetApplication.getInstance().finishCurrentActivity(); 
						} catch (JSONException e) {
							e.printStackTrace();
						}  
					}

					@Override
					public void onError(WeiboException arg0) {
						Log.d(TAG, "获取登录用户信息发生错误："+arg0.getMessage());
					}

					@Override
					public void onIOException(IOException arg0) {
						Log.d(TAG, "IO错误："+arg0.getMessage());
					}
					
				});
				AccessTokenKeeper.keepAccessToken(LoginActivity.this, accessToken);
				Log.d(TAG, "认证成功：");
			}
		}

		@Override
		public void onError(WeiboDialogError e) {
			Toast.makeText(getApplicationContext(), "Auth error : " + e.getMessage(),
					Toast.LENGTH_LONG).show();
		}

		@Override
		public void onCancel() {
			Toast.makeText(getApplicationContext(), "Auth cancel", Toast.LENGTH_LONG).show();
		}

		@Override
		public void onWeiboException(WeiboException e) {
			Toast.makeText(getApplicationContext(), "Auth exception : " + e.getMessage(),
					Toast.LENGTH_LONG).show();
		}

	}
}
