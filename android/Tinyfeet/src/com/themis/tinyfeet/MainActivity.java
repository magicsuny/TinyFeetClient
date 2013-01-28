package com.themis.tinyfeet;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.protobuf.InvalidProtocolBufferException;
import com.themis.tinyfeet.proto.AddressBookProtos.AddressBook;
import com.themis.tinyfeet.proto.AddressBookProtos.Person;
import com.themis.tinyfeet.utils.Utils;
import com.themis.tinyfeet.weibo.AccessTokenKeeper;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.api.UsersAPI;
import com.weibo.sdk.android.net.RequestListener;

public class MainActivity extends FragmentActivity {
	private ImageView userAvatarIv;
	private TextView userNameTv;
	private TextView lnglatTxt;
	private TextView addressTxt;
	private Button locationBtn;
	private LocationManager mLocationManager;
	private Handler updateWeiboHandler;
	private Handler updateLocationHandler;
	private final LocationListener listener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
        	 Message.obtain(updateLocationHandler,
        			 0,
                     location.getLatitude() + "," + location.getLongitude()).sendToTarget();
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };
    private TextView protoIdTxt;
    private TextView protoNameTxt;
    private TextView protoCodeTxt;
    private TextView convertTxt;
    private Button genProtoBtn;
    
    
	
	@Override
	protected void onStart() {
		super.onStart();
		LocationManager locationManager =
                (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        final boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!gpsEnabled) {
        	new EnableGpsDialogFragment().show(getSupportFragmentManager(), "enableGpsDialog");
        }
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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//将该Activity压入全局管理栈中
		TinyFeetApplication.getInstance().addActivity(this);
		SharedPreferences sharedata = getSharedPreferences("data", 0);
		String currentUid = sharedata.getString(Constants.SP_CURRENT_UID, null);
		UsersAPI userAPI = new UsersAPI(AccessTokenKeeper.readAccessToken(this));
		userAvatarIv = (ImageView) findViewById(R.id.userAvatarIv);
		userNameTv = (TextView) findViewById(R.id.userNameTv);
		lnglatTxt = (TextView) findViewById(R.id.lnglatTxt);
		addressTxt = (TextView)findViewById(R.id.addressTxt);
		locationBtn = (Button)findViewById(R.id.getLocationBtn);
		updateWeiboHandler = new Handler(){ 
			@Override
			public void handleMessage(Message msg) { 
				Bundle b = msg.getData();
	            String userName = b.getString("userName");
	            String avatarUrl = b.getString("avatarUrl");
	            Bitmap bitmap = Utils.returnBitMap(avatarUrl);
	            bitmap = Utils.getRoundedCornerBitmap(bitmap);
				userAvatarIv.setImageBitmap(bitmap);    
				userNameTv.setText(userName);
			}
		};
		updateLocationHandler = new Handler(){
			 public void handleMessage(Message msg) {
	                switch (msg.what) {
	                    case 0:
	                    	addressTxt.setText((String) msg.obj);
	                        break;
	                    case 1:
	                    	lnglatTxt.setText((String) msg.obj);
	                        break;
	                }
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

		            MainActivity.this.updateWeiboHandler.sendMessage(msg); 
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
		mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		
		
	    protoIdTxt =(TextView)findViewById(R.id.protoIdTxt);
	    protoNameTxt =(TextView)findViewById(R.id.protoNameTxt);
	    protoCodeTxt =(TextView)findViewById(R.id.protoCodeTxt);
	    convertTxt = (TextView)findViewById(R.id.convertTxt);
	    genProtoBtn = (Button)findViewById(R.id.genProtoBtn);
		
	}
	
    public void useCoarseFineProviders(View v) {
    	Location  gpsLocation = requestUpdatesFromProvider(
                  LocationManager.GPS_PROVIDER, R.string.not_support_gps);
        Location  networkLocation = requestUpdatesFromProvider(
                  LocationManager.NETWORK_PROVIDER, R.string.not_support_network);
        if (gpsLocation != null && networkLocation != null) {
            updateUILocation(Utils.getBetterLocation(gpsLocation, networkLocation));
        } else if (gpsLocation != null) {
            updateUILocation(gpsLocation);
        } else if (networkLocation != null) {
            updateUILocation(networkLocation);
        }
    }	
    
    private void updateUILocation(Location location) {
        // We're sending the update to a handler which then updates the UI with the new
        // location.
        Message.obtain(updateLocationHandler,
                0,
                location.getLatitude() + ", " + location.getLongitude()).sendToTarget();
    }
    
    private Location requestUpdatesFromProvider(final String provider, final int errorResId) {
        Location location = null;
        if (mLocationManager.isProviderEnabled(provider)) {
            mLocationManager.requestLocationUpdates(provider, 10000, 10, listener);
            location = mLocationManager.getLastKnownLocation(provider);
        } else {
            Toast.makeText(this, errorResId, Toast.LENGTH_LONG).show();
        }
        return location;
    }

	
	public void updateProtoCode(View v){
		String protoId = this.protoIdTxt.getText().toString();
		String protoName = this.protoNameTxt.getText().toString();
		Person.Builder person = Person.newBuilder();
		person.setId(Integer.valueOf(protoId));
		person.setName(protoName);
		person.setEmail("sunyao02@baidu.com");
		AddressBook.Builder addressBook = AddressBook.newBuilder();
		
		addressBook.addPerson(person);
		byte[] content = addressBook.build().toByteArray();
		this.protoCodeTxt.setText(addressBook.build().toByteArray().toString());
		try {
			AddressBook addressBookConvert = AddressBook.parseFrom(content);
			this.convertTxt.setText(addressBookConvert.toString());
		} catch (InvalidProtocolBufferException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	 /**
     * Dialog to prompt users to enable GPS on the device.
     */
    private class EnableGpsDialogFragment extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.enable_gps)
                    .setMessage(R.string.enable_gps_dialog)
                    .setPositiveButton(R.string.enable_gps, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        	 Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                             startActivity(settingsIntent);;
                        }
                    })
                    .create();
        }
    }
    
    
	
}
