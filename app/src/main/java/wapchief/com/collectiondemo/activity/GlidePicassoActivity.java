package wapchief.com.collectiondemo.activity;

import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import wapchief.com.collectiondemo.R;

/**
 * Created by Wu on 2017/2/20 0020 下午 3:06.
 * 描述：图片缓存
 */
public class GlidePicassoActivity extends AppCompatActivity {
    @BindView(R.id.img_glide)
    ImageView imgGlide;
    @BindView(R.id.glide_bt)
    Button glideBt;
    @BindView(R.id.picasso_bt)
    Button picassoBt;

    String url = "https://www.mypsy365.com/files/default/2017/09-25/10323536e637083862.jpg";
    String url2 = "https://preview.mypsy365.com/files/default/2017/11-05/224031fa5738864468.jpeg";
    Uri ur = Uri.parse(url);
    Uri ur2 = Uri.parse(url2);
    @BindView(R.id.clean_bt)
    Button cleanBt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.glide);
        ButterKnife.bind(this);
        initglide();


    }

    private void initglide() {
        Glide.with(this)
                .load(url)
                .asBitmap()
                .thumbnail(0.2f)
                .into(imgGlide);
    }

    private void initpicasso() {
        Picasso.with(this)
                .load(url2)
                .into(imgGlide);
    }

    private void cleanImg() {
//        Picasso.with(this)
//                .invalidate(ur);
//        Picasso.with(this)
//                .invalidate(ur2);
        Glide.get(this).clearMemory();
        Glide.get(this).clearDiskCache();
    }


    @OnClick({R.id.glide_bt, R.id.picasso_bt,R.id.clean_bt})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.glide_bt:
                initglide();
                break;
            case R.id.picasso_bt:
                initpicasso();
                break;
            case R.id.clean_bt:
//                cleanImg();
                break;
        }
    }

}
