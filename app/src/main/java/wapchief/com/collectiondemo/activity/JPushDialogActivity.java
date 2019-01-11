package wapchief.com.collectiondemo.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import wapchief.com.collectiondemo.R;
import wapchief.com.collectiondemo.bean.EmojiBean;
import wapchief.com.collectiondemo.framework.BaseApplication;
import wapchief.com.collectiondemo.greendao.DaoMaster;
import wapchief.com.collectiondemo.greendao.DaoSession;
import wapchief.com.collectiondemo.greendao.MessageDao;
import wapchief.com.collectiondemo.greendao.model.Message;

/**
 * Created by apple on 2017/6/14.
 * 推送的弹窗
 */

public class JPushDialogActivity extends Activity {

    //收到的消息
    @BindView(R.id.dialog_message)
    TextView dialogMessage;
    //取消
    @BindView(R.id.dialog_cancel)
    TextView dialogCancel;
    //保存
    @BindView(R.id.dialog_yes)
    TextView dialogYes;
    //数据库
    MessageDao messageDao;
    //接收数据
    String message;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_jpush_message);
        ButterKnife.bind(this);
        initDbHelp();
        initView();
    }

    String strEmoji = "😆";
    Pattern emoji = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
            Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
    private void initView() {

        String strEmj = new Gson().toJson(new EmojiBean(strEmoji));
        LogUtils.e("反序列化："+strEmj);
        if (getIntent().hasExtra("MESSAGE")) {
            message = getIntent().getStringExtra("MESSAGE");
        }
//        LogUtils.e("原始："+message);
//        Type type = new TypeToken<String>(){}.getType();
//        EmojiBean s = new Gson().fromJson(message, EmojiBean.class);
//        LogUtils.e("解析："+s.toString());
//        Matcher matcher = emoji.matcher(message);
        //方便格式化表情
//        SpannableString spannableString = new SpannableString(message);
        dialogMessage.setText(message);
//        while(matcher.find()) {
//            Log.e("接收到表情", "");
//            // 输入表情
//
//        }

    }

    /*初始化数据库相关*/
    private void initDbHelp() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(BaseApplication.mBaseApplication, "recluse-db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        messageDao = daoSession.getMessageDao();
    }

    @OnClick({R.id.dialog_message, R.id.dialog_cancel, R.id.dialog_yes})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.dialog_message:
                break;
            case R.id.dialog_cancel:

                finish();
                break;
            case R.id.dialog_yes:
                Intent intent = new Intent(this, MessageActivity.class);
                SimpleDateFormat formatter   =   new   SimpleDateFormat   ("yyyy年MM月dd日   HH:mm:ss");
                Date curDate =  new Date(System.currentTimeMillis());
                //获取当前时间
                String   str   =   formatter.format(curDate);
                messageDao.insert(new Message(null, "手动：" + str, message));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                JPushDialogActivity.this.finish();
                break;
        }
    }

}
