package com.qdigo.chargerent.entity.data;

import java.io.Serializable;

public class ConsumeOrder implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	public String orderId;
	public String startTime;
	public int orderStatus;
	public String endTime;
	public int minutes;
	public double consume;
	public double price;
	public int unitMinutes;
	public String policyNo;//保单号
}
