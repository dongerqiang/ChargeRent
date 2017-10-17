package com.qdigo.chargerent.entity.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jpj on 2017-09-16.
 */

public class RescueBean implements Parcelable {

    /**
     * aidStartTime : 1505550737582
     * applyTime : 1505549682000
     * consume : 4
     * kilometer : 0.0128
     * latitude : 31.224454485386936
     * longitude : 121.34704022794415
     * mobileNo : 15801753108
     * pushType : userRescue
     */

    public long aidStartTime;
    public long applyTime;
    public long consume;
    public double kilometer;
    public double latitude;
    public double longitude;
    public String mobileNo;
    public String pushType;
    public int rescueId;
    public String rescueNo;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.aidStartTime);
        dest.writeLong(this.applyTime);
        dest.writeLong(this.consume);
        dest.writeDouble(this.kilometer);
        dest.writeDouble(this.latitude);
        dest.writeDouble(this.longitude);
        dest.writeString(this.mobileNo);
        dest.writeString(this.pushType);
        dest.writeInt(this.rescueId);
        dest.writeString(this.rescueNo);
    }

    public RescueBean() {
    }

    protected RescueBean(Parcel in) {
        this.aidStartTime = in.readLong();
        this.applyTime = in.readLong();
        this.consume = in.readLong();
        this.kilometer = in.readDouble();
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
        this.mobileNo = in.readString();
        this.pushType = in.readString();
        this.rescueId = in.readInt();
        this.rescueNo = in.readString();
    }

    public static final Parcelable.Creator<RescueBean> CREATOR = new Parcelable.Creator<RescueBean>() {
        @Override
        public RescueBean createFromParcel(Parcel source) {
            return new RescueBean(source);
        }

        @Override
        public RescueBean[] newArray(int size) {
            return new RescueBean[size];
        }
    };
}
