package com.sinfeeloo.quantitycontrolview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.sinfeeloo.qcview.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<AmountBean> mList = new ArrayList<>();
    private TextView btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        btn = findViewById(R.id.tv_btn);

        for (int i = 0; i < 20; i++) {
            AmountBean bean = new AmountBean();
            bean.setCount(0);
            mList.add(bean);
        }
        QuantityControlAdapter adapter = new QuantityControlAdapter(this, mList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuilder str = new StringBuilder();
                for (AmountBean bean : mList) {
                    str.append(bean.getCount() + ",");
                }
                ToastUtils.showToast(getApplicationContext(), str.toString());
            }
        });
    }
}
