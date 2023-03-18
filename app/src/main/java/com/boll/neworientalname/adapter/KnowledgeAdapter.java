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
import com.boll.neworientalname.interfaces.ItemClickListener;

import java.util.List;

public class KnowledgeAdapter extends RecyclerView.Adapter<KnowledgeAdapter.ViewHolder> {

    private List<KnowledgeBean> knowledges;
    private ItemClickListener mItemClickListener;
    private Context mContext;
    private int mPosition = 0;
    public KnowledgeAdapter(List<KnowledgeBean> knowledges) {
        this.knowledges = knowledges;
    }

    public void setOnItemClickListener(ItemClickListener itemClickListener){
        mItemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_knowledge, parent, false);
        ViewHolder viewHolder = new ViewHolder(inflate);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        KnowledgeBean knowledgeBean = knowledges.get(position);
//        if (knowledgeBean.isCheck()){
////            holder.tvKnowledge.setBackground(mContext.getResources().getDrawable(R.drawable.bg_knowledge));
//            holder.tvKnowledge.setTextColor(mContext.getResources().getColor(R.color.zi_E56E19));
//        }else {
//            holder.tvKnowledge.setBackground(null);
//        }
        // 设置默认选择一年级
        if (mPosition != position) {
            holder.tvKnowledge.setTextColor(mContext.getResources().getColor(R.color.black));
        }else {
            holder.tvKnowledge.setTextColor(mContext.getResources().getColor(R.color.zi_E56E19));
        }
        holder.tvKnowledge.setText(knowledgeBean.getKnowledge());
        holder.tvKnowledge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPosition = position; // 选中点击的位置
                notifyDataSetChanged(); // 更新数据
                mItemClickListener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return knowledges.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvKnowledge;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvKnowledge = itemView.findViewById(R.id.tv_knowledge);
        }
    }

}
