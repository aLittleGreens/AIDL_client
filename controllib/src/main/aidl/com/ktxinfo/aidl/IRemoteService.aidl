// IRemoteService.aidl
package com.ktxinfo.aidl;
import com.ktxinfo.aidl.IRemoteServiceCallback;

// Declare any non-default types here with import statements

interface IRemoteService {

    /**
     * Often you want to allow a service to call back to its clients.
     * This shows how to do so, by registering a callback interface with
     * the service.
     */
    void registerCallback(IRemoteServiceCallback cb);

    /**
     * Remove a previously registered callback interface.
     */
    void unregisterCallback(IRemoteServiceCallback cb);

    //计算
     void setTarget(int value);

     //获取Service运行的进程ID
     int getPid();

}
