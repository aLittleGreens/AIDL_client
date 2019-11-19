package com.ktxinfo.aidl;

import android.os.Parcel;
import android.os.Parcelable;

import com.ktxinfo.controllib.bean.FunctionType;


/**
 * @author LittleGreens <a href="mailto:alittlegreens@foxmail.com">Contact me.</a>
 * @version 1.0
 * @since 2019/10/10 21:26
 */
public class DeviceBean implements Parcelable {
    /**
     * <p>This functionType {@link FunctionType}</p>
     */
    private int functionType;  //功能

    private int openDoorResponse; //回复开门 0:失败 / 1：成功


    private int queryDoorStatusResponse; //回复查询门 0：关门 / 1 ：关门


    private float queryTemperatureResponse; //回复温度


    private int deviceTypeResponse; //机型

    private int deviceVersion;  //固件版本

    public DeviceBean() {
    }

    public DeviceBean(int functionType) {
        this.functionType = functionType;
    }

    public int getFunctionType() {
        return functionType;
    }

    public void setFunctionType(int functionType) {
        this.functionType = functionType;
    }

    public int getOpenDoorResponse() {
        return openDoorResponse;
    }

    public void setOpenDoorResponse(int openDoorResponse) {
        this.openDoorResponse = openDoorResponse;
    }

    public int getQueryDoorStatusResponse() {
        return queryDoorStatusResponse;
    }

    public void setQueryDoorStatusResponse(int queryDoorStatusResponse) {
        this.queryDoorStatusResponse = queryDoorStatusResponse;
    }

    public float getQueryTemperatureResponse() {
        return queryTemperatureResponse;
    }

    public void setQueryTemperatureResponse(float queryTemperatureResponse) {
        this.queryTemperatureResponse = queryTemperatureResponse;
    }

    public int getDeviceTypeResponse() {
        return deviceTypeResponse;
    }

    public void setDeviceTypeResponse(int deviceTypeResponse) {
        this.deviceTypeResponse = deviceTypeResponse;
    }

    public int getDeviceVersion() {
        return deviceVersion;
    }

    public void setDeviceVersion(int deviceVersion) {
        this.deviceVersion = deviceVersion;
    }

    protected DeviceBean(Parcel in) {
        functionType = in.readInt();
        openDoorResponse = in.readInt();
        queryDoorStatusResponse = in.readInt();
        queryTemperatureResponse = in.readFloat();
        deviceTypeResponse = in.readInt();
        deviceVersion = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(functionType);
        dest.writeInt(openDoorResponse);
        dest.writeInt(queryDoorStatusResponse);
        dest.writeFloat(queryTemperatureResponse);
        dest.writeInt(deviceTypeResponse);
        dest.writeInt(deviceVersion);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DeviceBean> CREATOR = new Creator<DeviceBean>() {
        @Override
        public DeviceBean createFromParcel(Parcel in) {
            return new DeviceBean(in);
        }

        @Override
        public DeviceBean[] newArray(int size) {
            return new DeviceBean[size];
        }
    };

    @Override
    public String toString() {
        return "DeviceBean{" +
                "functionType=" + functionType +
                ", openDoorResponse=" + openDoorResponse +
                ", queryDoorStatusResponse=" + queryDoorStatusResponse +
                ", queryTemperatureResponse=" + queryTemperatureResponse +
                ", deviceTypeResponse=" + deviceTypeResponse +
                ", deviceVersion=" + deviceVersion +
                '}';
    }
}
