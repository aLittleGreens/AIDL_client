package com.littlegreens.aidl_client;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.littlegreens.controllib.ControlManager;
import com.littlegreens.controllib.listener.ControlServerListener;

import java.util.Random;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    Button mKillButton;
    TextView mCallbackText;
    private ControlManager controlManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = (Button) findViewById(R.id.bind);
        button.setOnClickListener(mBindListener);
        Button unbind = (Button) findViewById(R.id.unbind);
        unbind.setOnClickListener(mUnbindListener);
        mKillButton = (Button) findViewById(R.id.kill);
        mKillButton.setOnClickListener(mKillListener);
        mKillButton.setEnabled(false);
        mCallbackText = (TextView) findViewById(R.id.callback);
        mCallbackText.setText("Not attached.");
        controlManager = ControlManager.getInstance(this);
        controlManager.setOnControlServerListener(new ControlServerListener() {
            @Override
            public void onServiceConnected() {
                mKillButton.setEnabled(true);
                mCallbackText.setText("Attached.");
                Toast.makeText(MainActivity.this, "远程服务绑定成功",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onServiceDisconnected() {
                mKillButton.setEnabled(false);
                mCallbackText.setText("Disconnected.");
                Toast.makeText(MainActivity.this, "远程服务断开连接",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void valueChanged(int value) {
                mCallbackText.setText("valueChanged:" + value);
            }
        });
    }

    /**
     * 绑定
     */
    private View.OnClickListener mBindListener = new View.OnClickListener() {
        public void onClick(View v) {
            mCallbackText.setText("Binding.");
            controlManager.binderServer();
        }
    };
    /**
     * 解除绑定
     */
    private View.OnClickListener mUnbindListener = new View.OnClickListener() {
        public void onClick(View v) {

            if (controlManager.isBound()) {
                controlManager.unBindServer();
                mKillButton.setEnabled(false);
                mCallbackText.setText("Unbinding.");
            }
        }
    };

    private View.OnClickListener mKillListener = new View.OnClickListener() {
        public void onClick(View v) {
            controlManager.killRemoteProcess();
        }
    };

    public void sendValue(View view) {
        int value = new Random().nextInt(100);
        controlManager.setControlCommond(value);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        controlManager.unBindServer();
    }
}

