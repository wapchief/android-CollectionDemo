package wapchief.com.collectiondemo.bean;

import java.util.List;

/**
 * Created by wapchief on 2017/10/9.
 */

public class VoiceAI {

    /**
     * sn : 1
     * ls : false
     * bg : 0
     * ed : 0
     * ws : [{"bg":0,"cw":[{"sc":0,"w":"喂"}]},{"bg":0,"cw":[{"sc":0,"w":"喂"}]},{"bg":0,"cw":[{"sc":0,"w":"胃"}]},{"bg":0,"cw":[{"sc":0,"w":"喂"}]},{"bg":0,"cw":[{"sc":0,"w":"胃"}]}]
     */

    public int sn;
    public boolean ls;
    public int bg;
    public int ed;
    public List<WsBean> ws;

    public static class WsBean {
        /**
         * bg : 0
         * cw : [{"sc":0,"w":"喂"}]
         */

        public int bg;
        public List<CwBean> cw;

        public static class CwBean {
            /**
             * sc : 0.0
             * w : 喂
             */

            public double sc;
            public String w;
        }
    }
}
