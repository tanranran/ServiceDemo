package com.tan.service.demo.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.tan.service.demo.service.GrayService;

/**
 * User: Tanranran(tanjuran@gmail.com)
 * Date: 2016-05-30
 * Time: 10:36
 */
public class WakeReceiver extends BroadcastReceiver {

    private final static String TAG = WakeReceiver.class.getSimpleName();
    private final static int WAKE_SERVICE_ID = -1111;

    /**
     * 灰色保活手段唤醒广播的action
     */
    public final static String GRAY_WAKE_ACTION = "com.wake.gray";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (GRAY_WAKE_ACTION.equals(action)) {
            Intent wakeIntent = new Intent(context, GrayService.class);
            context.startService(wakeIntent);
        }
        Log.d("服务测试","action: "+action);
    }
}
