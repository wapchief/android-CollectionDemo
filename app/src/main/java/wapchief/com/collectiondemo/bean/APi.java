package wapchief.com.collectiondemo.bean;


import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by WuBing on 2017/1/6 0006 下午 1:50.
 * 描述：
 */
public interface APi {
//    http://apis.juhe.cn/mobile/get?phone=13429667914&key=6e1ce94fe231e817fb31daec3b3084d0
    @GET("mobile/get?phone=13429667914&key=6e1ce94fe231e817fb31daec3b3084d0")
    Call<NewBeans> getPhone(@Query("phone") String phone);

    @POST("mobile/get?phone=13429667914&key=6e1ce94fe231e817fb31daec3b3084d0")
    Call<NewBeans> postPhone(@Query("phone") String phone);

    @GET("mobile/get?phone=13429667914&key=6e1ce94fe231e817fb31daec3b3084d0")
    Observable<NewBeans> postRxPhone(@Query("phone") String phone);
}

