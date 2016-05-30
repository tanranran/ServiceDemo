package com.tan.service.demo;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent("com.tan.gray.service");
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private IRemoteService remoteService;
    private ServiceConnection serviceConnection = new ServiceConnection() {

        public void onServiceConnected(ComponentName name, IBinder service) {
            remoteService = IRemoteService.Stub.asInterface(service);
            try {
                int pid = remoteService.getPid();
                Log.d("服务测试","服务获取的AIDL"+pid);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            System.out.println("bind success! " + remoteService.toString());
        }
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
    }
}
