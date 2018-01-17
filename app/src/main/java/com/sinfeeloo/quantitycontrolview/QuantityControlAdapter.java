package com.sinfeeloo.quantitycontrolview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sinfeeloo.qcview.Constant;
import com.sinfeeloo.qcview.QuantityControlView;
import com.sinfeeloo.qcview.listener.OnAmountButtonListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: mhj
 * @date: 2018/1/9 9:45
 * @desc:
 */

public class QuantityControlAdapter extends RecyclerView.Adapter<QuantityControlAdapter.QuantityControlViewHolder> {

    private Context mContext;
    private List<AmountBean> mDatas = new ArrayList<>();
    public OnChangeCountListener listener = null;

    public QuantityControlAdapter(Context mContext, List<AmountBean> list) {
        this.mContext = mContext;
        this.mDatas = list;
    }

    @Override
    public QuantityControlViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        QuantityControlViewHolder viewHolder = new QuantityControlViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_quantity_contorl, parent, false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(QuantityControlViewHolder holder, final int position) {
        final AmountBean item = mDatas.get(position);
        holder.quantityControlView.setAmount(item.getCount());
        holder.quantityControlView.setMaxAmount(5);
        holder.quantityControlView.setOnAmountButtonListener(new OnAmountButtonListener() {

            @Override
            public void onAmountChange(int type, int count) {
                if (Constant.DECREASE == type) {

                } else if (Constant.INCREASE == type) {

                } else {

                }
                item.setCount(count);
            }

        });
    }


    @Override
    public int getItemCount() {
        return mDatas.size();
    }


    public class QuantityControlViewHolder extends RecyclerView.ViewHolder {
        QuantityControlView quantityControlView;

        public QuantityControlViewHolder(View view) {
            super(view);
            quantityControlView = view.findViewById(R.id.quantity_control_view);
        }
    }

    public interface OnChangeCountListener {
        void onModifyCount(int position, AmountBean bean);
    }

    public void setOnChangeCountListener(OnChangeCountListener onInCountListener) {
        this.listener = onInCountListener;
    }



}
