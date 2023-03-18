package com.boll.neworientalname.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.boll.neworientalname.R;
import com.boll.neworientalname.entivity.DescBean;
import com.boll.neworientalname.interfaces.ItemClickListener;
import com.boll.neworientalname.utils.Const;
import com.boll.neworientalname.utils.util;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * created by zoro at 2023/3/10
 * 分级/视频的 图片 + 文字描述
 */
public class DescAdapter extends RecyclerView.Adapter<DescAdapter.ViewHolder> {

    private static final String TAG = "DescAdapter";

    private List<DescBean> mDescBeans;
    private Context mContext;
    private ItemClickListener mItemClickListener;
    private int type;//1：一级界面，直接获取图片地址 2：二级界面，获取视频缩略图
    ArrayList<String> imgFileNameStr=new ArrayList<String>(); //存储是图片的文件名集合
    private String sdcardPath;
    InputStream is = null;
    public DescAdapter(List<DescBean> descBeans, int type,Context context) {
        mContext = context;
        mDescBeans = descBeans;
        this.type = type;
        sdcardPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "videoImg";
        File file = new File(sdcardPath);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public void setOnItemClickListener(ItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        if (type == 1){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_desc, parent, false);
        }else if (type == 2){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chinese_desc, parent, false);
        }else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_jp_desc, parent, false);
        }
        ViewHolder viewHolder = new ViewHolder(view,type);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        DescBean descBean = mDescBeans.get(position);
        String s = (descBean.getImgPath()).replace("vhc","jpg");
        Log.d(TAG, "descBean.getImgPath(): " + descBean.getImgPath());
        if (type == 1) {
            Glide.with(mContext)
                    .load(descBean.getImgPath())
                    .error(R.mipmap.img_default)//异常时候显示的图片
                    .placeholder(R.mipmap.img_default)//加载成功前显示的图片
                    .fallback(R.mipmap.img_default)//url为空的时候,显示的图片
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(10)))
                    .into(holder.imgDesc);

            holder.tvDesc.setText(descBean.getName());
            holder.imgDesc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListener.onItemClick(position);
                }
            });
        } else if (type == 2){
            Glide.with(mContext)
                    .load(descBean.getImgPath())
                    .error(R.mipmap.a1)//异常时候显示的图片
                    .placeholder(R.mipmap.a1)//加载成功前显示的图片
                    .fallback(R.mipmap.a1)//url为空的时候,显示的图片
//                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(10)))
                    .into(holder.ivChineseImg);
            holder.ivChineseText.setText(descBean.getName());
            holder.ivChineseImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListener.onItemClick(position);
                }
            });

        }else {
            Glide.with(mContext)
                    .load(Const.VIDEO_IMG + s)
                    .error(R.mipmap.a111)//异常时候显示的图片
                    .placeholder(R.mipmap.a111)//加载成功前显示的图片
                    .fallback(R.mipmap.a111)//url为空的时候,显示的图片
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(10)))
                    .into(holder.imgJpDesc);
            holder.tvJpDesc.setText(descBean.getName());
            holder.imgJpDesc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListener.onItemClick(position);
                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return mDescBeans.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgDesc,imgJpDesc;
        TextView tvDesc,tvJpDesc;
        ImageView ivChineseImg;
        TextView ivChineseText;

        public ViewHolder(@NonNull View itemView , int type) {
            super(itemView);
            if (type == 1){
                imgDesc = itemView.findViewById(R.id.img_desc);
                tvDesc = itemView.findViewById(R.id.tv_desc);
            }else if (type == 2){
                ivChineseImg = itemView.findViewById(R.id.iv_chinese_img);
                ivChineseText = itemView.findViewById(R.id.iv_chinese_text);
            }else {
                imgJpDesc = itemView.findViewById(R.id.img_jp_desc);
                tvJpDesc = itemView.findViewById(R.id.tv_jp_desc);
            }

        }

    }
}
