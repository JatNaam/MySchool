package com.example.myschool.ui.function;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myschool.MySchoolApplication;
import com.example.myschool.R;

import java.util.List;

public class LeftTransactionAdapter extends RecyclerView.Adapter<LeftTransactionAdapter.ViewHolder> {
    private final List<String> transactionList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View transactionView;
        TextView transactionBtn;

        public ViewHolder(View view) {
            super(view);
            transactionView = view;
            transactionBtn = view.findViewById(R.id.transaction);
        }
    }

    public LeftTransactionAdapter(List<String> list) {
        transactionList = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 用于创建一个ViewHolder实例，将news_item布局加载进来。
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaction, parent, false);

        final ViewHolder holder = new ViewHolder(view);
        holder.transactionView.setOnClickListener(v -> {
            int position = holder.getAdapterPosition();
            Toast.makeText(
                    MySchoolApplication.context, "你点击了" + transactionList.get(position)+",功能尚未开发！",
                    Toast.LENGTH_SHORT
            ).show();
        });
        return holder;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //用于对RecyclerView子项的数据进行赋值，会在每个子项滚动到屏幕内的时候执行，
        //这里我们通过position参数得到当前项的News实例，然后再将数据设置到ViewHolder的控件中
        String transaction = transactionList.get(position);
        holder.transactionBtn.setText(transaction);
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }
}
