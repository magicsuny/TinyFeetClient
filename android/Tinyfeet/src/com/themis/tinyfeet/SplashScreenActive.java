package com.themis.tinyfeet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;

public class SplashScreenActive extends Activity {
    public static final String TAG = "SplashScreenActive";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash_screen_active);
        //将该Activity压入全局管理栈中
        TinyFeetApplication.getInstance().addActivity(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashScreenActive.this,
                        LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                SplashScreenActive.this.startActivity(i); // 启动Main界面
                TinyFeetApplication.getInstance().finishCurrentActivity(); // 关闭自己这个开场屏
            }
        }, 3000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_splash_screen_active, menu);
        return true;
    }
}
