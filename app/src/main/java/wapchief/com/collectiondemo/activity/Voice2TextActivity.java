package wapchief.com.collectiondemo.activity;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.baidu.speech.EventListener;
import com.baidu.speech.EventManager;
import com.baidu.speech.EventManagerFactory;
import com.blankj.utilcode.utils.StringUtils;
import com.google.gson.Gson;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import wapchief.com.collectiondemo.R;
import wapchief.com.collectiondemo.bean.BaiDuVoice;
import wapchief.com.collectiondemo.controller.MyRecognizer;
import wapchief.com.collectiondemo.framework.BaseActivity;
import wapchief.com.collectiondemo.utils.JsonParser;

/**
 * Created by wapchief on 2017/10/9.
 */

public class Voice2TextActivity extends BaseActivity implements EventListener {

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
    @BindView(R.id.voice_bd)
    Button mVoiceBd;
    @BindView(R.id.voice_bd_stop)
    Button mVoiceBdStop;
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


    MyRecognizer myRecognizer;
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
        baiduRecognizer();
    }

    @OnClick({R.id.voice_bt, R.id.voice_play,R.id.voice_bd,R.id.voice_bd_stop})
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
                Log.e(TAG + "time", mp.getDuration() + "\n" + mp.getCurrentPosition());
//                timer.schedule(task,mp.getDuration(),500);
                break;
            case R.id.voice_bd:
start();
                break;
            case R.id.voice_bd_stop:
stop();
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

//    ===================================百度语音=========================================


    private void baiduRecognizer() {
//        parms.put("accept-audio-data", false);
//        parms.put("accept-audio-volume", true);
//        parms.put("pid", 8172882);

        manager = EventManagerFactory.create(this, "asr");
        manager.registerListener(this);

        if (enableOffline){
            loadOfflineEngine();
        }
    }

    public void initBaiDu() {

        myRecognizer = new MyRecognizer(this, new EventListener() {
            @Override
            public void onEvent(String s, String s1, byte[] bytes, int i, int i1) {
                if (s.equals(com.baidu.speech.asr.SpeechConstant.CALLBACK_EVENT_ASR_PARTIAL)){
                    if (s1.contains("\"nlu_result\"")){
                        if (i1 > 0 && bytes.length > 0) {
                            Log.e(TAG, "BaiDu解析：" + new String(bytes, i, i1));
                        }
                    }
                }
                if (s.equals(com.baidu.speech.asr.SpeechConstant.CALLBACK_EVENT_ASR_READY)) {
                    //引擎就绪，可以说话，ui改变
                    Log.e(TAG, "BaiDuEvent:"+s + "\n." + s1 + "");
                }
                if (s.equals(com.baidu.speech.asr.SpeechConstant.CALLBACK_EVENT_ASR_FINISH)) {
                    //识别结束
                    Log.e(TAG, "BaiDuEventFinish:"+s + "\n." + s1 + "");

                }
            }

        });
    }

    private boolean enableOffline = true;
    private EventManager manager;

    /*启动*/
    private void start() {
        Map<String, Object> params1 = new LinkedHashMap<>();
        String event = null;
        //开启录音
        event = com.baidu.speech.asr.SpeechConstant.ASR_START;
//        params1.put(com.baidu.speech.asr.SpeechConstant.APP_NAME, "");
        //音量数据回调开启
        params1.put(com.baidu.speech.asr.SpeechConstant.ACCEPT_AUDIO_VOLUME, false);
        //活动语音检测
        params1.put(com.baidu.speech.asr.SpeechConstant.VAD, com.baidu.speech.asr.SpeechConstant.VAD_DNN);
        //开启长语音，需要手动停止录音：ASR_START
        params1.put(com.baidu.speech.asr.SpeechConstant.VAD_ENDPOINT_TIMEOUT, 0);
        if (enableOffline) {
            //离线在线并行策略
            params1.put(com.baidu.speech.asr.SpeechConstant.DECODER, 2);
        }
        //开启语音音频数据回调
//        params1.put(com.baidu.speech.asr.SpeechConstant.ACCEPT_AUDIO_DATA, true);
        //设置文件保存路径
//        params1.put(com.baidu.speech.asr.SpeechConstant.IN_FILE, "/msc/test.pcm");
        //保存文件
//        params1.put(com.baidu.speech.asr.SpeechConstant.OUT_FILE, true);

        String json = null;
        json = new JSONObject(params1).toString();
        manager.send(event, json, null, 0, 0);
        Log.e(TAG, "BaiDujson：" + json);
    }

    /*停止*/
    private void stop() {
        manager.send(com.baidu.speech.asr.SpeechConstant.ASR_STOP, null, null, 0, 0);
    }
    String jsonstr="{\"results_recognition\":[\"\"],\"origin_result\":{\"corpus_no\":6475915388511426951,\"err_no\":0,\"result\":{\"word\":[\"\"]},\"sn\":\"61d9af66-f57c-4175-ae9c-aa6140e93d14_s-0\"},\"error\":0,\"best_result\":\"\",\"result_type\":\"final_result\"}";
    public void onEvent(String s, String s1, byte[] bytes, int i, int i1) {

        String logText = "name:" + s;
        Log.e(TAG, "EventText:"+s+"\n"+s1);
        if (!StringUtils.isEmpty(s1)) {
            logText += ",,,,,parame:" + s1;
        }
        if (s.equals("asr.partial")||s=="asr.partial") {
            jsonstr = s1;
            Gson gson = new Gson();
            BaiDuVoice voice = gson.fromJson(jsonstr,BaiDuVoice.class);
            Log.e(TAG, "BaiDuBean:"+jsonstr+"\n========="+voice.toString());
            mVoiceEt.setText(voice.best_result+=voice.best_result);
        }
        if (s.equals(com.baidu.speech.asr.SpeechConstant.CALLBACK_EVENT_ASR_PARTIAL)){
            if (s1.contains("\"nlu_result\"")){
                if (i1 > 0 && bytes.length > 0) {
                    Log.e(TAG, "BaiDu解析：" + new String(bytes, i, i1));
                    logText += "，，，解析：" + new String(bytes, i, i1);
                }
            }
        }
        if (s.equals(com.baidu.speech.asr.SpeechConstant.CALLBACK_EVENT_ASR_READY)) {
            //引擎就绪，可以说话，ui改变
            Log.e(TAG, "BaiDuEvent:"+s + "\n." + s1 + "");
        }
        if (s.equals(com.baidu.speech.asr.SpeechConstant.CALLBACK_EVENT_ASR_FINISH)) {
            //识别结束

            Log.e(TAG, "BaiDuEventFinish:"+s + "\n." + s1 + "");

        }

        printLog(logText);
    }
    /*动态权限申请*/
    private void initPermission(){
        String permission[]={Manifest.permission.RECORD_AUDIO,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.INTERNET,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};
        ArrayList<String> applyList = new ArrayList<>();

        for (String per : permission) {
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, per)) {
                applyList.add(per);
            }
        }

        String tmpList[] = new String[applyList.size()];
        if (!applyList.isEmpty()){
            ActivityCompat.requestPermissions(this,applyList.toArray(tmpList),123);
        }
    }

    private void printLog(String text){
        mVoiceTv.append(text + "\n");
    }

    private void  loadOfflineEngine(){
        Map<String, Object> parmes2 = new LinkedHashMap<>();
        parmes2.put(com.baidu.speech.asr.SpeechConstant.DECODER, 2);
        parmes2.put(com.baidu.speech.asr.SpeechConstant.ASR_OFFLINE_ENGINE_GRAMMER_FILE_PATH, "assets://baidu_speech_grammar.bsg");
        manager.send(com.baidu.speech.asr.SpeechConstant.ASR_KWS_LOAD_ENGINE, new JSONObject(parmes2).toString(), null, 0, 0);
    }

}