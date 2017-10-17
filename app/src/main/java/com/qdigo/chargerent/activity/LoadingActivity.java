package com.qdigo.chargerent.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.view.View;

import com.qdigo.chargerent.BaseActivity;
import com.qdigo.chargerent.R;
import com.qdigo.chargerent.utils.ToastUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

import butterknife.BindView;

/***
 * 启动加载
 * @author fu
 * @create date 2016.05.11
 */
@EActivity(R.layout.activity_loading_layout)
public class LoadingActivity extends BaseActivity {
	public static final int LOCATION_REQUEST = 70;
	@BindView(R.id.load_layout)
    View mLayout;
	Handler handler = new Handler();


	@AfterViews
	public void initViews(){

		//提示打开蓝牙，可快速用车
		handler.postDelayed(new Runnable() {
			public void run() {

				/*if(!SettingShareData.getInstance(app).getKeyValueBoolean("guide", false)){

					com.qdigo.chargerent.activity.GuideActivity_.intent(LoadingActivity.this).ishowBtn(true).flags(Intent.FLAG_ACTIVITY_CLEAR_TOP*//*|Intent.FLAG_ACTIVITY_NEW_TASK*//* ).start();
					finish();
				}else{
					initLocationPermission();

				}*/
				if(app.isLogin()){
					openActivity(LoginActivity_.class);

				}else{
					openActivity(MainActivity_.class);
				}
				finish();
			}
		}, 2 * 1000);
	}



	private static final int REQUEST_CODE = 72;
	private  void requestAlertWindowPermission() {
		Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
		intent.setData(Uri.parse("package:" + getPackageName()));
		startActivityForResult(intent, REQUEST_CODE);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_CODE) {
			if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
				if (Settings.canDrawOverlays(this)) {
					openActivity(MainActivity.class);
					finish();
				}else{
					ToastUtils.showShort(mContext, "请打开系统提示框权限");
				}
			}
		}

	}

	private void initLocationPermission() {
		if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
			if(ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
				requestLocationPermission();
			}else{
				openActivity(MainActivity.class);
				finish();
			}
		}else{
			openActivity(MainActivity.class);
			finish();
		}
	}
	private void requestLocationPermission() {
		if (ActivityCompat.shouldShowRequestPermissionRationale(this,
				Manifest.permission.ACCESS_FINE_LOCATION)) {
			Snackbar.make(mLayout, R.string.permission_location_rationale,
					Snackbar.LENGTH_INDEFINITE)
					.setAction(R.string.ok, new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							ActivityCompat.requestPermissions(LoadingActivity.this,
									new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
									LOCATION_REQUEST);
						}
					})
					.show();

		}else{
			//申请权限
			ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
					LOCATION_REQUEST);
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		if(requestCode == LOCATION_REQUEST){
			if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				// location permission has been granted, preview can be displayed
				openActivity(MainActivity.class);
				finish();
			} else {
				ToastUtils.showShort(mContext,R.string.permissions_not_granted);
//                Snackbar.make(mLayout, R.string.permissions_not_granted,
//                        Snackbar.LENGTH_SHORT).show();
				finish();

			}
		}else{
			super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		}
	}

}
