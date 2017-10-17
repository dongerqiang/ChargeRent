package com.qdigo.chargerent.entity.data;

/**
 * Created by jpj on 2017-04-18.
 */

public class PaymentRequest {
    String channel;
    int amount;
    String payType;

    public PaymentRequest(String channel, int amount, String payType) {
        this.channel = channel;
        this.amount = amount;
        this.payType = payType;
    }
}
