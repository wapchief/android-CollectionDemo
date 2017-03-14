package wapchief.com.collectiondemo.activity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Proxy;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import wapchief.com.collectiondemo.R;
import wapchief.com.collectiondemo.bean.APi;
import wapchief.com.collectiondemo.bean.NewBeans;
import wapchief.com.collectiondemo.utils.UToasts;
import wapchief.com.collectiondemo.utils.Url;

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
    @BindView(R.id.bt_network)
    Button btNetwork;
    private String utl_ok = "http://apis.juhe.cn/mobile/get?phone=";
    private String KEY = "&key=6e1ce94fe231e817fb31daec3b3084d0";
    private String URL;
    public static OkHttpClient mClient = new OkHttpClient();

    /**
     * 没有网络
     */
    public static final int NETWORKTYPE_INVALID = 0;
    /**
     * wap网络
     */
    public static final int NETWORKTYPE_WAP = 1;
    /**
     * 2G网络
     */
    public static final int NETWORKTYPE_2G = 2;
    /**
     * 3G和3G以上网络，或统称为快速网络
     */
    public static final int NETWORKTYPE_3G = 3;
    /**
     * wifi网络
     */
    public static final int NETWORKTYPE_WIFI = 4;
    static int mNetWorkType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.httptest);
        ButterKnife.bind(this);
        URL = utl_ok + etHttp.getText() + KEY;
    }

    @OnClick({R.id.bt_okGet, R.id.bt_okPost, R.id.bt_reGet, R.id.bt_rePost, R.id.bt_network})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_okGet:
                okhttpGet();
                break;
            case R.id.bt_okPost:
                okhttpPost();
                break;
            case R.id.bt_reGet:
                retrofitGet();
                break;
            case R.id.bt_rePost:
                retrofitPost();
                break;
            case R.id.bt_network:
                if (getNetWorkType(this) == NETWORKTYPE_WIFI) {
                    UToasts.showShort(this, "当前是wifi");
                } else {
                    UToasts.showShort(this, "不是wifi");
                }
                break;
        }
    }
    /**
     * retrofit之Get请求
     */
    public void retrofitGet() {
        Retrofit retrofit = new Retrofit.Builder()//创建Rectfit对象
                .addConverterFactory(GsonConverterFactory.create())//添加解析器
                .baseUrl(Url.BASE_JUHE_URL)//传入地址
                .build();
        APi aPi = retrofit.create(APi.class);//实例化接口对象
        Call<NewBeans> call = aPi.getPhone(etHttp.getText().toString());//传入参数

        //处理响应结果
        call.enqueue(new Callback<NewBeans>() {
            @Override
            //正确返回
            public void onResponse(Call<NewBeans> call, Response<NewBeans> response) {
                if (response.body().getResultcode().equals("200")) {
                    tv.setText("retrofit请求结果:"
                            + "\n"
                            + "省：" + response.body().getResult().getProvince().toString()
                            + "\n"
                            + "市：" + response.body().getResult().getCity().toString()
                            + "\n"
                            + "区号：" + response.body().getResult().getAreacode()
                            + "\n"
                            + "运营商：" + response.body().getResult().getCompany());
                } else {
                    tv.setText("手机号码输入有误，查询失败");
                }
            }

            //错误返回
            @Override
            public void onFailure(Call<NewBeans> call, Throwable t) {
            }
        });
    }

    public void retrofitPost() {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Url.BASE_JUHE_URL)
                .build();
        Call<NewBeans> call = retrofit.create(APi.class)
                .postPhone(etHttp.getText().toString());
        call.enqueue(new Callback<NewBeans>() {
            @Override
            public void onResponse(Call<NewBeans> call, Response<NewBeans> response) {
                if (response.body().getResultcode().equals("200")) {
                    tv.setText("retrofitPost请求结果:"
                            + "\n"
                            + "省：" + response.body().getResult().getProvince().toString()
                            + "\n"
                            + "市：" + response.body().getResult().getCity().toString()
                            + "\n"
                            + "区号：" + response.body().getResult().getAreacode()
                            + "\n"
                            + "运营商：" + response.body().getResult().getCompany());
                } else {
                    tv.setText("手机号码输入有误，查询失败");
                }
            }

            @Override
            public void onFailure(Call<NewBeans> call, Throwable t) {

            }
        });
    }

    /**
     * okhttp之Get请求
     */
    public void okhttpGet() {
        OkHttpUtils
                .get()
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
                        Gson gson = new Gson();
                        NewBeans newBeans = gson.fromJson(response, NewBeans.class);
                        newBeans.getResult();
                        UToasts.showLong(OkhttpRetrofitActivity.this, newBeans.getReason());

                        if (newBeans.getResultcode().equals("200")) {
                            tv.setText("okhttpGit请求结果:"
                                    + "\n"
                                    +
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

    /**
     * okhttp之post请求
     */
    public void okhttpPost() {
        OkHttpUtils
                .post()
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
                            tv.setText("okhttpPost请求结果:"
                                    + "\n"
                                    +
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


    /**
     * 获取网络状态，wifi,wap,2g,3g.
     *
     * @param context 上下文
     * @return int 网络状态 {@link #NETWORKTYPE_2G},{@link #NETWORKTYPE_3G},          *{@link #NETWORKTYPE_INVALID},{@link #NETWORKTYPE_WAP}* <p>{@link #NETWORKTYPE_WIFI}
     */
    public static int getNetWorkType(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            String type = networkInfo.getTypeName();
            if (type.equalsIgnoreCase("WIFI")) {
                mNetWorkType = NETWORKTYPE_WIFI;
            } else if (type.equalsIgnoreCase("MOBILE")) {
                String proxyHost = Proxy.getDefaultHost();
                mNetWorkType = TextUtils.isEmpty(proxyHost)
                        ? (isFastMobileNetwork(context) ? NETWORKTYPE_3G : NETWORKTYPE_2G)
                        : NETWORKTYPE_WAP;
            }
        } else {
            mNetWorkType = NETWORKTYPE_INVALID;
        }
        return mNetWorkType;
    }

    private static boolean isFastMobileNetwork(Context context) {
        // TODO Auto-generated method stub
        return false;
    }

}
