package wapchief.com.collectiondemo.activity;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import wapchief.com.collectiondemo.R;
import wapchief.com.collectiondemo.adapter.ItemTVAdapter;
import wapchief.com.collectiondemo.framework.BaseApplication;
import wapchief.com.collectiondemo.greendao.DaoMaster;
import wapchief.com.collectiondemo.greendao.DaoSession;
import wapchief.com.collectiondemo.greendao.MessageDao;
import wapchief.com.collectiondemo.greendao.model.Message;

/**
 * Created by Wu on 2017/5/10 0010 下午 1:30.
 * 描述：消息列表
 */
public class MessageActivity extends AppCompatActivity {

    @BindView(R.id.message_lv)
    ListView messageLv;
    @BindView(R.id.message_back)
    ImageView mMessageBack;
    private ItemTVAdapter adapter;
    private List<Message> list;
    MessageDao messageDao;
    Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        ButterKnife.bind(this);
        context = MessageActivity.this;
        initDbHelp();
        initview();

    }

    private void initview() {
        //查询所有
        list = messageDao.queryBuilder().list();
        //list倒序排列
        Collections.reverse(list);
        adapter = new ItemTVAdapter(context, list);
        messageLv.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    /*初始化数据库相关*/
    private void initDbHelp() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(BaseApplication.mBaseApplication, "recluse-db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        messageDao = daoSession.getMessageDao();
    }

    @OnClick(R.id.message_back)
    public void onViewClicked() {
        startActivity(new Intent(MessageActivity.this,RootActivity.class));
    }
}
