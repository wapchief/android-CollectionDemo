package wapchief.com.collectiondemo.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import wapchief.com.collectiondemo.R;
import wapchief.com.collectiondemo.bean.APi;
import wapchief.com.collectiondemo.bean.NewBeans;
import wapchief.com.collectiondemo.utils.UToasts;

/**
 * Created by Wu on 2017/2/17 0017 上午 10:55.
 * 描述：
 */
public class OkhttpRetrofitActivity extends AppCompatActivity {
    @BindView(R.id.bt_okGet)
    Button btOkGet;
    @BindView(R.id.bt_okPost)
    Button btOkPost;
    @BindView(R.id.bt_reGet)
    Button btReGet;
    @BindView(R.id.bt_rePost)
    Button btRePost;
    @BindView(R.id.et_http)
    EditText etHttp;
    @BindView(R.id.tv)
    TextView tv;
    private String utl_ok = "http://apis.juhe.cn/mobile/get?phone=";
    private String KEY = "&key=6e1ce94fe231e817fb31daec3b3084d0";
    private String URL;
    static String host_address = "https://tcc.taobao.com";
    public static OkHttpClient mClient = new OkHttpClient();

    private static Retrofit sRetrofit;

    List<NewBeans.ResultBean> list;
    NewBeans newBeans = new NewBeans();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.httptest);
        ButterKnife.bind(this);
//        init();
        URL = utl_ok + etHttp.getText() + KEY;
        //okhttp
//        Request.Builder builder=new Request.Builder();
//        builder.url(host_address + phone).get();
//        Request request = builder.build();
//        Call call = mClient.newCall(request);
//        try {
//            Response response = call.execute();
//            ResponseBody body = response.body();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    private void init() {
//        et = (EditText) findViewById(R.id.et_http);
//        tv = (TextView) findViewById(R.id.tv);
//        bt_okGet = (Button) findViewById(R.id.bt_okGet);
//        bt_okGet.setOnClickListener(this);

//        bt_okGet.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                okhttpGet();
//                okhttpPost();
//            }
//        });
    }

    Retrofit getsRetrofit() {
// 1.初始化一个建造者
        Retrofit.Builder builder = new Retrofit.Builder();

        // 2.添加主机地址
        builder.baseUrl(HttpUrl.parse(host_address))
                // .client(mOkHttpClient)可以使用指定的OkHttpClient
                // 3.添加数据转换工厂
                .addConverterFactory(ScalarsConverterFactory.create())// 处理基本数据类型
                .addConverterFactory(GsonConverterFactory.create())// 处理gson数据类型
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create());//添加回调的适配器，RxJava的回调形式（即非callback的形式）;

        // 4.生成配置好的retrofit
        return builder.build();
    }

    public void urltest() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(utl_ok + etHttp.getText() + KEY)//这里填入主机的地址
                .build();
        APi aPi = retrofit.create(APi.class);
        Log.e("-----------------api", aPi.toString());
        final Call<ResponseBody> call = aPi.listRepos(etHttp.getText().toString());
        Log.e("-----------------call", call.request().body().toString());
        Log.e("Alex", "body是" + call.request().body() + " url是" + call.request().url() + "  method是" + call.request().method());

        new Thread() {

            @Override
            public void run() {
                Response<ResponseBody> bodyResponse = null;
                try {
                    bodyResponse = call.execute();
                } catch (IOException e) {
                    Toast.makeText(OkhttpRetrofitActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
                }
                final String body = bodyResponse.body().toString();
                Log.e("-----------body", body.toString());
                tv.setText(body);

                super.run();

            }
        }.start();
        call.enqueue(new Callback<ResponseBody>() {
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Log.i("--------------Alex", "成功" + response.body().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i("-----------------Alex", "失败", t);

            }

        });
    }

    public void okhttpGet() {
        OkHttpUtils.get()
                .url(URL)
                .addParams("phone", etHttp.getText().toString())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Call call, Exception e, int id) {
                        UToasts.showShort(OkhttpRetrofitActivity.this, "请求失败:" + e.getMessage().toString());
                    }

                    @Override
                    public void onResponse(String response, int id) {
//                        UToasts.showShort(OkhttpRetrofitActivity.this,"响应成功:"+response+"==="+id);
//                        JSONObject jsonObject=newBeans.getResult();
//                        tv.setText(response);
                        Gson gson = new Gson();
                        NewBeans newBeans = gson.fromJson(response, NewBeans.class);
                        newBeans.getResult();
                        UToasts.showLong(OkhttpRetrofitActivity.this, newBeans.getReason());

                        if (newBeans.getResultcode().equals("200")) {
                            tv.setText(
                                    "省：" + newBeans.getResult().getProvince().toString()
                                            + "\n"
                                            + "市：" + newBeans.getResult().getCity().toString()
                                            + "\n"
                                            + "区号：" + newBeans.getResult().getAreacode()
                                            + "\n"
                                            + "运营商：" + newBeans.getResult().getCompany());
                        } else {
                            tv.setText("手机号码输入有误，查询失败");
                        }
                    }
                });

//        tv.setText();

    }

    public void okhttpPost() {
        OkHttpUtils.post()
                .url(URL)
                .addParams("phone", etHttp.getText().toString())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Gson gson = new Gson();
                        NewBeans newBeans = gson.fromJson(response, NewBeans.class);
                        newBeans.getResult();
                        UToasts.showLong(OkhttpRetrofitActivity.this, newBeans.getReason());

                        if (newBeans.getResultcode().equals("200")) {
                            tv.setText(
                                    "省：" + newBeans.getResult().getProvince().toString()
                                            + "\n"
                                            + "市：" + newBeans.getResult().getCity().toString()
                                            + "\n"
                                            + "区号：" + newBeans.getResult().getAreacode()
                                            + "\n"
                                            + "运营商：" + newBeans.getResult().getCompany());
                        } else {
                            tv.setText("手机号码输入有误，查询失败");
                        }

                    }
                });

    }

    @OnClick({R.id.bt_okGet, R.id.bt_okPost, R.id.bt_reGet, R.id.bt_rePost})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_okGet:
                okhttpGet();
                break;
            case R.id.bt_okPost:
                okhttpPost();
                break;
            case R.id.bt_reGet:
                break;
            case R.id.bt_rePost:
                break;
        }
    }

}
