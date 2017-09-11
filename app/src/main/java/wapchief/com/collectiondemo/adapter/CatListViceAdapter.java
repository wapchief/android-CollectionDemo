package wapchief.com.collectiondemo.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import wapchief.com.collectiondemo.R;
import wapchief.com.collectiondemo.fragment.GoodsListFragment;
import wapchief.com.collectiondemo.greendao.model.CarShop;


/***
 * 底部购物车
 */
public class CatListViceAdapter extends BaseAdapter {
    CatListViceAdapter goodsAdapter;
    private Context context;
    private List<CarShop> dataList;
    public CatListViceAdapter(Context context, List<CarShop> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        final Viewholder viewholder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_carlist_vice, null);
            viewholder = new Viewholder();
            viewholder.tv_name = (TextView) view.findViewById(R.id.tv_name);
            viewholder.tv_price = (TextView) view.findViewById(R.id.tv_price);
            viewholder.iv_add= (ImageView) view.findViewById(R.id.iv_add);
            viewholder.iv_remove= (ImageView) view.findViewById(R.id.iv_remove);
            viewholder.tv_count= (TextView) view.findViewById(R.id.tv_count);

            view.setTag(viewholder);
        } else {
            viewholder = (Viewholder) view.getTag();
        }

            viewholder.tv_name.setText(dataList.get(position).getName());
            viewholder.tv_count.setText(""+dataList.get(position).getNum());
            viewholder.tv_price.setText(dataList.get(position).getPrice()+"");
            viewholder.iv_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    context.handlerCarNum(1,dataList.valueAt(position),true);
                    goodsAdapter.notifyDataSetChanged();

                }
            });
            viewholder.iv_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    activity.handlerCarNum(0,dataList.valueAt(position),true);
                    goodsAdapter.notifyDataSetChanged();
                }
            });

        return view;
    }

    class Viewholder {
        TextView tv_price;
        TextView tv_name;
        ImageView iv_add,iv_remove;
        TextView tv_count;
    }

}