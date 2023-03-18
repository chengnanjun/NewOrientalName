package com.boll.neworientalname.adapter;

import static com.boll.neworientalname.utils.Const.VIDEO_IMG;
import static com.boll.neworientalname.utils.Const.VIDEO_URL;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.boll.neworientalname.R;
import com.boll.neworientalname.interfaces.ItemClickListener;
import com.boll.neworientalname.response.VideoBean;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder>{

    private List<VideoBean> mVideoBeans;
    private Context mContext;
    private ItemClickListener mItemClickListener;
    private int mPosition ;
    private List<Boolean> isClicks;//控件是否被点击,默认为false，如果被点击，改变值，控件根据值改变自身颜色


    public VideoAdapter(List<VideoBean> videoBeans,int mPosition) {
        mVideoBeans = videoBeans;
        this.mPosition = mPosition;
    }

    public void setOnItemClickListener(ItemClickListener itemClickListener){
        mItemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        VideoBean videoBean = mVideoBeans.get(position);
        String s = (videoBean.getUrl()).replace("vhc","jpg");

        Glide.with(mContext)
                .load(VIDEO_IMG + s)
                .error( R.mipmap.img_default)//异常时候显示的图片
                .placeholder( R.mipmap.img_default)//加载成功前显示的图片
                .fallback( R.mipmap.img_default)//url为空的时候,显示的图片
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(10)))
                .into(holder.itemImage);
        holder.itemName.setText(videoBean.getName());

        if (mPosition != position) {
            holder.ivPlay.setImageResource(R.mipmap.ic_play);
        }else {
            holder.ivPlay.setImageResource(R.mipmap.ic_pause);
        }


        holder.itemImage.setOnClickListener(new View.OnClickListener() {
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
        return mVideoBeans.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView itemImage,ivPlay;
        TextView itemName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.item_image);
            ivPlay = itemView.findViewById(R.id.iv_play);
            itemName = itemView.findViewById(R.id.item_name);
        }
    }
}
