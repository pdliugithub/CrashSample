package com.rossia.life.crashhandlersample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.rossia.life.crash.CrashHandler;

/**
 * @author pd_liu 2017/12/31.
 *         <p>
 *         测试异常的处理效果
 *         </p>
 */
public class CrashActivity extends AppCompatActivity {

    private View.OnClickListener mOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (R.id.thread_btn == v.getId()) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        throw new NullPointerException();
                    }
                }).start();
                return;
            }

            if (R.id.main_btn == v.getId()) {
                throw new NullPointerException();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crash);

        Button threadBtn = (Button) findViewById(R.id.thread_btn);
        Button mainBtn = (Button) findViewById(R.id.main_btn);

        threadBtn.setOnClickListener(mOnClick);
        mainBtn.setOnClickListener(mOnClick);
    }

    @Override
    protected void onResume() {
        super.onResume();

        CrashHandler.getInstance().setCurrentActivity(this);
    }
}
