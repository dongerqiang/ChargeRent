package com.qdigo.chargerent.entity.data;

import java.io.Serializable;

public class OrderInfo implements Serializable {
//	 "type": 0,
//     "startTime": "2016-12-05T04:45:00.000+0000",
//     "endTime": "2016-12-05T06:02:00.000+0000",
//     "orderAmount": 53.78,
//     "price": null,
//     "countTime": 47

	/*
	"orderId": 45,
    "price": 0.guide1,
    "orderStatus": guide2,
    "consume": 0.guide1,
    "startTime": "2017-04-08 13:43:35",
    "endTime": "2017-04-08 13:44:43"*/

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	public String type;
	public String startTime;
	public String endTime;
	public double orderAmount;
	public double price;
	public int countTime;
	public String orderId;
	public int unitMinutes;
}
