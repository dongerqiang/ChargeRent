package com.qdigo.chargerent.entity.data;

/**
 * Created by jpj on 2017-07-17.
 */

public class BikeInfo {
    /*
            "deviceId" : "0217050005",
            "battery" : 0,
            "status" : 0
    */

    public String deviceId;//车架号
    public int battery;
    public int status;//0 可用车，2使用中
}
