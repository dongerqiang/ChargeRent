package com.qdigo.chargerent;

/**
 * Created by jpj on 2017-03-06.
 * 此类是url
 */

public class ServerURL {
    //Header
    public static final String MOBILENO = "mobileNo";
    public static final String MOBILEDEVICEID = "mobiledeviceId";
    public static final String ACCESSTOKEN = "accesstoken";
    public static final String CONTENTTYPE = "Content-Type";

    //主服务器
    public static final String MAIN_SERVER = "http://api.qdigo.net/";

    //测试服务器
//    public static final String MAIN_SERVER = "http://niezhao.mynatapp.cc/";
    //注销
    public static final String LOGIN_OUT = MAIN_SERVER + "v1.0/userInfo/getAccounts";
    //用户消息
    public static final String USER_DATA_URL = MAIN_SERVER + "v1.0/userInfo/getInfo";
    //故障上传接口
    public static final String ERROR_UPLOAD_URL = MAIN_SERVER + "v1.0/ebike/faultReport";
    //故障图片上传接口
    public static final String ERROR_UPLOAD_IMAGE_URL = MAIN_SERVER + "v1.0/ebike/reportFault/uploadFaultPic";
    //查询信用积分历史
    public static final String CREDIT_HISTORY = MAIN_SERVER + "v1.0/ebike/credit/getCreditHistory";
    //查询信用积分
    public static final String CREDIT_UPDATE = MAIN_SERVER + "v1.0/ebike/credit/getCurrentCredit";
    //车辆详情
    public static final String BIKE_DETAIL_URL = MAIN_SERVER + "v1.0/ebike/getEBikeInfo";
    //获得param and sign
    public static final String PARAMS_SIGN_URL = MAIN_SERVER + "v1.0/ebike/credit/getSignAndParam";
    //上传openid
    public static final String UPLOAD_OPENED_ID = MAIN_SERVER + "v1.0/ebike/credit/sesameScore";
    //获得IMEI
    public static final String GET_IMEI_URL = MAIN_SERVER + "v1.0/ebike/getIMEI";
    //新版本
    public static final String NEW_VERSION = MAIN_SERVER + "v1.0/operation/getNewVersion";
    //新版本
    public static final String VERSION = MAIN_SERVER + "v1.0/operation/getVersion";
    //得到全部车辆
    public static final String ALL_BIKES = MAIN_SERVER + "v1.0/geo/getBikes";
    //车辆详情
    public static final String BIKE_DETAIL = MAIN_SERVER + "v1.0/ebike/getEBikeInfo/";
    //登陆
    public static final String LOGIN_URL = MAIN_SERVER + "v1.0/user/registerAndLogin";
    //得到验证码
    public static final String CODE_PIN_URL = MAIN_SERVER + "v1.0/user/getPinCode";
    //认证
    public static final String AUTH_IDENTIFY = MAIN_SERVER+"v1.0/userInfo/addIdentify";
    //更改手机号
    public static final String CHANGE_MOBILE = MAIN_SERVER+"v1.0/rootUserInfo/updateMobile";
    //解锁
    public static final String UNLOCK =MAIN_SERVER+"v1.0/ebike/unlockBike";
    //上锁
    public static final String LOCK =MAIN_SERVER+"v1.0/ebike/lockBike";
    //上电
    public static final String START =MAIN_SERVER+"v1.0/ebike/startBike";
    //断电
    public static final String STOP =MAIN_SERVER+"v1.0/ebike/closeBike";
    //还车
    public static final String REBACK_BIKE =MAIN_SERVER+"v1.0/ebike/endBike";
    //纠错
    public static final String ERROR_CHECK =MAIN_SERVER+"v1.0/ebike/faultDetect";
    //获取订单
    public static final String GET_CHARGE = MAIN_SERVER+"v1.0/payment/getCharge";
    //反馈
    public static final String FEED_BACK = MAIN_SERVER+"v1.0/userInfo/feedback";
    //扫描车辆
    public static final String SCAN_BIKE = MAIN_SERVER+"v1.0/ebike/scanBike";
    //我的消息列表
    public static final String MESSAGES = MAIN_SERVER+"v1.0/userInfo/getMessage";
    //我的订单
    public static final String ACCOUNTS = MAIN_SERVER+"v1.0/userInfo/getAccounts";
    //订单详情
    public static final String ACCOUNT_DETAIL = MAIN_SERVER+"v1.0/payment/getOrderInfo/";
    //更新用户信息
    public static final String UPDATE_USER_INFO = MAIN_SERVER+"v1.0/userInfo/updateInfo";
    //更新用户头像
    public static final String UPDATE_IMAGE = MAIN_SERVER+"v1.0/userInfo/uploadPicture";
    //退款
    public static final String REFUND = MAIN_SERVER+"v1.0/payment/refund";
    //查询轨迹
    public static final String RIDE_TRACK = MAIN_SERVER+"v1.0/ebike/tracking";
    //获取充电桩
    public static final String BIKES_STATION = MAIN_SERVER+"v1.0/geo/getBikeStations";
    //充电桩详情
    public static final String BIKE_STATION_DETAIL = MAIN_SERVER+"v1.0/geo/getBikeStationInfo";
    //获取控制车辆状态
    public static final String CONTROL_CAR_STATE = MAIN_SERVER +"v1.0/ebike/getControlInfo";
    //得到充值记录
    public static final String CHONGZHI_RECORD = MAIN_SERVER+"v1.0/payment/getCharges";
    //车是否在控制中
    public static final String IS_CONTROL = MAIN_SERVER+"v1.0/ebike/isControl";
    //学生证图片上传接口
    public static final String STUDENT_AUTH_URL = MAIN_SERVER+"v1.0/ebike/student/uploadPic";
    //学生信息提交接口
    public static final String STUDENT_INFO_UPLOAD=MAIN_SERVER+"v1.0/ebike/student/studentAuth";
    //蓝牙上传接口
    public static final String BT_UPLOAD=MAIN_SERVER+"/v1.0/bikeProtocol/bluetooth";
    //申请救援
    public static final String START_RESURE = "http://admin.qdigo.net:9002/backManage/applyAid";
    //支付救援费用
    public static final String PAY_RESCUE = "http://admin.qdigo.net:9002/backManage/endRescue";
    //取消订单
    public static final String CANCEL_DING="http://admin.qdigo.net:9002/backManage/cancelOrder";
    //上传各种操作位置
    public static final String UPLOAD_POSITION = MAIN_SERVER+"v1.0/ebike/uploadLocation";
    //关闭蓝牙
    public static final String CLOSE_BLE=MAIN_SERVER+"v1.0/ebike/closeBle";
    //救援列表
    public static final String RESCUE_LIST="http://admin.qdigo.net:9002/backManage/getUserAidOrder";
    //上传用户位置
    public static final String USER_ADDRESS = MAIN_SERVER+"v1.0/userInfo/userAddress";

}