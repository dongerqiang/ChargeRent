package com.qdigo.chargerent.activity;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.qdigo.chargerent.BaseActivity;
import com.qdigo.chargerent.MyApplication;
import com.qdigo.chargerent.R;
import com.qdigo.chargerent.ServerURL;
import com.qdigo.chargerent.entity.data.LoginUserInfo;
import com.qdigo.chargerent.entity.net.HttpExecute;
import com.qdigo.chargerent.entity.net.HttpRequest;
import com.qdigo.chargerent.entity.net.HttpResponseListener;
import com.qdigo.chargerent.entity.net.responseBean.BaseResponse;
import com.qdigo.chargerent.entity.net.responseBean.LoginResponse;
import com.qdigo.chargerent.utils.SettingShareData;
import com.qdigo.chargerent.utils.SystemOpt;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static com.qdigo.chargerent.Constants.DEVICE_FLAG;


@EActivity(R.layout.activity_login_activity)
public class LoginActivity<T> extends BaseActivity {
	@ViewById
	public EditText phoneEt,codeEt;
	@ViewById
	public CheckBox xyCk;
	@ViewById
	public TextView readTv;
	public static final String TAG = "LoginActivity";
	public Timer timer;
	int time = 0;
	
	@ViewById
	public Button codeButton,loginBtn;
	
	@Override
	@AfterViews
	public void initViews() {
		super.initViews();
		setTitleText(getString(R.string.text_login));
	}
	
	@Override
	public void leftImg(ImageView backImg) {
		backImg.setVisibility(View.VISIBLE);
	}
	
	@Click
	public void codeButton(){
		String phone = phoneEt.getText().toString();
		
		if(TextUtils.isEmpty(phone)){
			showToast(getString(R.string.mobile_is_null));
			return;
		}
		getNetCode(phone,"abd");
		startTimer();
	}
	
	@Click
	public void loginBtn(){
		
		String phone = phoneEt.getText().toString();
		String code = codeEt.getText().toString();
		
		if(TextUtils.isEmpty(phone)){
			showToast(getString(R.string.mobile_is_null));
			return;
		}
		
		if(TextUtils.isEmpty(code)){
			showToast(getString(R.string.toast_pin_is_null));
			return;
		}
		
		if(!xyCk.isChecked()){
			showToast(getString(R.string.confirm_xieyi));
			return;
		}
		
//		final String interfaceUrl =MyContacts.MAIN_URL+"v1.0/user/registerAndLogin";
		Map<String, String> param = new HashMap<String, String>();
		param.put("mobileNo", phone);
		param.put("pinCode", code);
		HttpRequest<LoginResponse> httpRequest  = new HttpRequest<LoginResponse>(this, ServerURL.LOGIN_URL, new HttpResponseListener<LoginResponse>() {


			@Override
			public void onFail(int code) {
				
			}

			@Override
			public void onResult(LoginResponse result) {
				if(null !=result){
					if(result.statusCode == 200){
						
						loginBtn.setEnabled(false);
						SettingShareData.getInstance(LoginActivity.this).setKeyValue("login_userInfo", new Gson().toJson(result.data));
						LoginUserInfo info  = app.getLocalUserinfo();
//						app.setPushAlias(info.mobileNo);
						finish();
						openActivity(MainActivity_.class);
						app.getUserInfoData(mContext);
					}
				}
			}

		}, LoginResponse.class, param,"POST",true);
		
		httpRequest.addHead("mobileNo", phone);
		httpRequest.addHead("mobiledeviceId", DEVICE_FLAG+ SystemOpt.getInstance().getAppSysInfo().getDeviceId());
		
		HttpExecute.getInstance().addRequest(httpRequest);

		
	}
	
	public void startTimer(){
		time = 60;
		codeButton.setEnabled(false);
		
		if(timer != null){
			timer.cancel();
			timer = null;
		}
		
		timer = new Timer();
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					public void run() {
						if(time < 0){
							stopTimer();
							return;
						}
						codeButton.setText((time--)+"s");
					}
				});
			}
		}, 0, 1000);
	}
	
	public void stopTimer(){
		codeButton.setEnabled(true);
		codeButton.setText(R.string.text_get_pin);
		if(timer != null){
			timer.cancel();
			timer = null;
		}
	}
	
	public void getNetCode(String phone, String devId){
//		final String interfaceUrl =MyContacts.MAIN_URL+"v1.0/user/getPinCode";

		HttpRequest<BaseResponse> httpRequest  = new HttpRequest<BaseResponse>(this,ServerURL.CODE_PIN_URL, new HttpResponseListener<BaseResponse>() {


			@Override
			public void onFail(int code) {
				Log.w(TAG, "getNetCode == "+code);
			}

			@Override
			public void onResult(BaseResponse result) {
				Log.w(TAG, "getNetCode == "+result.message+" ;"+result.statusCode);
			}

			
			
		},BaseResponse.class, null,"GET",false);
		
		httpRequest.addHead("mobileNo", phone);
		httpRequest.addHead("mobiledeviceId", DEVICE_FLAG+SystemOpt.getInstance().getAppSysInfo().getDeviceId());
		
		HttpExecute.getInstance().addRequest(httpRequest);
    
				
	}
	/*@Click
	public void readTv(){
		WebViewActivity_.intent(this).flags(Intent.FLAG_ACTIVITY_NEW_TASK).title(getString(R.string.title_user_rule)).url("fuwutiaokuan.html").start();
	}*/
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		MyApplication.isShowLoginPage = false;
		stopTimer();
	}
}
