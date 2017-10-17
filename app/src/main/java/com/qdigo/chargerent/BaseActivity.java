package com.qdigo.chargerent;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.qdigo.chargerent.activity.LoginActivity_;
import com.qdigo.chargerent.interfaces.ILogin;
import com.qdigo.chargerent.utils.TitleBar;
import com.qdigo.chargerent.utils.ToastUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;


/***
 * 基类Activity
 * @author fu
 * @create date 2016.05.11
 */
public class BaseActivity extends AppCompatActivity implements TitleBar.TitleCallBack,ILogin {
    public MyApplication app;
    public TitleBar titleBar = new TitleBar();
    public Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = MyApplication.getInstance();
        mContext = this;
        AppManager.getInstance().addActivity(this);// 将activity添加到list中
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getInstance().removeActivity(this);
    }


    public void initViews(){

        if(isLogin()){
            if(app.isLogin()){
                finish();
                MyApplication.isShowLoginPage = true;
                openActivity(LoginActivity_.class);
            }
        }

        titleBar.initView(getWindow().getDecorView().getRootView(),true);
        titleBar.addTitleCallBack(this);

    }


    /***
     * 返回按钮
     */
    @Override
    public void backClick(ImageView backImg) {
        finish();
    }



    /***
     * 右边按钮
     */
    @Override
    public void rightClick(ImageView rightImg) {

    }

    public  void setTitleText(String title){
        if(titleBar != null){
            titleBar.setTitle(title);
        }
    }



    public void findEditTextEnable(ViewGroup viewGroup){
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View view = viewGroup.getChildAt(i);
            if(view instanceof ViewGroup){
                findEditTextEnable((ViewGroup) view);
            }else if(view instanceof EditText){
                ((EditText)view).setEnabled(false);
            }
        }
    }


    @Override
    public void leftImg(ImageView backImg) {
        // TODO Auto-generated method stub

    }


    @Override
    public void rightImg(ImageView rightImg) {
        // TODO Auto-generated method stub

    }


    @Override
    public void rightTv(TextView rightTv) {
        // TODO Auto-generated method stub

    }

    public void showToast(String str){
        ToastUtils.showShort(this,str);
    }


    @Override
    protected void onResume() {
        super.onResume();
//        JPushInterface.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        JPushInterface.onPause(this);
    }


    @Override
    public boolean isLogin() {
        // TODO Auto-generated method stub
        return false;
    }
    protected void openActivity(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }

    public double formatDouble2(double d) {
        // 旧方法，已经不再推荐使用
//        BigDecimal bg = new BigDecimal(d).setScale(2, BigDecimal.ROUND_HALF_UP);


        // 新方法，如果不需要四舍五入，可以使用RoundingMode.DOWN
        BigDecimal bg = new BigDecimal(d).setScale(2, RoundingMode.UP);

        return bg.doubleValue();
    }

}
