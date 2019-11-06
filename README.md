# AIDL_client

 通过AIDL的方式 与 串口通讯的APP通讯。

 [1. 使用方式](#1-使用方式)
- [2. SDK简介](#2-sdk简介)
    - [2.1. ControlManager](#21-controlmanager)
    - [2.2. 该类主要功能](#22-该类主要功能)
 - [2.3. API简介](#23-api简介)
    - [2.3.1. 初始化ControlManager](#231-初始化controlmanager)
    - [2.3.2. 绑定与解绑远程服务](#232-绑定与解绑远程服务)
    - [2.3.3. 发送指令给远程服务](#233-发送指令给远程服务)
    - [2.3.4. 接口回调](#234-接口回调)
# 1. 使用方式

1. maven依赖方式
```Gradle

allprojects {
    repositories {
        google()
        jcenter()
    }
}

dependencies {
    implementation 'aidog.littlegreens:controllib:1.0.3'
}

```

2. 需要的话可提供aar/jar 的方式

# 2. SDK简介

## 2.1. ControlManager

## 2.2. 该类主要功能

1. 绑定远程服务、解绑远程服务
2. 提供了和远程服务通信接口
3. 提供了接口回调，接收远程服务回调的通知。

## 2.3. API简介

### 2.3.1. 初始化ControlManager

```Java
ControlManager controlManager = ControlManager.getInstance(context);
```

### 2.3.2. 绑定与解绑远程服务

1. 绑定
```Java
            if (!controlManager.isBound()) {
                controlManager.binderServer();
            }
```
2. 解绑
```Java
            if (!controlManager.isBound()) {
                 controlManager.unBindServer();
            }
```
3. 是否已经绑定
```Java
            controlManager.isBound();
           
```

### 2.3.3. 发送指令给远程服务

- 开门
```Java
         boolean isServerBinder = controlManager.openDoor();
          if (!isServerBinder) {
                    Toast.makeText(this, "远程服务没有绑定，请绑定后，再发送指令", Toast.LENGTH_SHORT).show();
                }
           
```

- 查询门状态
```Java
           controlManager.queryDoorStatus();
           
```
- 查询温度
```Java
            controlManager.queryTemperature();
           
```
- 查询机型和固件版本
```Java
            controlManager.queryDeviceVersion();
           
```

### 2.3.4. 接口回调

```Java

 controlManager.setOnControlServerListener(new ControlServerListener() {
            @Override
            public void onServiceConnected() {
                Toast.makeText(MainActivity.this, "远程服务绑定成功",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onServiceDisconnected() {
                Toast.makeText(MainActivity.this, "远程服务断开连接",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void valueChanged(DeviceBean deviceBean) {
                Log.d(TAG, "valueChanged: " + deviceBean.toString());
                switch (deviceBean.getFunctionType()) {
                    case FunctionType.OpenDoor:
                        int openDoorResponse = deviceBean.getOpenDoorResponse();
                        if (openDoorResponse == 0) {
                            mCallbackText.setText("开门失败");
                        } else {
                            mCallbackText.setText("开门成功");
                        }
                        break;
                    case FunctionType.QueryDoor:
                        int queryDoorStatusResponse = deviceBean.getQueryDoorStatusResponse();
                        if (queryDoorStatusResponse == 0) {
                            mCallbackText.setText("开门失败");
                        } else {
                            mCallbackText.setText("开门成功");
                        }
                        break;
                    case FunctionType.QueryTemperature:
                        float queryTemperatureResponse = deviceBean.getQueryTemperatureResponse();
                        if (queryTemperatureResponse == -1) {
                            mCallbackText.setText("温度传感器异常");
                        } else {
                            mCallbackText.setText("温度：" + queryTemperatureResponse + "摄氏度");
                        }
                        break;
                    case FunctionType.PersonNear:
                        mCallbackText.setText("人体靠近");
                        break;
                    case FunctionType.QueryDeviceVersion:
                        int deviceTypeResponse = deviceBean.getDeviceTypeResponse();
                        int deviceVersion = deviceBean.getDeviceVersion();
                        mCallbackText.setText("机型:" + deviceTypeResponse + " 固件版本:" + deviceVersion);
                        break;
                }

            }
        });

```
