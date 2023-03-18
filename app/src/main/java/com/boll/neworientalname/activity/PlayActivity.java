package com.boll.neworientalname.activity;


import static com.boll.neworientalname.utils.Const.VIDEO_URL;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.boll.neworientalname.R;
import com.boll.neworientalname.adapter.VideoAdapter;
import com.boll.neworientalname.interfaces.ItemClickListener;
import com.boll.neworientalname.response.CategoryBean;
import com.boll.neworientalname.response.TowChildBean;
import com.boll.neworientalname.response.VideoBean;
import com.boll.neworientalname.retrofit.VideoDataLoader;
import com.boll.neworientalname.utils.LogUtil;
import com.boll.neworientalname.video.JzvdStd;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import rx.functions.Action1;

public class PlayActivity extends BaseActivity {
    private static final String TAG = "PlayActivity";
    private RecyclerView video_list_recycle;
    private TextView name_text,desc_text;
    private JzvdStd jzvdStd;

    private Intent mIntent;
    private int category;//类目
    int position;
    private List<VideoBean> mVideoBeans;
    private VideoAdapter mVideoAdapter;
    private String filePath;

    private VideoDataLoader mVideoDataLoader;
    private LinearLayoutManager layoutManager;

    @Override
    public void setStatus() {
        hideBottomUIMenu();
    }

    @Override
    public int initLayout() {
        return R.layout.activity_play;
    }

    @Override
    public void initView() {
        video_list_recycle = findViewById(R.id.video_list_recycle);
        name_text = findViewById(R.id.name_text);
        desc_text = findViewById(R.id.desc_text);
        jzvdStd = findViewById(R.id.jzvdStd);
    }

    @Override
    public void initData() {
        mIntent = getIntent();
        category = mIntent.getIntExtra("category",101);
        position = mIntent.getIntExtra("position",0);
        mVideoBeans = (List<VideoBean>) mIntent.getSerializableExtra("videoBeans");

//        switch (position){
//            case 0:
//            case 1:
//                this.getWindow().setBackgroundDrawable(getResources().getDrawable(R.mipmap.bg_main_chinese));
//                break;
//        }

        switch (category){
            case 102:
                this.getWindow().setBackgroundDrawable(getResources().getDrawable(R.mipmap.bg_ch));
                break;
            case 103:
                this.getWindow().setBackgroundDrawable(getResources().getDrawable(R.mipmap.bg_pp));
                break;
            case 104:
                this.getWindow().setBackgroundDrawable(getResources().getDrawable(R.mipmap.bg_nk));
                break;
            case 105:
                this.getWindow().setBackgroundDrawable(getResources().getDrawable(R.mipmap.bg_main_chinese));
                break;
            case 106:
                this.getWindow().setBackgroundDrawable(getResources().getDrawable(R.mipmap.bg_nj));
                break;
            case 107:
                this.getWindow().setBackgroundDrawable(getResources().getDrawable(R.mipmap.xdf_bg));
                break;
            case 108:
                this.getWindow().setBackgroundDrawable(getResources().getDrawable(R.mipmap.bg_mw));
                break;
        }
        if (mVideoBeans == null){
            mVideoDataLoader = VideoDataLoader.getInstance();
            //获取所有类目接口地址
            mVideoDataLoader.getAllCategory().subscribe(new Action1<List<CategoryBean>>() {
                @Override
                public void call(List<CategoryBean> data) {
                    if (data != null && data.size() > 0) {
                        LogUtil.e(TAG,"==所有==: "+data);
                        for (CategoryBean categoryBean : data) {
                            if (categoryBean.getName().equals("中文分级阅读")) {
                                //两层数据结构
                                mVideoDataLoader.getTowChildBean(categoryBean.getUrl()).subscribe(new Action1<List<TowChildBean>>() {
                                    @Override
                                    public void call(List<TowChildBean> data) {

                                        mVideoBeans = data.get(0).getChildren().get(0).getChildren();

                                        name_text.setText(mVideoBeans.get(position).getName());
                                        desc_text.setText(mVideoBeans.get(position).getDesc());

                                        filePath = VIDEO_URL + mVideoBeans.get(position).getUrl();
                                        jzvdStd.setUp(filePath , "");
                                        jzvdStd.startVideo();

                                        layoutManager = new LinearLayoutManager(PlayActivity.this);
                                        layoutManager.scrollToPositionWithOffset(position, 0);
                                        mVideoAdapter = new VideoAdapter(mVideoBeans,position);
                                        video_list_recycle.setAdapter(mVideoAdapter);
                                        video_list_recycle.setLayoutManager(layoutManager);

                                        video_list_recycle.scrollToPosition(position); //更新item位置
                                        mVideoAdapter.setOnItemClickListener(new ItemClickListener() {
                                            @Override
                                            public void onItemClick(int position) {
                                                Log.e(TAG,mVideoBeans.get(position).toString());
                                                name_text.setText(mVideoBeans.get(position).getName());
                                                desc_text.setText(mVideoBeans.get(position).getDesc());
                                                filePath = VIDEO_URL+mVideoBeans.get(position).getUrl();
                                                jzvdStd.setUp(filePath , "");
                                                jzvdStd.startVideo();
                                            }
                                        });

                                    }
                                }, new Action1<Throwable>() {
                                    @Override
                                    public void call(Throwable throwable) {
                                        throwable.printStackTrace();
                                    }
                                });
                            }
                        }
                    }
                }
            }, new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {
                    throwable.printStackTrace();
                }
            });
        }
        else {
            name_text.setText(mVideoBeans.get(position).getName());
            desc_text.setText(mVideoBeans.get(position).getDesc());

            filePath = VIDEO_URL + mVideoBeans.get(position).getUrl();
            jzvdStd.setUp(filePath , "");
            jzvdStd.startVideo();

            layoutManager = new LinearLayoutManager(PlayActivity.this);
            layoutManager.scrollToPositionWithOffset(position, 0);
            mVideoAdapter = new VideoAdapter(mVideoBeans,position);
            video_list_recycle.setAdapter(mVideoAdapter);
            video_list_recycle.setLayoutManager(layoutManager);

            mVideoAdapter.setOnItemClickListener(new ItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    Log.e(TAG,mVideoBeans.get(position).toString());
                    name_text.setText(mVideoBeans.get(position).getName());
                    desc_text.setText(mVideoBeans.get(position).getDesc());
                    filePath = VIDEO_URL+mVideoBeans.get(position).getUrl();
                    jzvdStd.setUp(filePath , "");
                    jzvdStd.startVideo();
                }
            });
        }
    }
    @Override
    protected void onPause() {
        jzvdStd.onPause();
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        jzvdStd.onDestroy();
        super.onDestroy();
    }
}