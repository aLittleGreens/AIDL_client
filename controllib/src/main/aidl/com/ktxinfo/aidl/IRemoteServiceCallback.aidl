// IRemoteServiceCallback.aidl
package com.ktxinfo.aidl;

import com.ktxinfo.aidl.DeviceBean;
// Declare any non-default types here with import statements


oneway interface IRemoteServiceCallback {
   /**
        * Called when the service has a new value for you.
        */
       void valueChanged(in DeviceBean deviceBean);
}
