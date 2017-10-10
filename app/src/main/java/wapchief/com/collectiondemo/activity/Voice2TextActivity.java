package wapchief.com.collectiondemo.activity;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import wapchief.com.collectiondemo.R;
import wapchief.com.collectiondemo.framework.BaseActivity;
import wapchief.com.collectiondemo.utils.JsonParser;

/**
 * Created by wapchief on 2017/10/9.
 */

public class Voice2TextActivity extends BaseActivity {

    @BindView(R.id.voice_tv)
    TextView mVoiceTv;
    @BindView(R.id.voice_et)
    EditText mVoiceEt;
    @BindView(R.id.voice_bt)
    Button mVoiceBt;
    @BindView(R.id.voice_play)
    Button mVoicePlay;
    @BindView(R.id.voice_seekbar)
    SeekBar mVoiceSeekbar;
    private MediaPlayer mp = new MediaPlayer();
    //语音听写
    private SpeechRecognizer recognizer;
    //语音听写dialog
    private RecognizerDialog dialog;
    private SharedPreferences mSharedPreferences;
    private boolean mTranslateEnable = false;
    //引擎类型
    private String mEngineType = SpeechConstant.TYPE_CLOUD;
    private static String TAG = "Activity-Voice2Text";
    //储存听写结果
    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();
    private String text;
    private String dictationResultStr = "[";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice2text);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
//        mSharedPreferences=getSharedPreferences()
        recognizer = SpeechRecognizer.createRecognizer(this, null);
        initMP();
    }

    @OnClick({R.id.voice_bt, R.id.voice_play})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.voice_bt:

                //1.创建SpeechRecognizer对象
                dialog = new RecognizerDialog(Voice2TextActivity.this, initListener);
                //2.设置参数
                setParam();
                //3.设置回调
                dialog.setListener(dialogListener);
                //4.开始听写
                dialog.show();

                break;
            case R.id.voice_play:
                mVoiceSeekbar.setMax(mp.getDuration());
                mp.start();
                Log.e(TAG + "time", mp.getDuration()+"\n"+mp.getCurrentPosition());
//                timer.schedule(task,mp.getDuration(),500);
                break;
        }
    }

    /*初始化监听*/
    private InitListener initListener = new InitListener() {
        @Override
        public void onInit(int i) {
            if (i != ErrorCode.SUCCESS) {
                showToast(Voice2TextActivity.this, "初始化失败：" + i);
            }
        }
    };

    /*初始化dialog监听*/
    private RecognizerDialogListener dialogListener = new RecognizerDialogListener() {
        @Override
        public void onResult(RecognizerResult recognizerResult, boolean b) {
            Log.e(TAG, recognizerResult + "\n" + recognizerResult.getResultString());
            text = JsonParser.parseIatResult(recognizerResult.getResultString());
            mVoiceTv.setText(text);
            printResult(recognizerResult);


        }

        @Override
        public void onError(SpeechError speechError) {
            Log.e(TAG + "error", speechError + "\n" + speechError.getErrorCode() + "\n" + speechError.toString());

        }
    };


    /**
     * 参数设置
     *
     * @return
     */
    public void setParam() {
        //2.设置听写参数，详见《科大讯飞MSC API手册(Android)》SpeechConstant类
        recognizer.setParameter(SpeechConstant.DOMAIN, "iat");
        recognizer.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        recognizer.setParameter(SpeechConstant.ACCENT, "mandarin ");
        //设置音频保存路径
        recognizer.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        recognizer.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/iat.wav");
//3.开始听写   mIat.startListening(mRecoListener);
//听写监听器



    }

    /*解析器*/
    private void printResult(RecognizerResult results) {
        String text = JsonParser.parseIatResult(results.getResultString());

        String sn = null;
        // 读取json结果中的sn字段
        try {
            JSONObject resultJson = new JSONObject(results.getResultString());
            sn = resultJson.optString("sn");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mIatResults.put(sn, text);

        StringBuffer resultBuffer = new StringBuffer();
        for (String key : mIatResults.keySet()) {
            resultBuffer.append(mIatResults.get(key));
        }

        mVoiceEt.setText(resultBuffer.toString());
        mVoiceEt.setSelection(mVoiceEt.length());
        mVoiceTv.setText(resultBuffer.toString());
    }

    //初始化mp
    private void initMP() {
        File file = new File(Environment.getExternalStorageDirectory(), "/msc/iat.wav");

        try {
            mp.setDataSource(file.getPath());//设置播放音频文件的路径
            mp.prepare();//mp就绪
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    private Timer timer = new Timer();
    TimerTask task = new TimerTask() {
        @Override
        public void run() {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        mVoiceSeekbar.setProgress(mp.getCurrentPosition());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };

}