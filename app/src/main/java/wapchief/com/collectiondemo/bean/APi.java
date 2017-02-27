package wapchief.com.collectiondemo.bean;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;
import wapchief.com.collectiondemo.bean.NewBeans;

/**
 * Created by WuBing on 2017/1/6 0006 下午 1:50.
 * 描述：
 */
public interface APi {
//    @Headers({"6e1ce94fe231e817fb31daec3b3084d0","Content-Type:application/json"})
    @GET("/mobile/get")
    Call<NewBeans> getphone(@Query("phone") String phone);

    Call<ResponseBody> listRepos(@Query("tel") String phone);
}

