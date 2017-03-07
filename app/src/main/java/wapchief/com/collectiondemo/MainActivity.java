package wapchief.com.collectiondemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;
import wapchief.com.collectiondemo.activity.FlowLayoutActivity;
import wapchief.com.collectiondemo.activity.GlidePicassoActivity;
import wapchief.com.collectiondemo.activity.OkhttpRetrofitActivity;
import wapchief.com.collectiondemo.activity.UpdatePhotoActivity;
import wapchief.com.collectiondemo.adapter.RecyclerViewAdapter;
import wapchief.com.collectiondemo.customView.DividItemDecoration;
import wapchief.com.collectiondemo.utils.UToasts;


public class MainActivity extends AppCompatActivity {


    @BindView(R.id.et)
    EditText et;
    //    @BindView(R.id.refresh)
//    MaterialRefreshLayout refresh;
    @BindView(R.id.bt)
    Button bt;
    @BindView(R.id.bt2)
    Button bt2;

    @BindView(R.id.recyclerView1)
    RecyclerView recyclerView;

    RecyclerViewAdapter adapter;
    @BindView(R.id.bt_tfl)
    Button btTfl;
    @BindView(R.id.bt_retrofit)
    Button btRetrofit;
    @BindView(R.id.bt_glide)
    Button btGlide;
    @BindView(R.id.bt_cardview)
    Button btCardview;
    private List<String> data;

    OkHttpClient okHttpClient = new OkHttpClient();

    //     单个监听bt
    @OnClick(R.id.bt)
    void submit() {
        UToasts.showShort(this, "监听成功");
    }

    //     多个监听
    @OnClick({R.id.et, R.id.bt2, R.id.bt_tfl, R.id.bt_retrofit, R.id.bt_glide,R.id.bt_cardview})
    void submix(View view) {
        switch (view.getId()) {
            case R.id.et:
                UToasts.showShort(this, "输入框被点击了");
                break;
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
        }
    }

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


//    mApi=retrofit

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
//        data();
//        adapter=new RecyclerViewAdapter(this,data);
        //线性布局
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        //线性布局的分割线
        recyclerView.addItemDecoration(new DividItemDecoration(this, DividItemDecoration.VERTICAL_LIST));
//        recyclerView.setLayoutManager(manager);
        //网格布局
//        recyclerView.setLayoutManager(new GridLayoutManager(this,4));
//        recyclerView.setAdapter(adapter);
//        submit();
//        bt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //创建对象
//                Retrofit retrofit = new Retrofit.Builder()
//                        .baseUrl(URL)
//                        .addConverterFactory(GsonConverterFactory.create())
//                        .build();
//                APi api = retrofit.create(APi.class);
//                final Call<NewBeans> call = api.getiphone(et.getText().toString(), AppKey);
//
//                new Thread() {
//
//                    @Override
//                    public void run() {
//                        Response<NewBeans> bodyResponse = null;
//                        try {
//                            bodyResponse = call.execute();
//                        } catch (IOException e) {
//                            Toast.makeText(MainActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
//                        }
//                        final String body = bodyResponse.body().getResult().getCity();
//                        tv.setText(body);
//
//                        super.run();
//
//                    }
//                }.start();
//                call.enqueue(new Callback<NewBeans>() {
//                    @Override
//                    /**
//                     * 利用从NewBeans中获取信息，
//                     */
//                    public void onResponse(Call<NewBeans> call, Response<NewBeans> response) {
//                        tv.setText(response.body().toString());
//                    }
//                    @Override
//                    /**
//                     * 联网失败的消息，异常消息存放到Throwable中，
//                     * 通过.getMessage获取反馈的消息
//                     */
//                    public void onFailure(Call<NewBeans> call, Throwable t) {
//                        tv.setText(t.getMessage());
//                    }
//                });
//            }
//        });

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

}
