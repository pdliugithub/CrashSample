package com.rossia.life.crash.dialog;

import android.app.Activity;
import android.support.design.widget.BottomSheetDialog;
import android.view.View;

/**
 * @author pd_liu on 2017/12/21.
 */

public class CrashDialog {

    /**
     * 显示BottomSheetDialog.
     *
     * @param activity    activity.
     * @param contentView 内容视图.
     */
    public static void show(Activity activity, View contentView) {

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(activity);
        bottomSheetDialog.setCancelable(true);
        bottomSheetDialog.setCanceledOnTouchOutside(true);
        bottomSheetDialog.setContentView(contentView);
        bottomSheetDialog.show();
    }
}
