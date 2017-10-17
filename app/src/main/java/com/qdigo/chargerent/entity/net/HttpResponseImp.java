package com.qdigo.chargerent.entity.net;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.view.KeyEvent;
import android.view.WindowManager;

import com.qdigo.chargerent.MyApplication;
import com.qdigo.chargerent.R;
import com.qdigo.chargerent.entity.net.responseBean.BaseResponse;
import com.qdigo.chargerent.utils.ToastUtils;


public class HttpResponseImp<T> implements HttpListener<T> {
	private HttpResponseListener<T> callback;
	private boolean isShowDialog;
	private Dialog dialog;
	private Context ctx;
	public HttpResponseImp(Context ctx, HttpResponseListener<T> callback, boolean isShowDialog) {
		super();
		this.callback = callback; 
		this.ctx = ctx;
		this.isShowDialog = isShowDialog;
	}

	@Override
	public void onStart() {
		showLoadingDialog();
		if(callback != null){
			callback.onStart();
		}
	}

	@Override
	public void onFinish() {
		cancelDialog();
		if(callback != null){
			callback.onFinish();
		}
	}
	
	@Override
	public void onResult(T result) {
		if(callback != null){
			callback.onResult(result);
			BaseResponse response = (BaseResponse) result;
			if(response.statusCode == 405){
				/*if(!MyApplication.isShowLoginPage){
					MyApplication.isShowLoginPage = true;
					com.cargps.android.activity.LoginActivity_.intent(ctx).flags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_NEW_TASK).start();
				}*/
				MyApplication.getInstance().loginOut(ctx,null);
			}
		}
	}

	@Override
	public void onFail(int responseCode) {
		cancelDialog();
		if(callback != null){
			callback.onFail(responseCode);
            if (responseCode == 400) {//服务器未能理解请求。
                showToast(ctx.getString(com.qdigo.chargerent.R.string.net_code_400));
            } else if (responseCode == 403) {// 请求的页面被禁止
                showToast(ctx.getString(com.qdigo.chargerent.R.string.net_code_403));
            } else if (responseCode == 404) {// 服务器无法找到请求的页面
                showToast(ctx.getString(com.qdigo.chargerent.R.string.net_code_404));
            } else  if (500 == responseCode) {
                showToast(ctx.getString(com.qdigo.chargerent.R.string.net_code_500));
            } else if (501 == responseCode) {
                showToast(ctx.getString(com.qdigo.chargerent.R.string.net_code_501));
            } else if (502 == responseCode) {
                showToast(ctx.getString(com.qdigo.chargerent.R.string.net_code_502));
            } else if (503 == responseCode) {
                showToast(ctx.getString(com.qdigo.chargerent.R.string.net_code_503));
            } else if (504 == responseCode) {
                showToast(ctx.getString(com.qdigo.chargerent.R.string.net_code_504));
            } else if (505 == responseCode) {
                showToast(ctx.getString(com.qdigo.chargerent.R.string.net_code_505));
            } else if(1001 == responseCode){
            	showToast(ctx.getString(com.qdigo.chargerent.R.string.net_code_1001));
            }else{
            	showToast(ctx.getString(com.qdigo.chargerent.R.string.net_code_other));
            }
		}
	}
	
	private void showLoadingDialog(){
		if(!isShowDialog) return;
		dialog = new Dialog(ctx, R.style.custom_dialog);
		dialog.setCancelable(false);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				return true;
			}
		});
		
		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
		lp.width = MyApplication.getInstance().widthPixels;
		lp.height = MyApplication.getInstance().heightPixels;
//		dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);

		dialog.setContentView(R.layout.dialog_loading_layout);
		
		dialog.show();
	}
	
	private void cancelDialog(){
		if(dialog != null) dialog.dismiss();
	}
	
	private void showToast(String str){
		ToastUtils.showShort(ctx,str);
	}




}
