package com.littlegreens.controllib;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import com.littlegreens.aidl.IRemoteService;
import com.littlegreens.aidl.IRemoteServiceCallback;
import com.littlegreens.controllib.listener.ControlServerListener;

/**
 * @author LittleGreens <a href="mailto:alittlegreens@foxmail.com">Contact me.</a>
 * @version 1.0
 * @since 2019/9/23 14:23
 */
public class ControlManager {

    private static final String TAG = "ControlManager";
    private static ControlManager mControlManager = null;
    private Context mContext;
    private boolean mIsBound;

    IRemoteService mService = null;
    private static final int BUMP_MSG = 1;

    private Handler mHandler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BUMP_MSG:
                    Log.d(TAG, "handleMessage: " + msg.arg1);
                    if (mControlServerListener != null) {
                        mControlServerListener.valueChanged(msg.arg1);
                    }
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    };

    public ControlServerListener mControlServerListener = null;

    public void setOnControlServerListener(ControlServerListener controlServerListener) {
        this.mControlServerListener = controlServerListener;
    }


    private ControlManager(Context context) {
        this.mContext = context;
    }

    public static ControlManager getInstance(Context context) {
        if (mControlManager == null) {
            synchronized (ControlManager.class) {
                if (mControlManager == null) {
                    mControlManager = new ControlManager(context);
                }
            }
        }
        return mControlManager;
    }


    public boolean binderServer() {

        if (LittleGreensUtils.isAvilible(mContext)) {
            openServerProcess();
            return true;
        } else {
            Log.e(TAG, "binderServer: error,not found remote Server");
            return false;
        }
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {

            Log.e(TAG, "onServiceConnected: ");
            mService = IRemoteService.Stub.asInterface(service);
            try {
                mService.registerCallback(mCallback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            if (mControlServerListener != null) {
                mControlServerListener.onServiceConnected();
            }
        }

        /**
         * 该方法只在Service 被破坏了或者被杀死的时候调用
         * @param className
         */
        public void onServiceDisconnected(ComponentName className) {
            Log.e(TAG, "onServiceDisconnected: ");
            mService = null;
            if (mControlServerListener != null) {
                mControlServerListener.onServiceDisconnected();
            }
        }
    };


    public boolean isBound() {
        return mIsBound;
    }

    public void unBindServer() {
        if (mIsBound) {
            if (mService != null) {
                try {
                    mService.unregisterCallback(mCallback);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            mContext.unbindService(mConnection);
            mIsBound = false;
        }
    }

    /**
     * 程序退出时，调用杀死远程服务进程
     */
    public void killRemoteProcess() {
        if (mService != null) {
            try {
                int pid = mService.getPid();
                Process.killProcess(pid);
            } catch (RemoteException ex) {
                Toast.makeText(mContext,
                        "远程服务调用失败",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 远程回调接口实现
     */
    private IRemoteServiceCallback mCallback = new IRemoteServiceCallback.Stub() {
        /**
         * This is called by the remote service regularly to tell us about
         * new values.  Note that IPC calls are dispatched through a thread
         * pool running in each process, so the code executing here will
         * NOT be running in our main thread like most other things -- so,
         * to update the UI, we need to use a Handler to hop over there.
         */
        public void valueChanged(int value) {
            mHandler.sendMessage(mHandler.obtainMessage(BUMP_MSG, value, 0));
        }
    };

    /**
     * 发送控制命令道远程服务
     *
     * @param value
     */
    public void setControlCommond(int value) {
        if (mService != null && mIsBound) {
            try {
                Log.d(TAG, "setControlCommond: " + value);
                mService.setTarget(value);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void openServerProcess() {
        try {
            Intent intent = new Intent();
            intent.setData(Uri.parse("csd://pull.csd.demo/cyn?type=110"));
            intent.putExtra("", "");//这里Intent当然也可传递参数,但是一般情况下都会放到上面的URL中进行传递
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            /**android.intent.action.MAIN：打开另一程序
             */
            intent.setAction("android.intent.action.MAIN");

            mContext.startActivity(intent);

            /**最后将被挤压到后台的本应用重新置顶到最前端
             * 当自己的应用在后台时，将它切换到前台来*/
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    LittleGreensUtils.setTopApp(mContext);
                    Intent intent = new Intent(IRemoteService.class.getName());
                    intent.setPackage(Const.remotePacket);
                    //注意这里的Context.BIND_AUTO_CREATE，这意味这如果在绑定的过程中，
                    //如果Service由于某种原因被Destroy了，Android还会自动重新启动被绑定的Service。
                    // 你可以点击Kill Process 杀死Service看看结果
                    mContext.bindService(intent,
                            mConnection, Context.BIND_AUTO_CREATE);
                    mIsBound = true;
                }
            },50);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(mContext, "被启动的 APP 未安装", Toast.LENGTH_SHORT).show();
        }
    }

}
