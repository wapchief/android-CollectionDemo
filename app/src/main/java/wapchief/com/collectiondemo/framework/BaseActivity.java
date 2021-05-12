package wapchief.com.collectiondemo.framework;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.Toast;

/**
 * Created by wubing on 2017/7/10.
 */

public class BaseActivity extends AppCompatActivity{

    private ProgressDialog progressDialog;
    private Context context;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = BaseActivity.this;
    }


    /*加载中的进度条*/
    public void showProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
        progressDialog = new ProgressDialog(BaseActivity.this);
        progressDialog.setMessage("正在加载......");
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(false);
        try {
            progressDialog.show();
        } catch (WindowManager.BadTokenException exception) {
            exception.printStackTrace();
        }
    }

    /*自定义消息的加载进度条*/
    public void showProgressDialog(String msg) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
        progressDialog = new ProgressDialog(BaseActivity.this);
        progressDialog.setMessage(msg);
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(false);
        try {
            progressDialog.show();
        } catch (WindowManager.BadTokenException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * 隐藏正在加载的进度条
     *
     */
    public void dismissProgressDialog() {
        if (null != progressDialog && progressDialog.isShowing() == true) {
            progressDialog.dismiss();
        }
    }


    /**
     * 短时间Toast提示
     * @param activity
     * @param s
     */
    public void showToast(Activity activity, String s){
        Toast toast=Toast.makeText(activity,s,Toast.LENGTH_SHORT);
        toast.show();
    }

    /**
     * 长时间Toast提示
     * @param activity
     * @param s
     */
    public void showLongToast(Activity activity,String s){
        Toast toast=Toast.makeText(activity,s,Toast.LENGTH_LONG);
        toast.show();
    }

}
