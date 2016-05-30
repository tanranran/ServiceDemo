// IRemoteService.aidl
package com.tan.service.demo;
// Declare any non-default types here with import statements

import com.tan.service.demo.AidlCallback;

interface IRemoteService {
    int getPid();
    void registerCallback(AidlCallback cb);
    void unregisterCallback(AidlCallback cb);
}
