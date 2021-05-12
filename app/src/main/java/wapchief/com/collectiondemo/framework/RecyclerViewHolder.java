package wapchief.com.collectiondemo.framework;

import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import static androidx.recyclerview.widget.RecyclerView.*;

/**
 * Created by WuBing on 2017/1/13 0013 下午 3:25.
 * 描述：通用的ViewHolder
 */
public class RecyclerViewHolder extends ViewHolder {

    private SparseArray<View> arrayView;

    public RecyclerViewHolder(View itemView) {
        super(itemView);
        arrayView = new SparseArray<>();
    }

    /**
     * 通过填写的itemId来获取具体的View的对象
     *
     * @param itemId R.id.***
     * @param <T>    必须是View的子类
     * @return
     */
    public <T extends View> T getView(int itemId) {
        //arrayVie类似于Map容器，get(key)取出的是value值
        View mView = arrayView.get(itemId);
        if (mView == null) {
            //实例化具体的View类型
            mView = itemView.findViewById(itemId);
            arrayView.put(itemId, mView);
        }
        return (T) mView;
    }

    /**
     * 设置TextView的内容
     *
     * @param itemId
     * @param text
     */
    public void setText(int itemId, String text) {
        TextView tv = getView(itemId);
        tv.setText(text);
    }

    /**
     * 设置图片
     *
     * @param itemId
     * @param imageId
     */
    public void setBitmapImage(int itemId, int imageId) {
        ImageView iv = getView(itemId);
        iv.setImageResource(imageId);
    }


}
