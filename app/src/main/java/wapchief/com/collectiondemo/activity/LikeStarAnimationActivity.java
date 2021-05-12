package wapchief.com.collectiondemo.activity;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.widget.ImageView;

import com.wapchief.likestarlibrary.like.TCHeartLayout;
import com.wapchief.likestarlibrary.like.TCHeartView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import wapchief.com.collectiondemo.R;
import wapchief.com.collectiondemo.framework.BaseActivity;

/**
 * @author wapchief
 * @date 2018/3/27
 */

public class LikeStarAnimationActivity extends BaseActivity {

    @BindView(R.id.heart_layout)
    TCHeartLayout mHeartLayout;
    @BindView(R.id.btn_like)
    ImageView mBtnLike;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_like_star);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_like)
    public void onViewClicked() {
        mHeartLayout.addFavor();
    }
}
