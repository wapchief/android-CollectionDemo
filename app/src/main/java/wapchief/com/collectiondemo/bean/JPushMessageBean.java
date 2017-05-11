package wapchief.com.collectiondemo.bean;

/**
 * Created by Wu on 2017/5/10 0010 下午 1:17.
 * 描述：存放推送消息的实体
 */
public class JPushMessageBean {

    @Override
    public String toString() {
        return "JPushMessageBean{" +
                "n_builder_id=" + n_builder_id +
                ", ad_id='" + ad_id + '\'' +
                ", n_only=" + n_only +
                ", m_content=" + m_content +
                ", show_type=" + show_type +
                '}';
    }

    /**
     * n_builder_id : 0
     * ad_id : 3028492214
     * n_only : 1
     * m_content : {"n_content":"测试111111111111111111111111111111","n_style":0,"n_extras":{},"ad_t":0,"n_priority":0,"n_alert_type":7,"n_title":"","n_category":"","n_flag":1}
     * show_type : 4
     */

    private int n_builder_id;
    private String ad_id;
    private int n_only;
    private MContentBean m_content;
    private int show_type;

    public int getN_builder_id() {
        return n_builder_id;
    }

    public void setN_builder_id(int n_builder_id) {
        this.n_builder_id = n_builder_id;
    }

    public String getAd_id() {
        return ad_id;
    }

    public void setAd_id(String ad_id) {
        this.ad_id = ad_id;
    }

    public int getN_only() {
        return n_only;
    }

    public void setN_only(int n_only) {
        this.n_only = n_only;
    }

    public MContentBean getM_content() {
        return m_content;
    }

    public void setM_content(MContentBean m_content) {
        this.m_content = m_content;
    }

    public int getShow_type() {
        return show_type;
    }

    public void setShow_type(int show_type) {
        this.show_type = show_type;
    }

    public static class MContentBean {
        /**
         * n_content : 测试111111111111111111111111111111
         * n_style : 0
         * n_extras : {}
         * ad_t : 0
         * n_priority : 0
         * n_alert_type : 7
         * n_title :
         * n_category :
         * n_flag : 1
         */

        private String n_content;
        private int n_style;
        private NExtrasBean n_extras;
        private int ad_t;
        private int n_priority;
        private int n_alert_type;
        private String n_title;
        private String n_category;
        private int n_flag;

        public String getN_content() {
            return n_content;
        }

        public void setN_content(String n_content) {
            this.n_content = n_content;
        }

        public int getN_style() {
            return n_style;
        }

        public void setN_style(int n_style) {
            this.n_style = n_style;
        }

        public NExtrasBean getN_extras() {
            return n_extras;
        }

        public void setN_extras(NExtrasBean n_extras) {
            this.n_extras = n_extras;
        }

        public int getAd_t() {
            return ad_t;
        }

        public void setAd_t(int ad_t) {
            this.ad_t = ad_t;
        }

        public int getN_priority() {
            return n_priority;
        }

        public void setN_priority(int n_priority) {
            this.n_priority = n_priority;
        }

        public int getN_alert_type() {
            return n_alert_type;
        }

        public void setN_alert_type(int n_alert_type) {
            this.n_alert_type = n_alert_type;
        }

        public String getN_title() {
            return n_title;
        }

        public void setN_title(String n_title) {
            this.n_title = n_title;
        }

        public String getN_category() {
            return n_category;
        }

        public void setN_category(String n_category) {
            this.n_category = n_category;
        }

        public int getN_flag() {
            return n_flag;
        }

        public void setN_flag(int n_flag) {
            this.n_flag = n_flag;
        }

        public static class NExtrasBean {
        }
    }
}
