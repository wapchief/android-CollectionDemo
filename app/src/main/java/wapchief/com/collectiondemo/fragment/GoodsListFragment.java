package wapchief.com.collectiondemo.fragment;

import android.os.Bundle;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import wapchief.com.collectiondemo.R;
import wapchief.com.collectiondemo.adapter.CatListViceAdapter;
import wapchief.com.collectiondemo.adapter.ShopLeftAdapter;
import wapchief.com.collectiondemo.adapter.ShopRightAdapter;
import wapchief.com.collectiondemo.framework.GreenDaoHelper;
import wapchief.com.collectiondemo.greendao.CarShopDao;
import wapchief.com.collectiondemo.greendao.model.CarShop;
import androidx.fragment.app.Fragment;

/**
 * Created by wapchief on 2017/9/6.
 */

public class GoodsListFragment extends Fragment {
    @BindView(R.id.goodslist_left)
    ListView mGoodslistLeft;
    @BindView(R.id.goodslist_right)
    ListView mGoodslistRight;
    Unbinder unbinder;
    private String[] mTitles = new String[]{"生活食品","酒水饮料","粮油副食","日用品"};
    ShopLeftAdapter adapterLeft;
    ShopRightAdapter adapterRight;
    CatListViceAdapter viceAdapter;
    CarShopDao dao;
    GreenDaoHelper helper;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(this.getActivity(), R.layout.fragment_goodslist, null);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }


    private void initView() {
        initDB();
        initListLeft();
        initListRight();

    }

    /*初始化数据库*/
    private void initDB() {
        helper = new GreenDaoHelper(getActivity());
        dao = helper.initDao().getCarShopDao();
//        Log.e("carDaoroot", dao.queryBuilder().list()+"");

    }

    private void initListRight() {
        adapterRight = new ShopRightAdapter(this.getActivity(), getData(),dao);
        mGoodslistRight.setAdapter(adapterRight);
    }

    /*数据源*/
    private List<CarShop> getData(){

        List<CarShop> shopList = new ArrayList<>();
        CarShop shop;
        for (int i=0;i<10;i++) {
            shop = new CarShop();
            shop.setName("店铺"+i);
            shop.setPrice(10.1);
            shop.setStockCount("10000");
            shop.setShopId(i+"");
            shopList.add(shop);
        }
        return shopList;
    }

    private void initListLeft() {
        List<String> list = new ArrayList<>();
        for (int i=0;i<mTitles.length;i++){
            list.add(i,mTitles[i]);
        }
        mGoodslistLeft.setAdapter(new ArrayAdapter<String>(this.getActivity(),
                android.R.layout.simple_dropdown_item_1line,list));
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
