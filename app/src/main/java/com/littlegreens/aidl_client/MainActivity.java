package com.littlegreens.aidl_client;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.littlegreens.aidl.DeviceBean;
import com.littlegreens.controllib.ControlManager;
import com.littlegreens.controllib.bean.FunctionType;
import com.littlegreens.controllib.listener.ControlServerListener;

import java.util.Random;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    TextView mCallbackText;
    private ControlManager controlManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        controlManager = ControlManager.getInstance(this);
        controlManager.setOnControlServerListener(new ControlServerListener() {
            @Override
            public void onServiceConnected() {
                mCallbackText.setText("Attached.");
                Toast.makeText(MainActivity.this, "远程服务绑定成功",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onServiceDisconnected() {
                mCallbackText.setText("Disconnected.");
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
    }

    private void initView() {

        Button button = (Button) findViewById(R.id.bind);
        button.setOnClickListener(mBindListener);
        Button unbind = (Button) findViewById(R.id.unbind);
        unbind.setOnClickListener(mUnbindListener);
        mCallbackText = (TextView) findViewById(R.id.callback);
        mCallbackText.setText("Not attached.");

        findViewById(R.id.openDoor).setOnClickListener(this);
        findViewById(R.id.queryDoor).setOnClickListener(this);
        findViewById(R.id.queryTemperature).setOnClickListener(this);
        findViewById(R.id.queryDeviceVersion).setOnClickListener(this);
    }

    /**
     * 绑定
     */
    private View.OnClickListener mBindListener = new View.OnClickListener() {
        public void onClick(View v) {
            if (!controlManager.isBound()) {
                mCallbackText.setText("Binding.");
                controlManager.binderServer();
            } else {
                Log.e(TAG, "remote server already binder");
            }
        }
    };
    /**
     * 解除绑定
     */
    private View.OnClickListener mUnbindListener = new View.OnClickListener() {
        public void onClick(View v) {
            if (controlManager.isBound()) {
                controlManager.unBindServer();
                mCallbackText.setText("Unbind.");
            }
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        controlManager.unBindServer();
    }

    @Override
    public void onClick(View v) {
        boolean isServerBinder = false;
        switch (v.getId()) {
            case R.id.openDoor:
                isServerBinder = controlManager.openDoor();
                if (!isServerBinder) {
                    Toast.makeText(this, "远程服务没有绑定，请绑定后，再发送指令", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.queryDoor:
                isServerBinder = controlManager.queryDoorStatus();
                if (!isServerBinder) {
                    Toast.makeText(this, "远程服务没有绑定，请绑定后，再发送指令", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.queryTemperature:
                isServerBinder = controlManager.queryTemperature();
                if (!isServerBinder) {
                    Toast.makeText(this, "远程服务没有绑定，请绑定后，再发送指令", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.queryDeviceVersion:
                isServerBinder = controlManager.queryDeviceVersion();
                if (!isServerBinder) {
                    Toast.makeText(this, "远程服务没有绑定，请绑定后，再发送指令", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}

