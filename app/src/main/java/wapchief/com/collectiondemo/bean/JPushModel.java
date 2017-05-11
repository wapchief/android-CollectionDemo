package wapchief.com.collectiondemo.bean;

import java.io.Serializable;

/**
 * Created by Wu on 2017/5/10 0010 下午 4:37.
 * 描述：
 */
public class JPushModel<T> implements Serializable {
    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }

    @Override
    public String toString() {
        return "JPushModel{" +
                "t=" + t +
                '}';
    }

    public T t;
}
