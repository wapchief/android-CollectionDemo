package wapchief.com.collectiondemo.activity;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.TextPaint;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import wapchief.com.collectiondemo.R;
import wapchief.com.collectiondemo.customView.ExpandableTextView;
import wapchief.com.collectiondemo.framework.BaseActivity;

/**
 * @author wapchief
 * @date 2018/5/29
 */

public class ExpandableTextViewActivity extends BaseActivity {

    //    @BindView(R.id.expandable_text)
//    TextView mExpandableText;
//    @BindView(R.id.expand_collapse)
//    ImageButton mExpandCollapse;
    String mString = "测试测试测试测试测试测试";
    String mString2 = "测试1测试1测试1测试1测试1测试，测试试1测试1测试1测试1测试测试1测试1测试1测试1测试1测试测试1测试1测试1测试1测试1测试";
    @BindView(R.id.bt1)
    Button mBt1;
    @BindView(R.id.bt2)
    Button mBt2;
    @BindView(R.id.expand_collapse)
    ImageButton mExpandCollapse;
    @BindView(R.id.expand_text_view)
    ExpandableTextView mExpandTextView;
    @BindView(R.id.tv_bold)
    TextView mTvBold;
    @BindView(R.id.tv_bolds)
    TextView mTvBolds;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expandable);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mExpandTextView.setText(mString2);
//        mExpandTextView2.setText(mString);

        TextPaint tp = mTvBold .getPaint();
        TextPaint tp2 = mTvBolds .getPaint();
        tp.setFakeBoldText(true);
        tp2.setFakeBoldText(true);
        tp2.setStrokeWidth(8);
    }

    @OnClick({R.id.bt1, R.id.bt2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt1:
                mExpandTextView.setText(mString2);
//                mExpandTextView2.setText(mString2);
                break;
            case R.id.bt2:
                mExpandTextView.setText(mString);
//                mExpandTextView2.setText(mString);
                break;
        }
    }
}
