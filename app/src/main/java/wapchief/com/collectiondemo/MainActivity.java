package wapchief.com.collectiondemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;


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
    private List<String> data;

    OkHttpClient okHttpClient=new OkHttpClient();
    //     单个监听bt
    @OnClick(R.id.bt)
    void submit() {
        UToasts.showShort(this, "监听成功");
    }

    //     多个监听
    @OnClick({R.id.et, R.id.bt2})
    void submix(View view) {
        switch (view.getId()) {
            case R.id.et:
                UToasts.showShort(this, "输入框被点击了");
                break;
            case R.id.bt2:
                UToasts.showShort(this, "tv被点击了");
                Intent intent=new Intent(MainActivity.this,RecyclerViewActivity.class);
                startActivity(intent);
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
}
