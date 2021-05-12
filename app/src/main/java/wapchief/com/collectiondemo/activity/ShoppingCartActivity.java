package wapchief.com.collectiondemo.activity;

import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cpoopc.scrollablelayoutlib.ScrollableLayout;
import com.flipboard.bottomsheet.BottomSheetLayout;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import wapchief.com.collectiondemo.R;
import wapchief.com.collectiondemo.adapter.CatListViceAdapter;
import wapchief.com.collectiondemo.customView.MyListView;
import wapchief.com.collectiondemo.fragment.FragmentFactory;
import wapchief.com.collectiondemo.framework.BaseActivity;
import wapchief.com.collectiondemo.framework.GreenDaoHelper;
import wapchief.com.collectiondemo.greendao.CarShopDao;
import wapchief.com.collectiondemo.greendao.model.CarShop;

import static androidx.viewpager.widget.ViewPager.*;

/**
 * Created by wapchief on 2017/9/6.
 * 仿购物车
 */

public class ShoppingCartActivity extends BaseActivity {

    @BindView(R.id.tab)
    TabLayout mTab;
    @BindView(R.id.vp)
    ViewPager mVp;
    @BindView(R.id.scrollablelayout)
    ScrollableLayout mScrollablelayout;
    @BindView(R.id.shop_title)
    TextView mShopTitle;
    @BindView(R.id.title_bottom)
    RelativeLayout mTitleBottom;
    @BindView(R.id.shopping_money)
    TextView mShoppingMoney;
    @BindView(R.id.shopping_img)
    ImageView mShoppingImg;
    @BindView(R.id.shopping_number)
    TextView mShoppingNumber;
    @BindView(R.id.bottomSheetLayout)
    BottomSheetLayout mBottomSheetLayout;
    @BindView(R.id.shop_title_ll)
    RelativeLayout mShopTitleLl;
    private String[] titles = new String[]{"商品", "评价", "商家"};
    CarShopDao dao;
    CatListViceAdapter viceAdapter;
//    private View bottomSheet;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoppingcart);
        ButterKnife.bind(this);
//        ButterKnife.bind(bottomSheet);
        initView();
    }

    private void initView() {
        dao = new GreenDaoHelper(this).initDao().getCarShopDao();
        initTabLayout();
        initScroll();
        initViewPager();
        initCar();

    }

    private void initCar() {
        List<CarShop> shopList = dao.queryBuilder().where(CarShopDao.Properties.Price.isNotNull()).build().list();
        for (int i = 0; i < dao.queryBuilder().list().size(); i++) {
            Log.e("cardaoActivity", dao.queryBuilder().list().size() + "," + dao.queryBuilder().list().get(i).getNum() + "\n" +
                    dao.queryBuilder().list().get(i).getPrice());
            mShoppingMoney.setText("¥：" + dao.queryBuilder().list().get(i).getPrice());
            mShoppingNumber.setText("" + dao.queryBuilder().list().get(i).getNum());
        }


        mShoppingImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomSheet();
            }
        });
    }

    //创建购物车view
    private void showBottomSheet() {
//        bottomSheet = createBottomSheetView();
        if (mBottomSheetLayout.isSheetShowing()) {
            mBottomSheetLayout.dismissSheet();
        } else {
            if (getData().size() != 0) {
                mBottomSheetLayout.showWithSheetView(createBottomSheetView());
            }
        }
    }


    //查看购物车布局
    private View createBottomSheetView() {
        View view = LayoutInflater.from(this).inflate(R.layout.shop_carlist_vice, (ViewGroup) getWindow().getDecorView(), false);
        MyListView lv_product = (MyListView) view.findViewById(R.id.lv_product);
        TextView clear = (TextView) view.findViewById(R.id.clear);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dao.deleteAll();

            }
        });

        viceAdapter = new CatListViceAdapter(this, getData());
        lv_product.setAdapter(viceAdapter);
        return view;
    }

    /*数据源*/
    private List<CarShop> getData() {

        List<CarShop> shopList = new ArrayList<>();
        CarShop shop;
        for (int i = 0; i < 10; i++) {
            shop = new CarShop();
            shop.setName("店铺" + i);
            shop.setPrice(10.2 + i);
            shop.setStockCount("10000");
            shop.setShopId(i + "");
            shop.setNum(0);
            shopList.add(shop);
        }
        return shopList;
    }

    private void initViewPager() {

        mVp.setAdapter(new MyPagerAdapter(this.getSupportFragmentManager()));
        mTab.setupWithViewPager(mVp);
        mVp.addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position != 0) {
                    mTitleBottom.setVisibility(View.GONE);
                } else {
                    mTitleBottom.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /*滚动效果*/
    private void initScroll() {
        mScrollablelayout.setOnScrollListener(new ScrollableLayout.OnScrollListener() {
            @Override
            public void onScroll(int i, int i1) {
                float scale = (float) i1 - i;
                float alphaTv = scale / i1 * 250;
                float s = i / i1;
                mShopTitleLl.setBackgroundColor(Color.argb((int) alphaTv, 73, 64, 66));
//                mShopTitle.setTextColor(Color.argb((int) ((float)255*s),255,255,255));
                if (i == i1) {
//                    title.setVisibility(View.GONE);
                    mShopTitle.setTextColor(Color.BLACK);
                } else if (i < i1) {
                    mShopTitle.setTextColor(Color.BLACK);
                } else {
                    mShopTitle.setTextColor(Color.BLACK);

                }
                LinearLayout.LayoutParams lp = new
                        LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);

            }
        });
    }


    /*初始化tab标签*/
    private void initTabLayout() {

        for (int i = 0; i < titles.length; i++) {
            mTab.addTab(mTab.newTab().setText(titles[i]));
        }


    }

    private class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = FragmentFactory.createFragment(position);
            return fragment;

        }
    }
}
