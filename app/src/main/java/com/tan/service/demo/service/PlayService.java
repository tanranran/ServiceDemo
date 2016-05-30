package com.tan.service.demo.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * User: Tanranran(tanjuran@gmail.com)
 * Date: 2016-05-30
 * Time: 09:49
 */
public class PlayService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}