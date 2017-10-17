package com.qdigo.chargerent;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.google.gson.Gson;
import com.qdigo.chargerent.ble.MyBluetoothGattCallback;
import com.qdigo.chargerent.entity.data.LoginUserInfo;
import com.qdigo.chargerent.entity.data.UserInfo;
import com.qdigo.chargerent.entity.net.HttpExecute;
import com.qdigo.chargerent.entity.net.HttpRequest;
import com.qdigo.chargerent.entity.net.HttpResponseListener;
import com.qdigo.chargerent.entity.net.responseBean.ChrgResponse;
import com.qdigo.chargerent.entity.net.responseBean.UserInfoResponse;
import com.qdigo.chargerent.interfaces.ICallBack;
import com.qdigo.chargerent.utils.LogUtils;
import com.qdigo.chargerent.utils.SettingShareData;
import com.qdigo.chargerent.utils.SystemOpt;
import com.qdigo.chargerent.utils.ToastUtils;
import com.tencent.bugly.crashreport.CrashReport;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.qdigo.chargerent.Constants.ACTION_LOGIN_OUT_REVEICER;
import static com.qdigo.chargerent.Constants.DEVICE_FLAG;

@SuppressLint("NewApi")
public class MyApplication extends Application {

    public SystemOpt systemOpt = SystemOpt.getInstance();

    public static String INPUT_IMEI = "";
    public static String MAC="";
    public LoginUserInfo rootUserInfo;
    public UserInfo userInfo;
    public MyBluetoothGattCallback bluetoothGattCallback = MyBluetoothGattCallback.getInstance();
    public static boolean isShowLoginPage = false;
    Handler handler = new Handler();
    public int heightPixels = 800;
    public int widthPixels = 480;
    public static boolean isActivity = false;
    //经纬度
    public static double latitude = -1f;
    public static double longitude = -1f;
    private static MyApplication application;

    public static MyApplication getInstance(){
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        changeLanguane();
        //bugly
        initBugly();
        //jpush
//        JPushInterface.setDebugMode(true);
//        JPushInterface.init(getApplicationContext());
        // 获取屏幕高度
        systemOpt.init(this);//读取系统信息

//		NoHttpRequest.getInstance().init(this);

        widthPixels = systemOpt.widthPixels;
        heightPixels = systemOpt.heightPixels;
        rootUserInfo = getLocalUserinfo();
//        studentBean = getLocalStudentInfo();

        MyApplication.MAC = getMac();
        MyApplication.INPUT_IMEI = getInputImei();
        LogUtils.logDug("imei = "+MyApplication.INPUT_IMEI);
//        CrashReport.testJavaCrash();
    }

    private void initBugly() {
        // TODO Auto-generated method stub
        Context context = getApplicationContext();
        // 获取当前包名
        String packageName = context.getPackageName();
        // 获取当前进程名
        String processName = getProcessName(android.os.Process.myPid());
        // 设置是否为上报进程
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(context);
        strategy.setUploadProcess(processName == null || processName.equals(packageName));
        // 初始化Bugly
        CrashReport.initCrashReport(context, "3d1bf31eab", false, strategy);
    }

    private String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }

    /***
     * 密码规则
     * @param password
     * @return
     */
    public  boolean isValidPassword(String password){
        if(password.isEmpty()) return false;
        String regex = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    public boolean isTelPhone(String phone){
        if(phone.isEmpty()) return false;
        String regex = "^(1(([357][0-9])|(47)|[8][012356789]))\\d{8}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }

    public boolean isEmail(String email){
        if(email.isEmpty()) return false;
        String regex = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public LoginUserInfo getLocalUserinfo(){
        String str = SettingShareData.getInstance(this).getKeyValueString("login_userInfo", "");
        if(str.isEmpty()) return null;
        LoginUserInfo info = new Gson().fromJson(str, LoginUserInfo.class);
        rootUserInfo = info;
        if(rootUserInfo != null){
            return rootUserInfo;
        }
        return null;
    }

    /*public StudentBean getLocalStudentInfo(){
        String str = SettingShareData.getInstance(this).getKeyValueString("student_info", "");
        if(str.isEmpty()) return null;
        StudentBean info = new Gson().fromJson(str, StudentBean.class);
        studentBean = info;
        if(studentBean != null){
            return studentBean;
        }
        return null;
    }*/

    public UserInfo getUserInfo(){
        String str = SettingShareData.getInstance(this).getKeyValueString("userInfo", "");
        if(str.isEmpty()) return null;
        UserInfo info = new Gson().fromJson(str, UserInfo.class);
        userInfo = info;
        if(userInfo != null){
            return userInfo;
        }
        return null;
    }

    public void setUserInfo(UserInfo userInfo){
        if(userInfo == null) return;
        this.userInfo = userInfo;
        SettingShareData.getInstance(this).setKeyValue("userInfo", new Gson().toJson(userInfo));
    }

    public void setLocalUserinfo(LoginUserInfo loginUserInfo){
        if(loginUserInfo != null){
            SettingShareData.getInstance(this).setKeyValue("login_userInfo", new Gson().toJson(loginUserInfo));
            this.rootUserInfo = loginUserInfo;
        }

    }
    /*public void setLocalStudentInfo(StudentBean studentBean){
        if(studentBean != null){
            SettingShareData.getInstance(this).setKeyValue("student_info", new Gson().toJson(studentBean));
            this.studentBean = studentBean;
        }

    }*/

    public void setInputImei(String imei){
        SettingShareData.getInstance(this).setKeyValue("imei", imei);
    }

    public String getInputImei(){
        return SettingShareData.getInstance(this).getKeyValueString("imei", "");
    }

    public void loginOut(final Context context,final ICallBack back){

        if(rootUserInfo == null) return;
        HttpRequest<ChrgResponse> httpRequest = new HttpRequest<ChrgResponse>(context, ServerURL.LOGIN_OUT,new HttpResponseListener<ChrgResponse>() {

            @Override
            public void onResult(ChrgResponse result) {
                if(result != null && result.data != null ){
                    if(result.statusCode == 200){

                        clearLocalInfo(context);
                        sendBroadcast();
                        ToastUtils.showShort(context,getString(R.string.login_out_success));
                        if(back != null){
                            back.callback();
                        }

                    }
                }

                if(result !=null){
                    if(result.statusCode == 405){
                        clearLocalInfo(context);
//                        ToastUtil.showToast(context,getString(R.string.login_out_success));
                        ToastUtils.showShort(context,result.message);
                        sendBroadcast();
                        if(back != null){
                            back.callback();
                        }
                    }
                }

            }

            @Override
            public void onFail(int code) {
                // TODO Auto-generated method stub

            }
        } ,ChrgResponse.class, null, "GET", true);

        httpRequest.addHead("mobileNo", rootUserInfo.mobileNo);
        httpRequest.addHead("mobiledeviceId", DEVICE_FLAG+SystemOpt.getInstance().getAppSysInfo().getDeviceId());
        httpRequest.addHead("accesstoken", rootUserInfo.accessToken);

        HttpExecute.getInstance().addRequest(httpRequest);
    }


    public void sendBroadcast(){
        Intent intent = new  Intent();
        //设置intent的动作为com.example.broadcast，可以任意定义
        intent.setAction(ACTION_LOGIN_OUT_REVEICER);
        //发送无序广播
        sendBroadcast(intent);
    }

    /**
     * 清楚本地缓存
     * @param context
     */
    public void clearLocalInfo(Context context){
        rootUserInfo =null;
        userInfo = null;
        SettingShareData.getInstance(context).setKeyValue("userInfo", "");
        SettingShareData.getInstance(context).setKeyValue("login_userInfo", "");
        SettingShareData.getInstance(context).setKeyValue("student_info", "");
    }

    //隐藏虚拟键盘
    public void HideKeyboard(View v)
    {
        InputMethodManager imm = ( InputMethodManager ) v.getContext( ).getSystemService( Context.INPUT_METHOD_SERVICE );
        if ( imm.isActive( ) ) {
            imm.hideSoftInputFromWindow( v.getApplicationWindowToken( ) , 0 );

        }
    }

    public boolean isLogin(){
        return getLocalUserinfo() == null;
    }

    public void getUserInfoData(Context context){
        if(rootUserInfo == null) return;
        Map<String, String> param = new HashMap<String, String>();
        HttpRequest<UserInfoResponse> httpRequest = new HttpRequest<UserInfoResponse>(context, ServerURL.USER_DATA_URL, new HttpResponseListener<UserInfoResponse>() {

            @Override
            public void onResult(UserInfoResponse result) {
                if(result != null && result.data != null){
                    if(result.statusCode == 200){
                        setUserInfo(result.data);
                    }
                }
            }

            @Override
            public void onFail(int code) {

            }
        }, UserInfoResponse.class, param, "GET", false);

        httpRequest.addHead("mobileNo", MyApplication.getInstance().rootUserInfo.mobileNo);
        httpRequest.addHead("mobiledeviceId", DEVICE_FLAG+SystemOpt.getInstance().getAppSysInfo().getDeviceId());
        httpRequest.addHead("accesstoken", MyApplication.getInstance().rootUserInfo.accessToken);

        HttpExecute.getInstance().addRequest(httpRequest);
    }


    public static int caculateTime(String startTime,String endTime,String format){
        DateFormat df = new SimpleDateFormat(format);

        try{
            Date start = df.parse(startTime);
            Date end = df.parse(endTime);
            long diff = end.getTime() - start.getTime();  //ms
            long min2 = diff/1000/60;

            long days = diff / (1000 * 60 * 60 * 24);

            long day=diff/(24*60*60*1000);
            long hour=(diff/(60*60*1000)-day*24);
            long min=((diff/(60*1000))-day*24*60-hour*60);
            long s=(diff/1000-day*24*60*60-hour*60*60-min*60);

            StringBuffer sb = new StringBuffer();
            if(day>0){
                sb=sb.append(day+"天");
            }
            if(hour>0){
                sb=sb.append(hour+"小时");

            }
            if(min>0){
                sb=sb.append(min+"分");
            }
            if(s>0){
                sb=sb.append(s+"秒");
            }
            if(min2 <=1){
                return 1;
            }else{
                return (int)min2;
            }
//		   return sb.toString();

        }catch (Exception e){
            return 1;
        }
    }

    public void setMac(String mac) {
        SettingShareData.getInstance(this).setKeyValue(Constants.BLE_MAC, mac);
    }

    public String getMac(){
        return SettingShareData.getInstance(this).getKeyValueString(Constants.BLE_MAC, "");
    }

    @SuppressWarnings("deprecation")
    public void changeLanguane(){
        int keyValueInt = SettingShareData.getInstance(application).getKeyValueInt(Constants.LANGUAGE, 0);

        Resources res = application.getResources();
        Configuration config = res.getConfiguration();
        DisplayMetrics metrics = res.getDisplayMetrics();
        if(keyValueInt == 0){
            config.locale = new Locale("zh","CN");
        }else{
            config.locale = new Locale("en","US");
        }
        res.updateConfiguration(config, metrics);
    }
}
