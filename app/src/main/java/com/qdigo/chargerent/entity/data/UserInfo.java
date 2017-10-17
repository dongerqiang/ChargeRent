package com.qdigo.chargerent.entity.data;

public class UserInfo {
	
	/*     
	 	"userId" : guide2,
	    "userName" : "小滴",
	    "userImgurl" : "/home/pic/guide1uide1.jpg",
	    "myWallet" : 0.0,
	    "requireDeposit" : 299.0,//押金金额
	    "mobileNo" : "15801753108",
	    "realName" : null,
	    "idNo" : null,
	    "balance" : 0.0, //余额
        "deposit" : 0.0, //充值
	    url : v1.0/userInfo/getInfo
	    
	    "refundStatus" : "",//还款状态  success（退款成功） pending(退款中) 
	    					fail（退款失败） null or ""(未申请退款)
	    "zmScore" : null,//芝麻分 （认证标志）String (null or "" 未认证)
	    "wxliteOpenId" : null*/
    	
    public String userId;
    public String userName;
    public String userImgurl;//图片路径
    public double myWallet;//钱包
    public double deposit;//充值
    public double balance;//余额
    public String mobileNo;//手机号
    public double requireDeposit;//押金金额
    public String zmScore;//芝麻分 （认证标志）
    
    public String refundStatus; //还款状态  success pending fail null(未还款)
    public String realName;//姓名
    public String idNo; // 省份证
    public String studentAuth;//学生认证状态 success pending fail null
    public String city;//用户所在城市
}
