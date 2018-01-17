package com.sinfeeloo.qcview.util;

import android.content.Context;
import android.widget.Toast;

public class ToastUtils {


    private static Toast toast;

    /**
     * 显示单例的吐司，能连续快速弹的吐司
     *
     * @param text
     */
    public static void showToast(Context context, String text) {
        if (toast == null) {
            toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        }
        toast.setText(text);
        toast.show();
    }
}

