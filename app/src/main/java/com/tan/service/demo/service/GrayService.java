package com.tan.service.demo.service;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.tan.service.demo.AidlCallback;
import com.tan.service.demo.IRemoteService;
import com.tan.service.demo.receiver.WakeReceiver;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 灰色保活手法创建的Service进程
 *
 * @author Clock
 * @since 2016-04-12
 */
public class  GrayService extends Service {

    private final static String TAG = GrayService.class.getSimpleName();
    /**
     * 定时唤醒的时间间隔，5分钟
     */
    private final static int ALARM_INTERVAL = 1 * 60 * 1000;
    private final static int WAKE_REQUEST_CODE = 6666;

    private final static int GRAY_SERVICE_ID = -1001;

    public void onCreate() {
        Log.i(TAG, "GrayService->onCreate");

        Integer cacheTime = 1000 * 3;
        Timer timer = new Timer();
        // (TimerTask task, long delay, long period)任务，延迟时间，多久执行
        timer.schedule(new TimerTask() {
            public void run() {
                Log.i(TAG, "GrayService->onCreate"+new Date());
            }
        }, 1000, cacheTime);
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "GrayService->onStartCommand");
        if (Build.VERSION.SDK_INT < 18) {
            startForeground(GRAY_SERVICE_ID, new Notification());//API < 18 ，此方法能有效隐藏Notification上的图标
        } else {
            Intent innerIntent = new Intent(this, GrayInnerService.class);
            startService(innerIntent);
            startForeground(GRAY_SERVICE_ID, new Notification());
        }

        //发送唤醒广播来促使挂掉的UI进程重新启动起来
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent();
        alarmIntent.setAction(WakeReceiver.GRAY_WAKE_ACTION);
        PendingIntent operation = PendingIntent.getBroadcast(this, WAKE_REQUEST_CODE, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), ALARM_INTERVAL, operation);

        return START_STICKY;
    }

    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public void onDestroy() {
        Log.i(TAG, "GrayService->onDestroy");
        super.onDestroy();

    }
    private final IRemoteService.Stub mBinder = new IRemoteService.Stub() {
        public int getPid(){
            return 999999999;
        }

        @Override
        public void registerCallback(AidlCallback cb) throws RemoteException {

        }

        @Override
        public void unregisterCallback(AidlCallback cb) throws RemoteException {

        }

        GrayService getGrayService() {
            // 返回Activity所关联的Service对象，这样在Activity里，就可调用Service里的一些公用方法和公用属性
            return GrayService.this;
        }
    };

    /**
     * 给 API >= 18 的平台上用的灰色保活手段
     */
    public static class GrayInnerService extends Service {

        @Override
        public void onCreate() {
            Log.i(TAG, "InnerService -> onCreate");
            super.onCreate();
        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            Log.i(TAG, "InnerService -> onStartCommand");
            startForeground(GRAY_SERVICE_ID, new Notification());
            //stopForeground(true);
            stopSelf();
            return super.onStartCommand(intent, flags, startId);
        }

        @Override
        public IBinder onBind(Intent intent) {
            // TODO: Return the communication channel to the service.
            throw new UnsupportedOperationException("Not yet implemented");
        }

        @Override
        public void onDestroy() {
            Log.i(TAG, "InnerService -> onDestroy");
            stopForeground(true);
            super.onDestroy();
        }
    }
}