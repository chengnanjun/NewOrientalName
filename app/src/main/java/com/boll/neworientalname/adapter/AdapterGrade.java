package com.boll.neworientalname.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.boll.neworientalname.R;
import com.boll.neworientalname.interfaces.ItemCallback;

import java.util.List;

public class AdapterGrade extends RecyclerView.Adapter<AdapterGrade.ViewHolder> {

    private Context mContext;
    private List<String> mGradeList;
    private int mPosition = 0;
    private ItemCallback itemCallback;

    public AdapterGrade(Context context, List<String> gradeList) {
        this.mContext = context;
        this.mGradeList = gradeList;

    }
    public void setItemCallback(ItemCallback itemCallback){
        this.itemCallback = itemCallback;
    }
    @NonNull
    @Override
    public AdapterGrade.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pop_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterGrade.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        String commonConstant = mGradeList.get(position);
        holder.mTextView.setText(commonConstant);

        // 设置默认选择一年级
        if (mPosition != position) {
            holder.mTextView.setTextColor(mContext.getResources().getColor(R.color.black));
        }else {
            holder.mTextView.setTextColor(mContext.getResources().getColor(R.color.zi_1677FF));
        }
        holder.mTextView.setOnClickListener(view -> {
            mPosition = position; // 选中点击的位置
            notifyDataSetChanged(); // 更新数据
            if (itemCallback != null){
                itemCallback.item(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mGradeList.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView mTextView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.item_grade_tv);
        }
    }
}
