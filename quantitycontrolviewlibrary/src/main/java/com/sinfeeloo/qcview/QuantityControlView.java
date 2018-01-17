package com.sinfeeloo.qcview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sinfeeloo.qcview.listener.OnAmountButtonListener;
import com.sinfeeloo.qcview.util.ToastUtils;


/**
 * QuantityControlView
 *
 * @author: mhj
 * @date: 2018/1/9 9:54
 * <p>
 * 自定义组件：购买数量，带减少增加按钮
 */
public class QuantityControlView extends LinearLayout implements View.OnClickListener {
    Context context;
    private int amount = 0; //购买数量
    private int minAmount = 0; //最小数量
    private int maxAmount = 999999; //商品库存
    private String minWarnStr = "当前已是最小购买数量";
    private String maxWarnStr = "已达到最大购买数量";

    //是否可以点击编辑
    private boolean editable = false;
    private int tvTextSize;
    private int tvTextColor;
    private int bg;
    private int decrease_bg;
    private int increase_bg;
    private int unable_increase_bg;
    private int unable_decrease_bg;
    private TextView tvAmount;
    private EditText etAmount;
    private ImageView btnDecrease;
    private ImageView btnIncrease;
    private LinearLayout llQuantityControl;
    private OnAmountButtonListener onAmountButtonListener = null;
    EditCountDialog editCountDialog;

    public QuantityControlView(Context context) {
        this(context, null);
    }

    public QuantityControlView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.layout_quantity_control, this);
        llQuantityControl = findViewById(R.id.ll_quantity_control);
        tvAmount = findViewById(R.id.tvAmount);
        etAmount = findViewById(R.id.etAmount);
        btnDecrease = findViewById(R.id.btnDecrease);
        btnIncrease = findViewById(R.id.btnIncrease);
        btnDecrease.setOnClickListener(this);
        btnIncrease.setOnClickListener(this);
        tvAmount.setOnClickListener(this);

        TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attrs, R.styleable.QuantityControlView);
        int btnWidth = obtainStyledAttributes.getDimensionPixelSize(R.styleable.QuantityControlView_qc_btnWidth, LayoutParams.WRAP_CONTENT);
        int btnHeight = obtainStyledAttributes.getDimensionPixelSize(R.styleable.QuantityControlView_qc_btnHeight, LayoutParams.WRAP_CONTENT);
        int tvWidth = obtainStyledAttributes.getDimensionPixelSize(R.styleable.QuantityControlView_qc_tvWidth, 80);
        tvTextSize = obtainStyledAttributes.getDimensionPixelSize(R.styleable.QuantityControlView_qc_tvTextSize, 15);
        tvTextColor = obtainStyledAttributes.getColor(R.styleable.QuantityControlView_qc_tvTextColor, getResources().getColor(R.color.textColor));
        editable = obtainStyledAttributes.getBoolean(R.styleable.QuantityControlView_qc_editable, false);
        minAmount = obtainStyledAttributes.getInteger(R.styleable.QuantityControlView_qc_minAmount, 0);
        bg = obtainStyledAttributes.getResourceId(R.styleable.QuantityControlView_qc_bg, R.drawable.bg_default);
        decrease_bg = obtainStyledAttributes.getResourceId(R.styleable.QuantityControlView_qc_decrease_bg, R.mipmap.icon_decrease);
        increase_bg = obtainStyledAttributes.getResourceId(R.styleable.QuantityControlView_qc_increase_bg, R.mipmap.icon_increase);
        unable_decrease_bg = obtainStyledAttributes.getResourceId(R.styleable.QuantityControlView_qc_decrease_unable_bg, R.mipmap.icon_unable_decrease);
        unable_increase_bg = obtainStyledAttributes.getResourceId(R.styleable.QuantityControlView_qc_increase_unable_bg, R.mipmap.icon_unable_increase);
        obtainStyledAttributes.recycle();

        //设置按钮属性
        LayoutParams btnParams = new LayoutParams(btnWidth, btnHeight);
        btnDecrease.setLayoutParams(btnParams);
        btnIncrease.setLayoutParams(btnParams);
        llQuantityControl.setBackgroundResource(bg);
        //检测按钮状态
        checkButtonStatus();
        //设置文字属性
        LayoutParams textParams = new LayoutParams(tvWidth, LayoutParams.MATCH_PARENT);
        //是否可以编辑
        if (editable) {//可以编辑
            tvAmount.setVisibility(View.GONE);
            etAmount.setVisibility(View.VISIBLE);
            //设置文字属性
            etAmount.setText(String.valueOf(amount));
            etAmount.setLayoutParams(textParams);
            etAmount.setTextSize(tvTextSize);
            etAmount.setTextColor(tvTextColor);
        } else {//不可编辑
            tvAmount.setVisibility(View.VISIBLE);
            etAmount.setVisibility(View.GONE);
            //设置文字属性
            tvAmount.setText(String.valueOf(amount));
            tvAmount.setLayoutParams(textParams);
            tvAmount.setTextSize(tvTextSize);
            tvAmount.setTextColor(tvTextColor);
        }

    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btnDecrease) {//减少
            if (amount > minAmount) {
                amount--;
                //当数量等于最小数量时减少按钮变成灰色
                if (amount == minAmount) {
                    btnDecrease.setImageResource(unable_decrease_bg);
                }
                //恢复添加按钮正常颜色
                if (amount == maxAmount - 1) {
                    btnIncrease.setImageResource(increase_bg);
                }
                onAmountButtonListener.onAmountChange(Constant.DECREASE, amount);
            } else {
                ToastUtils.showToast(getContext(), minWarnStr);
            }
        } else if (i == R.id.btnIncrease) {//增加

            if (amount < maxAmount) {
                amount++;
                //恢复减少按钮的颜色
                if (amount == minAmount + 1) {
                    btnDecrease.setImageResource(decrease_bg);
                }
                //当数量等于最大数量时增加按钮成灰色
                if (amount == maxAmount) {
                    btnIncrease.setImageResource(unable_increase_bg);
                }
                onAmountButtonListener.onAmountChange(Constant.INCREASE, amount);
            } else {
                ToastUtils.showToast(getContext(), maxWarnStr);
            }
        } else if (i == R.id.tvAmount) {
            onEditModifyCount(amount);
        }


        notifyAmount();
    }


    /**
     * 通过编辑方式修改数量
     * @param count
     */
    private void onEditModifyCount(int count) {
        editCountDialog = new EditCountDialog(context, R.style.DialogStyle, count, new EditCountDialog.OnDialogListener() {
            @Override
            public void onGoodsCount(int count) {
                setAmount(count);
                onAmountButtonListener.onAmountChange(Constant.EDIT, getAmount());
            }
        });
        editCountDialog.show();
    }

    private void checkButtonStatus(){
        if (amount == minAmount) {
            btnDecrease.setImageResource(unable_decrease_bg);
        } else {
            btnDecrease.setImageResource(decrease_bg);
        }

        if (amount == maxAmount) {
            btnIncrease.setImageResource(unable_increase_bg);
        } else {
            btnIncrease.setImageResource(increase_bg);
        }
    }

    /**
     * 设置数量
     *
     * @param tempAmount
     */
    public void setAmount(int tempAmount) {
        if (tempAmount > maxAmount) {
            this.amount = maxAmount;
            ToastUtils.showToast(context,maxWarnStr);
        } else {
            this.amount = tempAmount;
        }
        //检测按钮状态
        checkButtonStatus();
        notifyAmount();
    }

    /**
     * 获得数量
     *
     * @return
     */
    public int getAmount() {
        return this.amount;
    }

    /**
     * 刷新数量显示
     */
    public void notifyAmount() {
        tvAmount.setText(String.valueOf(this.amount));
    }


    public void setMaxAmount(int tempMaxAmount) {
        this.maxAmount = tempMaxAmount;
    }

    public int getMaxAmount() {
        return this.maxAmount;
    }

    public void setMinAmount(int tempMinAmount) {
        this.minAmount = tempMinAmount;
    }

    public int getMinAmount() {
        return this.minAmount;
    }


    public void setOnAmountButtonListener(OnAmountButtonListener listener) {
        this.onAmountButtonListener = listener;
    }


}

