package wapchief.com.collectiondemo.bean;

import java.io.Serializable;

/**
 * Created by Wu on 2017/4/11 0011 下午 4:03.
 * 描述：
 */
public class TModel<T> implements Serializable{
    public T t;


    @Override
    public String toString() {
        return "TModel{" +
                "t=" + t +
                '}';
    }

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }
}
