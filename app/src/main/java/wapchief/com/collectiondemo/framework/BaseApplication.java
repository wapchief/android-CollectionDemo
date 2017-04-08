package wapchief.com.collectiondemo.framework;

import android.app.Application;
import android.content.Context;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Wu on 2017/4/7 0007 下午 2:45.
 * 描述：自定义Application
 */
public class BaseApplication extends Application{
    private Context mContext;
    @Override
    public void onCreate() {
        super.onCreate();
        //开启极光调试
        JPushInterface.setDebugMode(true);

        mContext=BaseApplication.this;
        //实例化极光推送
        JPushInterface.init(mContext);
    }
}

