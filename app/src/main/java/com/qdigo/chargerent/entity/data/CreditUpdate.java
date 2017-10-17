package com.qdigo.chargerent.entity.data;


import com.qdigo.chargerent.entity.net.responseBean.BaseResponse;

/**
 * Created by jpj on 2017-02-27.
 */

public class CreditUpdate extends BaseResponse {

    /**
     * statusCode : 200
     * message : 成功返回当前信用积分
     * data : {"changeScore":guide1,"curScore":101,"lastChangeTime":"2017-02-23 15:04"}
     */

    public DataBean data;

    public static class DataBean {
        /**
         * changeScore : guide1
         * curScore : 101
         * lastChangeTime : 2017-02-23 15:04
         */

    	public int changeScore;
    	public int curScore;
    	public String lastChangeTime;
       
    }
}
