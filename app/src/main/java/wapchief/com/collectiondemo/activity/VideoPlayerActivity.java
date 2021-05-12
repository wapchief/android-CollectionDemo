package wapchief.com.collectiondemo.activity;

import android.os.Bundle;
import androidx.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;
import wapchief.com.collectiondemo.R;
import wapchief.com.collectiondemo.framework.BaseActivity;

/**
 * Created by wapchief on 2017/9/30.
 */

public class VideoPlayerActivity extends BaseActivity {

    @BindView(R.id.video_player)
    JZVideoPlayerStandard mVideoPlayer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mVideoPlayer.setUp("http://jzvd.nathen.cn/c6e3dc12a1154626b3476d9bf3bd7266/6b56c5f0dc31428083757a45764763b0-5287d2089db37e62345123a1be272f8b.mp4"
                , JZVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, "mv");
        mVideoPlayer.thumbImageView.setImageDrawable(getResources().getDrawable(R.drawable.bar_bg));
    }

    @Override
    public void onBackPressed() {
        if (JZVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JZVideoPlayer.releaseAllVideos();
    }
}
