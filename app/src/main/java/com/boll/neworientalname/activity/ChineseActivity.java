package com.boll.neworientalname.activity;

import static android.view.View.OVER_SCROLL_NEVER;
import static com.boll.neworientalname.R.drawable.bg_chinese;
import static com.boll.neworientalname.utils.util.getGradeCodeByString;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.boll.neworientalname.R;
import com.boll.neworientalname.adapter.AdapterGrade;
import com.boll.neworientalname.adapter.DescAdapter;
import com.boll.neworientalname.entivity.DescBean;
import com.boll.neworientalname.interfaces.ItemCallback;
import com.boll.neworientalname.interfaces.ItemClickListener;
import com.boll.neworientalname.response.CategoryBean;
import com.boll.neworientalname.response.ThreeChildBean;
import com.boll.neworientalname.response.TowChildBean;
import com.boll.neworientalname.response.VideoBean;
import com.boll.neworientalname.retrofit.VideoDataLoader;
import com.boll.neworientalname.utils.Const;
import com.boll.neworientalname.utils.LogUtil;
import com.boll.neworientalname.view.ContentTitleView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rx.functions.Action1;

public class ChineseActivity extends BaseActivity implements View.OnClickListener,ItemCallback {
    private static final String TAG = "ChineseActivity";
    private Context mContext;
    private ImageView tvTitle,classImg;
    private LinearLayout llDescList,iv_chinese_linear;
    private TextView class_view;
    private NestedScrollView iv_nestedScrollView;
    private Intent mIntent;
    private int category;
    private PopupWindow gradePopup;
    private VideoDataLoader mVideoDataLoader;
    private DescAdapter mDescAdapter;
    private RelativeLayout gradeView;
    List<String> gradeList = new ArrayList<>();

    @Override
    public void setStatus() {
        hideBottomUIMenu();
    }

    @Override
    public int initLayout() {
        return R.layout.activity_chinese;
    }

    @Override
    public void initView() {
        llDescList = findViewById(R.id.ll_desc_list);
        iv_chinese_linear = findViewById(R.id.iv_chinese_linear);
        iv_nestedScrollView = findViewById(R.id.iv_nestedScrollView);
        tvTitle = findViewById(R.id.tv_title);
        class_view = findViewById(R.id.class_view);
        gradeView = findViewById(R.id.grade_view);
        classImg = findViewById(R.id.class_img);
        gradeView.setOnClickListener(this);
    }

    @Override
    public void initData() {
        mContext = this;
        mIntent = getIntent();
        category = mIntent.getIntExtra("category", 101);
        Collections.addAll( gradeList,"一年级","二年级","三年级","四年级","五年级","六年级","七年级","八年级","九年级");
        iv_chinese_linear.setBackgroundResource(bg_chinese);

        switch (category) {
            case 101:
                tvTitle.setImageResource(R.mipmap.tv_jpp);
                iv_nestedScrollView.setVerticalScrollBarEnabled(false);
                this.getWindow().setBackgroundDrawable(getResources().getDrawable(R.mipmap.bg_jp));
                gradeView.setVisibility(View.VISIBLE);
                break;
            case 105:
                tvTitle.setImageResource(R.mipmap.tv_chinese);
                iv_nestedScrollView.setVerticalScrollBarEnabled(true); //动态显示滚动条
                Drawable drawable = getResources().getDrawable(R.mipmap.bg_main_chinese);
                this.getWindow().setBackgroundDrawable(drawable);
                gradeView.setVisibility(View.GONE);
                break;

        }
        getALL(1); // 默认进入数据是一年级


    }

    private void getALL(int mIndex){
        mVideoDataLoader = VideoDataLoader.getInstance();
        //获取SharedPreferences对象
        SharedPreferences sharedPreferences = getSharedPreferences("allChineseString",MODE_PRIVATE);
        String allChineseString = sharedPreferences.getString("allChineseString","");
        if (allChineseString.isEmpty()){
            //获取所有类目接口地址
            mVideoDataLoader.getAllCategory().subscribe(new Action1<List<CategoryBean>>() {
                @Override
                public void call(List<CategoryBean> data) {
                    if (!data.isEmpty()){
                        String allString = JSON.toJSONString(data);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        //将获取过来的值放入文件
                        editor.putString("allChineseString", allString);
                        // 提交数据
                        editor.commit();
                    }
                    if (data != null && data.size() > 0) {
                        LogUtil.e(TAG,"==所有==: "+data);
                        for (CategoryBean categoryBean : data) {
                            getStudyData(categoryBean,mIndex);
                        }
                    }
                }
            }, new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {
                    throwable.printStackTrace();
                }
            });
        }else {
            List<CategoryBean> data = JSONArray.parseArray(allChineseString,CategoryBean.class);
            if (data != null && data.size() > 0) {
                LogUtil.e(TAG,"==所有==: "+data);
                for (CategoryBean categoryBean : data) {
                    getStudyData(categoryBean,mIndex);
                }
            }
        }

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.grade_view:
                showGradePopup();
        }
    }
    //年级弹窗
    private void showGradePopup() {
        pop(gradePopup);
        classImg.setImageResource(R.mipmap.tv_shang);
        if (gradePopup == null) {
            @SuppressLint("InflateParams") View view = LayoutInflater.from(this).inflate(R.layout.pop_grade, null);
            gradePopup = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            gradePopup.setBackgroundDrawable(new ColorDrawable());
            gradePopup.setOutsideTouchable(true);
            gradePopup.setFocusable(false); //隐藏导航栏
            gradePopup.setTouchable(true);
            gradePopup.setOnDismissListener(() -> classImg.setImageResource(R.mipmap.tv_xia));
            RecyclerView recyclerView = view.findViewById(R.id.gradeRecycler);
            AdapterGrade adapterGrade = new AdapterGrade(this, gradeList);
            adapterGrade.setItemCallback(this);
            recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
            recyclerView.setAdapter(adapterGrade);
        }else {
            gradePopup.setFocusable(false); //隐藏导航栏
        }
        gradePopup.showAsDropDown(gradeView, 0,0);
    }

    //中文分级阅读
    private void getChinese(List<TowChildBean> data){
        List<VideoBean> videoBeans = data.get(0).getChildren().get(0).getChildren();
        List<DescBean> mDescBeans = new ArrayList<>();
        for (int i = 0; i < videoBeans.size(); i++) {
            VideoBean videoBean = videoBeans.get(i);
            DescBean descBean = new DescBean();
            descBean.setName(videoBean.getName());
            if (TextUtils.isEmpty(videoBean.getPict_url())){
                descBean.setImgPath("");
            }else {
                descBean.setImgPath(videoBean.getPict_url());
            }
            mDescBeans.add(descBean);
        }
        //添加子项recyclerView
        addRecyclerView(mDescBeans,2);
        //点击跳转到播放界面
        mDescAdapter.setOnItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(int position) {
                //添加视频数据集合
                Log.d(TAG, "videoBeans size: " + videoBeans.size());
                Intent intent = new Intent(mContext, PlayActivity.class);
                intent.putExtra("category", category);
                intent.putExtra("position", position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("videoBeans", (Serializable) videoBeans);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
    //小学初中
    private void getLittleCentreSchool(List<ThreeChildBean> data,int mIndex){

        int index = 0;
        for (int i = 0; i < data.size(); i++) {
            ThreeChildBean threeChildBean = data.get(i);
            //添加子项图片、文字
            if (i == index && index < data.size()) {
                List<DescBean> mDescBeans = new ArrayList<>();
                for (ThreeChildBean.ChildrenBean childrenBean : threeChildBean.getChildren()) {
                    if (childrenBean.getGrade() == mIndex){
                        //添加子项标题
                        addContentTitleView(threeChildBean.getZj());
                        break;
                    }
                }
                for (ThreeChildBean.ChildrenBean childrenBean : threeChildBean.getChildren()) {
                    if (childrenBean.getGrade() == mIndex){
                        DescBean descBean = new DescBean();
                        descBean.setName(childrenBean.getName());
                        descBean.setImgPath(childrenBean.getPict_url());
                        mDescBeans.add(descBean);
                    }
                }
                //添加子项recyclerView
                addRecyclerView(mDescBeans,1);
                //点击跳转到播放界面
                mDescAdapter.setOnItemClickListener(new ItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        //获取第二层数据传给二级界面
                        ThreeChildBean.ChildrenBean childrenBean = threeChildBean.getChildren().get(position);
                        Intent intent = new Intent(mContext, SecondListActivity.class);
                        intent.putExtra("category", category);
                        intent.putExtra("title", childrenBean.getName());
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("childrenBean", (Serializable) childrenBean);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
            }
            index++;
            Log.d(TAG, "threeChildBean: " + threeChildBean.toString());
        }
    }

    private void getStudyData(CategoryBean categoryBean,int mIndex) {
        if (category == 105 && categoryBean.getName().equals("中文分级阅读")){
            //获取SharedPreferences对象
            SharedPreferences sharedPreferences = getSharedPreferences("chinese",MODE_PRIVATE);
            String chinese = sharedPreferences.getString("chinese","");
            if (chinese.isEmpty()){
                mVideoDataLoader.getTowChildBean(categoryBean.getUrl()).subscribe(new Action1<List<TowChildBean>>() {
                    @Override
                    public void call(List<TowChildBean> data) {
                        if (!data.isEmpty()){
                            String chinese = JSON.toJSONString(data);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            //将获取过来的值放入文件
                            editor.putString("chinese", chinese);
                            // 提交数据
                            editor.commit();
                        }
                        getChinese(data);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                    }
                });
            }else {
                List<TowChildBean> chineseData = JSONArray.parseArray(chinese,TowChildBean.class);
                getChinese(chineseData);
            }

        }
        else if ((category == 101 && categoryBean.getName().equals("小学"))) {
            //获取SharedPreferences对象
            SharedPreferences sharedPreferences = getSharedPreferences("littleCentreSchool",MODE_PRIVATE);
            String littleCentreSchool = sharedPreferences.getString("littleCentreSchool","");
            if (littleCentreSchool.isEmpty()){
                //三层数据结构
                mVideoDataLoader.getThreeChildBean(categoryBean.getUrl()).subscribe(new Action1<List<ThreeChildBean>>() {
                    @Override
                    public void call(List<ThreeChildBean> data) {
                        if (!data.isEmpty()){
                            String littleCentreSchool = JSON.toJSONString(data);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            //将获取过来的值放入文件
                            editor.putString("littleCentreSchool", littleCentreSchool);
                            // 提交数据
                            editor.commit();
                        }
                        getLittleCentreSchool(data,mIndex);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
            }else {
                List<ThreeChildBean> littleCentreData = JSONArray.parseArray(littleCentreSchool,ThreeChildBean.class);
                getLittleCentreSchool(littleCentreData,mIndex);
            }
        }else if ((category == 101 && categoryBean.getName().equals("初中") && mIndex >= 7)){
            //获取SharedPreferences对象
            SharedPreferences sharedPreferences = getSharedPreferences("centreSchool",MODE_PRIVATE);
            String centreSchool = sharedPreferences.getString("centreSchool","");
            if (centreSchool.isEmpty()){
                //三层数据结构
                mVideoDataLoader.getThreeChildBean(categoryBean.getUrl()).subscribe(new Action1<List<ThreeChildBean>>() {
                    @Override
                    public void call(List<ThreeChildBean> data) {
                        if (!data.isEmpty()){
                            String centreSchool = JSON.toJSONString(data);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            //将获取过来的值放入文件
                            editor.putString("centreSchool", centreSchool);
                            // 提交数据
                            editor.commit();
                        }
                        getLittleCentreSchool(data,mIndex);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
            }else {
                List<ThreeChildBean> centreSchoolData = JSONArray.parseArray(centreSchool,ThreeChildBean.class);
                getLittleCentreSchool(centreSchoolData,mIndex);
            }
        }
    }

    private void addContentTitleView(String towChildBean) {
        ContentTitleView contentTitleView = new ContentTitleView(mContext);
        contentTitleView.setTitle(towChildBean);
        llDescList.addView(contentTitleView);
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
            layoutParams.setMargins(40, 0, -200, 5);
            recyclerView.setLayoutParams(layoutParams);
            recyclerView.setOverScrollMode(OVER_SCROLL_NEVER);//去掉阴影效果
            GridLayoutManager layoutManager = new GridLayoutManager(mContext, 4);
            recyclerView.setLayoutManager(layoutManager);
            mDescAdapter = new DescAdapter(mDescBeans,type,this);
            recyclerView.setAdapter(mDescAdapter);
            llDescList.addView(recyclerView);
        }

    }

    /**
     * 判断窗口是否唯一
     * @param popupWindow
     */
    public  void pop(PopupWindow popupWindow){
        if (popupWindow != null) {
            popupWindow.setFocusable(true);//要先让popupwindow获得焦点，才能正确获取popupwindow的状态
            if (popupWindow.isShowing()) {
                popupWindow.dismiss();
            }
        }
    }

    //选择年级
    @Override
    public void item(int index) {
        gradePopup.dismiss();
        class_view.setText(getGradeCodeByString(index+1));
        llDescList.removeAllViews();

        if (index>=6){
        //获取SharedPreferences对象
            SharedPreferences sharedPreferences = getSharedPreferences("centreSchool",MODE_PRIVATE);
            String centreSchool = sharedPreferences.getString("centreSchool","");
            if (centreSchool.isEmpty()){
                getALL(index+1);
            }else {
                List<ThreeChildBean> centreSchoolData = JSONArray.parseArray(centreSchool,ThreeChildBean.class);
                getLittleCentreSchool(centreSchoolData,index+1);
            }
        }else {
            //获取SharedPreferences对象
            SharedPreferences sharedPreferences = getSharedPreferences("littleCentreSchool",MODE_PRIVATE);
            String littleCentreSchool = sharedPreferences.getString("littleCentreSchool","");
            if (littleCentreSchool.isEmpty()){
                getALL(index+1);
            }else {
                List<ThreeChildBean> littleCentreData = JSONArray.parseArray(littleCentreSchool,ThreeChildBean.class);
                getLittleCentreSchool(littleCentreData,index+1);
            }
        }
    }
}