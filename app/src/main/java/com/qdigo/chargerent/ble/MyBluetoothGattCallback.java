package com.qdigo.chargerent.ble;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;


/**
 * Created by jpj on 2017-09-04.
 */

public class MyBluetoothGattCallback extends BluetoothGattCallback {
    public static final String BLE="comunication";
    private static final int ORDER_LENGTH = 3;
    private static final int HEART_LENGTH = 4;
    private List<BluetoothGattService> mBtServices;
    private static final String BLUX_DEVICE_UUID_STRING = "78667579-4E28-477f-9EF3-44C041A1AC5F";
    private final static UUID VIRTUAL_DEVICE_CHAR_WRITE_UUID =
            UUID.fromString("78667579-66B6-4755-AF51-8937D87D4251");
    private final static UUID VIRTUAL_DEVICE_CHAR_READ_UUID =
            UUID.fromString("78667579-CC60-4E25-B1CE-6FB511B90785");
    private final static UUID VIRTUAL_DEVICE_CHAR_IRQ_UUID =
            UUID.fromString("78667579-1DF0-447D-95E0-5E5E2A9C01E2");

    private final static UUID UUID_CLIENT_CHARACTERISTIC_CONFIG =
            UUID.fromString("00002902-0000-1000-8000-00805F9B34FB");

    private byte[] HEART_DATA ={0x40,0x78,0x13,(byte)0x90,0X01};

    private byte[] LOCK_DATA={0x40,0x66,0x32,0x10,0x01};
    private byte[] UNLOCK_DATA={0x40,0x66,0x32,0x11,0x01};
    private byte[] START_DATA={0x40,0x66,0x32,0x12,0x01};
    private byte[] FIND_DATA={0x40,0x66,0x12,0x07};
    private byte[] JUDGE_DATA_ONE={0x40,0x78,0x33};
    private byte[] judgeImei;
    //懒汉式单例模式
    private MyBluetoothGattCallback() {
    }

    private static MyBluetoothGattCallback instance = new MyBluetoothGattCallback();
    public static MyBluetoothGattCallback getInstance() {
        return instance;
    }

    @Override
    public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
        Log.w(BLE, " onConnectionStateChange == " + newState + " : " + status);

        if(newState == BluetoothProfile.STATE_CONNECTED) {
           //连接成功 -》搜索服务
            discoverService(gatt,status);
            connected = true;
            BroadCastUtils.getInstance().sendConnectState(true);
        } else if(newState == BluetoothProfile.STATE_DISCONNECTED){
            //连接失败
            disConnected(true);
        }
    }

    private void discoverService(BluetoothGatt gatt, int status) {
        if(status != BluetoothGatt.GATT_SUCCESS){ //if(mStatus == 133) {
            if(gatt != null) {
                gatt.close();
                gatt = null;
            }
            disConnected(true);
            return;
        }
        //扫描服务
        gatt.discoverServices();
    }

    /*//断开连接
    private void disConnected() {
        Log.w(BLE,"disConnected");
        BroadCastUtils.getInstance().sendConnectState(false);
    }*/
    private void disConnected(boolean isAuto) {
        Log.w(BLE,"isAuto = "+isAuto );
        connected =false;
        if(isAuto){
            //断开，需要自动重连
            BroadCastUtils.getInstance().sendAutoConnect();
        }else{
            //主动断开
            BroadCastUtils.getInstance().sendConnectState(false);
        }
    }
    @Override
    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
        Log.w(BLE, "<serdis: " + status);

        // all services discovered.
        serviceDiscoveredDelayed(gatt,status);
    }
    private BluetoothGattService connectService;
    // Private members
    private BluetoothGattCharacteristic mBtCharRead;
    private BluetoothGattCharacteristic mBtCharWrite;
    private BluetoothGattCharacteristic mBtCharIrq;
    BluetoothGatt mGatt;
    private void serviceDiscoveredDelayed(BluetoothGatt gatt, int status) {
        if(status != BluetoothGatt.GATT_SUCCESS)
            return;
        mGatt = gatt;
        mBtServices = gatt.getServices();
        connectService =null;
        for(BluetoothGattService btService : mBtServices) {
           //找到读写的服务
            if(btService.getUuid().equals(UUID.fromString(BLUX_DEVICE_UUID_STRING))){
                connectService = btService;
            }
        }
        if(connectService == null){
            Log.w(BLE,"没找到服务");
        }else{
            //得到三个character
            Log.w(BLE,"找到服务了");
            mBtCharRead = connectService.getCharacteristic(VIRTUAL_DEVICE_CHAR_READ_UUID);
            mBtCharWrite = connectService.getCharacteristic(VIRTUAL_DEVICE_CHAR_WRITE_UUID);
            mBtCharIrq = connectService.getCharacteristic(VIRTUAL_DEVICE_CHAR_IRQ_UUID);
            enableNotification(true);
        }

    }
    private void enableNotification(boolean enable) {
        if(mGatt !=null && mBtCharIrq != null) {
            BluetoothGattDescriptor desc = mBtCharIrq.getDescriptor(UUID_CLIENT_CHARACTERISTIC_CONFIG);
            mGatt.setCharacteristicNotification(mBtCharIrq, enable);
            byte [] data = enable ? BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE : BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE;
            if(mGatt != null) {
                desc.setValue(data);
                mGatt.writeDescriptor(desc);
            }
        }
    }

    @Override
    public void onDescriptorWrite(final BluetoothGatt gatt, final BluetoothGattDescriptor descriptor, int status) {
        Log.w(BLE, "<dscwr: " + status);
        if(status == BluetoothGatt.GATT_SUCCESS){
            //先写验证包
            writeJudegData(gatt,descriptor,judgeImei);
            //打开心跳包
            if(mHandler !=null){
                //写心跳
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        writeHeartData(gatt,descriptor,HEART_DATA);
                    }
                },800);

            }
        }
    }

    private void writeJudegData(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, byte[] judgeImei) {
        if(gatt !=null && mBtCharWrite!=null){
            if(judgeImei!=null){
                mBtCharWrite.setValue(judgeImei);
                gatt.writeCharacteristic(mBtCharWrite);
            }
        }
    }

    private void writeHeartData(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, byte[] heart_data) {
        /*if(gatt !=null && mBtCharIrq!=null){
            mBtCharIrq.setValue(heart_data);
            gatt.writeCharacteristic(mBtCharIrq);
        }*/
        if(gatt !=null && mBtCharWrite!=null){
            mBtCharWrite.setValue(heart_data);
            gatt.writeCharacteristic(mBtCharWrite);
        }
    }

    @Override
    public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        super.onCharacteristicWrite(gatt, characteristic, status);
    }

    @Override
    public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        super.onCharacteristicRead(gatt, characteristic, status);
    }

    @Override
    public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
        Log.w(BLE, "<chrchg");
        CharacteristicChanged(gatt,characteristic);
    }

    private void CharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
        if(characteristic !=null){
            byte[] value = characteristic.getValue();
            Log.w(BLE,"CharacteristicChanged == "+ Arrays.toString(value));
//            if(myBleStateListener!=null){
                //指令数据包
                if(value.length == ORDER_LENGTH){
                    //上电成功 18，撤防、断电17、上锁16、
                   int order = value[ORDER_LENGTH-1]&0xff;
                    mBleStateListener.onOrederFinish(order);
                }
                //心跳数据包，表示在连接,超时没收到则断开
                if(value.length == HEART_LENGTH){

                    int[] state = {0,0,0,0,0,0,0,0};
                    //是否外接电源
                    if((value[HEART_LENGTH-1]&0x01)==1){
                        state[0] = 1;
                    }else{
                        state[0] = 0;
                    }
                    //是否上电
                    if((value[HEART_LENGTH-1]&0x02)==1){
                        state[1] = 1;
                    }else{
                        state[1] = 0;
                    }
                    //是否上锁
                    if((value[HEART_LENGTH-1]&0x04)==1){
                        state[2] = 1;
                    }else{
                        state[2] = 0;
                    }
                    //是否震动
                    if((value[HEART_LENGTH-1]&0x08)==1){
                        state[3] = 1;
                    }else{
                        state[3] = 0;
                    }
                    //是否轮动
                    if((value[HEART_LENGTH-1]&0x10)==1){
                        state[4] = 1;
                    }else{
                        state[4] = 0;
                    }
                    //是否打开ble
                    if((value[HEART_LENGTH-1]&0x20)==1){
                        state[5] = 1;
                    }else{
                        state[5] = 0;
                    }
                    state[6]=(value[HEART_LENGTH-2]&0xFF);
                    state[7]=(value[HEART_LENGTH-1&0xff]);
                    BroadCastUtils.getInstance().sendHeartState(state);
                }
            }
    }
    BleStateListener mBleStateListener;
    public void start(BleStateListener bleStateListener) {
        this.mBleStateListener = bleStateListener;
        wirteData(START_DATA);
    }

    public void stop(BleStateListener bleStateListener) {
        this.mBleStateListener = bleStateListener;
        wirteData(UNLOCK_DATA);
    }

    public void lock(BleStateListener bleStateListener) {
        this.mBleStateListener = bleStateListener;
        wirteData(LOCK_DATA);
    }

    public void unlock(BleStateListener bleStateListener) {
        this.mBleStateListener = bleStateListener;
        wirteData(UNLOCK_DATA);
    }

    public void find() {
        wirteData(FIND_DATA);
    }

    private void wirteData(byte [] data){
        if(mGatt!=null && mBtCharWrite !=null){
            Log.w(BLE,"-----> "+ Arrays.toString(data));
            mBtCharWrite.setValue(data);
            mGatt.writeCharacteristic(mBtCharWrite);
        }
    }


    /**
     * 主动断开链接
     */
    public void close() {
        if(mGatt!=null){
            mGatt.close();
        }
        disConnected(false);
    }



    Handler mHandler ;
    public void setImei(String num, Handler handler) {
        mHandler=handler;
        byte[] imei =parseImei(num);
        Log.w(BLE, Arrays.toString(imei));
        //拼接数据
        if(imei!=null && imei.length==6){
            byte[] bytes = new byte[imei.length + JUDGE_DATA_ONE.length];
            System.arraycopy(JUDGE_DATA_ONE, 0, bytes, 0, JUDGE_DATA_ONE.length);
//            imei = reverseByte(imei);
            System.arraycopy(imei, 0, bytes, JUDGE_DATA_ONE.length, imei.length);
            judgeImei = bytes ;
            Log.w(BLE, Arrays.toString(judgeImei));
        }
    }

    private byte[] reverseByte(byte[] imei) {
        byte[] bytes = new byte[imei.length];
        for(int i=0;i<imei.length;i++){
            bytes[i] = imei[imei.length - i - 1];
        }
        return bytes;
    }

    private byte[] parseImei(String bleName) {
        //10000461

        if(!TextUtils.isEmpty(bleName)){
            int[] a = new int[bleName.length()];
            for (int i=0;i<bleName.length();i++){
                //先由字符串转换成char,再转换成String,然后Integer
                a[i] = Integer.parseInt( String.valueOf(bleName.charAt(i)));
            }
            // [ 1 0 0 0 0 4 6 1]
            byte[] dataByte = new byte[6];
            dataByte[0] =  (byte)((a[0]+a[1]+a[2]+a[3]+a[4]+a[5]+a[6]+a[7])&0xff);
            dataByte[1] =  (byte)((a[0]^a[1]^a[2]^a[3]^a[4]^a[5]^a[6]^a[7])&0xff);
            dataByte[2] =  (byte)(((a[0]+a[1]+a[2]+a[3])|(a[4]*a[5]*a[6]*a[7]))&0xff);
            dataByte[3] =  (byte)(((a[0]+a[1]+a[2]+a[3])*(a[4]+a[5]+a[6]+a[7]))&0xff);
            dataByte[4] =  (byte)(((a[0]*a[1]*a[2]*a[3])+(a[4]+a[5]+a[6]+a[7]))&0xff);
            dataByte[5] =  (byte)(((a[0]^a[1]^a[2]^a[3])+(a[4]*a[5]+a[6]*a[7]))&0xff);
            return dataByte;
        }
        return null;
    }

    private boolean connected = false;
    public  boolean isConnected(){
        return connected;
    }
}
