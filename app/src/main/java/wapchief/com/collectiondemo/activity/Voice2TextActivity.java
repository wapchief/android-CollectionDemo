package wapchief.com.collectiondemo.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.idst.nls.NlsClient;
import com.alibaba.idst.nls.NlsListener;
import com.alibaba.idst.nls.StageListener;
import com.alibaba.idst.nls.internal.protocol.NlsRequest;
import com.alibaba.idst.nls.internal.protocol.NlsRequestProto;
import com.baidu.speech.EventListener;
import com.baidu.speech.EventManager;
import com.baidu.speech.EventManagerFactory;
import com.google.gson.Gson;
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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import wapchief.com.collectiondemo.R;
import wapchief.com.collectiondemo.bean.AliVoice;
import wapchief.com.collectiondemo.bean.BaiDuVoice;
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
    @BindView(R.id.voice_ali)
    Button mVoiceAli;
    @BindView(R.id.voice_ali_stop)
    Button mVoiceAliStop;
    @BindView(R.id.voice_xf)
    Button mVoiceXf;
    @BindView(R.id.voice_xf_stop)
    Button mVoiceXfStop;
    private MediaPlayer mp = new MediaPlayer();
    //语音听写
    private SpeechRecognizer recognizer;
    //语音听写dialog
    private RecognizerDialog dialog;
    private static String TAG = "Activity-Voice2Text";
    //储存听写结果
    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();
    private String text;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice2text);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        initPermission();
        recognizer = SpeechRecognizer.createRecognizer(this, null);
        initMediaPlayer();
        initBaiduRecognizer();
        initAliRecognizer();
    }

    @OnClick({R.id.voice_bt, R.id.voice_play,
            R.id.voice_bd, R.id.voice_bd_stop,
            R.id.voice_ali, R.id.voice_ali_stop,
            R.id.voice_xf, R.id.voice_xf_stop})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.voice_bt:

                //1.创建SpeechRecognizer对象
                dialog = new RecognizerDialog(Voice2TextActivity.this, initListener);
//                dialog.setUILanguage();
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
            case R.id.voice_ali:
                mVoiceTv.setText("正在录音.....");
                nlsRequest.authorize("LTAIUw0pk2a4ZpEm", "tXhl0Tr4hmZy5RfKvh77rCbrs7wAKT");
                nlsClient.start();
                mVoiceAli.setText("录音中...");
                break;
            case R.id.voice_ali_stop:
                mVoiceTv.setText("");
                nlsClient.stop();
                mVoiceAli.setText("阿里语音识别");
//                nlsRequest.
                break;
            case R.id.voice_xf:
                startXF();
                mVoiceXf.setText("录音中...");
                break;
            case R.id.voice_xf_stop:
                stopXF();
                mVoiceXf.setText("讯飞语音识别（无ui）");
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

        mVoiceEt.setText("讯飞识别结果：" + resultBuffer.toString());
        mVoiceEt.setSelection(mVoiceEt.length());
//        mVoiceTv.setText(resultBuffer.toString());
    }

    //播放音频文件
    private void initMediaPlayer() {
        File file = new File(Environment.getExternalStorageDirectory(), "/msc/iat.wav");

        try {
            mp.setDataSource(file.getPath());//设置播放音频文件的路径
            mp.prepare();//mp就绪
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

//    ===================================百度语音=========================================


    /*注册服务*/
    private void initBaiduRecognizer() {

        manager = EventManagerFactory.create(this, "asr");
        manager.registerListener(this);
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
        //开启长语音(无静音超时断句)，需要手动停止录音：ASR_START
//        params1.put(com.baidu.speech.asr.SpeechConstant.VAD_ENDPOINT_TIMEOUT, 0);
        if (enableOffline) {
            //离线在线并行策略
//            params1.put(com.baidu.speech.asr.SpeechConstant.DECODER, 2);
        }
        //开启语音音频数据回调
        params1.put(com.baidu.speech.asr.SpeechConstant.ACCEPT_AUDIO_DATA, true);
        //设置文件回调
//        params1.put(com.baidu.speech.asr.SpeechConstant.IN_FILE, Environment.getExternalStorageDirectory() + "/msc/baidu.wav");
        //保存文件
        params1.put(com.baidu.speech.asr.SpeechConstant.OUT_FILE, Environment.getExternalStorageDirectory() + "/msc/baidu.wav");

        String json = null;
        json = new JSONObject(params1).toString();
        manager.send(event, json, null, 0, 0);
        Log.e(TAG, "BaiDujson：" + json);
    }

    /*停止*/
    private void stop() {
        manager.send(com.baidu.speech.asr.SpeechConstant.ASR_STOP, null, null, 0, 0);
    }

    String jsonstr = "{\"results_recognition\":[\"\"],\"origin_result\":{\"corpus_no\":6475915388511426951,\"err_no\":0,\"result\":{\"word\":[\"\"]},\"sn\":\"61d9af66-f57c-4175-ae9c-aa6140e93d14_s-0\"},\"error\":0,\"best_result\":\"\",\"result_type\":\"final_result\"}";

    /*语音识别回调*/
    public void onEvent(String s, String s1, byte[] bytes, int i, int i1) {

        switch (s) {
            case com.baidu.speech.asr.SpeechConstant.CALLBACK_EVENT_ASR_PARTIAL:
                //成功
                jsonstr = s1;
                Gson gson = new Gson();
                BaiDuVoice voice = gson.fromJson(jsonstr, BaiDuVoice.class);
                Log.e(TAG, "BaiDuBean:" + jsonstr + "\n=========" + voice.toString());
                mVoiceTv.setText(jsonstr);
                mVoiceEt.setText("百度识别结果：" + voice.best_result);
                break;
            case com.baidu.speech.asr.SpeechConstant.CALLBACK_EVENT_ASR_READY:
                //引擎就绪，可以说话，ui改变
                mVoiceBd.setText("正在录音....");
                break;
            case com.baidu.speech.asr.SpeechConstant.CALLBACK_EVENT_ASR_FINISH:
                //识别结束。包含异常及错误信息都通过该方法解析json获取
                mVoiceBd.setText("百度语音识别");
                break;

        }

    }

    //*************************************阿里语音识别********************************************
    String appKey = "nls-service";

    NlsClient nlsClient;
    NlsRequest nlsRequest;

    /*初始化*/
    private void initAliRecognizer() {
       /*初始化参数*/
        nlsRequest = new NlsRequest(new NlsRequestProto(getApplicationContext()));
        nlsRequest.setApp_key(appKey);
        nlsRequest.setAsr_sc("opu");
        nlsClient = NlsClient.newInstance(this, mRecognizerListener, mStageLinstener, nlsRequest);
        nlsClient.setMaxRecordTime(60000);
        nlsClient.setMaxStallTime(1000);
        nlsClient.setMinRecordTime(500);
        nlsClient.setRecordAutoStop(false);
//       nlsClient.setMinimalSpeechLength(200);
    }

    /*识别监听*/
    private NlsListener mRecognizerListener = new NlsListener() {
        public void onRecognizingResult(int status, RecognizedResult result) {
            switch (status) {

                case NlsClient.ErrorCode.SUCCESS:
                    //成功
                    Log.e(TAG, result.asr_out);
                    JSON.parse(result.asr_out);
                    mVoiceTv.setText("success:" + result.asr_out);
                    AliVoice voice = new Gson().fromJson(result.asr_out, AliVoice.class);
                    mVoiceEt.setText("阿里识别结果：" + voice.result);
                    break;
                case NlsClient.ErrorCode.RECOGNIZE_ERROR:
                    mVoiceTv.setText("RECOGNIZE_ERROR:" + "识别失败");
                    break;
                case NlsClient.ErrorCode.RECORDING_ERROR:
                    mVoiceTv.setText("RECORDING_ERROR:" + result.toString());

                    break;
                case NlsClient.ErrorCode.NOTHING:
                    mVoiceTv.setText("NOTHING:" + "没有识别结果");
                    break;
            }

        }

    };


    /*录音状态监听*/
    private StageListener mStageLinstener = new StageListener() {
        //录音开始的回调
        public void onStartRecording(NlsClient recognizer) {
            Log.e(TAG, "onStartRecording:" + recognizer.getObject());
            super.onStartRecording(recognizer);
        }

        //录音结束的回调
        public void onStopRecording(NlsClient recognizer) {
            Log.e(TAG, "onStopRecording:" + recognizer.getObject());
            super.onStopRecording(recognizer);
        }

        //识别开始的回调
        public void onStartRecognizing(NlsClient recognizer) {
            Log.e(TAG, "onStartRecognizing:" + recognizer.getObject().toString());
            super.onStartRecognizing(recognizer);
        }

        //识别结束的回调
        public void onStopRecognizing(NlsClient recognizer) {
            //获取录音样本，将样本保存到本地
            try {
                OutputStream outputStream = new FileOutputStream(new File(Environment.getExternalStorageDirectory() + "/msc/ali.wav"));
                outputStream.write(recognizer.getObject());
                outputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.e(TAG, "onStopRecognizing:" + recognizer.getObject().toString());
            super.onStopRecognizing(recognizer);
        }

    };


    /*动态权限申请*/
    private void initPermission() {
        String permission[] = {Manifest.permission.RECORD_AUDIO,
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
        if (!applyList.isEmpty()) {
            ActivityCompat.requestPermissions(this, applyList.toArray(tmpList), 123);
        }
    }

    //=====================================讯飞=======================================
    //开启
    private void startXF() {
        setParam();
        recognizer.startListening(recognizerListener);
    }

    //停止
    private void stopXF() {

        recognizer.stopListening();
    }


    /*监听*/
    private RecognizerListener recognizerListener = new RecognizerListener() {
        @Override
        public void onVolumeChanged(int i, byte[] bytes) {
            //音量变化
        }

        @Override
        public void onBeginOfSpeech() {
            //开始说话
            Log.e(TAG, "XF开始说话");
        }

        @Override
        public void onEndOfSpeech() {
            //结束说话
            Log.e(TAG, "XF结束说话");

        }

        @Override
        public void onResult(RecognizerResult recognizerResult, boolean b) {
            //返回结果需要判断null
            text = JsonParser.parseIatResult(recognizerResult.getResultString());
            Log.e(TAG, "XFResult:" + text + "\n" + recognizerResult.getResultString());
            mVoiceTv.setText(recognizerResult.getResultString());
            printResult(recognizerResult);
        }

        @Override
        public void onError(SpeechError speechError) {
            //错误回调
            Log.e(TAG, "XFError:" + speechError.toString());

        }

        @Override
        public void onEvent(int i, int i1, int i2, Bundle bundle) {
            //事件拓展
        }
    };
}