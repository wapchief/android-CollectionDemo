package wapchief.com.collectiondemo.bean;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import wapchief.com.collectiondemo.bean.NewBeans;

/**
 * Created by WuBing on 2017/1/6 0006 下午 1:50.
 * 描述：
 */
public interface APi {
//    @Headers({"6e1ce94fe231e817fb31daec3b3084d0","Content-Type:application/json"})
    @GET("mobile/git")
    Call<NewBeans> getiphone(@Query("phone") String phone, @Query("key") String key);
}
