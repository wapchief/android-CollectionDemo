package wapchief.com.collectiondemo.activity;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.greenrobot.greendao.query.QueryBuilder;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import wapchief.com.collectiondemo.R;
import wapchief.com.collectiondemo.adapter.SearchViewGreenDaoAdapter;
import wapchief.com.collectiondemo.framework.BaseApplication;
import wapchief.com.collectiondemo.framework.GreenDaoHelper;
import wapchief.com.collectiondemo.framework.system.SystemStatusManager;
import wapchief.com.collectiondemo.framework.system.X_SystemBarUI;
import wapchief.com.collectiondemo.greendao.model.User;
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
    String name = "";
    String[] names = {"android", "HTML", "java", "PHP", "C", "C++", "NodeJs", "Hexo", "Github"};
    @BindView(R.id.search_ok)
    Button searchOk;
    List<User> list;
    QueryBuilder qb;
    Context mContext;
    SearchViewGreenDaoAdapter adapter;
    View viewflowlayout;
    Context context;
    GreenDaoHelper helper;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_greendao);
        ButterKnife.bind(this);
        X_SystemBarUI.initSystemBar(this,R.color.colorPrimary);
        mContext = SearchViewGreenDaoActivity.this;
        delectUnderline();
        //初始化数据库
        initDbHelp();

        initDate();
        searchGreendaoLv.setTextFilterEnabled(true);
    }

//    /*初始化数据库相关*/
    private void initDbHelp() {
        helper = new GreenDaoHelper(this);
        userDao = helper.initDao().getUserDao();
    }

    private void initDate() {
        //搜索历史列表
        updateList();
        //热门搜索
        searchGreendaoFlowlayout.setAdapter(new TagAdapter<String>(names) {
            @Override
            public View getView(FlowLayout parent, int position, final String s) {
                final TextView tv = (TextView) LayoutInflater.from(mContext).inflate(
                        R.layout.search_page_flowlayout_tv, searchGreendaoFlowlayout, false);
                tv.setText(s);
                tv.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(mContext, "" + s, Toast.LENGTH_SHORT).show();

                    }
                });
                return tv;
            }
        });

        //搜索文本监听
        searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            //点击搜索
            public boolean onQueryTextSubmit(String query) {
                name = query;
                Log.e("name--------", name + "");
                insertDB();
                return false;
            }

            @Override
            //当搜索内容改变
            public boolean onQueryTextChange(String newText) {
                name = newText;
                Log.e("newText---------", newText);
                if (name.equals("")) {
//                    searchGreendaoLv.setFilterText(name);
                } else {
//                    insertDB();
//                    searchGreendaoLv.clearTextFilter();
                }
                return false;
            }
        });

    }


    /**
     * 初始化adapter，更新list，重新加载列表
     */
    private void updateList() {
        //查询所有
        list = userDao.queryBuilder().list();
        //这里用于判断是否有数据
        if (list.size()==0){
            searchGreendaoRl.setVisibility(View.VISIBLE);
            searchGreendaoDelete.setVisibility(View.GONE);
        }else {
            searchGreendaoRl.setVisibility(View.GONE);
            searchGreendaoDelete.setVisibility(View.VISIBLE);
        }
        //list倒序排列
        Collections.reverse(list);
        adapter = new SearchViewGreenDaoAdapter(mContext, list);
        searchGreendaoLv.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    //增
    private void insertDB() {
        try {
            if (list.size() < 8) {
                //删除已经存在重复的搜索历史
                List<User> list2 = userDao.queryBuilder()
                        .where(UserDao.Properties.Name.eq(name)).build().list();
                userDao.deleteInTx(list2);
                //添加
                if (!name.equals(""))
                userDao.insert(new User(null, name));
                Toast.makeText(mContext, "插入数据成功:" + name, Toast.LENGTH_SHORT).show();
            } else {
                //删除第一条数据，用于替换最后一条搜索
                userDao.delete(userDao.queryBuilder().list().get(0));
                //删除已经存在重复的搜索历史
                List<User> list3 = userDao.queryBuilder()
                        .where(UserDao.Properties.Name.eq(name)).build().list();
                userDao.deleteInTx(list3);
                //添加
                if (!name.equals(""))
                userDao.insert(new User(null, name));
            }
            updateList();
        } catch (Exception e) {
            Toast.makeText(mContext, "插入失败", Toast.LENGTH_SHORT).show();
        }

    }

    //清空数据库
    private void delectAllDB() {
        try {
            userDao.deleteAll();
            list.clear();
            adapter.notifyDataSetChanged();
            searchGreendaoRl.setVisibility(View.VISIBLE);
            searchGreendaoDelete.setVisibility(View.GONE);
            Toast.makeText(mContext, "清空数据库", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("exception-----delete", user + "message:" + e.getMessage() + "");
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
                delectAllDB();
                break;
            case R.id.search_ok:
                insertDB();
                break;

        }
    }

    /**
     * 去掉searchview下划线
     */
    public void delectUnderline() {
        if (searchGreendaoFlowlayout != null) {
            try {        //--拿到字节码
                Class<?> argClass = searchGreendaoFlowlayout.getClass();
                //--指定某个私有属性,mSearchPlate是搜索框父布局的名字
                Field ownField = argClass.getDeclaredField("mSearchPlate");
                //--暴力反射,只有暴力反射才能拿到私有属性
                ownField.setAccessible(true);
                View mView = (View) ownField.get(searchGreendaoFlowlayout);
                //--设置背景
                mView.setBackgroundColor(Color.TRANSPARENT);
            } catch (Exception e) {
                e.printStackTrace();
            }
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
