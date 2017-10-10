package wapchief.com.collectiondemo.framework;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.speech.SpeechRecognizer;

import com.github.yuweiguocn.library.greendao.MigrationHelper;
import com.iflytek.cloud.Setting;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

import org.greenrobot.greendao.query.QueryBuilder;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.im.android.api.JMessageClient;
import wapchief.com.collectiondemo.greendao.DaoMaster;
import wapchief.com.collectiondemo.greendao.DaoSession;
import wapchief.com.collectiondemo.greendao.MessageDao;
import wapchief.com.collectiondemo.greendao.SQLiteOpenHelper;
import wapchief.com.collectiondemo.greendao.UserDao;

/**
 * Created by Wu on 2017/4/7 0007 下午 2:45.
 * 描述：自定义Application
 */
public class BaseApplication extends Application {
    public static BaseApplication mBaseApplication;
    private Context mContext;
    private SQLiteOpenHelper helper;
    private DaoMaster master;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = BaseApplication.this;
        mBaseApplication = this;
        //开启极光调试
        JPushInterface.setDebugMode(true);
        //实例化极光推送
        JPushInterface.init(mContext);
        //实例化极光IM,并自动同步聊天记录
        JMessageClient.init(mContext, true);
        //数据库调试。建议打包上线时关闭
        QueryBuilder.LOG_SQL = true;
        QueryBuilder.LOG_VALUES = true;
        //初始化讯飞语音
        SpeechUtility.createUtility(mContext, SpeechConstant.APPID +"=59daecea," + SpeechConstant.FORCE_LOGIN +"=true");
        //讯飞调试日志开启
//        Setting.setShowLog(true);
        //数据库升级
        updateDB();
    }

    private void updateDB() {
        //是否开启调试
        MigrationHelper.DEBUG = true;
        helper = new SQLiteOpenHelper(this
                , "recluse-db");
        master = new DaoMaster(helper.getWritableDb());
    }
}

