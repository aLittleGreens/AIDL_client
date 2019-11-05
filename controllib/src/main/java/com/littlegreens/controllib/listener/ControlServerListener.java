package com.littlegreens.controllib.listener;

import com.littlegreens.aidl.DeviceBean;

/**
 * @author LittleGreens <a href="mailto:alittlegreens@foxmail.com">Contact me.</a>
 * @version 1.0
 * @since 2019/9/23 14:48
 */
public interface ControlServerListener {

    void onServiceConnected();

    void onServiceDisconnected();

    /**
     * 远程服务传递的值
     *
     * @param deviceBean DeviceBean
     */
    void valueChanged(DeviceBean deviceBean);


}
