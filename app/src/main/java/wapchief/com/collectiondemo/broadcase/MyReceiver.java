package wapchief.com.collectiondemo.broadcase;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import java.util.logging.Logger;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Wu on 2017/4/7 0007 下午 2:40.
 * 描述：极光推送广播器
 */
public class MyReceiver extends BroadcastReceiver{
    private static final String TAG = "MyReceiver";

    private NotificationManager nm;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (null == nm) {
            nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }

        Bundle bundle = intent.getExtras();
        Log.e(TAG, "onReceive - " + intent.getAction() );

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            Log.e(TAG, "JPush用户注册成功,registration为："+bundle.getString(JPushInterface.ACTION_REGISTRATION_ID));

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.e(TAG, "接受到推送下来的自定义消息");

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.e(TAG, "接受到推送下来的通知");

//            receivingNotification(context,bundle);

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.e(TAG, "用户点击打开了通知");

//            openNotification(context,bundle);

        } else {
            Log.e(TAG, "Unhandled intent - " + intent.getAction());
        }
    }
}
