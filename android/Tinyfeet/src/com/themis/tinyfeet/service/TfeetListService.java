package com.themis.tinyfeet.service;

import android.app.Activity;
import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.os.*;
import android.util.Log;
import com.themis.tinyfeet.apiService.TfeetAPI;
import com.themis.tinyfeet.bo.TfeetBO;
import com.themis.tinyfeet.db.TfeetDbHelper;
import com.themis.tinyfeet.db.model.TfeetColumn;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sunharuka
 * Date: 13-4-9
 * Time: 下午9:14
 * To change this template use File | Settings | File Templates.
 */
public class TfeetListService extends IntentService{
    private static final String TAG = "TfeetListService";
    public TfeetListService() {
        super("TfeetList");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
            Bundle bundle = intent.getExtras();
            Messenger messenger =  (Messenger)bundle.get("messenger");
            Message msg = Message.obtain();

            //List<TfeetBO> tfeetList = TfeetAPI.getLocTfeetList(100, 200, 1, 0, 0);
            List<TfeetBO> tfeetList = new ArrayList<TfeetBO>();

            msg.arg1 = Activity.RESULT_OK;
            msg.obj = tfeetList;
            try {
                messenger.send(msg);
            } catch (android.os.RemoteException e1) {
                Log.w(TAG, "Exception sending message", e1);
            }
    }

}
