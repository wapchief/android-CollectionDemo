package wapchief.com.collectiondemo;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

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
    private List data = new ArrayList<>();
    Handler handler=new Handler();

    TextView tv_item;
    boolean isLoading;

    private Context context;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_view);
//        context=this;
        ButterKnife.bind(this);
        initview();
        initData();
//    View view= LayoutInflater.from(this).inflate(R.layout.recycler_view_item,null);
//        ButterKnife.bind(this, view);
//        tv_item.setText("测试钱钱钱钱钱钱钱");

    }

    private void initview() {

        tv_item = (TextView) findViewById(R.id.tv_item);
        adapter = new RecyclerViewAdapter(this,data);
        //设置刷新状态颜色
        swipeRefreshLayout.setColorSchemeResources(
                R.color.blue
                ,R.color.oriange
                ,R.color.black
                ,R.color.red);
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
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.addItemDecoration(new DividItemDecoration(this, DividItemDecoration.VERTICAL_LIST));
        recyclerView.setAdapter(adapter);

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
                Log.d("测试-----------test", "正在滚动");

                int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                if (lastVisibleItemPosition + 1 == adapter.getItemCount()) {

                    Log.d("测试--------------test", "执行加载");

                    boolean isRefreshing = swipeRefreshLayout.isRefreshing();
                    if (isRefreshing) {
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
                UToasts.showShort(RecyclerViewActivity.this,"点击了item:"+position);
            }

            @Override
            public void onItemLongClick(View view, int position) {

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

        for (int i = 0; i < 9; i++) {
            data.add(i);
        }
        //数据刷新,重绘当前可见区域
        adapter.notifyDataSetChanged();
        //setRefreshing方法必须在view初始化完毕之后使用
        swipeRefreshLayout.setRefreshing(false);
        //获取到数据变化通知刷新
        adapter.notifyItemRemoved(adapter.getItemCount());
    }


}
