package com.themis.tinyfeet.service;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Binder;
import android.os.IBinder;
import android.util.Base64;
import android.util.Log;
import com.themis.tinyfeet.Constants;
import com.themis.tinyfeet.db.TfeetDbHelper;
import com.themis.tinyfeet.db.model.TfeetColumn;
import com.themis.tinyfeet.proto.Tfeet;
import com.themis.tinyfeet.utils.HttpUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created with IntelliJ IDEA.
 * User: sunharuka
 * Date: 13-4-9
 * Time: 下午9:14
 * To change this template use File | Settings | File Templates.
 */
public class TfeetListService extends IntentService implements ITfeetListServ{
    private static final String TAG = "TfeetListService";
    private ServiceBinder serviceBinder = new ServiceBinder();

    public TfeetListService() {
        super("TfeetList");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            String responseStr = HttpUtils.getResponseForGet(Constants.TFEET_LOC_LIST,this);
            if(responseStr!=null){
                TfeetDbHelper tfeetDbHelper = new TfeetDbHelper(this);
                Tfeet.TFeetList tfeetList = Tfeet.TFeetList.parseFrom(Base64.decode(responseStr,Base64.DEFAULT));
                for(Tfeet.TFeet tfeet:tfeetList.getTfeetList()){
                    ContentValues values = TfeetColumn.fillValues(tfeet);
                    tfeetDbHelper.insert(values);
                }

                //

            }
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public IBinder onBind(Intent intent) {
        return serviceBinder;
    }

    public class ServiceBinder extends Binder implements ITfeetListServ {

    }
}
