package com.ktxinfo.controllib.listener;

/**
 * @author LittleGreens <a href="mailto:alittlegreens@foxmail.com">Contact me.</a>
 * @version 1.0
 * @since 2019/10/10 21:17
 */
public interface OnDeviceControlCommondListener {

    /**
     * 开门
     * @return 发送是否成功
     */
    boolean openDoor();

    /**
     * 查询门状态
     * @return 发送是否成功
     */
    boolean queryDoorStatus();

    /**
     * 查询温度
     * @return 发送是否成功
     */
    boolean queryTemperature();

    /**
     * APP启动握手指令,查询固件版本
     * @return 发送是否成功
     */
    boolean queryDeviceVersion();
}
