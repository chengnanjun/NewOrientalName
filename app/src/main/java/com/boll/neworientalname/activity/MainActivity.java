package com.boll.neworientalname.activity;

import static android.view.View.OVER_SCROLL_NEVER;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.boll.neworientalname.R;
import com.boll.neworientalname.adapter.DescAdapter;
import com.boll.neworientalname.entivity.DescBean;
import com.boll.neworientalname.interfaces.ItemClickListener;
import com.boll.neworientalname.response.CategoryBean;
import com.boll.neworientalname.response.TowChildBean;
import com.boll.neworientalname.response.VideoBean;
import com.boll.neworientalname.retrofit.VideoDataLoader;
import com.boll.neworientalname.utils.LogUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import rx.functions.Action1;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    private Context mContext;
    private ImageView tvTitle;
    private LinearLayout llDescList,iv_chinese_linear;
    private NestedScrollView iv_nestedScrollView;
    private Intent mIntent;
    private int category;

    private LinearLayout llNiujin;
    private ImageView niujin1;
    private ImageView niujin2;
    private ImageView niujin3;
    private ImageView niujin4;


    private VideoDataLoader mVideoDataLoader;
    private List<TowChildBean> mTowChildBeans;
    private DescAdapter mDescAdapter;

    @Override
    public void setStatus() {
        hideBottomUIMenu();
    }

    @Override
    public int initLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        llDescList = findViewById(R.id.ll_desc_list);
        iv_chinese_linear = findViewById(R.id.iv_chinese_linear);
        iv_nestedScrollView = findViewById(R.id.iv_nestedScrollView);
        tvTitle = findViewById(R.id.tv_title);

        llNiujin = findViewById(R.id.ll_niujin);
        niujin1 = findViewById(R.id.niujin1);
        niujin2 = findViewById(R.id.niujin2);
        niujin3 = findViewById(R.id.niujin3);
        niujin4 = findViewById(R.id.niujin4);

        niujin1.setOnClickListener(this);
        niujin2.setOnClickListener(this);
        niujin3.setOnClickListener(this);
        niujin4.setOnClickListener(this);
    }

    @Override
    public void initData() {
        mContext = this;
        mIntent = getIntent();
        category = mIntent.getIntExtra("category", 102);

        switch (category) {
            case 102://剑桥彩虹
                tvTitle.setImageResource(R.mipmap.tv_ch);
                iv_nestedScrollView.setVerticalScrollBarEnabled(false);
                this.getWindow().setBackgroundDrawable(getResources().getDrawable(R.mipmap.bg_ch));
                break;
            case 103://泡泡剑桥
                tvTitle.setImageResource(R.mipmap.tv_pp);
                iv_nestedScrollView.setVerticalScrollBarEnabled(false);
                this.getWindow().setBackgroundDrawable(getResources().getDrawable(R.mipmap.bg_pp));
                break;
            case 104://纷分尼克
                tvTitle.setImageResource(R.mipmap.tv_nk);
                iv_nestedScrollView.setVerticalScrollBarEnabled(false);
                this.getWindow().setBackgroundDrawable(getResources().getDrawable(R.mipmap.bg_nk));
                break;
            case 106://牛津自然拼读
                tvTitle.setImageResource(R.mipmap.tv_nj);
                iv_nestedScrollView.setVerticalScrollBarEnabled(false);
                this.getWindow().setBackgroundDrawable(getResources().getDrawable(R.mipmap.bg_nj));
                iv_chinese_linear.setVisibility(View.GONE);
                llNiujin.setVisibility(View.VISIBLE);
                break;
            case 107://贝瓦爱学习
                tvTitle.setImageResource(R.mipmap.tv_bw);
                iv_nestedScrollView.setVerticalScrollBarEnabled(false);
                this.getWindow().setBackgroundDrawable(getResources().getDrawable(R.mipmap.xdf_bg));
                break;
            case 108://萌娃爱学习
                tvTitle.setImageResource(R.mipmap.tv_mw);
                iv_nestedScrollView.setVerticalScrollBarEnabled(false);
                this.getWindow().setBackgroundDrawable(getResources().getDrawable(R.mipmap.bg_mw));
                break;
            default:
                break;
        }
        mVideoDataLoader = VideoDataLoader.getInstance();
        //获取SharedPreferences对象
        SharedPreferences sharedPreferences = getSharedPreferences("user",MODE_PRIVATE);
        String mAllString = sharedPreferences.getString("allString","");

        if (mAllString.isEmpty()){
            //获取所有类目接口地址
        mVideoDataLoader.getAllCategory().subscribe(new Action1<List<CategoryBean>>() {
                @Override
                public void call(List<CategoryBean> data) {
                    LogUtil.e(TAG,"allData: "+data);
                    if (!data.isEmpty()){
                        String allString = JSON.toJSONString(data);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        //将获取过来的值放入文件
                        editor.putString("allString", allString);
                        // 提交数据
                        editor.commit();
                    }

                    if (data != null && data.size() > 0) {
                        Map<String,String> map = new HashMap<>();
                        LogUtil.e(TAG,"==所有==: "+data);
                        for (CategoryBean categoryBean : data) {
                            getStudyData(categoryBean);
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
            List<CategoryBean> data = JSONArray.parseArray(mAllString,CategoryBean.class);
            if (data != null && data.size() > 0) {
                LogUtil.e(TAG,"==所有==: "+data);
                for (CategoryBean categoryBean : data) {
                    getStudyData(categoryBean);
                }
            }
        }

    }
    @Override
    public void onClick(View view) {
        int position = 0;
        switch (view.getId()) {
            case R.id.niujin1:
                position = 0;
                break;
            case R.id.niujin2:
                position = 1;
                break;
            case R.id.niujin3:
                position = 2;
                break;
            case R.id.niujin4:
                position = 3;
                break;
        }
        List<VideoBean> videoBeans = new ArrayList<>();
        //添加视频数据集合
        videoBeans.addAll(mTowChildBeans.get(0).getChildren().get(position).getChildren());
        Log.d(TAG, "videoBeans size: " + videoBeans.size());
        Intent intent = new Intent(mContext, PlayActivity.class);
        intent.putExtra("category", category);
        intent.putExtra("position", 0);
        Bundle bundle = new Bundle();
        bundle.putSerializable("videoBeans", (Serializable) videoBeans);
        intent.putExtras(bundle);
        startActivity(intent);
    }



    /**
     * 获取不同类目的数据
     *
     * @param categoryBean
     */
    private void getStudyData(CategoryBean categoryBean) {
        if (category == 107 && categoryBean.getName().equals("学前")){
            //获取SharedPreferences对象
            SharedPreferences sharedPreferences = getSharedPreferences("beiWa",MODE_PRIVATE);
            String beiWa = sharedPreferences.getString("beiWa","");
            if (beiWa.isEmpty()){
                mVideoDataLoader.getTowChildBean(categoryBean.getUrl()).subscribe(new Action1<List<TowChildBean>>() {
                    @Override
                    public void call(List<TowChildBean> data) {
                        if (!data.isEmpty()){
                            String beiWa = JSON.toJSONString(data);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            //将获取过来的值放入文件
                            editor.putString("beiWa", beiWa);
                            // 提交数据
                            editor.commit();
                        }
                        beiWa(data);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                    }
                });
            }else {
                List<TowChildBean> beiWaData = JSONArray.parseArray(beiWa,TowChildBean.class);
                beiWa(beiWaData);
            }

        }
        else if (category == 108 && categoryBean.getName().equals("学前")){
            //获取SharedPreferences对象
            SharedPreferences sharedPreferences = getSharedPreferences("MW",MODE_PRIVATE);
            String MW = sharedPreferences.getString("MW","");
            if (MW.isEmpty()){
                mVideoDataLoader.getTowChildBean(categoryBean.getUrl()).subscribe(new Action1<List<TowChildBean>>() {
                    @Override
                    public void call(List<TowChildBean> data) {
                        if (!data.isEmpty()){
                            String MW = JSON.toJSONString(data);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            //将获取过来的值放入文件
                            editor.putString("MW", MW);
                            // 提交数据
                            editor.commit();
                        }
                        mengWa(data);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                    }
                });
            }else {
                List<TowChildBean> mengWaData = JSONArray.parseArray(MW,TowChildBean.class);
                mengWa(mengWaData);
            }

        }
        else if (category == 102 && categoryBean.getName().equals("英语分级阅读")){
            //获取SharedPreferences对象
            SharedPreferences sharedPreferences = getSharedPreferences("jQ",MODE_PRIVATE);
            String jQ = sharedPreferences.getString("jQ","");
            if (jQ.isEmpty()){
                mVideoDataLoader.getTowChildBean(categoryBean.getUrl()).subscribe(new Action1<List<TowChildBean>>() {
                    @Override
                    public void call(List<TowChildBean> data) {
                        if (!data.isEmpty()){
                            String jQ = JSON.toJSONString(data);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            //将获取过来的值放入文件
                            editor.putString("jQ", jQ);
                            // 提交数据
                            editor.commit();
                        }
                        getJQ(data);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                    }
                });
            }else {
                List<TowChildBean> jQData = JSONArray.parseArray(jQ,TowChildBean.class);
                getJQ(jQData);
            }

        }
        else if (category == 103 && categoryBean.getName().equals("英语分级阅读")){
            //获取SharedPreferences对象
            SharedPreferences sharedPreferences = getSharedPreferences("PP",MODE_PRIVATE);
            String PP = sharedPreferences.getString("PP","");
            if (PP.isEmpty()){
                mVideoDataLoader.getTowChildBean(categoryBean.getUrl()).subscribe(new Action1<List<TowChildBean>>() {
                    @Override
                    public void call(List<TowChildBean> data) {
                        if (!data.isEmpty()){
                            String PP = JSON.toJSONString(data);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            //将获取过来的值放入文件
                            editor.putString("PP", PP);
                            // 提交数据
                            editor.commit();
                        }
                        getPP(data);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });
            }else {
                List<TowChildBean> ppData = JSONArray.parseArray(PP,TowChildBean.class);
                getPP(ppData);
            }

        }
        else if (category == 104 && categoryBean.getName().equals("英语分级阅读")){
            //获取SharedPreferences对象
            SharedPreferences sharedPreferences = getSharedPreferences("NK",MODE_PRIVATE);
            String NK = sharedPreferences.getString("NK","");
            if (NK.isEmpty()){
                mVideoDataLoader.getTowChildBean(categoryBean.getUrl()).subscribe(new Action1<List<TowChildBean>>() {
                    @Override
                    public void call(List<TowChildBean> data) {
                        if (!data.isEmpty()){
                            String NK = JSON.toJSONString(data);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            //将获取过来的值放入文件
                            editor.putString("NK", NK);
                            // 提交数据
                            editor.commit();
                        }
                        getNK(data);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });
            }else {
                List<TowChildBean> nkData = JSONArray.parseArray(NK,TowChildBean.class);
                getNK(nkData);
            }

        }
        else if (category == 106 && categoryBean.getName().equals("牛津自然拼读")){
            mVideoDataLoader.getTowChildBean(categoryBean.getUrl()).subscribe(new Action1<List<TowChildBean>>() {
                @Override
                public void call(List<TowChildBean> data) {
                    if (data != null){
                        mTowChildBeans = data;
                    }
                }
            }, new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {
                    throwable.printStackTrace();
                }
            });
        }
    }
    //贝瓦
    public void beiWa(List<TowChildBean> data){
        int index = 0;
        for (int i = 0; i < data.size(); i++) {
            TowChildBean towChildBean = data.get(i);
            if (Objects.equals(towChildBean.getZj(), "贝瓦爱学习")){
                if (i == index && index < data.size()) {
                    List<DescBean> mDescBeans = new ArrayList<>();
                    //添加子项图片、文字
                    for (TowChildBean.ChildrenBean childrenBean : towChildBean.getChildren()) {
                        DescBean descBean = new DescBean();
                        descBean.setName(childrenBean.getName());
                        descBean.setImgPath(childrenBean.getPict_url());
                        mDescBeans.add(descBean);
                    }
                    //添加子项recyclerView
                    addRecyclerView(mDescBeans,1);
                    //点击跳转到播放界面
                    mDescAdapter.setOnItemClickListener(new ItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            List<VideoBean> videoBeans = new ArrayList<>();
                            //添加视频数据集合
                            videoBeans.addAll(towChildBean.getChildren().get(position).getChildren());
                            Log.d(TAG, "videoBeans size: " + videoBeans.size());
                            Intent intent = new Intent(mContext, PlayActivity.class);
                            intent.putExtra("category", category);
                            intent.putExtra("position", 0);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("videoBeans", (Serializable) videoBeans);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    });
                }
            }
            index++;
            Log.d(TAG, "towChildBean: " + towChildBean.toString());
        }
    }
    //萌娃
    public void mengWa(List<TowChildBean> data){
        int index = 0;
        for (int i = 0; i < data.size(); i++) {
            TowChildBean towChildBean = data.get(i);
//                        //添加子项标题
//                        addContentTitleView(towChildBean.getZj());
            if ((Objects.equals(towChildBean.getZj(), "萌娃玩具课")) | (Objects.equals(towChildBean.getZj(), "萌娃素养课"))){
                if (i == index && index < data.size()) {
                    List<DescBean> mDescBeans = new ArrayList<>();
                    //添加子项图片、文字
                    for (TowChildBean.ChildrenBean childrenBean : towChildBean.getChildren()) {
                        DescBean descBean = new DescBean();
                        descBean.setName(childrenBean.getName());
                        descBean.setImgPath(childrenBean.getPict_url());
                        mDescBeans.add(descBean);
                        LogUtil.e(TAG,"==萌娃1111==："+mDescBeans.get(0).getName());
                    }

                    //添加子项recyclerView
                    addRecyclerView(mDescBeans,1);
                    //点击跳转到播放界面
                    mDescAdapter.setOnItemClickListener(new ItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            List<VideoBean> videoBeans = new ArrayList<>();
                            //添加视频数据集合
                            videoBeans.addAll(towChildBean.getChildren().get(position).getChildren());
                            Log.d(TAG, "videoBeans size: " + videoBeans.size());
                            Intent intent = new Intent(mContext, PlayActivity.class);
                            intent.putExtra("category", category);
                            intent.putExtra("position", 0);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("videoBeans", (Serializable) videoBeans);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    });
                }
            }
            index++;
            Log.d(TAG, "towChildBean: " + towChildBean.toString());
        }
    }
    //剑桥彩虹
    public void getJQ(List<TowChildBean> data){
        int index = 0;
        for (int i = 0; i < data.size(); i++) {
            TowChildBean towChildBean = data.get(i);
//                        //添加子项标题
//                        addContentTitleView(towChildBean.getZj());
            if (Objects.equals(towChildBean.getZj(), "剑桥彩虹少儿英语分级阅读")){
                if (i == index && index < data.size()) {
                    List<DescBean> mDescBeans = new ArrayList<>();
                    //添加子项图片、文字
                    for (TowChildBean.ChildrenBean childrenBean : towChildBean.getChildren()) {
                        DescBean descBean = new DescBean();
                        descBean.setName(childrenBean.getName());
                        descBean.setImgPath(childrenBean.getPict_url());
                        mDescBeans.add(descBean);
                        LogUtil.e(TAG,"==剑桥==："+mDescBeans.get(0).getName());
                    }

                    //添加子项recyclerView
                    addRecyclerView(mDescBeans,1);
                    //点击跳转到播放界面
                    mDescAdapter.setOnItemClickListener(new ItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            List<VideoBean> videoBeans = new ArrayList<>();
                            //添加视频数据集合
                            videoBeans.addAll(towChildBean.getChildren().get(position).getChildren());
                            Log.d(TAG, "videoBeans size: " + videoBeans.size());
                            Intent intent = new Intent(mContext, PlayActivity.class);
                            intent.putExtra("category", category);
                            intent.putExtra("position", 0);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("videoBeans", (Serializable) videoBeans);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    });
                }
            }
            index++;
            Log.d(TAG, "towChildBean: " + towChildBean.toString());
        }
    }
    //泡泡剑桥
    public void getPP(List<TowChildBean> data){
        int index = 0;
        for (int i = 0; i < data.size(); i++) {
            TowChildBean towChildBean = data.get(i);
//                        //添加子项标题
//                        addContentTitleView(towChildBean.getZj());
            if (Objects.equals(towChildBean.getZj(), "泡泡剑桥儿童英语故事阅读")){
                if (i == index && index < data.size()) {
                    List<DescBean> mDescBeans = new ArrayList<>();
                    //添加子项图片、文字
                    for (TowChildBean.ChildrenBean childrenBean : towChildBean.getChildren()) {
                        DescBean descBean = new DescBean();
                        descBean.setName(childrenBean.getName());
                        descBean.setImgPath(childrenBean.getPict_url());
                        mDescBeans.add(descBean);
                        LogUtil.e(TAG,"==泡泡==："+mDescBeans.get(0).getName());
                    }

                    //添加子项recyclerView
                    addRecyclerView(mDescBeans,1);
                    //点击跳转到播放界面
                    mDescAdapter.setOnItemClickListener(new ItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            List<VideoBean> videoBeans = new ArrayList<>();
                            //添加视频数据集合
                            videoBeans.addAll(towChildBean.getChildren().get(position).getChildren());
                            Log.d(TAG, "videoBeans size: " + videoBeans.size());
                            Intent intent = new Intent(mContext, PlayActivity.class);
                            intent.putExtra("category", category);
                            intent.putExtra("position", 0);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("videoBeans", (Serializable) videoBeans);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    });
                }
            }

            index++;
            Log.d(TAG, "towChildBean: " + towChildBean.toString());
        }
    }
    //纷分尼克
    public void getNK(List<TowChildBean> data){
        int index = 0;
        for (int i = 0; i < data.size(); i++) {
            TowChildBean towChildBean = data.get(i);
//                        //添加子项标题
//                        addContentTitleView(towChildBean.getZj());
            if (Objects.equals(towChildBean.getZj(), "纷分尼克英语分级阅读")){
                if (i == index && index < data.size()) {
                    List<DescBean> mDescBeans = new ArrayList<>();
                    //添加子项图片、文字
                    for (TowChildBean.ChildrenBean childrenBean : towChildBean.getChildren()) {
                        DescBean descBean = new DescBean();
                        descBean.setName(childrenBean.getName());
                        descBean.setImgPath(childrenBean.getPict_url());
                        mDescBeans.add(descBean);
                        LogUtil.e(TAG,"==尼克==："+mDescBeans.get(0).getName());
                    }

                    //添加子项recyclerView
                    addRecyclerView(mDescBeans,1);
                    //点击跳转到播放界面
                    mDescAdapter.setOnItemClickListener(new ItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            List<VideoBean> videoBeans = new ArrayList<>();
                            //添加视频数据集合
                            videoBeans.addAll(towChildBean.getChildren().get(position).getChildren());
                            Log.d(TAG, "videoBeans size: " + videoBeans.size());
                            Intent intent = new Intent(mContext, PlayActivity.class);
                            intent.putExtra("category", category);
                            intent.putExtra("position", 0);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("videoBeans", (Serializable) videoBeans);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    });
                }
            }

            index++;
            Log.d(TAG, "towChildBean: " + towChildBean.toString());
        }
    }
    private void addRecyclerView(List<DescBean> mDescBeans,int type) {
        if (type==2){
            RecyclerView recyclerView = new RecyclerView(mContext);
            LinearLayout.LayoutParams layoutParams = new LinearLayout
                    .LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(40, 0 ,0, 10);
            recyclerView.setLayoutParams(layoutParams);
            recyclerView.setOverScrollMode(OVER_SCROLL_NEVER);//去掉阴影效果
            GridLayoutManager layoutManager = new GridLayoutManager(mContext, 5);
            recyclerView.setLayoutManager(layoutManager);
            mDescAdapter = new DescAdapter(mDescBeans,type,this);
            recyclerView.setAdapter(mDescAdapter);
            llDescList.addView(recyclerView);
        }
        else if (type == 1){
            RecyclerView recyclerView = new RecyclerView(mContext);
            LinearLayout.LayoutParams layoutParams = new LinearLayout
                    .LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 0, 0, 0);
            recyclerView.setLayoutParams(layoutParams);
            recyclerView.setOverScrollMode(OVER_SCROLL_NEVER);//去掉阴影效果
            GridLayoutManager layoutManager = new GridLayoutManager(mContext, 4);
            recyclerView.setLayoutManager(layoutManager);
            mDescAdapter = new DescAdapter(mDescBeans,type,this);
            recyclerView.setAdapter(mDescAdapter);
            llDescList.addView(recyclerView);
        }
    }
}