package wapchief.com.collectiondemo.activity;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.zhy.view.flowlayout.TagFlowLayout;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import wapchief.com.collectiondemo.R;
import wapchief.com.collectiondemo.bean.User;
import wapchief.com.collectiondemo.greendao.DaoMaster;
import wapchief.com.collectiondemo.greendao.DaoSession;
import wapchief.com.collectiondemo.greendao.UserDao;

/**
 * Created by Wu on 2017/4/26 0026 下午 5:41.
 * 描述：
 */
public class SearchViewGreenDaoActivity extends AppCompatActivity {

    User user;
    UserDao userDao;
    DaoSession mDaoSession;
    @BindView(R.id.searchview)
    SearchView searchview;
    @BindView(R.id.search_greendao_flowlayout)
    TagFlowLayout searchGreendaoFlowlayout;
    @BindView(R.id.search_greendao_lv)
    ListView searchGreendaoLv;
    @BindView(R.id.search_greendao_rl)
    RelativeLayout searchGreendaoRl;
    @BindView(R.id.search_greendao_delete)
    Button searchGreendaoDelete;
    int id = 0;
    String name = "黄海佳";
    String[] names={"aaaaa","bbbbbbb","ccccccc","ddddddd","eeeee","fffff"};
    @BindView(R.id.search_ok)
    Button searchOk;
    ArrayList<User> list;
    QueryBuilder qb;
    Context mContext;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_greendao);
        ButterKnife.bind(this);
        mContext = SearchViewGreenDaoActivity.this;
        //初始化数据库
        initDbHelp();
        qb = userDao.queryBuilder();

        list = (ArrayList<User>) qb.where(UserDao.Properties.Id.eq(id)).list();
        List<User> staffs=userDao.queryBuilder().list();
//        Log.e("all---------", staffs. + "");
        try {
//            names = (String[]) staffs.toArray();

            searchGreendaoLv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, names));
        }catch (Exception e){
            Log.e("exception-------", e.getMessage());
        }
        searchGreendaoLv.setTextFilterEnabled(true);
        initDate();
    }

    private void initDate() {
        //搜索文本监听
        searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            //点击搜索
            public boolean onQueryTextSubmit(String query) {
                name = query;
                Log.e("name--------", name + "");
                User entity = new User();
                entity.setId(null);
                entity.setName(name);
//                if (list.size() > 0) {
//                    Toast.makeText(mContext, "主键重复", Toast.LENGTH_SHORT).show();
//                } else {

                    userDao.insert(entity);
                    Toast.makeText(mContext, "插入数据成功:"+query, Toast.LENGTH_SHORT).show();



//                }
                return false;
            }

            @Override
            //当搜索内容改变
            public boolean onQueryTextChange(String newText) {
                name = newText;
                if (newText.equals("")) {
                    searchGreendaoLv.setFilterText(newText);
                } else {
//                    insertDB();
                    searchGreendaoLv.clearTextFilter();
                }
                return false;
            }
        });
    }

    /*初始化数据库相关*/
    private void initDbHelp() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "recluse-db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        userDao = daoSession.getUserDao();
    }

    //增
    private void insertDB() {


    }
    //删
    private void delectDB(){
        try {
            userDao.delete(user);

        }catch (Exception e){
            Log.e("exception-----delete", user+"message:"+e.getMessage()+"");
        }
    }
    //查
    private void searchDB(){
        if (list.size() > 0) {
            for (User user : list) {
                Log.e("user-------getname", "\r\n" + user.getName());
                Log.e("user-------getid", "\r\n" + user.getId());

            }
        } else {
            Toast.makeText(this, "找不到相关的数据", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick({R.id.searchview,
            R.id.search_greendao_flowlayout,
            R.id.search_greendao_rl,
            R.id.search_greendao_delete,
            R.id.search_ok})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.searchview:
                break;
            case R.id.search_greendao_flowlayout:
                break;
            case R.id.search_greendao_rl:
                break;
            case R.id.search_greendao_delete:
                delectDB();
                break;
            case R.id.search_ok:
                searchDB();
                break;

        }
    }

    /**
     * 根据输入的词查找数据，放到动态数组中，用于弹窗联想词汇
     *
     * @param hotSearchList 动态数组
     * @param word          输入单词
     * @return 搜索的结果集合
     */
//    private List<User> insertMatchKeywordsToRecycleView(ArrayList<String> hotSearchList, String word) {
//        // 先查找历史，按时间排序
//        List<User> tables = mKeywordsTableDao.queryBuilder()
//                .where(UserDao.Properties.Keyword.like("%" + word + "%"),
//                        UserDao.Properties.Type.eq(Constant.SEARCH_KEYWORDS_HISTORY))
//                .orderDesc(UserDao.Properties.Time)
//                .list();
//        for (User table : tables) {
//            hotSearchList.add(table.getKeyword());
//            DebugLog.e("查找历史有匹配");
//        }
//        // 然后查找词库，按次数排序
//        List<User> tables1 = mKeywordsTableDao.queryBuilder()
//                .where(SearchKeywordsTableDao.Properties.Keyword.like("%" + word + "%"),
//                        // 热词不包括在联想里面，因为联想里面会包含热词
//                        SearchKeywordsTableDao.Properties.Type.eq(Constant.SEARCH_KEYWORDS_ASSOCIATIVE))
//                .orderDesc(SearchKeywordsTableDao.Properties.Count)
//                .list();
//        for (SearchKeywordsTable table : tables1) {
//            // 没跟历史词重复才添加上去
//            if (!hotSearchList.contains(table.getKeyword())) {
//                DebugLog.e("查找词库有匹配，且不跟历史重复");
//                hotSearchList.add(table.getKeyword());
//            }
//        }
//        return tables;
//    }
}
