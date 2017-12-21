package com.rossia.life.crashhandlersample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.rossia.life.crash.CrashHandler;

/**
 * @author pd_liu 2017/12/21.
 *         <p>
 *         演示异常处理的功能
 *         </p>
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void start(View view){
        CrashHandler.getInstance().startHandler(this);
        CrashHandler.getInstance().setDebug(true);
        startActivity(new Intent(this, CrashActivity.class));
    }

    @Override
    protected void onResume() {
        super.onResume();

        CrashHandler.getInstance().setCurrentActivity(this);
    }
}
