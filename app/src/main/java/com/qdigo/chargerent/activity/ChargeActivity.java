package com.qdigo.chargerent.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qdigo.chargerent.BaseActivity;
import com.qdigo.chargerent.R;
import com.qdigo.chargerent.entity.net.HttpExecute;
import com.qdigo.chargerent.entity.net.HttpRequest;
import com.qdigo.chargerent.entity.net.HttpResponseListener;
import com.qdigo.chargerent.entity.net.responseBean.BaseResponse;
import com.qdigo.chargerent.interfaces.DialogCallback;
import com.qdigo.chargerent.utils.DialogUtils;
import com.qdigo.chargerent.utils.LogUtils;
import com.qdigo.chargerent.utils.SystemOpt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.qqtheme.framework.picker.SinglePicker;

import static com.qdigo.chargerent.Constants.DEVICE_FLAG;
import static com.qdigo.chargerent.ServerURL.START_RESURE;


public class ChargeActivity extends BaseActivity implements View.OnClickListener {

    private android.widget.TextView tv_dot;
    private android.widget.TextView tv_voltage;
    private TextView tv_cost;
    private TextView tv_time;
    private android.widget.Button btnsubmit;
    private LinearLayout lljifei;
    public static final String TAG = "ChargeActivity";
    List<String>  timeData = new ArrayList<>();
    List<String>  voltageData = new ArrayList<>();
    List<String>  dotData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_key);
        intData();
        initViews();
    }

    private void intData() {
        timeData.add("15 分钟");
        timeData.add("30 分钟");
        timeData.add("45 分钟");
        timeData.add("1 小时");
        timeData.add("2 小时");
        timeData.add("3 小时");
        timeData.add("4 小时");
        timeData.add("5 小时");
        timeData.add("6 小时");
        timeData.add("7 小时");
        timeData.add("8 小时");
        timeData.add("9 小时");

        voltageData.add("110 V");
        voltageData.add("220 V");

        dotData.add("端口 1");
        dotData.add("端口 2");
        dotData.add("端口 3");
        dotData.add("端口 4");
        dotData.add("端口 5");
        dotData.add("端口 6");
        dotData.add("端口 7");
        dotData.add("端口 8");
    }

    @Override
    public void initViews() {
        super.initViews();
        setTitleText("充电设置");
        Intent intent = getIntent();
        if(intent!=null){
            String number = intent.getStringExtra("number");
            showToast(number);
        }
        this.btnsubmit = (Button) findViewById(R.id.btn_submit);
        this.tv_time = (TextView) findViewById(R.id.tv_time);
        this.tv_cost = (TextView) findViewById(R.id.tv_cost);
        this.tv_voltage = (TextView) findViewById(R.id.tv_voltage);
        this.tv_dot = (TextView) findViewById(R.id.tv_dot);
        lljifei = (LinearLayout) findViewById(R.id.ll_jifei);
        lljifei.setOnClickListener(this);
        btnsubmit.setOnClickListener(this);
        tv_time.setOnClickListener(this);
        tv_dot.setOnClickListener(this);
        tv_voltage.setOnClickListener(this);
    }

    @Override
    public boolean isLogin() {
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit:
                onKeySumit();
                break;
            case R.id.tv_dot:
                chooseDot();
                break;
            case R.id.tv_time:
                chooseTime();
                break;
            case R.id.tv_voltage:
                chooseVoltage();
                break;
            case R.id.ll_jifei:

                break;
        }
    }

    private void chooseVoltage() {
        SinglePicker singlePicker = new SinglePicker(this,voltageData);
        singlePicker.setOnItemPickListener(new SinglePicker.OnItemPickListener() {
            @Override
            public void onItemPicked(int index, Object item) {
                tv_voltage.setText(voltageData.get(index));
            }
        });
        singlePicker.show();
    }

    private void chooseDot() {
        SinglePicker singlePicker = new SinglePicker(this,dotData);
        singlePicker.setOnItemPickListener(new SinglePicker.OnItemPickListener() {
            @Override
            public void onItemPicked(int index, Object item) {
                tv_dot.setText(dotData.get(index));
            }
        });
        singlePicker.show();
    }

    private void chooseTime() {

        SinglePicker singlePicker = new SinglePicker(this,timeData);
        singlePicker.setOnItemPickListener(new SinglePicker.OnItemPickListener() {
            @Override
            public void onItemPicked(int index, Object item) {
                String s = timeData.get(index);
                tv_time.setText(s);
                if(s.contains("分钟")){
                    String[] split = s.split(" ");
                    float parseInt = (float)Integer.parseInt(split[0]);
                    tv_cost.setText(parseInt/60 *2+" 元");
                }
                if(s.contains("小时")){
                    String[] split = s.split(" ");
                    int parseInt = Integer.parseInt(split[0]);
                    tv_cost.setText(parseInt *2+" 元");
                }
            }
        });
        singlePicker.show();
    }

    private void onKeySumit() {
        if(check()){
            Dialog onkeyDialog = DialogUtils.createOnkeyDialog(mContext, time,voltage,dot,money, new DialogCallback() {
                @Override
                public void confirm() {
                    super.confirm();
                    LogUtils.logDug("onKeySumit");
//                    submit();
                    com.qdigo.chargerent.activity.PayActivity_.intent(ChargeActivity.this).chongZhi(true).money(cost).flags(Intent.FLAG_ACTIVITY_NEW_TASK).start();

                }
            });
            onkeyDialog.show();
        }
    }

    //发起救援
    private void submit() {
        if(app.rootUserInfo==null) return;
        Map<String, String> param = new HashMap<String, String>();
//        param.put("imeiOrDeviceId", bikeNum);
//        param.put("failInfo",descr);
        String type = "noPower";
        param.put("aidType",type);
        HttpRequest<BaseResponse> httpRequest = new HttpRequest<BaseResponse>(this, START_RESURE, new HttpResponseListener<BaseResponse>() {

            @Override
            public void onResult(BaseResponse result) {
                if(result != null && result.statusCode == 200){
                    Log.w(TAG,"申请救援");
                    // 0 接单中 1 接单成功 2 接单失败
                    /*Intent intent = new Intent(OneKeyActivity.this, RescueStateActivity.class);
                    intent.putExtra(Constants.RESCUE_STATE,0);
                    startActivity(intent);*/
                    // TODO: 2017-10-18
                    finish();
                }else{
                    showToast(result.message);
                }


            }

            @Override
            public void onFail(int code) {
                LogUtils.logDug("startBike failed == "+code);
                showToast(getString(R.string.toast_start_fail));
            }
        }, BaseResponse.class, param, "POST", true);

        httpRequest.addHead("mobileNo", app.rootUserInfo.mobileNo);
        httpRequest.addHead("mobiledeviceId", DEVICE_FLAG+ SystemOpt.getInstance().getAppSysInfo().getDeviceId());
        httpRequest.addHead("accesstoken", app.rootUserInfo.accessToken);
        HttpExecute.getInstance().addRequest(httpRequest);
    }

    String time;
    String voltage;
    String dot;
    double cost;
    String money;
    private boolean check() {
         time = tv_time.getText().toString().trim();
         voltage = tv_voltage.getText().toString().trim();
         dot = tv_dot.getText().toString().trim();
         money = tv_cost.getText().toString().trim();
        if(!TextUtils.isEmpty(money)){
            String s = money.split(" ")[0];
            cost = Double.parseDouble(s.trim());
        }
        if(TextUtils.isEmpty(time)){
            showToast("请选择充电时长");
            return false;
        }
        if(TextUtils.isEmpty(voltage)){
            showToast("请选择充电电压");
            return false;
        }
        if(TextUtils.isEmpty(dot)){
            showToast("请选择充电端口");
            return false;
        }

        return true;
    }

}
