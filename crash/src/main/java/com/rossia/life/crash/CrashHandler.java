package com.rossia.life.crash;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.rossia.life.crash.dialog.CrashDialog;
import com.rossia.life.crash.interf.CrashCallback;

/**
 * @author pd_liu on 2017/12/21.
 *         <p>
 *         异常处理者
 *         </p>
 *         <p>
 *         1、创建对象{@link #getInstance()} .
 *         2、开启{@link #startHandler(Activity)} .
 *         3、关闭{@link #stopHandler()} .
 *         4、是否Debug模式 {@link #setDebug(boolean)} .
 *         5、结果Callback {@link #setCrashCallback(CrashCallback)} .
 *         6、Activity可见时进行调用 {@link #setCurrentActivity(Activity)} .
 *         </p>
 */

public class CrashHandler {

    /**
     * 默认的捕获异常处理者.
     */
    private Thread.UncaughtExceptionHandler mDefaultUncaughtExceptionHandler;

    /**
     * ResultCallback.
     */
    private CrashCallback crashCallback;

    /**
     * Handler
     */
    private Handler mHandler;

    /**
     * 是否已经开启、运行异常处理者{@link CrashHandler}
     */
    private boolean mRunning;

    /**
     * 是否是Debug模式
     * 如果是Debug模式，则将错误信息以弹出框的形式展示
     */
    private boolean mDebug = false;

    /**
     * 显示默认的错误信息提示框
     */
    private boolean showDefaultTipDialog = true;

    private Object mLock = new Object();

    private Activity mActivity;

    private CrashHandler() {
    }

    public static CrashHandler getInstance() {
        return Holder.INSTANCE;
    }

    static class Holder {
        private static final CrashHandler INSTANCE = new CrashHandler();
    }

    public CrashHandler startHandler(Activity activity) {

        synchronized (mLock) {

            if (!mRunning) {

                mActivity = activity;

                mDefaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();

                //设置线程未捕获异常的默认处理者.
                Thread.setDefaultUncaughtExceptionHandler(mThreadWorkHandler);


                mHandler = new Handler(activity.getMainLooper());

                mRunning = true;

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {

                        while (mRunning) {
                            try {
                                Looper.loop();
                            } catch (Exception e) {
                                e.printStackTrace();
                                callback(getErrorDetail(e));
                            }
                        }
                    }
                });

            }

        }

        return this;
    }

    public CrashHandler stopHandler() {

        synchronized (mLock) {

            if (mRunning) {
                mRunning = false;

                //将异常处理的权利移交给默认的处理器
                Thread.setDefaultUncaughtExceptionHandler(mDefaultUncaughtExceptionHandler);
            }

        }

        return this;
    }

    public void setDebug(boolean debug) {
        this.mDebug = debug;
    }

    public boolean getDebug() {
        return mDebug;
    }

    public void setCurrentActivity(Activity activity) {
        this.mActivity = activity;
    }

    public void setCrashCallback(CrashCallback crashCallback) {
        this.crashCallback = crashCallback;
    }

    /**
     * 封装Callback操作
     *
     * @param errorMsg 异常信息
     */
    private void callback(final String errorMsg) {
        if (crashCallback == null) {

            if (showDefaultTipDialog && mActivity != null && mDebug) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        View dialog = LayoutInflater.from(mActivity).inflate(R.layout.dialog_crash_layout, null, true);
                        TextView title = dialog.findViewById(R.id.title);
                        title.setText(R.string.app_crash_dialog_title);

                        TextView bodyTxt = dialog.findViewById(R.id.body);
                        bodyTxt.setText(errorMsg);
                        Log.e("CrashHandler: ", errorMsg);
                        CrashDialog.show(mActivity, dialog);
                    }
                });
            }
            return;
        }
        crashCallback.callback(errorMsg);
    }

    /**
     * 用于捕获子线程中的异常
     */
    private Thread.UncaughtExceptionHandler mThreadWorkHandler = new Thread.UncaughtExceptionHandler() {
        @Override
        public void uncaughtException(Thread t, Throwable e) {
            callback(getErrorDetail(e));
        }
    };

    private String getErrorDetail(Throwable e) {

        StringBuffer sb = new StringBuffer();
        sb.append("\t\n");
        sb.append("Name:");
        sb.append("\t");
        sb.append(e.getClass().getName());
        StackTraceElement[] stackTraces = e.getStackTrace();
        sb.append("Detail:");
        sb.append("\n");
        sb.append("\t");
        for (StackTraceElement element : stackTraces) {
            sb.append("\n");
            sb.append(element.toString());
        }
        return sb.toString();
    }

}
