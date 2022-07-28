package com.example.myschool.ui.home;

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

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.ViewHolder> {
    private final List<String> noticeList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View noticeView;
        TextView noticeTitle;

        public ViewHolder(View view) {
            super(view);
            noticeView = view;
            noticeTitle = view.findViewById(R.id.notice);
        }
    }

    public NoticeAdapter(List<String> list) {
        noticeList = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 用于创建一个ViewHolder实例，将news_item布局加载进来。
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notice, parent, false);

        final ViewHolder holder = new ViewHolder(view);
        holder.noticeView.setOnClickListener(v -> {
            int position = holder.getAdapterPosition();
            Toast.makeText(
                    MySchoolApplication.context, "你点击了第" + position + "项！",
                    Toast.LENGTH_SHORT
            ).show();
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //用于对RecyclerView子项的数据进行赋值，会在每个子项滚动到屏幕内的时候执行，
        //这里我们通过position参数得到当前项的News实例，然后再将数据设置到ViewHolder的控件中
        String notice = noticeList.get(position);
        holder.noticeTitle.setText(notice);
    }

    @Override
    public int getItemCount() {
        return noticeList.size();
    }
}
