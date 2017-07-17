package wapchief.com.collectiondemo.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import wapchief.com.collectiondemo.R;

/**
 * Created by wapchief on 2017/7/15.
 */

public class TimerButtonActivity extends AppCompatActivity {

    @BindView(R.id.timer1)
    Button mTimer1;
    @BindView(R.id.timer2)
    Button mTimer2;
    @BindView(R.id.timer3)
    Button mTimer3;
    private int time = 10;
    private CountDownTimer countDownTimer;
    private Timer timer = new Timer();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        ButterKnife.bind(this);
    }


    @OnClick({R.id.timer1, R.id.timer2, R.id.timer3})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.timer1:
                startTime1();
                break;
            case R.id.timer2:
                startTime2();
                break;
            case R.id.timer3:
                try {
                    timer.schedule(task, time, 1000);
                } catch (Exception e) {

                }
                break;
        }
    }
    //方法3，使用TimerTask

    TimerTask task = new TimerTask() {
        @Override
        public void run() {

            runOnUiThread(new Runnable() {      // UI thread
                @Override
                public void run() {
                    time--;
                    mTimer3.setText("已发送(" + String.valueOf(time) + ")");
                    mTimer3.setEnabled(false);
                    if (time <= 0) {
                        mTimer3.setEnabled(true);
                        mTimer3.setText("重新获取验证码");
                    }
                }
            });
        }
    };

    //方法2，使用CountDownTimer
    private void startTime2() {

        countDownTimer = new CountDownTimer(10000, 100) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimer2.setEnabled(false);
                mTimer2.setText("已发送(" + millisUntilFinished / 1000 + ")");

            }

            @Override
            public void onFinish() {
                mTimer2.setEnabled(true);
                mTimer2.setText("重新获取验证码");

            }
        }.start();
    }


    // 方法1，使用线程
    public void startTime1() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                time--;
                if (time <= 0) {
                    mHandler.sendEmptyMessage(5);
                } else {
                    mHandler.sendEmptyMessage(4);
                    mHandler.postDelayed(this, 1000);
                }
            }
        };
        new Thread(runnable).start();
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    setResult(RESULT_OK);
                    TimerButtonActivity.this.finish();
                    break;
                case 4:
                    mTimer1.setEnabled(false);
                    mTimer1.setText("已发送(" + String.valueOf(time) + ")");
                    break;
                case 5:
                    mTimer1.setEnabled(true);
                    mTimer1.setText("重新获取验证码");

                    time = 10;
                    break;
            }

        }
    };

}
