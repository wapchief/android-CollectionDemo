package wapchief.com.collectiondemo.activity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import wapchief.com.collectiondemo.R;
import wapchief.com.collectiondemo.customView.MyAlertDialog;
import wapchief.com.collectiondemo.framework.system.SystemStatusManager;

/**
 * Created by wapchief on 2017/8/22.
 */

public class LottieAnimationActivity extends AppCompatActivity {
    @BindView(R.id.title_bar_back)
    ImageView mTitleBarBack;
    @BindView(R.id.title_bar_title)
    TextView mTitleBarTitle;
    @BindView(R.id.lottie_love)
    LottieAnimationView mLottieLove;
    @BindView(R.id.title_bar_content)
    TextView mTitleBarContent;
    @BindView(R.id.title)
    RelativeLayout mTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lottie_animation);
        ButterKnife.bind(this);
        new SystemStatusManager(this).setTranslucentStatus(Color.alpha(00000000));
        initView();

    }

    private void initView() {
        mTitleBarTitle.setText("LottieAnimationView");
        mLottieLove.setAnimation("lottie.json");
        mLottieLove.loop(true);
        mLottieLove.playAnimation();
    }

    @OnClick({R.id.title_bar_back, R.id.lottie_love, R.id.title_bar_content})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_bar_back:
                mLottieLove.setAnimation("permission.json");
                mLottieLove.loop(true);
                mLottieLove.playAnimation();
                break;
            case R.id.lottie_love:
                mLottieLove.cancelAnimation();
                mLottieLove.setAnimation("love.json");
                mLottieLove.loop(true);
                mLottieLove.playAnimation();
                break;
            case R.id.title_bar_content:
                MyAlertDialog dialog = new MyAlertDialog(LottieAnimationActivity.this,
                        new String[]{"love", "permission", "rey_updated", "trail_loading", "give"},
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                switch (i) {
                                    case 0:
                                        mLottieLove.setAnimation("love.json");
                                        mLottieLove.loop(true);
                                        mLottieLove.playAnimation();
                                        break;
                                    case 1:
                                        mLottieLove.setAnimation("permission.json");
                                        mLottieLove.loop(true);
                                        mLottieLove.playAnimation();
                                        break;
                                    case 2:
                                        mLottieLove.setAnimation("rey_updated.json");
                                        mLottieLove.loop(true);
                                        mLottieLove.playAnimation();
                                        break;
                                    case 3:
                                        mLottieLove.setAnimation("trail_loading.json");
                                        mLottieLove.loop(true);
                                        mLottieLove.playAnimation();
                                        break;
                                    case 4:
                                        mLottieLove.setAnimation("give.json");
                                        mLottieLove.loop(true);
                                        mLottieLove.playAnimation();
                                        break;
                                }
                            }
                        });
                dialog.initDialog();
                break;
        }
    }

}
