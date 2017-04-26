package wapchief.com.collectiondemo.framework;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.im.android.api.JMessageClient;
import wapchief.com.collectiondemo.greendao.DaoMaster;
import wapchief.com.collectiondemo.greendao.DaoSession;
import wapchief.com.collectiondemo.greendao.UserDao;

/**
 * Created by Wu on 2017/4/7 0007 下午 2:45.
 * 描述：自定义Application
 */
public class BaseApplication extends Application{
    private Context mContext;
    private UserDao mUserDao;
    @Override
    public void onCreate() {
        super.onCreate();
        //开启极光调试
        JPushInterface.setDebugMode(true);

        mContext=BaseApplication.this;
        //实例化极光推送
        JPushInterface.init(mContext);
        //实例化极光IM,并自动同步聊天记录
        JMessageClient.init(mContext,true);
        //实例化数据库
        initDbHelp();
    }


    /*初始化数据库相关*/
    private void initDbHelp() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "recluse-db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        mUserDao = daoSession.getUserDao();
    }
}

