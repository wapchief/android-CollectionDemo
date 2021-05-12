package wapchief.com.collectiondemo.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.bartoszlipinski.recyclerviewheader2.RecyclerViewHeader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import wapchief.com.collectiondemo.R;
import wapchief.com.collectiondemo.adapter.RecyclerViewAdapter;
import wapchief.com.collectiondemo.customView.DividItemDecoration;
import wapchief.com.collectiondemo.framework.system.SystemStatusManager;
import wapchief.com.collectiondemo.utils.ToastsUtils;

/**
 * Created by WuBing on 2017/1/10 0010 上午 9:22.
 * 描述：测试RecyclerViewTest
 */
public class RecyclerViewActivity extends AppCompatActivity {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    RecyclerViewAdapter adapter;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindColor(R.color.theme_lanse)
    int black_transparent;
    @BindColor(R.color.black)
    int black;
    @BindColor(R.color.blue)
    int blue;
    @BindColor(R.color.white)
    int white;
    @BindView(R.id.view_relat)
    RelativeLayout viewRelat;
    @BindView(R.id.back_top)
    ImageView backTop;
    @BindView(R.id.recyclerView_header)
    RecyclerViewHeader recyclerViewHeader;
    @BindView(R.id.vf)
    ViewFlipper vf;
    private List data = new ArrayList<>();
    Handler handler = new Handler();
    TextView tv_item;
    boolean isLoading;
    LinearLayoutManager layoutManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_view);
        new SystemStatusManager(this).setTranslucentStatus(R.color.colorPrimary);

        ButterKnife.bind(this);
        initview();
        initData();
        initHeader();
    }

    /*添加header*/
    private void initHeader() {
//        vf.startFlipping();
//        View view = View.inflate(this, R.layout.item_viewflipper, null);
//        TextView tv = (TextView) view.findViewById(R.id.item_viewfip_tv);
//        tv.setText("新鲜水果限时开抢新鲜水果限时开抢新鲜水果限时开抢新鲜水果限时开抢新鲜水果限时开抢新鲜水果限时开抢！");
//        vf.addView(view);
//        recyclerViewHeader.attachTo(recyclerView);
    }

    private void initview() {

        tv_item = (TextView) findViewById(R.id.tv_item);
        adapter = new RecyclerViewAdapter(this, data);
        //设置刷新状态颜色
        swipeRefreshLayout.setColorSchemeResources(
                R.color.blue
                , R.color.oriange
                , R.color.black
                , R.color.red);
        //开启一个刷新的线程
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                //开始
                swipeRefreshLayout.setRefreshing(true);
            }
        });
        //监听刷新状态操作
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //设置延迟刷新时间1500
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //刷新数据
                        data.clear();
                        getdata();
                    }
                }, 1800);
            }
        });
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        //添加分割线
        recyclerView.addItemDecoration(new DividItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            //当RecyclerView的滑动状态改变时触发
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.d("测试-----------test", "状态改变 = " + newState);

            }

            /**
             * 当RecyclerView滑动时触发
             * 类似点击事件的MotionEvent.ACTION_MOVE
             */
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                selectItem();

                //获取可见item个数
                int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                if (lastVisibleItemPosition + 1 == adapter.getItemCount()) {
                    //加载
                    boolean isRefreshing = swipeRefreshLayout.isRefreshing();
                    if (isRefreshing) {
                        tvTitle.setBackgroundColor(getResources().getColor(R.color.black_transparent));
                        adapter.notifyItemRemoved(adapter.getItemCount());
                        return;
                    }
                    if (!isLoading) {
                        isLoading = true;
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                getdata();
                                Log.d("测试--------------test", "完成加载更多");

                                isLoading = false;
                            }
                        }, 1000);
                    }
                }

            }
        });
        //点击事件
        adapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.d("测试--------------test", "点击了item = " + position);
                ToastsUtils.showShort(RecyclerViewActivity.this, "点击了item:" + position);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
        backTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.scrollToPosition(0);
            }
        });
    }

    /**
     * 首次进入activity执行刷新数据
     */
    public void initData() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getdata();
            }
        }, 1500);
    }


    /**
     * 提供数据源
     */
    private void getdata() {

        for (int i = 0; i < 10; i++) {
            data.add(i);
        }

        updateView();
    }

    /**
     * 更新数据
     */
    private void updateView() {
        //数据刷新,重绘当前可见区域
        adapter.notifyDataSetChanged();
        //setRefreshing方法必须在view初始化完毕之后使用
        swipeRefreshLayout.setRefreshing(false);
        //获取到数据变化通知刷新
        adapter.notifyItemRemoved(adapter.getItemCount());
    }

    /**
     * 设置滑动距离时的效果
     */
    public void selectItem() {
        if (getScollYDistance() <= 0) {
            //静止并处于最顶端状态
            tvTitle.setBackgroundColor(black_transparent);
            tvTitle.setTextColor(white);
            tvTitle.setVisibility(View.VISIBLE);
        } else if (getScollYDistance() > 0 && getScollYDistance() <= 400) {//滑动在0-400距离的时候
            if (getScollYDistance() <= 200) {//处于滑动到中间的时候
                tvTitle.setTextColor(blue);
            } else {//滑出到200以外
//                tvTitle.setBackgroundColor(Color.argb((int) 255, 254, 184, 6));
                tvTitle.setTextColor(white);
            }
            float scale = (float) getScollYDistance() / 400;
            float alpha = (255 * scale);
            // 只是layout背景透明(仿知乎滑动效果)
            tvTitle.setBackgroundColor(Color.argb((int) alpha, 254, 184, 6));
        } else {
//            tvTitle.setBackgroundColor(Color.argb((int) 0, 254, 184, 6));
            tvTitle.setVisibility(View.GONE);
        }
        //弹出top返回顶部按钮
        if (getScollYDistance() >= 400) {
            backTop.setVisibility(View.VISIBLE);
        } else {
            backTop.setVisibility(View.GONE);
        }

    }

    /**
     * 获取滑动的总距离
     *
     * @return
     */
    public int getScollYDistance() {
        int position = layoutManager.findFirstVisibleItemPosition();

        View firstVisiableChildView = layoutManager.findViewByPosition(position);
        int firstVisiableChildViewTop = firstVisiableChildView.getTop();
        int itemHeight = firstVisiableChildView.getHeight();
        //第一个可见的item*item高度-最顶端位置
        return (position) * itemHeight - firstVisiableChildViewTop;
    }


    /**
     * 设置循环滚动的View数组
     *
     * @param views
     */
    public void setViews(List<View> views) {
        if (views == null || views.size() == 0) return;
        vf.removeAllViews();
        for (int i = 0; i < views.size(); i++) {
            vf.addView(views.get(i));
        }
        vf.startFlipping();
    }

}
