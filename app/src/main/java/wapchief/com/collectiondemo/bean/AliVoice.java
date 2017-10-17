package wapchief.com.collectiondemo.bean;

import java.util.List;

/**
 * Created by wapchief on 2017/10/17.
 */

public class AliVoice {

    /**
     * result : 大理语音保存
     * uid : c56615cdbeba43c5a721ff79197b6768
     * confidence : 1
     * finish : 1
     * version : 4.0
     * nbest : [{"result":"大理语音保存","lexical":"大理 语音 保存","confidence":1}]
     * status : 1
     */

    public String result;
    public String uid;
    public int confidence;
    public int finish;
    public String version;
    public int status;
    public List<NbestBean> nbest;

    public static class NbestBean {
        /**
         * result : 大理语音保存
         * lexical : 大理 语音 保存
         * confidence : 1
         */

        public String result;
        public String lexical;
        public int confidence;
    }
}
