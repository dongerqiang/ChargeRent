package com.qdigo.chargerent.entity.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jpj on 2017-09-22.
 */

public class Rescue implements Parcelable {


    /**
     * rescueId : 5
     * rescueNo : 170921000015
     * imei : 860720020011209
     * deviceId : 5107030011
     * aidType : 一键呼救
     * consume : 0.1
     * aidEndTime : null
     * failInfo :
     * applyTime : 2017-09-21T04:11:01.000+0000
     * payType : 1
     * type : 0
     * aidStartTime : 2017-09-21T04:17:08.000+0000
     * opsMobile : 15801753108
     * kilometer : 1
     */

    public int rescueId;
    public String rescueNo;
    public String imei;
    public String deviceId;
    public String aidType;
    public double consume;
    public String aidEndTime;
    public String failInfo;
    public String applyTime;
    public int payType;
    public int type;
    public String aidStartTime;
    public String opsMobile;
    public double kilometer;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.rescueId);
        dest.writeString(this.rescueNo);
        dest.writeString(this.imei);
        dest.writeString(this.deviceId);
        dest.writeString(this.aidType);
        dest.writeDouble(this.consume);
        dest.writeString(this.aidEndTime);
        dest.writeString(this.failInfo);
        dest.writeString(this.applyTime);
        dest.writeInt(this.payType);
        dest.writeInt(this.type);
        dest.writeString(this.aidStartTime);
        dest.writeString(this.opsMobile);
        dest.writeDouble(this.kilometer);
    }

    public Rescue() {
    }

    protected Rescue(Parcel in) {
        this.rescueId = in.readInt();
        this.rescueNo = in.readString();
        this.imei = in.readString();
        this.deviceId = in.readString();
        this.aidType = in.readString();
        this.consume = in.readDouble();
        this.aidEndTime = in.readString();
        this.failInfo = in.readString();
        this.applyTime = in.readString();
        this.payType = in.readInt();
        this.type = in.readInt();
        this.aidStartTime = in.readString();
        this.opsMobile = in.readString();
        this.kilometer = in.readDouble();
    }

    public static final Parcelable.Creator<Rescue> CREATOR = new Parcelable.Creator<Rescue>() {
        @Override
        public Rescue createFromParcel(Parcel source) {
            return new Rescue(source);
        }

        @Override
        public Rescue[] newArray(int size) {
            return new Rescue[size];
        }
    };
}
