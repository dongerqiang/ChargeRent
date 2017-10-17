package com.qdigo.chargerent.ble;

import android.content.Context;
import android.content.Intent;

/**
 * Created by jpj on 2017-09-05.
 */

public class BroadCastUtils {

    public static final String BRODCAST_CONNECT_STATE = "connect_state";
    public static final String BRODCAST_CONNECT_HEART_STATE = "connected_heart_beat";
    public static final String BRODCAST_UPLOAD_HEART_STATE = "upload_heart_beat";
    public static final String BRODCAST_CONNECT_AUTO = "connect_auto";
    public static final String CONNECT_KEY="connect_key";

    public static final String BIKE_HEART_ARRAY="bike_heart_array";
    public static final String BIKE_STATE_ORIGINAL = "bike_state_original";

    private BroadCastUtils(){}
    private static BroadCastUtils instance = new BroadCastUtils();
    public static BroadCastUtils getInstance() {
        return instance;
    }
    private Context context;

    //initialize
    public void init(Context context){
        this.context=context;
    }

    /**
     * 发送链接状态的广播
     * @param connect true：连接上，false:连接失败
     */
    public void sendConnectState(boolean connect){
        Intent intent = new Intent(BRODCAST_CONNECT_STATE);
        intent.putExtra(CONNECT_KEY,connect);
        context.sendBroadcast(intent);
    }

    public void sendAutoConnect(){
        Intent intent = new Intent(BRODCAST_CONNECT_AUTO);
        context.sendBroadcast(intent);
    }

    public void sendHeartState(int[] connect){
        Intent intent = new Intent(BRODCAST_CONNECT_HEART_STATE);
        intent.putExtra(BIKE_HEART_ARRAY,connect);
        context.sendBroadcast(intent);
    }
}
