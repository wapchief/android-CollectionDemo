package wapchief.com.collectiondemo.activity;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import butterknife.ButterKnife;
import wapchief.com.collectiondemo.R;
import wapchief.com.collectiondemo.bean.FlowLayoutBean;


public class FlowLayoutActivity extends AppCompatActivity implements View.OnClickListener{


    private TagFlowLayout searchPageFlowlayout;
    private ListView searchPageLv;
    private Button searchPageDelete;
    private RelativeLayout searchPageRl;


//    @BindView(R.id.search_page_flowlayout)
//    TagFlowLayout searchPageFlowlayout;
//    @BindView(R.id.search_page_lv)
//    ListView searchPageLv;
//    @BindView(R.id.search_page_rl)
//    RelativeLayout searchPageRl;
//    @BindView(R.id.search_page_delete)
//    Button searchPageDelete;

    MyAdapter myAdapter;
    private TagAdapter<String> adapter;
    private String[] mVals = new String[]
            {"Hello", "Android", "Weclome Hi ", "Button", "TextView", "Hello",
                    "Android", "Weclome", "Button ImageView", "TextView", "Helloworld"};
    private List<FlowLayoutBean> list=new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_page);
//        ButterKnife.bind(this);
        initview();
    }

    //初始化数据
    private void initview() {


        searchPageFlowlayout= (TagFlowLayout) findViewById(R.id.search_page_flowlayout);
        searchPageLv=(ListView) findViewById(R.id.search_page_lv);
        searchPageDelete= (Button) findViewById(R.id.search_page_delete);
        searchPageDelete.setOnClickListener(this);
        searchPageRl= (RelativeLayout) findViewById(R.id.search_page_rl);

        final LayoutInflater mInflater = LayoutInflater.from(FlowLayoutActivity.this);
        searchPageFlowlayout.setAdapter(new TagAdapter<String>(mVals)
        {
            @Override
            public View getView(FlowLayout parent, int position, String s)
            {
                TextView tv = (TextView) mInflater.inflate(R.layout.search_page_flowlayout_tv,
                        searchPageFlowlayout, false);
                tv.setText(s);
                return tv;
            }
        });
//        adapter.setSelectedList(1);
//        searchPageFlowlayout.setAdapter(adapter);
        searchPageFlowlayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener()
        {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent)
            {
                Toast.makeText(FlowLayoutActivity.this, mVals[position], Toast.LENGTH_SHORT).show();
                //view.setVisibility(View.GONE);
                return true;
            }
        });
        searchPageFlowlayout.setOnSelectListener(new TagFlowLayout.OnSelectListener()
        {
            @Override
            public void onSelected(Set<Integer> selectPosSet)
            {
                FlowLayoutActivity.this.setTitle("choose:" + selectPosSet.toString());
            }
        });


        initData();
        myAdapter=new MyAdapter(FlowLayoutActivity.this,list);
        searchPageLv.setAdapter(myAdapter);

    }
    //提供数据源
    private void initData(){
        FlowLayoutBean tv=null;
        for (int i = 0; i < mVals.length; i++) {
            tv=new FlowLayoutBean();
            tv.setTv(mVals[i]);
            list.add(tv);
        }
    }

//    @OnClick({R.id.tv_title, R.id.search_page_flowlayout, R.id.search_page_lv, R.id.search_page_rl, R.id.search_page_delete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_title:
                break;
            case R.id.search_page_flowlayout:
                break;
            case R.id.search_page_lv:
                break;
            case R.id.search_page_rl:
                break;
            case R.id.search_page_delete:
                searchPageRl.setVisibility(View.VISIBLE);
                searchPageLv.setVisibility(View.GONE);
                searchPageDelete.setVisibility(View.INVISIBLE);
                break;
        }
    }

    class MyAdapter extends BaseAdapter {
        private Context context;
        private List<FlowLayoutBean> list;

        public MyAdapter(Context context, List<FlowLayoutBean> list) {
            this.context = context;
            this.list = list;
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
        public View getView(int i, View view, ViewGroup viewGroup) {
            FlowLayoutBean tv = (FlowLayoutBean) getItem(i);
            ViewHolder viewHolder;
            if (view == null) {
                view = LayoutInflater.from(context).inflate(R.layout.search_page_flowlayout_tv, null);
                viewHolder = new ViewHolder();
                viewHolder.flowlayout_tv = (TextView) view.findViewById(R.id.flowlayout_tv);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            viewHolder.flowlayout_tv.setText(tv.getTv());

            return view;
        }

        //创建ViewHolder类
        class ViewHolder {
            TextView flowlayout_tv;
        }
    }
}
