package com.qdigo.chargerent.activity;

import com.qdigo.chargerent.BaseActivity;
import com.qdigo.chargerent.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity {


    @Override
    @AfterViews
    public void initViews() {
        super.initViews();
        setTitleText("主页");
    }

    @Override
    public boolean isLogin() {
        return true;
    }

    @Click
    public void btnSao(){
        openActivity(ScanBikeActivity.class);
    }
}
