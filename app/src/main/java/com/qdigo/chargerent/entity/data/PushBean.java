package com.qdigo.chargerent.entity.data;

/**
 * Created by jpj on 2017-08-29.
 */

public class PushBean {


    /**
     * data : ""
     * pushType : appEndSuccess
     *
     * display, //只做展示
       autoReturn, //自动还车,app据此判断退出控制界面
       warn, // 警告,比如低电量报警
       buttonEndFail, buttonEndSuccess, //长按车上按钮，成功或失败
       appEndSuccess,
       refundSuccess
       opsRescue, userRescue //推送给管理员和用户的救援订单
     */

    private String data;
    private String pushType;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getPushType() {
        return pushType;
    }

    public void setPushType(String pushType) {
        this.pushType = pushType;
    }
}
