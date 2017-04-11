package wapchief.com.collectiondemo.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.api.BasicCallback;
import wapchief.com.collectiondemo.MainActivity;
import wapchief.com.collectiondemo.R;
import wapchief.com.collectiondemo.bean.TModel;
import wapchief.com.collectiondemo.framework.callback.BasicCallBack;

/**
 * Created by Wu on 2017/4/11 0011 下午 3:08.
 * 描述：极光IM
 */
public class JPushIMActivity extends AppCompatActivity {

    @BindView(R.id.jpush_im_userName)
    EditText jpushImUserName;
    @BindView(R.id.jpush_im_passWord)
    EditText jpushImPassWord;
    @BindView(R.id.jpush_im_submit)
    Button jpushImSubmit;
    @BindView(R.id.jpush_im_login)
    Button jpushImLogin;

    String msg;
    private Context context;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jpush_im_register);
        ButterKnife.bind(this);
        context=JPushIMActivity.this;
    }

    @OnClick({R.id.jpush_im_submit, R.id.jpush_im_login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.jpush_im_submit:
                dismissSoftKeyboard(this);
                if (jpushImUserName.getText().toString().equals("")|jpushImPassWord.getText().toString().equals("")){
                    Toast.makeText(context,"用户名和密码不能为空",Toast.LENGTH_SHORT).show();
                }else {
                    jpushImRegister(jpushImUserName.getText().toString(),jpushImPassWord.getText().toString());
                }

                break;
            case R.id.jpush_im_login:
                dismissSoftKeyboard(this);
                if (jpushImUserName.getText().toString().equals("")|jpushImPassWord.getText().toString().equals("")){
                    Toast.makeText(context,"用户名和密码不能为空",Toast.LENGTH_SHORT).show();
                }else {
                    jpushImLogin(jpushImUserName.getText().toString(), jpushImPassWord.getText().toString());
                }
                break;
        }
    }

    /**
     * 登录
     * @param username
     * @param password
     */
    public void jpushImLogin(String username,String password){
        JMessageClient.login(username, password, new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                if (i==801003){
                    Toast.makeText(context,"用户名不存在！",Toast.LENGTH_SHORT).show();
                    jpushImUserName.setText("");
                }
                if (i==801004){
                    Toast.makeText(context,"密码错误！",Toast.LENGTH_SHORT).show();
                    jpushImPassWord.setText("");
                }
                if (i==0){
                    Toast.makeText(context,"登录成功！",Toast.LENGTH_SHORT).show();
                    jpushImPassWord.setText("");
                    msg="登录成功，";
                    successDialog();
                }
            }
        });
    }

    /**
     * 注册
     * @param username
     * @param password
     */
    public void jpushImRegister(String username,String password){
        JMessageClient.register(username, password, new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {

                if (i==0){
                    Toast.makeText(context,"注册成功！",Toast.LENGTH_SHORT).show();
                    msg="注册成功，";
                    successDialog();

                }else if (i==898001){
                    Toast.makeText(context,"用户已存在，创建失败！",Toast.LENGTH_SHORT).show();
                    jpushImUserName.setText("");
                    jpushImPassWord.setText("");
                } else {
                    Toast.makeText(context,"注册失败：只能是字母或者数字开头。支持字母、数字、下划线、英文点、减号、 @。",Toast.LENGTH_LONG).show();

                }
//                Log.e("i----", i + "\n"
//                        + s);
            }
        });
    }

    /**
     * 登录成功，是否完善资料
     */
    public void successDialog(){
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(context);
        normalDialog.setMessage(msg+"是否去完善资料?");
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                    }
                });
        normalDialog.setNegativeButton("关闭",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        // 显示
        normalDialog.show();
    }

    /**
     * 本段代码用来处理如果输入法还显示的话就消失掉输入键盘
     */
    protected void dismissSoftKeyboard(Activity activity) {
        try {
            InputMethodManager inputMethodManage = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManage.hideSoftInputFromWindow(activity
                            .getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
