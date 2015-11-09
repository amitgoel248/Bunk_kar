package com.tutecentral.BUNK;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


public class splashscreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        Thread thread = new Thread() {

            public void run() {
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();

                } finally {
                    Intent intent = new Intent("com.tutecentral.BUNK.MAINACTIVITY");
                    startActivity(intent);
                }
            }
        };
        thread.start();
    }

    protected void onPause()
    {
        super.onPause();
        finish();
    }

}
