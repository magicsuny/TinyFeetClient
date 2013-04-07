package com.themis.tinyfeet;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.widget.GridView;
import android.widget.ImageView;
import com.themis.tinyfeet.bo.TfeetStack;
import com.themis.tinyfeet.utils.Utils;
import com.themis.tinyfeet.view.TifeetImageStackAdapter;
import com.themis.tinyfeet.weibo.AccessTokenKeeper;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.api.UsersAPI;
import com.weibo.sdk.android.net.RequestListener;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DiscoveryActivity extends Activity {
	private ImageView userAvatarIv;
	private GridView tfeetsGridView;
	private Handler updateWeiboHandler;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_discovery);
		TinyFeetApplication.getInstance().addActivity(this);
		SharedPreferences sharedata = getSharedPreferences("data", 0);
		String currentUid = sharedata.getString(Constants.SP_CURRENT_UID, null);
		UsersAPI userAPI = new UsersAPI(AccessTokenKeeper.readAccessToken(this));
		userAvatarIv = (ImageView) findViewById(R.id.avatarImageView);
		updateWeiboHandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				Bundle b = msg.getData();
	            String userName = b.getString("userName");
	            String avatarUrl = b.getString("avatarUrl");
	            Bitmap bitmap = Utils.returnBitMap(avatarUrl);
	            bitmap = Utils.getRoundedCornerBitmap(bitmap);
				userAvatarIv.setImageBitmap(bitmap);    
			}
		};
		userAPI.show(Long.parseLong(currentUid), new RequestListener() {
			@Override
			public void onComplete(String arg0) {
				try {
					JSONTokener jsonParser = new JSONTokener(arg0);
					JSONObject userInfo = (JSONObject) jsonParser.nextValue();
					String userName = userInfo.getString("name");
					String avatarUrl = userInfo.getString("profile_image_url");
					Message msg = new Message();
		            Bundle b = new Bundle();// 存放数据
		            b.putString("userName", userName);
		            b.putString("avatarUrl", avatarUrl);
		            msg.setData(b);

		            DiscoveryActivity.this.updateWeiboHandler.sendMessage(msg); 
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

			@Override
			public void onIOException(IOException arg0) {

			}

			@Override
			public void onError(WeiboException arg0) {

			}

		});
		tfeetsGridView = (GridView)this.findViewById(R.id.tfeetStackGridView);
		String picUrl = "http://pan.baidu.com/res/static/images/yun_pan_logo.gif";
		List<TfeetStack> tfeetStackList = new ArrayList<TfeetStack>();
		for(int i=0;i<20;i++){
			TfeetStack tmpStack = new TfeetStack();
			double num = Math.random()*5;
			List<String> tmpUrlList = new ArrayList<String>();
			for(int j=0;j<num;j++){
				tmpUrlList.add(picUrl);
			}
			tmpStack.setTfeetList(tmpUrlList);
			tfeetStackList.add(tmpStack);
		}
		TifeetImageStackAdapter adapter = new TifeetImageStackAdapter(this,tfeetStackList);
		tfeetsGridView.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_discovery, menu);
		return true;
	}

}
