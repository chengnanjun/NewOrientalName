package com.boll.neworientalname.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boll.neworientalname.R;
import com.boll.neworientalname.adapter.DescAdapter;
import com.boll.neworientalname.adapter.KnowledgeAdapter;
import com.boll.neworientalname.adapter.KnowledgeBean;
import com.boll.neworientalname.entivity.DescBean;
import com.boll.neworientalname.interfaces.ItemClickListener;
import com.boll.neworientalname.response.OneChildrenBean;
import com.boll.neworientalname.response.ThreeChildBean;
import com.boll.neworientalname.response.TowChildBean;
import com.boll.neworientalname.response.VideoBean;
import com.boll.neworientalname.utils.Const;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SecondListActivity extends BaseActivity {
    private static final String TAG = "SecondListActivity";
    private Context mContext;
    private Intent mIntent;
    private ImageView iconBack;
    private TextView tvTitle;
    private RecyclerView knowledgeRecyclerView;
    private LinearLayout llContent;
    private RecyclerView recyclerView;

    private String title;
    private int category;
    private ThreeChildBean.ChildrenBean childrenBean;
    private List<TowChildBean.ChildrenBean> children;
    private List<KnowledgeBean> knowledges;
    private KnowledgeAdapter mKnowledgeAdapter;

    private List<VideoBean> videoBeans;
    private List<DescBean> mDescBeans;
    private DescAdapter mDescAdapter;

    private int type;//1：小学、初中；2、高中

    @Override
    public void setStatus() {
        hideBottomUIMenu();
    }

    @Override
    public int initLayout() {
        return R.layout.activity_second_list;
    }

    @Override
    public void initView() {
        tvTitle = findViewById(R.id.tv_title);
        knowledgeRecyclerView = findViewById(R.id.knowledgeRecyclerView);
        llContent = findViewById(R.id.ll_content);

        recyclerView = findViewById(R.id.recyclerView);


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        knowledgeRecyclerView.setLayoutManager(layoutManager);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,3);
        recyclerView.setLayoutManager(gridLayoutManager);
    }

    @Override
    public void initData() {
        mContext = this;
        mIntent = getIntent();
        title = mIntent.getStringExtra("title");
        category = mIntent.getIntExtra("category",101);
        Object obj = mIntent.getSerializableExtra("childrenBean");
        tvTitle.setText(title);

        knowledges = new ArrayList<>();
        mDescBeans = new ArrayList<>();

        //添加左侧知识点
        if (obj instanceof ThreeChildBean.ChildrenBean){
            type = 1;
            childrenBean = (ThreeChildBean.ChildrenBean) obj;
            for (int i = 0; i < childrenBean.getChildren().size(); i++) {
                List<OneChildrenBean> children = childrenBean.getChildren();
                KnowledgeBean knowledgeBean = new KnowledgeBean();
                String name = children.get(i).getName();
                if (i == 0) {
                    knowledgeBean.setCheck(true);
                }
                knowledgeBean.setKnowledge(name);
                knowledges.add(knowledgeBean);
            }
            getVideoList(0);
        }else {
            type = 2;
            children = (List<TowChildBean.ChildrenBean>) obj;
            for (int i = 0; i < children.size(); i++) {
                String name = children.get(i).getName();
                KnowledgeBean knowledgeBean = new KnowledgeBean();
                if (i == 0) {
                    knowledgeBean.setCheck(true);
                }
                knowledgeBean.setKnowledge(name);
                knowledges.add(knowledgeBean);
            }
            getVideoList1(0);
        }

        mKnowledgeAdapter = new KnowledgeAdapter(knowledges);
        knowledgeRecyclerView.setAdapter(mKnowledgeAdapter);

        mDescAdapter = new DescAdapter(mDescBeans,3,mContext);
        recyclerView.setAdapter(mDescAdapter);

        mKnowledgeAdapter.setOnItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(int position) {
                //切换左侧知识点
                List<KnowledgeBean> mKnowledges = new ArrayList<>();
                for (int i = 0; i < knowledges.size(); i++) {
                    KnowledgeBean knowledgeBean = new KnowledgeBean();
                    if (i == position) {
                        knowledgeBean.setCheck(true);
                    } else {
                        knowledgeBean.setCheck(false);
                    }
                    knowledgeBean.setKnowledge(knowledges.get(i).getKnowledge());
                    mKnowledges.add(knowledgeBean);
                }
                knowledges.clear();
                knowledges.addAll(mKnowledges);
                mKnowledgeAdapter.notifyDataSetChanged();

                if (type == 1){
                    getVideoList(position);
                }else {
                    getVideoList1(position);
                }
                mDescAdapter.notifyDataSetChanged();
            }
        });

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
    /**
     * 获取右侧视频列表(小学、初中)
     * @param position
     */
    private void getVideoList(int position) {
        mDescBeans.clear();
        OneChildrenBean oneChildrenBean = childrenBean.getChildren().get(position);
        videoBeans = oneChildrenBean.getChildren();
        for (int j = 0; j < videoBeans.size(); j++) {
            VideoBean videoBean = videoBeans.get(j);
            DescBean descBean = new DescBean();
            descBean.setName(videoBean.getName());
            descBean.setImgPath(videoBean.getUrl());
            mDescBeans.add(descBean);
            Log.d(TAG, "getVideoList: " + descBean.getImgPath());
        }
    }

    /**
     * 获取右侧视频列表(高中)
     * @param position
     */
    private void getVideoList1(int position) {
        mDescBeans.clear();
        videoBeans = this.children.get(position).getChildren();
        for (int j = 0; j < videoBeans.size(); j++) {
            VideoBean videoBean = videoBeans.get(j);
            DescBean descBean = new DescBean();
            descBean.setName(videoBean.getName());
            descBean.setImgPath(videoBean.getUrl());
            mDescBeans.add(descBean);
            Log.d(TAG, "getVideoList1: " + descBean.getImgPath());
        }
    }
}