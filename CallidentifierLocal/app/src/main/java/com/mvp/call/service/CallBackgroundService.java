package com.mvp.call.service;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import com.mvp.call.service.IncomingCallListener;

public class CallBackgroundService extends Service {

    private IncomingCallListener incomingCallListener = null;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter intentFilterObject = new IntentFilter();
        intentFilterObject.addAction(android.telephony.TelephonyManager.ACTION_PHONE_STATE_CHANGED);
        incomingCallListener = new IncomingCallListener();
        // Register the broadcast receiver with the intent filter object.
        registerReceiver(incomingCallListener, intentFilterObject);
        Log.v("Call", "MyBgService onCreate.....................: incomingCallListener is registered.");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Unregister incomingCallListener when destroy.
        if (incomingCallListener != null) {
            unregisterReceiver(incomingCallListener);
            Log.v("Call", "MyBgService onDestroy.................: incomingCallListener is unregistered.");
        }
    }

}