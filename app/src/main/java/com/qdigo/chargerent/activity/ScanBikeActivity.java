package com.qdigo.chargerent.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.qdigo.chargerent.AppManager;
import com.qdigo.chargerent.Constants;
import com.qdigo.chargerent.MyApplication;
import com.qdigo.chargerent.R;
import com.qdigo.chargerent.interfaces.ILogin;
import com.qdigo.chargerent.scan.camera.CameraManager;
import com.qdigo.chargerent.scan.decoding.CaptureFragmentHandler;
import com.qdigo.chargerent.scan.decoding.InactivityTimer;
import com.qdigo.chargerent.scan.view.ViewfinderView;
import com.qdigo.chargerent.utils.LogUtils;
import com.qdigo.chargerent.utils.SettingShareData;
import com.qdigo.chargerent.utils.TitleBar;
import com.qdigo.chargerent.utils.ToastUtils;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Vector;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ScanBikeActivity extends AppCompatActivity implements View.OnClickListener, SurfaceHolder.Callback, TitleBar.TitleCallBack,ILogin {


    @BindView(R.id.viewfinderView)
    ViewfinderView viewfinderView;
    @BindView(R.id.surfaceView)
    SurfaceView surfaceView;
    @BindView(R.id.openledImg)
    ImageView openledImg;
    private MyApplication app;
    private CaptureFragmentHandler handler;
    private boolean hasSurface;
    private InactivityTimer inactivityTimer;
    boolean isLed = false;
    private boolean isResume;
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;
    private boolean vibrate;
    private MediaPlayer mediaPlayer;
    private boolean playBeep;
    private static final float BEEP_VOLUME = 0.10f;
    private Context mContext;
    public TitleBar titleBar = new TitleBar();
    private AudioManager mAudioManager;
    boolean isFirst = false;
    Handler mHandler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_bike);
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        //最大音量
        int maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,(int)(maxVolume*0.4),0);
        ButterKnife.bind(this);
        app = MyApplication.getInstance();
        mContext = this;
        AppManager.getInstance().addActivity(this);
        initViews();
        mediaBatteryPlayer = new MediaPlayer();
        mediaBatteryPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
            }
        });
        int keyValueInt = SettingShareData.getInstance(mContext).getKeyValueInt(Constants.VOICE_INT, 0);
        if(keyValueInt<2){
            keyValueInt++;
            playMedia("start_ok.wav");
            SettingShareData.getInstance(mContext).setKeyValue(Constants.VOICE_INT, keyValueInt);
        }
        if(!isFirst){
            openBluetooth();
        }

    }

    BluetoothAdapter adapter;
    private static final int REQUEST_ENABLE_BT = 0;
    private void openBluetooth() {
        final BluetoothManager bluetoothManager =(BluetoothManager)getSystemService(Context.BLUETOOTH_SERVICE);
        adapter = bluetoothManager.getAdapter();
        // 确保蓝牙在手机上可以开启
        if (adapter != null && !adapter.isEnabled()) {
            if(!isFirst){
                ToastUtils.showShort(mContext,getString(R.string.ble_tip));
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                    }
                },1500);
                isFirst = true;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == RESULT_OK) {
                ToastUtils.showShort(mContext,getString(R.string.toast_on_lanya));
            } else {
                ToastUtils.showShort(mContext,getString(R.string.toast_off_lanya));
            }
        }
    }

    @Override
    protected void onDestroy() {
        isResume = false;
        inactivityTimer.shutdown();
        super.onDestroy();

        AppManager.getInstance().removeActivity(this);
        ReleasePlayer();
    }
    /**
     * 释放播放器资源
     */
    private void ReleasePlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();

            //关键语句
            mediaPlayer.reset();

            mediaPlayer.release();
            mediaPlayer = null;
        }

        if(mediaBatteryPlayer !=null){
            mediaBatteryPlayer.stop();

            //关键语句
            mediaBatteryPlayer.reset();

            mediaBatteryPlayer.release();
            mediaBatteryPlayer = null;
        }
    }
    public void initViews() {

        if(isLogin()){
            if(MyApplication.getInstance().isLogin()){
                finish();
                MyApplication.isShowLoginPage = true;
                com.qdigo.chargerent.activity.LoginActivity_.intent(this).flags(Intent.FLAG_ACTIVITY_SINGLE_TOP).start();
            }
        }

        titleBar.initView(getWindow().getDecorView().getRootView(),true);
        titleBar.addTitleCallBack(this);
        if(titleBar != null){
            titleBar.setTitle(getString(R.string.title_sacn_bike));
        }
        openledImg.setOnClickListener(this);
        app = MyApplication.getInstance();
        CameraManager.init(app);
        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);
        //晚上自动打开手电筒
        if(isNight()){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    isLed = true;
                    CameraManager.get().openLed();
                }
            },1000);

        }

    }

    /**
     * 判断是否是晚上
     * @return
     */
    public boolean isNight(){
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        if (hour >= 18 || hour<=5) {
            return true;
        }
        return false;
    }
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;
    }
    @Override
    public void onPause() {
        super.onPause();
        isResume = false;
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.openledImg:
                if (!isLed) {
                    CameraManager.get().openLed();
                } else {
                    CameraManager.get().closeLed();
                }
                isLed = !isLed;
                break;
        }
    }

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onResume() {
        super.onResume();
        isResume = true;

        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        decodeFormats = null;
        characterSet = null;

        playBeep = true;
        AudioManager audioService = (AudioManager) app.getSystemService(Context.AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        initBeepSound();
        vibrate = true;
    }

    protected void openActivity(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }

    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            // The volume on STREAM_SYSTEM is not adjustable, and users found it
            // too loud,
            // so we now play on the music stream.
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);

            AssetFileDescriptor file = getResources().openRawResourceFd(
                    R.raw.beep);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(),
                        file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }

    private final MediaPlayer.OnCompletionListener beepListener = new MediaPlayer.OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
            int pix[] = getDisplayScreenResolution(this);
            float w = pix[0];
            int h = pix[1];
            float scale = ((float) CameraManager.get().cameraResolution.x) / ((float) CameraManager.get().cameraResolution.y);

            float nw = w * scale;

            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((int) nw, h);
            surfaceView.setLayoutParams(layoutParams);
        } catch (IOException ioe) {
            return;
        } catch (RuntimeException e) {
            return;
        }
        if (handler == null) {
            handler = new CaptureFragmentHandler(this, decodeFormats,
                    characterSet);
        }
    }

    public void handleDecode(Result result, Bitmap barcode) {
        inactivityTimer.onActivity();
        playBeepSoundAndVibrate();
        String resultString = result.getText();
        if (resultString.equals("")) {
            ToastUtils.showShort(mContext,R.string.scan_failed);
        } else {
            //扫描文本：www.qdigo.com/download.html?a=12345566,88888
            LogUtils.logDug(resultString);
            //TODO
            try {
                if(!TextUtils.isEmpty(resultString)){
                    doCharge(resultString);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        }

    }

    private void doCharge(String resultString) {

    }

    public void restartScan(){
        if(isResume){
            if(handler !=null){
                handler.postDelayed(new Runnable() {
                    public void run() {
                        if (handler !=null){
                            handler.restartPreviewAndDecode();
                        }
                    }
                }, 2 * 1000);

            }
        }
    }


    private static final long VIBRATE_DURATION = 200L;

    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) app.getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    public int[] getDisplayScreenResolution(Context context) {
        int[] screenSizeArray = new int[2];

        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        display.getMetrics(dm);

        screenSizeArray[0] = dm.widthPixels;
        screenSizeArray[1] = dm.heightPixels;
        int ver = Build.VERSION.SDK_INT;
        if (ver < 13) {
            screenSizeArray[1] = dm.heightPixels;
        } else if (ver == 13) {
            try {
                Method mt = display.getClass().getMethod("getRealHeight");
                screenSizeArray[1] = (Integer) mt.invoke(display);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (ver > 13) {
            try {
                Method mt = display.getClass().getMethod("getRawHeight");
                screenSizeArray[1] = (Integer) mt.invoke(display);

            } catch (Exception e) {
                screenSizeArray[1] = dm.heightPixels;
            }
        }

        return screenSizeArray;
    }

    @Override
    public boolean isLogin() {
        return true;
    }

    @Override
    public void backClick(ImageView backImg) {
        finish();
    }

    @Override
    public void rightClick(ImageView rightImg) {

    }

    @Override
    public void leftImg(ImageView backImg) {

    }

    @Override
    public void rightImg(ImageView rightImg) {

    }

    @Override
    public void rightTv(TextView rightTv) {

    }


    private MediaPlayer mediaBatteryPlayer;

    private void playMedia(String fileName) {
        if (mediaBatteryPlayer != null && mediaBatteryPlayer.isPlaying()) {
            mediaBatteryPlayer.stop();
        }

        try {
            AssetManager assetManager = getAssets();
            AssetFileDescriptor fileDesc = assetManager.openFd(fileName);
            mediaBatteryPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaBatteryPlayer.reset();
            mediaBatteryPlayer.setDataSource(fileDesc.getFileDescriptor(),
                    fileDesc.getStartOffset(),
                    fileDesc.getLength()
            );
            mediaBatteryPlayer.prepareAsync();
            mediaBatteryPlayer.setVolume(1.0f,1.0f);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
