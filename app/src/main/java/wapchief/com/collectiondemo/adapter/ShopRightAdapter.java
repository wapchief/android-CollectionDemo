package wapchief.com.collectiondemo.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import wapchief.com.collectiondemo.R;
import wapchief.com.collectiondemo.framework.GreenDaoHelper;
import wapchief.com.collectiondemo.greendao.CarShopDao;
import wapchief.com.collectiondemo.greendao.model.CarShop;

/**
 * Created by wapchief on 2017/9/7.
 */

public class ShopRightAdapter extends BaseAdapter {
    private Context context;
    private List<CarShop> list;
    CarShopDao dao;
    CarShop shop;
    int num = 0;
    public ShopRightAdapter(Context context, List<CarShop> list,CarShopDao dao) {
        this.context = context;
        this.list = list;
        this.dao = dao;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view == null) {
            view = View.inflate(context,R.layout.item_shoplist_right,
                    null);
            holder = new ViewHolder();
            holder.tv_num = (TextView) view.findViewById(R.id.shop_num);
            holder.tv_money = (TextView) view.findViewById(R.id.shop_money);
            holder.tv_stock = (TextView) view.findViewById(R.id.shop_stockCount);
            holder.tv_name = (TextView) view.findViewById(R.id.shop_name);
            holder.img_jian = (ImageView) view.findViewById(R.id.shop_jian);
            holder.img_add = (ImageView) view.findViewById(R.id.shop_add);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.tv_name.setText(list.get(i).getName());
        holder.tv_stock.setText(list.get(i).getStockCount());
        holder.tv_money.setText("¥  "+list.get(i).getPrice());
        //查询单条购物车的添加数量
//        for (int i1=0;i<=dao.queryBuilder().list().size();i1++) {
////            num = dao.queryBuilder().list().get(i1).getNum();
//            Log.e("cardaoAdapter",num+""+dao.queryBuilder().list() );
//
//        }
        //添加商品存到数据库

        final ViewHolder finalHolder = holder;
        final int[] numTotal = {0};
        holder.img_jian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                num--;
                shop=new CarShop(null,
                        num,
                        list.get(i).getName(),
                        list.get(i).getPrice() * num,
                        list.get(i).getShopId(),
                        list.get(i).getEntityId(),
                        i,
                        list.get(i).getHeadpic(),
                        list.get(i).getSpec(),
                        list.get(i).getStockCount());
                Log.e("carDaonum=", num + "\n"+list.get(i).getPrice()*num);
                if (num<=0){
                    dao.delete(shop);
                }else {
                    dao.update(shop);
                }
            }
        });

        holder.img_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                num++;
                numTotal[0] = num;
                Log.e("carDaoadd=", num +"\n"+ list.get(i).getPrice()*num);
//                shop=new CarShop(null,
//                        num,
//                        list.get(i).getName(),
//                        list.get(i).getPrice() * num,
//                        list.get(i).getShopId(),
//                        list.get(i).getEntityId(),
//                        i,
//                        list.get(i).getHeadpic(),
//                        list.get(i).getSpec(),
//                        list.get(i).getStockCount());
//                dao.update(shop);

            }
        });
//        holder.tv_num.setText(dao.queryBuilder().list().get(i).getNum());
        holder.tv_num.setText(numTotal[0]+"");

        return view;
    }

    class ViewHolder implements View.OnClickListener{
        TextView tv_num,tv_money,tv_stock,tv_name;
        ImageView img_jian, img_add;

        @Override
        public void onClick(View view) {

        }
    }
//
//    public void setOnLinearItemClickListener(View convertView,
//                                             OnLinearItemClickListener listener, int position) {
////        if (null != listener) {
////            listener.onWork(position, convertView);
////        }
////    }
}
