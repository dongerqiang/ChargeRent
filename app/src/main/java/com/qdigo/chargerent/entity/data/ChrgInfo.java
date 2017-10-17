package com.qdigo.chargerent.entity.data;

import java.util.List;

public class ChrgInfo {
	
	public String stationId;
	public double latitude;
	public double longitude;
	public int  status;
	public String stationName;
	public int bikeCount;
	public int radius;
	public List<ChargePoint> points;
}
