package com.victordev.urban.urbanpty;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashScreenAct extends AppCompatActivity implements Runnable {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        new Thread(this).start();
    }

    @Override
    public void run() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        goToMainActivity();
        finish();
    }

    private void goToMainActivity() {
        startActivity(new Intent(getApplicationContext(), Main2Activity.class));
    }
}
