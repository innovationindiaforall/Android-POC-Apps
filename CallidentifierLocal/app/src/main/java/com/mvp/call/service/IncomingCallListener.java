package com.mvp.call.service;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.Build;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mvp.call.R;
import com.mvp.call.service.IncomingCallListener;


public class IncomingCallListener extends BroadcastReceiver {

    private Context mContext;
    private String mainIncomingNo = "NONE", name = "NONE";
    private Intent intentMain;
    private WindowManager wm;
    private static LinearLayout ly1;
    private WindowManager.LayoutParams params1;

    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "IncomingCallListener............", Toast.LENGTH_LONG).show();
        mContext = context;
        intentMain = intent;
        invokeTelephoneManager(context, intent);
    }

    private void invokeTelephoneManager(Context context, Intent intent) {
        try {
            TelephonyManager tmgr = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            MyAndroidMobileStateListener PhoneListener = new MyAndroidMobileStateListener();
            tmgr.listen(PhoneListener, PhoneStateListener.LISTEN_CALL_STATE);

        } catch (Exception e) {
            Toast.makeText(context, "IncomingCallListener..TelephonyManager error", Toast.LENGTH_LONG).show();
        }
    }

    //------------------------------------------------------------------------------------------------
    private class MyAndroidMobileStateListener extends PhoneStateListener {

        public void onCallStateChanged(int state, String incomingNumber) {

            mainIncomingNo = incomingNumber;
            Log.v("Call", "...........mainIncomingNo:" + mainIncomingNo);
            SharedPreferences sharedpreferences;
            sharedpreferences = mContext.getSharedPreferences("MyPref", Activity.MODE_PRIVATE);
            String mobileNoPreference = sharedpreferences.getString(mainIncomingNo, null);
            Log.v("Call", "..........Before split shared pref mobileNoPreference:" + mobileNoPreference);
            if (mobileNoPreference != null && mobileNoPreference.contains("-")) {
                String[] parts = mobileNoPreference.split("-");
                mobileNoPreference = parts[0]; // 004
                name = parts[1]; // 034556
            }
            Log.v("Call", "...........after split shared pref mobileNoPreference:" + mobileNoPreference);
            Log.v("Call", "...........after split shared pref...............name:" + name);
            if (mobileNoPreference != null && mobileNoPreference.equalsIgnoreCase(mainIncomingNo)) {
                Log.v("Call", "If..mobileNoPreference.equalsIgnoreCase(mainIncomingNo)...............");
                showCustomPopupMenu1(mContext, intentMain);
            } else if (mainIncomingNo.isEmpty()) {
                Log.v("Call", "else if....mainIncomingNo.isEmpty()............................");
                showCustomPopupMenu1(mContext, intentMain);
            } else {
                Log.v("Call", "else.......NOT same no............................");
            }
        }
    }

    //------------------------------------------------------------------------------------------------
    private void showCustomPopupMenu1(Context context, Intent intent) {

        wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        ViewGroup viewgroup = (ViewGroup) View.inflate(mContext, R.layout.custom_card, null);
        TextView title = (TextView) viewgroup.findViewById(R.id.name);
        TextView phone = (TextView) viewgroup.findViewById(R.id.no);
        title.setText("" + name);
        phone.setText("" + mainIncomingNo);
        int LAYOUT_FLAG;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
        }
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                300,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.CENTER | Gravity.CENTER;
        params.x = 0;
        params.y = 0;
        wm.addView(viewgroup, params);

        // To remove the view once the dialer app is closed.
        if (intent.getAction().equals("android.intent.action.PHONE_STATE")) {
            String state1 = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            if (state1.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                if (viewgroup != null) {
                    wm.removeView(viewgroup);
                    viewgroup = null;
                    Log.v("Call", " wm.removeView(viewgroup);.................");
                }
                if (viewgroup != null)
                    viewgroup.removeAllViewsInLayout();
            }
        }

    }
    //------------------------------------------------------------------------------------------------
}
