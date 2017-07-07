package wapchief.com.collectiondemo.activity;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import wapchief.com.collectiondemo.MainActivity;
import wapchief.com.collectiondemo.RecyclerViewActivity;

/**
 * Created by J!nl!n on 2017/3/9.
 */
public class RootActivity extends ListActivity {

    private String[] mTitles = new String[]{
            "MainActivity+SideBar",
            "RecyclerView+swipeRefrashlayout+Header",
            "TagFlowLayout热门搜索",
            "okhttp+Retrofit+RxJava查询手机号码归属地",
            "Glide+Picasso请求加载网络图片",
            "CardView上传图片",
            "JPush（极光）即时通讯",
            "GreenDao+Flow仿热门搜索，本地缓存",
            "SlidingUpPanel仿美团配送",
    };

    private Class[] mActivities = new Class[]{
            MainActivity.class,
            RecyclerViewActivity.class,
            FlowLayoutActivity.class,
            OkhttpRetrofitActivity.class,
            GlidePicassoActivity.class,
            UpdatePhotoActivity.class,
            JPushIMActivity.class,
            SearchViewGreenDaoActivity.class,
            SlidingUpMeiTuanActivity.class,

    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setListAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mTitles));
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        startActivity(new Intent(this, mActivities[position]));
    }

}
