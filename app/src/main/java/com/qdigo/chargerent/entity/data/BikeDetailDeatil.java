package com.qdigo.chargerent.entity.data;

public class BikeDetailDeatil {
	/*
		"bkId": null,
		"deviceId": null,
		"imeiId": null,
		"type": "A",
		"battery": 100,
		"longitude": 0,
		"latitude": 0
	*/
	
	public String imeiId;//IMEI号
	public String bkId;
	public String deviceId;
	public double longitude;
	public double latitude;
	public String type;// A 简易版 B 普通版 C 豪华版
	public int battery;//电量
	
	
}
