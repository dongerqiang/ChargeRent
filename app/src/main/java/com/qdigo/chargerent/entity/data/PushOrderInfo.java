package com.qdigo.chargerent.entity.data;

public class PushOrderInfo {
	/*
	"orderId": 45, 
    "price": 0.guide1,
    "orderStatus": guide2,
    "consume": 0.guide1,
    "startTime": "2017-04-08 13:43:35", 
    "endTime": "2017-04-08 13:44:43"*/
	
	public int orderId;//订单号
	public double price;//价格
	public int orderStatus;//订单状态
	public double consume;//消费金额
	public String startTime;//开始骑行
	public String endTime;//结束骑行
	public int unitMinutes;
}
