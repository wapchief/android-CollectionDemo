package wapchief.com.collectiondemo.framework;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import org.greenrobot.greendao.AbstractDao;

import wapchief.com.collectiondemo.greendao.DaoMaster;
import wapchief.com.collectiondemo.greendao.DaoSession;
import wapchief.com.collectiondemo.greendao.model.Message;

/**
 * Created by apple on 2017/7/4.
 * 封装Greendao数据库
 */

public class GreenDaoHelper{

    Context context;
    DaoMaster.DevOpenHelper helper;
    SQLiteDatabase db;
    DaoMaster daoMaster;
    DaoSession daoSession;


    public GreenDaoHelper(Context context) {
        this.context = context;
    }

    public DaoSession initDao(){
        helper = new DaoMaster.DevOpenHelper(context, "recluse-db", null);
        db= helper.getWritableDatabase();
        daoMaster= new DaoMaster(db);
        daoSession= daoMaster.newSession();
        return daoSession;
    }
}
