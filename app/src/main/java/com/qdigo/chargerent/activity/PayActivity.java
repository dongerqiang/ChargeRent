package com.qdigo.chargerent.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.gson.Gson;
import com.pingplusplus.android.Pingpp;
import com.qdigo.chargerent.BaseActivity;
import com.qdigo.chargerent.R;
import com.qdigo.chargerent.entity.data.ConsumeOrder;
import com.qdigo.chargerent.entity.data.PaymentRequest;
import com.qdigo.chargerent.entity.net.HttpExecute;
import com.qdigo.chargerent.entity.net.HttpRequest;
import com.qdigo.chargerent.entity.net.HttpResponseListener;
import com.qdigo.chargerent.entity.net.responseBean.AlipayResponse;
import com.qdigo.chargerent.utils.LogUtils;
import com.qdigo.chargerent.utils.SystemOpt;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.HashMap;
import java.util.Map;

import static com.qdigo.chargerent.Constants.DEVICE_FLAG;
import static com.qdigo.chargerent.ServerURL.GET_CHARGE;


/***
 * 充值
 * @author fu
 *
 */
@SuppressWarnings("deprecation")
@EActivity(R.layout.activity_topup_layout)
public class PayActivity extends BaseActivity {
    /**
     * 微信支付渠道
     */
    private static final String CHANNEL_WECHAT = "wx";
    /**
     * 支付支付渠道
     */
    private static final String CHANNEL_ALIPAY = "alipay";
    
    @ViewById
    CheckBox alipayCb,weiXinCb;
	@ViewById
	public Button payBtn;
	@ViewById
	public TextView payHintTv,showMoenyTv;

	private String payType;
	
	private String channel = CHANNEL_ALIPAY;
	@Extra
	public double money;

	@Extra
	public boolean yajin;
	@Extra
	public boolean chongZhi;
	@Extra
	public ConsumeOrder order;
	@Extra
	public String code;
	
	@Override
	@AfterViews
	public void initViews() {
		super.initViews();

		if(yajin){
			setTitleText(getString(R.string.title_pay_yajin));
			payHintTv.setText(R.string.title_pay_yajin);
			payType = "1";
		}else{
			setTitleText(getString(R.string.title_pay_money));
			payType = "2";
			payHintTv.setText(R.string.title_pay_money);
		}
		
		LogUtils.logDug("chongzhi == "+chongZhi);
        showMoenyTv.setText("￥"+money+getString(R.string.price_unit));
		
		alipayCb.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				alipayCb.setChecked(true);
				weiXinCb.setChecked(false);
				channel = CHANNEL_ALIPAY;
			}
		});
		
		weiXinCb.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				weiXinCb.setChecked(true);
				alipayCb.setChecked(false);
				channel = CHANNEL_WECHAT;
			}
		});
	}
	
	@Click
	public void payBtn(){

		double pay = money;
		pay = pay * 100f;
		int m = (int) pay;
		PaymentRequest paymentRequest = new PaymentRequest(channel,m,payType);
		Map<String, String> param = new HashMap<String, String>();
		param.put("data", new Gson().toJson(paymentRequest));
		HttpRequest<AlipayResponse> httpRequest = new HttpRequest<AlipayResponse>(this, GET_CHARGE, new HttpResponseListener<AlipayResponse>() {

			@Override
			public void onResult(AlipayResponse result) {
				if(result != null && result.statusCode == 200){
					Pingpp.createPayment(PayActivity.this, new Gson().toJson(result.data), "qwalletXXXXXXX");
				}
			}
			@Override
			public void onFail(int code) {
				
			}
		},AlipayResponse.class ,param, "POST", true);
		
		httpRequest.addHead("mobileNo", app.rootUserInfo.mobileNo);
		httpRequest.addHead("mobiledeviceId", DEVICE_FLAG+ SystemOpt.getInstance().getAppSysInfo().getDeviceId());
		httpRequest.addHead("accesstoken", app.rootUserInfo.accessToken);
		
		HttpExecute.getInstance().addRequest(httpRequest);
	}
	
	/**
	 * onActivityResult 获得支付结果，如果支付成功，服务器会收到ping++ 服务器发送的异步通知。
	 * 最终支付成功根据异步通知为准
	 */
    @SuppressWarnings("static-access")
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //支付页面返回处理
        if (requestCode == Pingpp.REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getExtras().getString("pay_result");
				Bundle extras = data.getExtras();

				/* 处理返回值
                 * "success" - payment succeed
                 * "fail"    - payment failed
                 * "cancel"  - user canceldk
                 * "invalid" - payment plugin not installed
                 */
//                String errorMsg = data.getExtras().getString("error_msg"); // 错误信息
//                String extraMsg = data.getExtras().getString("extra_msg"); // 错误信息
                if(result.equals("success")){
                	if(yajin){
						// TODO: 2017-10-18  
					}else{
						// TODO: 2017-10-18  
					}
                	showToast(getString(R.string.toast_pay_success));
                	finish();
                }else{
                	if(yajin){
                		showToast(getString(R.string.toast_pay_fail));
                	}else{
                		showToast(getString(R.string.toast_pay_fail));
                	}
                	showToast( data.getExtras().getString("error_msg"));
                	
                }
                LogUtils.logDug(result);

            }
        }
    }
	
	@Override
	public boolean isLogin() {
		return true;
	}

}
