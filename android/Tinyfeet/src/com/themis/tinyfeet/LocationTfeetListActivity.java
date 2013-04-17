package com.themis.tinyfeet;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.*;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.themis.tinyfeet.bo.TfeetBO;
import com.themis.tinyfeet.service.TfeetListService;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sunharuka
 * Date: 13-4-15
 * Time: 下午8:53
 * To change this template use File | Settings | File Templates.
 */
public class LocationTfeetListActivity extends Activity {
    private Handler loadLocTfeetListhandler;
    private TextView tv;
    private Button button;
    private ImageView imageView;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locationtfeetlist);
        tv = (TextView)this.findViewById(R.id.serviceTextView);
        button = (Button)this.findViewById(R.id.takePhotoBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePictureIntent,0);
            }
        });
        imageView = (ImageView)this.findViewById(R.id.takePhotoBack);
        loadLocTfeetListhandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                List<TfeetBO> path = (List<TfeetBO>)msg.obj;
                if (msg.arg1 == RESULT_OK && path != null) {
                    tv.setText(path.toString());
                    Toast.makeText(LocationTfeetListActivity.this,
                            "Downloaded" + path.toString(), Toast.LENGTH_LONG)
                            .show();
                } else {
                    Toast.makeText(LocationTfeetListActivity.this, "Download failed.",
                            Toast.LENGTH_LONG).show();
                }
            }
        };

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bundle extras = data.getExtras();
        Bitmap photo = (Bitmap)extras.get("data");
        imageView.setImageBitmap(photo);
    }

    @Override
    protected void onStart(){
        super.onStart();
        Intent intent = new Intent(this,TfeetListService.class);
        intent.putExtra("messenger",new Messenger(this.loadLocTfeetListhandler));
        startService(intent);

    }

}