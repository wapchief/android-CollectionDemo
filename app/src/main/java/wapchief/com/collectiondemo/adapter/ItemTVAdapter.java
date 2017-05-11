package wapchief.com.collectiondemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import butterknife.ButterKnife;
import wapchief.com.collectiondemo.R;
import wapchief.com.collectiondemo.bean.JPushMessageBean;
import wapchief.com.collectiondemo.greendao.model.Message;
import wapchief.com.collectiondemo.greendao.model.User;

/**
 * Created by Wu on 2017/5/10 0010 下午 3:52.
 * 描述：
 */
public class ItemTVAdapter extends BaseAdapter {
    Context context;
    List<Message> list;

    public ItemTVAdapter(Context context, List<Message> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Message tv = (Message) getItem(position);
        ViewHolder vh = null;
        if(convertView == null){
            convertView = View.inflate(context, R.layout.item_textview, null);
            vh = new ViewHolder();
            vh.item_tv = (TextView) convertView.findViewById(R.id.item_textview);
            vh.item_title = (TextView) convertView.findViewById(R.id.item_title);
            convertView.setTag(vh);
        }else{
            vh = (ViewHolder) convertView.getTag();
        }
        vh.item_tv.setText(tv.getContent());
        vh.item_title.setText(tv.getTitle());
        return convertView;
    }
    class ViewHolder{
        TextView item_tv,item_title;
    }
}