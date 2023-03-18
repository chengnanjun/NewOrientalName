package com.boll.neworientalname.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.boll.neworientalname.R;

public class HomeActivity extends BaseActivity implements View.OnClickListener {

    private Button jingpinke;
    private Button liulin,meitounao,more;
    private Button jqch,ppjq,ffnk;
    private Button niujin;
    private Button beiwa;
    private Button mengwa;

    private Intent mIntent,playIntent,chineseIntent,jpIntent;

    @Override
    public void setStatus() {
        hideBottomUIMenu();
    }

    @Override
    public int initLayout() {
        return R.layout.activity_home;
    }

    @Override
    public void initView() {
        jingpinke = findViewById(R.id.jingpinke);
        liulin = findViewById(R.id.liulin);
        meitounao = findViewById(R.id.meitounao);
        more = findViewById(R.id.more);
        jqch = findViewById(R.id.jqch);ppjq = findViewById(R.id.ppjq);ffnk = findViewById(R.id.ffnk);
        niujin = findViewById(R.id.niujin);
        beiwa = findViewById(R.id.beiwa);
        mengwa = findViewById(R.id.mengwa);
        jingpinke.setOnClickListener(this);
        liulin.setOnClickListener(this);
        meitounao.setOnClickListener(this);
        more.setOnClickListener(this);
        jqch.setOnClickListener(this);ppjq.setOnClickListener(this);ffnk.setOnClickListener(this);
        niujin.setOnClickListener(this);
        beiwa.setOnClickListener(this);
        mengwa.setOnClickListener(this);
    }

    @Override
    public void initData() {
        mIntent = new Intent(this,MainActivity.class);
        playIntent = new Intent(this, PlayActivity.class);
        chineseIntent = new Intent(this,ChineseActivity.class);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.jingpinke://新东方在线精品课（小学、初中、高中内容）
                chineseIntent.putExtra("category",101);
                startActivity(chineseIntent);
                finish();
                break;
            case R.id.jqch://剑桥彩虹
                mIntent.putExtra("category",102);
                startActivity(mIntent);
                finish();
                break;
            case R.id.ppjq://泡泡剑桥
                mIntent.putExtra("category",103);
                startActivity(mIntent);
                finish();
                break;
            case R.id.ffnk://纷分尼克
                mIntent.putExtra("category",104);
                startActivity(mIntent);
                finish();
                break;
            case R.id.liulin://柳林风声
                playIntent.putExtra("position",0);
                startActivity(playIntent);
                finish();
                break;
            case R.id.meitounao://没头脑和不高兴
                playIntent.putExtra("position",1);
                startActivity(playIntent);
                finish();
                break;
            case R.id.more://更多
                chineseIntent.putExtra("category",105);
                startActivity(chineseIntent);
                finish();
                break;
            case R.id.niujin://牛津
                mIntent.putExtra("category",106);
                startActivity(mIntent);
                finish();
                break;
            case R.id.beiwa://贝瓦
                mIntent.putExtra("category",107);
                startActivity(mIntent);
                finish();
                break;
            case R.id.mengwa://萌娃
                mIntent.putExtra("category",108);
                startActivity(mIntent);
                finish();
                break;
        }

    }
}
