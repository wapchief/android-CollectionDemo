package wapchief.com.collectiondemo.greendao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.github.yuweiguocn.library.greendao.MigrationHelper;

import org.greenrobot.greendao.database.Database;

/**
 * Created by WB on 2017/7/7.
 * 用于数据的升级
 */

public class SQLiteOpenHelper extends DaoMaster.OpenHelper {
    public SQLiteOpenHelper(Context context, String name) {
        super(context, name);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);
        //设置需要升级的表
        MigrationHelper.migrate(db,MessageDao.class,UserDao.class,CarShopDao.class);
    }
}
