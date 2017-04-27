package wapchief.com.collectiondemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import okhttp3.OkHttpClient;
import wapchief.com.collectiondemo.activity.FlowLayoutActivity;
import wapchief.com.collectiondemo.activity.GlidePicassoActivity;
import wapchief.com.collectiondemo.activity.JPushIMActivity;
import wapchief.com.collectiondemo.activity.OkhttpRetrofitActivity;
import wapchief.com.collectiondemo.activity.SearchViewGreenDaoActivity;
import wapchief.com.collectiondemo.activity.UpdatePhotoActivity;
import wapchief.com.collectiondemo.adapter.RecyclerViewAdapter;
import wapchief.com.collectiondemo.utils.UToasts;


public class MainActivity extends AppCompatActivity {


    @BindView(R.id.bt)
    Button bt;
    @BindView(R.id.bt2)
    Button bt2;


    RecyclerViewAdapter adapter;
    @BindView(R.id.bt_tfl)
    Button btTfl;
    @BindView(R.id.bt_retrofit)
    Button btRetrofit;
    @BindView(R.id.bt_glide)
    Button btGlide;
    @BindView(R.id.bt_cardview)
    Button btCardview;
    @BindView(R.id.jPush_im)
    Button jPushIm;
    @BindView(R.id.side_main)
    DrawerLayout sideMain;
    @BindView(R.id.greendao)
    Button greendao;
    private List<String> data;
    @BindView(R.id.side_bar_img)
    ImageView side_bar_img;
    OkHttpClient okHttpClient = new OkHttpClient();
    @BindView(R.id.nav_view)
    NavigationView nav_view;


    private String AppKey = "6e1ce94fe231e817fb31daec3b3084d0";
    private String URL = "http://apis.juhe.cn/";
    public String mApi = null;
    private int currentPage = 0;
    private int totalPages = 0;
    /**
     * 记录刷新状态
     */
    private final int STATE_NORMAL = 0;
    private final int STATE_REFRESH = 1;
    private final int STATE_LOADMORE = 2;
    private int curState = STATE_NORMAL;
    public static boolean isForeground = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.side_bar_main);
        ButterKnife.bind(this);
        //线性布局
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        //加载侧滑菜单
        setNavClick();
        //注册激光
        JPushInterface.init(getApplicationContext());

        Log.e("id-------", JPushInterface.getRegistrationID(this));

    }

    //     单个监听bt
    @OnClick(R.id.bt)
    void submit() {
        UToasts.showShort(this, "监听成功");
    }

    //     多个监听
    @OnClick({
            R.id.bt2,
            R.id.bt_tfl,
            R.id.bt_retrofit,
            R.id.bt_glide,
            R.id.bt_cardview,
            R.id.side_bar_img,
            R.id.jPush_im,
            R.id.greendao})
    void submix(View view) {
        switch (view.getId()) {
            case R.id.bt2:
                UToasts.showShort(this, "tv被点击了");
                Intent intent = new Intent(MainActivity.this, RecyclerViewActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_tfl:
                Intent intent1 = new Intent(MainActivity.this, FlowLayoutActivity.class);
                startActivity(intent1);
                break;
            case R.id.bt_retrofit:
                Intent intent2 = new Intent(MainActivity.this, OkhttpRetrofitActivity.class);
                startActivity(intent2);
                break;
            case R.id.bt_glide:
                Intent intent3 = new Intent(MainActivity.this, GlidePicassoActivity.class);
                startActivity(intent3);
                break;
            case R.id.bt_cardview:
                Intent intent4 = new Intent(MainActivity.this, UpdatePhotoActivity.class);
                startActivity(intent4);
                break;
            case R.id.side_bar_img:
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.side_main);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
                break;
            case R.id.jPush_im:
                Intent intent5 = new Intent(MainActivity.this, JPushIMActivity.class);
                startActivity(intent5);
                break;
            case R.id.greendao:
                Intent intent6 = new Intent(MainActivity.this, SearchViewGreenDaoActivity.class);
                startActivity(intent6);
                break;
        }

    }


    @Override
    protected void onResume() {
        isForeground = true;
        super.onResume();
    }


    @Override
    protected void onPause() {
        isForeground = false;
        super.onPause();
    }


    @Override
    protected void onDestroy() {
//        unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }

    /**
     * 单击回退
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitBy2Click();
        }
        return false;
    }

    /**
     * 双击退出
     */
    private static Boolean isExit = false;

    private void exitBy2Click() {
        Timer tExit = null;
        if (isExit == false) {
            isExit = true; // 准备退出
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; // 取消退出
                }
            }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

        } else {
            finish();
            System.exit(0);
        }
    }

    /**
     * NavigationView侧滑菜单
     */
    public void setNavClick() {
        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.side_bar1) {

                } else if (id == R.id.side_bar2) {

                } else if (id == R.id.side_bar3) {

                } else if (id == R.id.side_bar4) {

                } else if (id == R.id.side_bar5) {

                } else if (id == R.id.side_bar6) {

                } else if (id == R.id.side_bar7) {

                }

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.side_main);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

}
