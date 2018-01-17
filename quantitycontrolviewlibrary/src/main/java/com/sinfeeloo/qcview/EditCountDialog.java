package com.sinfeeloo.qcview;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.sinfeeloo.qcview.util.SoftKeyboardUtils;


/**
 * EditCountDialog
 *
 * @author: mhj
 * @date: 2018/1/9 16:04
 * <p>
 * 可编辑数量框
 */
public class EditCountDialog extends Dialog {
    private Context context;
    private EditText countEt;
    private int count;
    private OnDialogListener listener;

    public EditCountDialog(@NonNull Context context) {
        super(context);
    }

    public EditCountDialog(@NonNull Context context, @StyleRes int themeResId, int count, OnDialogListener listener) {
        super(context, themeResId);
        this.context = context;
        this.listener = listener;
        this.count = count;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_setgoodscount);
        countEt = findViewById(R.id.count_tv);
        countEt.setText(String.valueOf(count));
        findViewById(R.id.cancel_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SoftKeyboardUtils.hideSoftInput(context, countEt);
                dismiss();
            }
        });
        findViewById(R.id.ok_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(countEt.getText().toString())) {
                    listener.onGoodsCount(Integer.parseInt(countEt.getText().toString()));
                } else {
                    listener.onGoodsCount(0);
                }
                SoftKeyboardUtils.hideSoftInput(context, countEt);
                dismiss();
            }
        });
    }


    public interface OnDialogListener {
        void onGoodsCount(int count);
    }
}
